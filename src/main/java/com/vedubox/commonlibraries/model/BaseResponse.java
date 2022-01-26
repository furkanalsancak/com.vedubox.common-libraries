package com.vedubox.commonlibraries.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Getter
@SuperBuilder
public abstract class BaseResponse {

    private static final long serialVersionUID = 1L;

    // public BaseResponse() {};

    /**
     * - An Instant represents a moment on the timeline in UTC with a resolution of up to nanoseconds.
     * - The toString method generates a String object with text representing the date-time value using
     *   one of the standard ISO 8601 formats.
     */
    @Builder.Default
    @JsonProperty("Timestamp")
    private String timestamp = Instant.now().toString();

    @Nullable
    @Builder.Default
    @JsonProperty("XCorrelationId")
    // @JsonInclude(JsonInclude.Include.NON_NULL)
    private String xCorrelationId = null;
}
