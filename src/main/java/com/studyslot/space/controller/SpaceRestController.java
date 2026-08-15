package com.studyslot.space.controller;

import com.studyslot.space.dto.SpaceResponse;
import com.studyslot.space.repository.SpaceRepository;
import com.studyslot.space.service.SpaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
public class SpaceRestController {

    private final SpaceRepository spaceRepository;
    private final SpaceService spaceService;

    public SpaceRestController(SpaceRepository spaceRepository,SpaceService spaceService) {
        this.spaceService = spaceService;
        this.spaceRepository = spaceRepository;
    }

    @GetMapping
    public List<SpaceResponse> getSpaces(@RequestParam(required = false) String keyword) {
        return spaceService.search(keyword)
                .stream()
                .map(SpaceResponse::new)
                .toList();
    }
}