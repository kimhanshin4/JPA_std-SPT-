package me.funold.jdbc.template;

import java.sql.*;
import me.funold.jdbc.vo.*;
import org.springframework.jdbc.core.*;

public class AccountRowMapper implements RowMapper<AccountVO> {

    @Override
    public AccountVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        var vo = new AccountVO();
        vo.setId(rs.getInt("ID"));
        vo.setUsername(rs.getString("USERNAME"));
        vo.setPassword(rs.getString("PASSWORD"));
        return vo;
    }
}
