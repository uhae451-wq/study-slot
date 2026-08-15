package com.studyslot.space.dto;

import lombok.Getter;

@Getter
public class SpaceSearchDto {
    private String type;
    private String keyword;

    public SpaceSearchDto(String type, String keyword) {
        this.type = type;
        this.keyword = keyword;
    }
}
