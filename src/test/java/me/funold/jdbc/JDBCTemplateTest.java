package me.funold.jdbc;

import me.funold.jdbc.template.*;
import me.funold.jdbc.vo.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import org.springframework.jdbc.core.*;
import org.springframework.test.annotation.*;

@JdbcTest // Jdbc Slice Test
@AutoConfigureTestDatabase(replace = Replace.NONE) // 테스트용 DB 쓰지 않도록
@Rollback(value = false) // Transactional 에 있는 테스트 변경은 기본적으론 롤백 하도록 되어있다.
public class JDBCTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("SQL Mapper - JDBC Template 실습")
    void sqlMapper_JDBCTemplateTest() {
        // given
        var accountTemplateDAO = new AccountTemplateDAO(jdbcTemplate);

        // when
        var id = accountTemplateDAO.insertAccount(new AccountVO("new user2", "new password2"));

        // then
        var account = accountTemplateDAO.selectAccount(id);
        assert account.getUsername().equals("new user2");
    }
}