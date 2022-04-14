package com.java.urlshortener.service;

import com.google.common.hash.Hashing;
import com.java.urlshortener.dao.UrlDao;
import com.java.urlshortener.model.Url;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class UrlShortenServiceImpl implements  UrlShortenService {

    @Autowired
    private UrlDao urlDao;

    public UrlShortenServiceImpl(UrlDao urlDao){
        this.urlDao =urlDao;
    }

    @Override
    public String createShortLink(String url){
        String shortUrl = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
        urlDao.save(shortUrl, url);

        return shortUrl;
    }

    @Override
    @Cacheable(value="urls", key="#id")
    public String getUrlFromId(String id){
        Url url = urlDao.getUrl(id);
        return url!=null ? url.getFull_url() : "";
    }

    @Override
    public  void validateTargets() {
        UrlValidator urlValidator = new UrlValidator();
        List<Url> urlList = urlDao.getAll();
        urlList.parallelStream().forEach(x -> log.info(x.getFull_url() +
                (urlValidator.isValid(x.getFull_url()) ? " is Valid" : " Not Valid")));
    }
}
