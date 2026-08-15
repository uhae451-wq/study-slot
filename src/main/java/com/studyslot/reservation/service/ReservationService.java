package com.studyslot.reservation.service;

import com.studyslot.reservation.entity.Reservation;
import com.studyslot.reservation.repository.ReservationRepository;
import com.studyslot.timeslot.entity.TimeSlot;
import com.studyslot.timeslot.repository.TimeSlotRepository;
import com.studyslot.user.entity.User;
import com.studyslot.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeSlotRepository timeSlotRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long reserveSlot(Long userId, Long timeSlotId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 동시성 문제 방지를 위해 비관적 락으로 조회
        TimeSlot timeSlot = timeSlotRepository.findByIdForUpdate(timeSlotId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 슬롯입니다."));

        if (timeSlot.isReserved()) {
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }

        timeSlot.markReserved();

        Reservation reservation = new Reservation(user, timeSlot);
        reservationRepository.save(reservation);

        return reservation.getId();
    }

    @Transactional
    public void cancelReservation(Long userId, Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new IllegalStateException("본인 예약만 취소할 수 있습니다.");
        }

        reservation.cancel();
        reservation.getTimeSlot().markAvailable();
    }

    public List<Reservation> getMyReservations(Long userId) {
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}