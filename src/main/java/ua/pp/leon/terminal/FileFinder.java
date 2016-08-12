package ua.pp.leon.terminal;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Performs search (recursive if required) of supported files.
 *
 * @author Andrii Zalevskyi <azalevskyi@gmail.com>
 */
public class FileFinder {

    private static final Logger LOG = Logger.getLogger(FileFinder.class.getName());

    /**
     * Performs a search of media files in provided folder(s). Can search recursively in all
     * sub-directories.
     *
     * @param dir initial directory.
     * @param recursive use recursive search.
     * @return 
     */
    public Set<File> searchDir(File dir, boolean recursive) {
        Set<File> result = new LinkedHashSet<>();
        File[] files = dir.listFiles(); // TODO: Try to use FilenameFilter.
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && recursive) {
                    result.addAll(searchDir(file, recursive));
                } else {
                    String name = file.getName();
                    if (isSupported(name)) {
//                        LOG.log(Level.INFO, "File found: {0}", file.getAbsolutePath());
                        result.add(file);
                    }
                }
            }
        }
        return result;
    }

    private boolean isSupported(String fileName) {
        boolean result = false;
        for (SupportedFiles type : SupportedFiles.values()) {
            if (fileName.endsWith(type.toString())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private enum SupportedFiles {
        TXT(".txt"),
        LOG(".log");

        private final String extension;

        SupportedFiles(String extension) {
            this.extension = extension;
        }

        @Override
        public String toString() {
            return extension;
        }
    }
}
