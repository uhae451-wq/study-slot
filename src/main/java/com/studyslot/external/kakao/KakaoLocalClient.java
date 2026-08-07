package com.studyslot.external.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoLocalClient {

    private final RestClient restClient;

    public KakaoLocalClient(@Value("${kakao.rest-api-key}") String restApiKey) {
        this.restClient = RestClient.builder()
                            .baseUrl("https://dapi.kakao.com")
                            .defaultHeader(HttpHeaders.AUTHORIZATION,"KakaoAK " + restApiKey)
                            .build();
    }

    public KakaoPlaceSearchResponse search(String query) {
        return restClient.get()
                        .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", query)
                        .build())
                        .retrieve()
                        .body(KakaoPlaceSearchResponse.class);
    }
}