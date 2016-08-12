package ua.pp.leon.terminal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrii Zalevskyi <azalevskyi@gmail.com>
 */
public class DBOps {

    private static final Logger LOG = Logger.getLogger(DBOps.class.getName());

    private final Properties properties = new Properties();

    public DBOps() throws IOException, ClassNotFoundException {
        // Read application configuration from properties.
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = loader.getResourceAsStream("application.properties")) {
            properties.load(is);
        }
        // Register JDBC driver.
        Class.forName(properties.getProperty("datasource.driver-class-name"));
    }

    public void storeResultsInDB(List<FileStatistic> fss) {
        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("datasource.url"),
                properties.getProperty("datasource.username"),
                properties.getProperty("datasource.password"))) {
            for (FileStatistic fs : fss) {
                int fileId = storeFile(connection, fs);
                if (fileId != 0 && !fs.getLines().isEmpty()) {
                    storeLines(connection, fileId, fs.getLines());
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private int storeFile(Connection connection, FileStatistic fs) {
        int fileId = getFileId(connection, fs.getFileName());
        if (fileId == 0) {
            fileId = insertNewFile(connection, fs);
        } else {
            updateFile(connection, fs, fileId);
            deleteLines(connection, fileId);
        }
        return fileId;
    }

    private int getFileId(Connection connection, String fileName) {
        int id = 0;
        String sql = String.format(SQL.SELECT_FILE_BY_NAME, fileName);
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    id = rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return id;
    }

    private int insertNewFile(Connection connection, FileStatistic fs) {
        int fileId = 0;
        String sql = String.format(SQL.INSERT_FILE, fs.getFileName(), fs.getLongestWord(),
                fs.getShortestWord(), fs.getAverageWord(), fs.getLineLength());
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet rs = statement.getGeneratedKeys()) {
                while (rs.next()) {
                    fileId = rs.getInt("GENERATED_KEY");
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return fileId;
    }

    private void updateFile(Connection connection, FileStatistic fs, int fileId) {
        String sql = String.format(SQL.UPDATE_FILE, fs.getLongestWord(), fs.getShortestWord(),
                fs.getAverageWord(), fs.getLineLength(), fileId);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private void deleteLines(Connection connection, int fileId) {
        String sql = String.format(SQL.DELETE_LINES, fileId);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private void storeLines(Connection connection, int id, List<LineStatistic> lines) {
        StringBuilder values = new StringBuilder();
        Iterator<LineStatistic> i = lines.iterator();
        while (i.hasNext()) {
            LineStatistic l = i.next();
            values.append(String.format(SQL.INSERT_LINES_VALUES, id, l.getLongestWord(),
                    l.getShortestWord(), l.getAverageWord(), l.getLineLength()));
            if (i.hasNext()) {
                values.append(",");
            }
        }
        String sql = String.format(SQL.INSERT_LINES, values.toString());
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, sql, ex);
        }
    }
}
