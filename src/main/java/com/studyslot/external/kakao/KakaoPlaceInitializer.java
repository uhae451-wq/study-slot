package com.studyslot.external.kakao;

import com.studyslot.space.service.SpaceImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class KakaoPlaceInitializer implements CommandLineRunner {

    private final SpaceImportService spaceImportService;

    public KakaoPlaceInitializer(SpaceImportService spaceImportService) {
        this.spaceImportService = spaceImportService;
    }

    @Override
    public void run(String... args) {
        spaceImportService.importInitialSpaces();

    }
}