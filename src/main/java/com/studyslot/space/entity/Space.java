package com.studyslot.space.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "spaces")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_place_id", nullable = false, unique = true)
    private String kakaoPlaceId;

    @Column(nullable = false)
    private String name;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "address")
    private String address;

    private String phone;

    private Double longitude;

    private Double latitude;

    @Column(name = "place_url")
    private String placeUrl;

    public Space(
            String kakaoPlaceId,
            String name,
            String roadAddress,
            String address,
            String phone,
            Double longitude,
            Double latitude,
            String placeUrl
    ) {
        this.kakaoPlaceId = kakaoPlaceId;
        this.name = name;
        this.roadAddress = roadAddress;
        this.address = address;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
        this.placeUrl = placeUrl;
    }
}