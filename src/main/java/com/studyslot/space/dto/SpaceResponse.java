package com.studyslot.space.dto;

import com.studyslot.space.entity.Space;

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

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRoadAddress() { return roadAddress; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getPlaceUrl() { return placeUrl; }
}