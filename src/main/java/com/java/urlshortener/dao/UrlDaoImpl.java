package com.java.urlshortener.dao;

import com.java.urlshortener.model.Url;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UrlDaoImpl implements  UrlDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(String shortUrl, String url) {
        Url existingUrl = getUrl(shortUrl);
        if(existingUrl==null) {
            String sql = "insert into TBL_URL (id, full_url) values (?, ?)";
            jdbcTemplate.update(sql, shortUrl, url);
        }
    }

    @Override
    public Url getUrl(String id) {
        String sql = "Select * from TBL_URL where id = ?";
        try{
         return jdbcTemplate.queryForObject(sql, new UrlRowMapper(), id );
        } catch (EmptyResultDataAccessException e){
            log.info("No result found for id: " + id);
        }
        return null;
    }

    @Override
    public List<Url> getAll(){
        String sql = "Select * from TBL_URL";
        return jdbcTemplate.query(sql, new UrlRowMapper() );
    }
}
