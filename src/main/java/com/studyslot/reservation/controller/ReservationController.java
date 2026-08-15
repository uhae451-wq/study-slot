package com.studyslot.reservation.controller;

import com.studyslot.space.entity.Space;
import com.studyslot.space.repository.SpaceRepository;
import com.studyslot.timeslot.entity.TimeSlot;
import com.studyslot.timeslot.repository.TimeSlotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final SpaceRepository spaceRepository;
    private final TimeSlotRepository timeSlotRepository;

    public ReservationController(SpaceRepository spaceRepository,TimeSlotRepository timeSlotRepository) {
        this.spaceRepository = spaceRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @GetMapping
    public String reservationAction(@AuthenticationPrincipal Long userId,@RequestParam("spaceId") Long spaceId,
                                    @RequestParam(value = "date", required = false) String date,Model model) {
        if (userId == null) {
            return "redirect:/user/login?needSignup=true";
        }
        LocalDate targetDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();

        Space space = spaceRepository.findById(spaceId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));

        List<TimeSlot> slots = timeSlotRepository.findBySpaceIdAndSlotDateOrderByStartTime(spaceId, targetDate);

        // 날짜 탭에 뿌릴 날짜 목록 (오늘부터 7일치)
        List<LocalDate> availableDates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            availableDates.add(today.plusDays(i));
        }
        model.addAttribute("space", space);
        model.addAttribute("slots", slots);
        model.addAttribute("date", targetDate);
        model.addAttribute("availableDates", availableDates);
        return "reservation/action";
    }
}
