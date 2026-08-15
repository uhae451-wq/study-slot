package com.studyslot.reservation.controller;

import com.studyslot.reservation.dto.ReservationRequestDto;
import com.studyslot.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> reserve(@AuthenticationPrincipal Long userId,
                                     @RequestBody ReservationRequestDto dto) {
        try {
            Long reservationId = reservationService.reserveSlot(userId, dto.getTimeSlotId());
            return ResponseEntity.ok(Map.of("reservationId", reservationId));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> cancel(@AuthenticationPrincipal Long userId,
                                    @PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(userId, reservationId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}