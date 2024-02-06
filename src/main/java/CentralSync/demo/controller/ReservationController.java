package CentralSync.demo.controller;

import CentralSync.demo.Model.Reservation;
import CentralSync.demo.exception.ReservationNotFoundException;
import CentralSync.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping("/reservation")
    Reservation newReservation(@RequestBody Reservation newReservation) {
        return reservationRepository.save(newReservation);

    }

    @GetMapping("/reservations")
    List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("reservation/{id}")
    Reservation getUserById(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @PutMapping("/reservation/{id}")
    public Reservation updateReservation(@RequestBody Reservation newReservation, @PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setResStatus(newReservation.getResStatus());
                    reservation.setResQuantity(newReservation.getResQuantity());
                    reservation.setReason(newReservation.getReason());
                    reservation.setDate(newReservation.getDate());
                    reservation.setDepName(newReservation.getDepName());
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new ReservationNotFoundException(id));

    }
}



