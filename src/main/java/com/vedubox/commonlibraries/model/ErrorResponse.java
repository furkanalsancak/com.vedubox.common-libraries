package com.vedubox.commonlibraries.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
public class ErrorResponse extends BaseResponse {

    // private static final long serialVersionUID = 1L;
    //
    // /**
    //  * - An Instant represents a moment on the timeline in UTC with a resolution of up to nanoseconds.
    //  * - The toString method generates a String object with text representing the date-time value using
    //  *   one of the standard ISO 8601 formats.
    //  */
    // @JsonProperty("Timestamp")
    // @Builder.Default
    // private String timestamp = Instant.now().toString();

    // @Nullable
    // @JsonProperty("XCorrelationId")
    // @JsonInclude(Include.NON_NULL)
    // private String xCorrelationId = "";

    @JsonProperty("Code")
    private String code = "";

    @JsonProperty("Message")
    private String message = "";

    @Nullable
    @JsonProperty("Details")
    @JsonInclude(Include.NON_NULL)
    private List<String> details = new ArrayList<String>();
}
