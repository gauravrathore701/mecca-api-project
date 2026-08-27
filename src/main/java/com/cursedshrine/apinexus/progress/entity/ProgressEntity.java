package com.cursedshrine.apinexus.progress.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressEntity {
    private String show;
    private String path;
    private Double position;
    private Double duration;
    private Boolean finished;
}
