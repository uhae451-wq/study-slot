package com.studyslot.common.admin;

import com.studyslot.space.entity.Space;
import com.studyslot.space.repository.SpaceRepository;
import com.studyslot.timeslot.service.TimeSlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SpaceRepository spaceRepository;
    private final TimeSlotService timeSlotService;

    public AdminController(SpaceRepository spaceRepository, TimeSlotService timeSlotService) {
        this.spaceRepository = spaceRepository;
        this.timeSlotService = timeSlotService;
    }

    @PostMapping("/generate-slots")
    public ResponseEntity<?> generateSlotsForAllSpaces() {
        List<Space> spaces = spaceRepository.findAll();
        for (Space space : spaces) {
            timeSlotService.generateSlotsForDays(space, 7);
        }
        return ResponseEntity.ok("슬롯 생성 완료: " + spaces.size() + "개 공간");
    }
}