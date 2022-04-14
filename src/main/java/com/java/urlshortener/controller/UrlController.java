package com.java.urlshortener.controller;

import com.java.urlshortener.config.MyTask;
import com.java.urlshortener.model.UrlErrorResponse;
import com.java.urlshortener.model.UrlRequest;
import com.java.urlshortener.service.UrlShortenService;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping(value = "/shortenUrl")
public class UrlController {

    @Autowired
    private UrlShortenService urlShortenService;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @PostMapping(value = "/create",  consumes = {"application/json"})
    public ResponseEntity createShortLink(@RequestBody final UrlRequest urlRequest, HttpServletRequest request) {
        UrlValidator urlValidator = new UrlValidator();
        if(!urlValidator.isValid(urlRequest.getUrl())){
            UrlErrorResponse errorResponse = new UrlErrorResponse("Invalid Url", "400");
            return new ResponseEntity<UrlErrorResponse>(errorResponse, HttpStatus.OK);
        }
        final String id = urlShortenService.createShortLink(urlRequest.getUrl());
        String shortUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/shortenUrl/" + id;
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping(value ="/{id}")
    public ResponseEntity<?> redirect(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(id.isEmpty()){
            UrlErrorResponse errorResponse = new UrlErrorResponse("Invalid Url", "400");
            return new ResponseEntity<UrlErrorResponse>(errorResponse, HttpStatus.OK);
        }
        String redirectUrl = urlShortenService.getUrlFromId(id);
        if(redirectUrl.isEmpty()){
            UrlErrorResponse errorResponse = new UrlErrorResponse("Url does not exist", "404");
            return new ResponseEntity<UrlErrorResponse>(errorResponse, HttpStatus.OK);
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUrl);
        response.sendRedirect(redirectUrl);
        return null;
    }

    @GetMapping(value ="/checkProcess")
    public ResponseEntity<String> checkProcess() {
        if(threadPoolTaskScheduler.getScheduledThreadPoolExecutor().getQueue().size()==0){
            threadPoolTaskScheduler.scheduleAtFixedRate(new MyTask(urlShortenService), Instant.now(), Duration.ofSeconds(5));
            return ResponseEntity.ok("Restarting background process");
        } else{
            return ResponseEntity.ok("Background process already running");
        }
    }
}
