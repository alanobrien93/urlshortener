package com.java.urlshortener.model;

import lombok.Data;

@Data
public class UrlErrorResponse {

    private String status;
    private String error;

    public UrlErrorResponse(String status, String error){
        this.status = status;
        this.error = error;
    }

}
