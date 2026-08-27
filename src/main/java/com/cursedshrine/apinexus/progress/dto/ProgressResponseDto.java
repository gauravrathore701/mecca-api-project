package com.cursedshrine.apinexus.progress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponseDto {
    private boolean success;
    private String message;
    private Object data;
}
