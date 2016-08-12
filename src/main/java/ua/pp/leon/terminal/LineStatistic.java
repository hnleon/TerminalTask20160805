package ua.pp.leon.terminal;

import java.util.Objects;

/**
 *
 * @author Andrii Zalevskyi <azalevskyi@gmail.com>
 */
public class LineStatistic {

    protected Integer longestWord = 0;
    protected Integer shortestWord = 0;
    protected Integer averageWord = 0;
    protected Integer lineLength = 0;

    public LineStatistic() {
        //
    }

    public LineStatistic(int longestWord, int shortestWord, int averageWord, int lineLength) {
        this.longestWord = longestWord;
        this.shortestWord = shortestWord;
        this.averageWord = averageWord;
        this.lineLength = lineLength;
    }
    
    public Integer getLongestWord() {
        return longestWord;
    }

    public void setLongestWord(Integer longestWord) {
        this.longestWord = longestWord;
    }

    public Integer getShortestWord() {
        return shortestWord;
    }

    public void setShortestWord(Integer shortestWord) {
        this.shortestWord = shortestWord;
    }

    public Integer getAverageWord() {
        return averageWord;
    }

    public void setAverageWord(Integer averageWord) {
        this.averageWord = averageWord;
    }

    public Integer getLineLength() {
        return lineLength;
    }

    public void setLineLength(Integer lineLength) {
        this.lineLength = lineLength;
    }

    @Override
    public String toString() {
        return "LineStatistic {"
                + "longestWord=" + longestWord
                + ", shortestWord=" + shortestWord
                + ", averageWord=" + averageWord
                + ", lineLength=" + lineLength + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.longestWord);
        hash = 53 * hash + Objects.hashCode(this.shortestWord);
        hash = 53 * hash + Objects.hashCode(this.averageWord);
        hash = 53 * hash + Objects.hashCode(this.lineLength);
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
        final LineStatistic other = (LineStatistic) obj;
        if (!Objects.equals(this.longestWord, other.longestWord)) {
            return false;
        }
        if (!Objects.equals(this.shortestWord, other.shortestWord)) {
            return false;
        }
        if (!Objects.equals(this.averageWord, other.averageWord)) {
            return false;
        }
        if (!Objects.equals(this.lineLength, other.lineLength)) {
            return false;
        }
        return true;
    }
}
