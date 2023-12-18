package me.funold.jdbc;

import java.sql.*;
import me.funold.jdbc.dao.*;
import me.funold.jdbc.vo.*;
import org.junit.jupiter.api.*;

public class JDBCTest {

    @Test
    @DisplayName("JDBC 테이블 생성 실습")
    void jdbcTest() throws SQLException {
        // docker run -p 5432:5432 -e POSTGRES_PASSWORD=pass -e POSTGRES_USER=teasun -e POSTGRES_DB=messenger --name postgres_boot -d postgres
        //-p 포트번호, -e 환경변수, -d 데몬으로 백그라운드에 띄우겠다?
        // docker exec -i -t postgres_boot bash
        //docker실행 후 postgress_boot란 이름의 컨테이너에 접속, bash명령어 실행
        // su - postgres
        //계정을 root => postgres로 변경
        // psql --username teasun --dbname messenger
        //사용자 이름과 DB 이름을 함께 명시해 접속
        // \list (데이터 베이스 조회)
        // \dt (테이블 조회)

        //JAVA APP에서 Container에 접속하기 위해 postgresQL의 주소를 입력
        String url = "jdbc:postgresql://localhost:5432/messenger"; //해당 경로로
        String username = "teasun"; //해당 이름과
        String password = "pass"; //해당 비밀번호로 접속

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String createSql = "CREATE TABLE ACCOUNT (id SERIAL PRIMARY KEY ,username varchar(255), password varchar(255))";
            PreparedStatement statement = connection.prepareStatement(createSql);
            statement.execute(); //Query 실행

            //자원 해제
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("JDBC 삽입/조회 실습")
    void jdbcInsertSelectTest() throws SQLException {
        // given
        String url = "jdbc:postgresql://localhost:5432/messenger";
        String username = "teasun";
        String password = "pass";

        // when
        //'try with resource' - 문단이 끝날 때 자동으로 close()` => 따로 .close()필요 x
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection created: " + connection);

            String insertSql = "INSERT INTO ACCOUNT (id, username, password) VALUES ((SELECT coalesce(MAX(ID), 0) + 1 FROM ACCOUNT A), 'elsa', 'icecream4')";
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.execute();
            }

            // then
            String selectSql = "SELECT * FROM ACCOUNT";
            try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
                var rs = statement.executeQuery();
                while (rs.next()) {
                    System.out.printf("%d, %s, %s", rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"));
                }
            }
        }
    }

    @Test
    @DisplayName("JDBC DAO 삽입/조회 실습")
    void jdbcDAOInsertSelectTest() throws SQLException {
        // given
        AccountDAO accountDAO = new AccountDAO();

        // when
        var id = accountDAO.insertAccount(new AccountVO("new user", "new password"));

        // then
        var account = accountDAO.selectAccount(id);
        assert account.getUsername().equals("new user");
    }

}
