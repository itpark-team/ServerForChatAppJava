package com.example.netmodel;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response {
    private int status;
    private String data;
}
