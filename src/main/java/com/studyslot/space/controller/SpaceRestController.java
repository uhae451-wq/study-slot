package com.studyslot.space.controller;

import com.studyslot.space.dto.SpaceResponse;
import com.studyslot.space.repository.SpaceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
public class SpaceRestController {

    private final SpaceRepository spaceRepository;

    public SpaceRestController(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @GetMapping
    public List<SpaceResponse> getSpaces() {
        return spaceRepository.findAll()
                .stream()
                .map(SpaceResponse::new)
                .toList();
    }
}