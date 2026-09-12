import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public final class Database {
    private static final String URL = System.getProperty(
            "sudoku.db.url", "jdbc:mysql://localhost:3306/gamedb?serverTimezone=UTC");
    private static final String USER = System.getProperty(
            "sudoku.db.user", System.getenv().getOrDefault("SUDOKU_DB_USER", "root"));
    private static final String PASSWORD = System.getProperty(
            "sudoku.db.password", System.getenv().getOrDefault("SUDOKU_DB_PASSWORD", ""));

    private Database() {
    }

    public static LoginResult authenticate(String username, String password) throws SQLException {
        String userSql = "SELECT user_id, username FROM `User` WHERE username = ? AND password = ?";
        String adminSql = "SELECT admin_id, admin_name FROM Admin WHERE admin_name = ? AND password = ?";

        try (Connection connection = getConnection();
                PreparedStatement userStatement = connection.prepareStatement(userSql);
                PreparedStatement adminStatement = connection.prepareStatement(adminSql)) {
            userStatement.setString(1, username);
            userStatement.setString(2, password);
            try (ResultSet result = userStatement.executeQuery()) {
                if (result.next()) {
                    return new LoginResult(result.getString("username"), false, result.getInt("user_id"));
                }
            }

            adminStatement.setString(1, username);
            adminStatement.setString(2, password);
            try (ResultSet result = adminStatement.executeQuery()) {
                if (result.next()) {
                    return new LoginResult(result.getString("admin_name"), true, -1);
                }
            }
        }
        return null;
    }

    public static boolean registerUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO `User` (username, password) VALUES (?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException exception) {
            return false;
        }
    }

    public static List<PlayerStatus> getPlayerStatuses() throws SQLException {
        String sql = "SELECT u.username, u.password, "
                + "COALESCE(SUM(gs.matches_played), 0) AS matches_played, "
                + "COALESCE(SUM(gs.score), 0) AS score "
                + "FROM `User` u LEFT JOIN Game_Stat gs ON gs.user_id = u.user_id "
                + "GROUP BY u.user_id, u.username, u.password ORDER BY u.username";
        List<PlayerStatus> players = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                players.add(new PlayerStatus(
                        result.getString("username"),
                        result.getString("password"),
                        result.getInt("matches_played"),
                        result.getInt("score")));
            }
        }
        return players;
    }

    public static void recordMatch(int userId, int score) throws SQLException {
        String sql = "INSERT INTO Game_Stat (username, matches_played, score, user_id) "
                + "SELECT username, 1, ?, user_id FROM `User` WHERE user_id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, score);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    public static int getTotalScore(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(score), 0) AS total_score "
                + "FROM Game_Stat WHERE user_id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getInt("total_score");
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static final class LoginResult {
        private final String username;
        private final boolean admin;
        private final int userId;

        private LoginResult(String username, boolean admin, int userId) {
            this.username = username;
            this.admin = admin;
            this.userId = userId;
        }

        public String getUsername() { return username; }
        public boolean isAdmin() { return admin; }
        public int getUserId() { return userId; }
    }

    public static final class PlayerStatus {
        private final String username;
        private final String password;
        private final int matchesPlayed;
        private final int score;

        private PlayerStatus(String username, String password, int matchesPlayed, int score) {
            this.username = username;
            this.password = password;
            this.matchesPlayed = matchesPlayed;
            this.score = score;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public int getMatchesPlayed() { return matchesPlayed; }
        public int getScore() { return score; }
    }
}