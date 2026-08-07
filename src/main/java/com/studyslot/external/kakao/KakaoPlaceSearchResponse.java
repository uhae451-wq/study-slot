package com.studyslot.external.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoPlaceSearchResponse(List<Document> documents) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Document(
            String id,

            @JsonProperty("place_name")
            String placeName,

            @JsonProperty("road_address_name")
            String roadAddressName,

            @JsonProperty("address_name")
            String addressName,

            String phone,

            @JsonProperty("place_url")
            String placeUrl,

            String x,
            String y
    ) {
    }
}