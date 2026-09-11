package com.restaurant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/quan_ly_nha_hang"
            + "?useSSL=false"
            + "&serverTimezone=Asia/Ho_Chi_Minh"
            + "&characterEncoding=UTF-8"
            + "&connectTimeout=5000"
            + "&socketTimeout=5000"
            + "&tcpKeepAlive=true";

    private static final String USER = "root";

    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        DriverManager.setLoginTimeout(5);
        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    public static boolean testConnection() {
        try (Connection ketNoi = getConnection()) {
            return ketNoi != null && !ketNoi.isClosed();
        } catch (Exception e) {
            System.err.println("Kiểm tra kết nối MySQL thất bại: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {

        try {

            Connection ketNoi = getConnection();

            System.out.println("================================");
            System.out.println("KẾT NỐI MYSQL THÀNH CÔNG!");
            System.out.println("Database: quan_ly_nha_hang");
            System.out.println("================================");

            ketNoi.close();

        } catch (SQLException e) {

            System.out.println("KẾT NỐI MYSQL THẤT BẠI!");

            e.printStackTrace();
        }
    }
}