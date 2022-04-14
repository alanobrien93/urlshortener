package com.java.urlshortener.dao;

import com.java.urlshortener.model.Url;

import java.util.List;

public interface UrlDao {

    void save(String shortUrl, String url);

    Url getUrl(String id);

    List<Url> getAll();
}
