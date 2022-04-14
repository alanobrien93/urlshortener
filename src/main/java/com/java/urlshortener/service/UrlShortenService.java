package com.java.urlshortener.service;

public interface UrlShortenService {

    String createShortLink(String url);

    String getUrlFromId(String id);

    void validateTargets();
}
