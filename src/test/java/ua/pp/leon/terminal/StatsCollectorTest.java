package ua.pp.leon.terminal;

import java.io.File;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrii Zalevskyi <azalevskyi@gmail.com>
 */
public class StatsCollectorTest extends StatsCollector {

    public StatsCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGeneral() throws Throwable {
        File file = new File("sample_texts/for_tests/general.txt");
        StatsCollector sc = new StatsCollector(file);
        FileStatistic fs = sc.call();
        assertEquals("Longest word", 4, fs.longestWord.intValue());
        assertEquals("Shortest word", 3, fs.shortestWord.intValue());
        assertEquals("Average word", 3, fs.averageWord.intValue());
        assertEquals("Line length", 10, fs.lineLength.intValue());
        assertEquals("Lines count", 3, fs.lines.size());
    }

    @Test
    public void testEmptyFile() throws Throwable {
        FileStatistic reference = new FileStatistic("empty.txt", new LinkedList<LineStatistic>(), 0, 0, 0, 0);
        File file = new File("sample_texts/for_tests/empty.txt");
        StatsCollector sc = new StatsCollector(file);
        FileStatistic fs = sc.call();
        assertEquals(reference, fs);
    }

    @Test
    public void testNoWordsFile() throws Throwable {
        File file = new File("sample_texts/for_tests/no_words.txt");
        StatsCollector sc = new StatsCollector(file);
        FileStatistic fs = sc.call();
        assertEquals("Longest word", 0, fs.longestWord.intValue());
        assertEquals("Shortest word", 0, fs.shortestWord.intValue());
        assertEquals("Average word", 0, fs.averageWord.intValue());
        assertEquals("Line length", 9, fs.lineLength.intValue());
        assertEquals("Lines count", 5, fs.lines.size());
    }

    @Test
    public void testProccessLine() throws Throwable {
        LineStatistic reference = new LineStatistic(4, 3, 3, 8);
        StatsCollector sc = new StatsCollector(null);
        LineStatistic ls = sc.proccessLine("123 4567");
        assertEquals(reference, ls);
    }
}
