package com.lvonce.wind;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class WindHttpResource {

    @Data
    public static class WindHttpResourceList {

        @JsonProperty
        List<WindHttpResource> https;

        @JsonCreator
        public WindHttpResourceList() {
        }

        @JsonCreator
        public WindHttpResourceList(@JsonProperty("https") List<WindHttpResource> https) {
            this.https = https;
        }

    }

    @Data
    public static class InputFetcher {
        @JsonProperty
        String sourceName;
        @JsonProperty
        String targetName;
        @JsonProperty
        String sourceType;
        @JsonProperty
        String targetType;
        @JsonProperty
        String validator;

        @JsonCreator
        public InputFetcher() {}

        @JsonCreator
        public InputFetcher(@JsonProperty("sourceName") String sourceName,
                            @JsonProperty("targetName") String targetName,
                            @JsonProperty("sourceType") String sourceType,
                            @JsonProperty("targetType") String targetType,
                            @JsonProperty("validator") String validator) {
            this.sourceName = sourceName;
            this.targetName = targetName;
            this.sourceType = sourceType;
            this.targetType = targetType;
            this.validator = validator;
        }
    }

    @Data
    public static class OutputFetcher {
        @JsonProperty
        String sourceName;
        @JsonProperty
        String targetName;
        @JsonProperty
        String sourceType;
        @JsonProperty
        String targetType;

        @JsonCreator
        public OutputFetcher(){}

        @JsonCreator
        public OutputFetcher(@JsonProperty("sourceName") String sourceName,
                             @JsonProperty("targetName") String targetName,
                             @JsonProperty("sourceType") String sourceType,
                             @JsonProperty("targetType") String targetType) {
            this.sourceName = sourceName;
            this.targetName = targetName;
            this.sourceType = sourceType;
            this.targetType = targetType;
        }
    }


    @JsonProperty
    String url;
    @JsonProperty
    String method;
    @JsonProperty
    List<InputFetcher> headers;
    @JsonProperty
    List<InputFetcher> params;
    @JsonProperty
    String body;
    @JsonProperty
    String template;
    @JsonProperty
    List<OutputFetcher> outputs;

    @JsonCreator
    public WindHttpResource(
            @JsonProperty("url") String url,
            @JsonProperty("method") String method,
            @JsonProperty("headers") List<InputFetcher> headers,
            @JsonProperty("params") List<InputFetcher> params,
            @JsonProperty("body") String body,
            @JsonProperty("template") String template,
            @JsonProperty("outputs") List<OutputFetcher> outputs
    ) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.params = params;
        this.body = body;
        this.template = template;
        this.outputs = outputs;

    }
}
