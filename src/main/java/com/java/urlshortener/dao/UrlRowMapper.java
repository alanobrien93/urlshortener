package com.java.urlshortener.dao;

import com.java.urlshortener.model.Url;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UrlRowMapper implements RowMapper<Url> {

    @Override
    public Url mapRow(ResultSet resultSet, int i) throws SQLException {
        Url url = new Url();

        url.setId(resultSet.getString("id"));
        url.setFull_url(resultSet.getString("full_url"));

        return url;
    }

}
