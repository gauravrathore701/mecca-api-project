package com.cursedshrine.apinexus.progress.dto;

import lombok.Data;

@Data
public class ProgressRequestDto {
    private String show;
    private String path;
    private Double position;
    private Double duration;
    private Boolean finished;
}
