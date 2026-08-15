package com.studyslot.timeslot.service;

import com.studyslot.space.entity.Space;
import com.studyslot.timeslot.entity.TimeSlot;
import com.studyslot.timeslot.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    // 특정 공간의 특정 날짜에 1시간 단위 슬롯 생성 (운영시간 09~22시 가정)
    public void generateDailySlots(Space space, LocalDate date) {
        LocalTime openTime = LocalTime.of(9, 0);
        LocalTime closeTime = LocalTime.of(22, 0);

        List<TimeSlot> slots = new ArrayList<>();
        LocalTime cursor = openTime;

        while (cursor.isBefore(closeTime)) {
            LocalTime next = cursor.plusHours(1);
            slots.add(new TimeSlot(space, date, cursor, next));
            cursor = next;
        }

        timeSlotRepository.saveAll(slots);
    }

    // Space 등록 시 앞으로 N일치 슬롯 한번에 생성
    public void generateSlotsForDays(Space space, int days) {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < days; i++) {
            generateDailySlots(space, today.plusDays(i));
        }
    }
}