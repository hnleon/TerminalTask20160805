package ua.pp.leon.terminal;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrii Zalevskyi <azalevskyi@gmail.com>
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    private static final String HELP
            = "Collects statistic of text files: longest word, shortest word, etc.\n"
            + "USAGE: java -jar TaskTerminal.jar [OPTIONS] SOURCE\n"
            + "SOURCE                   text file or a directory to search files in\n"
            + "-r, -R, --recursive      recursive search for files in all subdirectories\n\n"
            + "SAMPLE: java -jar TaskTerminal.jar -r sample_texts";
    private static final String FILE_NOT_FOUND = "File not found: %s";
    private static final String NO_FILES_TO_PARSE = "No file to parse...";

    public static void main(String[] args) {
        // TODO: Remove mock params.
//        args = new String[]{"-r", "sample_texts/Cicero.txt"};
//        args = new String[]{"sample_texts"};
//        args = new String[]{"-r", "sample_texts"};
        Main t = new Main();
        t.run(args);
    }

    public void run(String[] params) {
        // Preparing for DB operations.
        DBOps dbo = null;
        try {
            dbo = new DBOps();
        } catch (IOException | ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Failed to initialize JDBC", ex);
        }

        // Parse command-line parameters.
        ParsedParameters pp = parseParameters(params);
        if (ParseResult.failed.equals(pp.parseResult)) {
            if (null != pp.errorDescription && !pp.errorDescription.isEmpty()) {
                System.out.println(pp.errorDescription);
            }
            echoTerminalHelp();
            return;
        }

        Set<File> files = findFiles(pp.dirs, pp.recursive);
        files.addAll(pp.files);
        List<FileStatistic> stats = collectStatistic(files);
        stats.forEach((System.out::println));

        if (dbo != null) {
            dbo.storeResultsInDB(stats);
        }
    }

    private Set<File> findFiles(Set<File> dirs, boolean recursive) {
        Set<File> result = new LinkedHashSet<>();
        if (!dirs.isEmpty()) {
            FileFinder ff = new FileFinder();
            for (File dir : dirs) {
                Set<File> files = ff.searchDir(dir, recursive);
                result.addAll(files);
            }
        }
        return result;
    }

    private List<FileStatistic> collectStatistic(Set<File> files) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<FileStatistic>> futures = new LinkedList<>();
        for (File file : files) {
            Callable<FileStatistic> callable = new StatsCollector(file);
            Future<FileStatistic> future = executor.submit(callable);
            futures.add(future);
        }
        List<FileStatistic> result = waitForFutures(futures);
        executor.shutdown();
        return result;
    }

    private List<FileStatistic> waitForFutures(List<Future<FileStatistic>> futures) {
        List<FileStatistic> result = new LinkedList<>();
        while (0 != futures.size()) {
            Iterator<Future<FileStatistic>> i = futures.iterator();
            while (i.hasNext()) {
                Future<FileStatistic> future = i.next();
                try {
                    FileStatistic fs = future.get(50, TimeUnit.MILLISECONDS);
                    result.add(fs);
                } catch (ExecutionException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                } catch (TimeoutException | InterruptedException ex) {
                    continue;
                }
                i.remove();
            }
        }
        return result;
    }

    private void echoTerminalHelp() {
        System.out.println(HELP);
    }

    private ParsedParameters parseParameters(String[] params) {
        ParsedParameters result = new ParsedParameters();
        StringBuilder description = new StringBuilder();
        for (String param : params) {
            if ("-r".equalsIgnoreCase(param) || "--recursive".equalsIgnoreCase(param)) {
                result.recursive = true;
                continue;
            }
            File file = new File(param);
            if (!file.exists()) {
                result.parseResult = ParseResult.failed;
                description.append(String.format(FILE_NOT_FOUND, param));
                break;
            }
            if (file.isFile()) {
                result.files.add(file);
            } else if (file.isDirectory()) {
                result.dirs.add(file);
            }
        }
        if (ParseResult.ok.equals(result.parseResult)
                && result.files.isEmpty() && result.dirs.isEmpty()) {
            result.parseResult = ParseResult.failed;
            description.append(NO_FILES_TO_PARSE);
        }
        if (0 != description.length()) {
            result.errorDescription = description.toString();
        }
        return result;
    }

    

    private class ParsedParameters {

        public ParseResult parseResult = ParseResult.ok;
        public Set<File> dirs = new LinkedHashSet<>();
        public Set<File> files = new LinkedHashSet<>();
        public boolean recursive = false;
        public String errorDescription = null;
    }

    private enum ParseResult {
        ok, failed
    }
}
