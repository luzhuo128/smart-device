package com.v1.control.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyMessage implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    private String topic;
    private String content;
    private int qos;
}