package com.vedubox.commonlibraries.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SuccessResponse<T> extends BaseResponse {

    @JsonProperty("Data")
    private T data;
}
