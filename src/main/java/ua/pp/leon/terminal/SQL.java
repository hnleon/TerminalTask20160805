package ua.pp.leon.terminal;

/**
 *
 * @author Andrii Zalevskyi <azalevskyi@gmail.com>
 */
public class SQL {

    public static final String SELECT_FILE_BY_NAME
            = "SELECT * FROM file_statistic WHERE file_name = '%s'";
    public static final String INSERT_FILE
            = "INSERT INTO file_statistic "
            + "(file_name, longest_word, shortest_word, average_word, line_length) "
            + "VALUES ('%s', %d, %d, %d, %d);";
    public static final String UPDATE_FILE
            = "UPDATE file_statistic SET longest_word=%d, shortest_word=%d, average_word=%d, line_length=%d "
            + "WHERE id=%d;";
    public static final String DELETE_LINES
            = "DELETE FROM line_statistic WHERE file_id=%d";
    public static final String INSERT_LINES
            = "INSERT INTO line_statistic "
            + "(file_id, longest_word, shortest_word, average_word, line_length) "
            + "VALUES %s;";
    public static final String INSERT_LINES_VALUES
            = "(%d, %d, %d, %d, %d)";

}
