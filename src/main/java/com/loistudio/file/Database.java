package com.loistudio.file;

import java.sql.*;

public class Database {
    private Connection conn;
    private Statement stmt;
    private String db_name;

    public Database(String db_name) {
        this.conn = null;
        this.stmt = null;
        this.db_name = db_name;
    }

    public void open() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + db_name);
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS data (key TEXT PRIMARY KEY, value TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String key, String value) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT OR REPLACE INTO data VALUES (?, ?)");
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT value FROM data WHERE key = ?");
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(String key) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM data WHERE key = ?");
            pstmt.setString(1, key);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}