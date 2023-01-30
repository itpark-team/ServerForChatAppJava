package com.example.dto.message;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddNewMessageRequestDto {
    private long fromUserId;
    private long toUserId;
    private String text;
}
