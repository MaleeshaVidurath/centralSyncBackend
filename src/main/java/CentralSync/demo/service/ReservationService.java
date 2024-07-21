package CentralSync.demo.service;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.Reservation;
import CentralSync.demo.model.Status;

import java.util.List;

public interface ReservationService {
    public Reservation saveReservation(Reservation reservation);

    public List<Reservation> getAllReservation();

    public Reservation getReservationById(Long resId);

    public Reservation updateReservationById(Reservation newReservation, Long resId);

    public String deleteReservationById(Long resId);

    Reservation updateResStatusReject(Long resId);

    Reservation updateResStatusAccept(Long resId);
    List<Reservation> getReservationByUserId(Long userId);

    Long countByStatusAndUserId(Status status, Long userId);
}
