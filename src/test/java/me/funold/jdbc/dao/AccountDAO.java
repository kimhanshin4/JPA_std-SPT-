package me.funold.jdbc.dao;

import java.sql.*;
import me.funold.jdbc.vo.*;

public class AccountDAO {

    //JDBC 관련 변수
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    private static final String url = "jdbc:postgresql://localhost:5432/messenger";
    private static final String username = "teasun";
    private static final String password = "pass";
    //SQL 쿼리 명령어
    private final String ACCOUNT_INSERT = "INSERT INTO account(ID, USERNAME, PASSWORD) "
        + "VALUES((SELECT coalesce(MAX(ID), 0) + 1 FROM ACCOUNT A), ?, ?)";
    private final String ACCOUNT_SELECT = "SELECT * FROM account WHERE ID = ?";

    //CRUD 기능 메서드
    public Integer insertAccount(AccountVO vo) {
        var id = -1;
        String[] returnId = {"id"};
        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.prepareStatement(ACCOUNT_INSERT, returnId);
            stmt.setString(1, vo.getUsername());
            stmt.setString(2, vo.getPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public AccountVO selectAccount(Integer id) {
        AccountVO vo = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.prepareStatement(ACCOUNT_SELECT);
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                vo = new AccountVO();
                vo.setId(rs.getInt("ID"));
                vo.setUsername(rs.getString("USERNAME"));
                vo.setPassword(rs.getString("PASSWORD"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vo;
    }


}
