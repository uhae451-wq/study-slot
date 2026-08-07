package com.studyslot.external.kakao;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class KakaoPlaceInitializer implements CommandLineRunner {

    private final KakaoLocalClient kakaoLocalClient;

    public KakaoPlaceInitializer(KakaoLocalClient kakaoLocalClient) {
        this.kakaoLocalClient = kakaoLocalClient;
    }

    @Override
    public void run(String... args) {
        KakaoPlaceSearchResponse result =
                kakaoLocalClient.search("강남 스터디카페");

        result.documents().forEach(place ->
                System.out.println(place.placeName())
        );
    }
}