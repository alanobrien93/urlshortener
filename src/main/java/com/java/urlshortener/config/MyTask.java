package com.java.urlshortener.config;

import com.java.urlshortener.service.UrlShortenService;

public class MyTask implements Runnable {

    private final UrlShortenService urlShortenService;

    public MyTask(UrlShortenService urlShortenService) {
        this.urlShortenService = urlShortenService;
    }

    @Override
    public void run() {
        urlShortenService.validateTargets();
    }
}
