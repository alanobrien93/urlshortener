package com.java.urlshortener.config;

import com.java.urlshortener.service.UrlShortenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;

@Component
public class MyScheduler {

    @Autowired
    private UrlShortenService urlShortenService;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @PostConstruct
    public void startScheduler() {
        threadPoolTaskScheduler.scheduleAtFixedRate(new MyTask(urlShortenService),Instant.now(), Duration.ofMinutes(5));
    }
}
