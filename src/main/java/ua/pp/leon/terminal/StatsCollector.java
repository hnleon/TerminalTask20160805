package ua.pp.leon.terminal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrii Zalevskyi <azalevskyi@gmail.com>
 */
public class StatsCollector implements Callable<FileStatistic> {

    private static final Logger LOG = Logger.getLogger(StatsCollector.class.getName());

    private File source = null;

    public StatsCollector() {
        //
    }

    public StatsCollector(File source) {
        this.source = source;
    }

    public void setSource(File source) {
        this.source = source;
    }

    @Override
    public FileStatistic call() throws Exception {
        FileStatistic result = new FileStatistic();
        result.setFileName(source.getName());
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (0 == line.length()) {
                    continue;
                }
                LineStatistic ls = proccessLine(line);
                result.appendLine(ls);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        calculateFileStatistic(result);
        return result;
    }

    protected LineStatistic proccessLine(String line) {
        LineStatistic result = new LineStatistic();
        result.setLineLength(line.length());
        int wordsInLine = 0;
        long totalWordsLength = 0;
        String[] words = line.split("[\\p{Punct}\\s]+");
        if (words.length != 0) {
            result.setLongestWord(Integer.MIN_VALUE);
            result.setShortestWord(Integer.MAX_VALUE);
        }
        for (String word : words) {
            int length = word.length();
            wordsInLine++;
            totalWordsLength += length;
            if (result.getLongestWord() < length) {
                result.setLongestWord(length);
            }
            if (0 < length && length < result.getShortestWord()) {
                result.setShortestWord(length);
            }
        }
        if (wordsInLine != 0) {
            result.setAverageWord((int) (totalWordsLength / wordsInLine));
        }
        return result;
    }

    protected void calculateFileStatistic(FileStatistic fs) {
        List<LineStatistic> lines = fs.getLines();
        int linesCount = lines.size();
        long longestWord = 0;
        long shortestWord = 0;
        long averageWord = 0;
        long lineLength = 0;
        for (LineStatistic line : lines) {
            longestWord += line.getLongestWord();
            shortestWord += line.getShortestWord();
            averageWord += line.getAverageWord();
            lineLength += line.getLineLength();
        }
        fs.setLongestWord((int) Math.round((double) longestWord / linesCount));
        fs.setShortestWord((int) Math.round((double) shortestWord / linesCount));
        fs.setAverageWord((int) Math.round((double) averageWord / linesCount));
        fs.setLineLength((int) Math.round((double) lineLength / linesCount));
    }
}
