package com.example.netmodel;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Request {
    private String command;
    private String jsonData;
}
