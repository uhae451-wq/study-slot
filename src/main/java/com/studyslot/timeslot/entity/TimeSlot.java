package com.studyslot.timeslot.entity;

import com.studyslot.space.entity.Space;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private boolean reserved; // true면 이미 예약됨

    public TimeSlot(Space space, LocalDate slotDate, LocalTime startTime, LocalTime endTime) {
        this.space = space;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reserved = false;
    }

    public void markReserved() {
        this.reserved = true;
    }

    public void markAvailable() {
        this.reserved = false;
    }
}