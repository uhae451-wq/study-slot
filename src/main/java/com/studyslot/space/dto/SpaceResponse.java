package com.studyslot.space.dto;

import com.studyslot.space.entity.Space;
import lombok.Getter;

@Getter
public class SpaceResponse {

    private final Long id;
    private final String name;
    private final String roadAddress;
    private final String address;
    private final String phone;
    private final Double latitude;
    private final Double longitude;
    private final String placeUrl;

    public SpaceResponse(Space space) {
        this.id = space.getId();
        this.name = space.getName();
        this.roadAddress = space.getRoadAddress();
        this.address = space.getAddress();
        this.phone = space.getPhone();
        this.latitude = space.getLatitude();
        this.longitude = space.getLongitude();
        this.placeUrl = space.getPlaceUrl();
    }
}