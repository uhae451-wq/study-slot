package com.studyslot.space.service;

import com.studyslot.external.kakao.KakaoLocalClient;
import com.studyslot.external.kakao.KakaoPlaceSearchResponse;
import com.studyslot.space.entity.Space;
import com.studyslot.space.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpaceImportService {

    private final KakaoLocalClient kakaoLocalClient;
    private final SpaceRepository spaceRepository;

    public SpaceImportService(
            KakaoLocalClient kakaoLocalClient,
            SpaceRepository spaceRepository
    ) {
        this.kakaoLocalClient = kakaoLocalClient;
        this.spaceRepository = spaceRepository;
    }

    public void importInitialSpaces() {
        if (spaceRepository.count() > 0) {
            System.out.println("이미 공간 데이터가 있어 가져오기를 건너뜁니다.");
            return;
        }

        KakaoPlaceSearchResponse result =
                kakaoLocalClient.search("강남 스터디카페");

        List<Space> spaces = result.documents()
                .stream()
                .map(this::toSpace)
                .toList();

        spaceRepository.saveAll(spaces);

        System.out.println(spaces.size() + "개의 공간을 저장했습니다.");
    }

    private Space toSpace(KakaoPlaceSearchResponse.Document place) {
        return new Space(
                place.id(),
                place.placeName(),
                place.roadAddressName(),
                place.addressName(),
                place.phone(),
                Double.parseDouble(place.x()),
                Double.parseDouble(place.y()),
                place.placeUrl()
        );
    }


}