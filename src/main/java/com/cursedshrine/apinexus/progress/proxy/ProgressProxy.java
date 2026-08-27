package com.cursedshrine.apinexus.progress.proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

// Shape of the request body sent to the downstream auth API's /progress endpoint
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgressProxy {
    private String show;
    private String path;
    private Double position;
    private Double duration;
    private Boolean finished;
}
