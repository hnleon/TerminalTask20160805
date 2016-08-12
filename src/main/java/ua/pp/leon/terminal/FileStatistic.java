package ua.pp.leon.terminal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Andrii Zalevskyi <azalevskyi@gmail.com>
 */
public class FileStatistic extends LineStatistic {

    protected String fileName = null;
    protected List<LineStatistic> lines = new LinkedList<>();

    public FileStatistic() {
        //
    }

    public FileStatistic(String fileName, List<LineStatistic> lines,
            int longestWord, int shortestWord, int averageWord, int lineLength) {
        this.fileName = fileName;
        this.lines = lines;
        this.longestWord = longestWord;
        this.shortestWord = shortestWord;
        this.averageWord = averageWord;
        this.lineLength = lineLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<LineStatistic> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public void appendLine(LineStatistic ls) {
        lines.add(ls);
    }

    @Override
    public String toString() {
        return "FileStatistic {"
                + "fileName=" + fileName
                + ", longestWord=" + longestWord
                + ", shortestWord=" + shortestWord
                + ", averageWord=" + averageWord
                + ", lineLength=" + lineLength
                + '}';
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + Objects.hashCode(this.fileName);
        hash = 37 * hash + Objects.hashCode(this.lines);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final FileStatistic other = (FileStatistic) obj;
        if (!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        if (!Objects.equals(this.lines, other.lines)) {
            return false;
        }
        return true;
    }
}
