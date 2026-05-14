package database;
import java.sql.*;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:src/database/CampusEvent.db";
    public static Connection conn = connectDatabase(DB_URL);

    public static ResultSet getRS(String sql,Object...params){
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for(int i = 0; i < params.length; i++){
                pstmt.setObject(i + 1, params[i]);
            }
            return pstmt.executeQuery(); 
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void execute(String sql, Object...params){
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for(int i = 0; i < params.length; i++){
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection connectDatabase(String URL){
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
