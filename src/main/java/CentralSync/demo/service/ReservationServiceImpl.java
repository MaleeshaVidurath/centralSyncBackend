/*package CentralSync.demo.service;

import CentralSync.demo.exception.ReservationNotFoundException;
import CentralSync.demo.model.Reservation;
import CentralSync.demo.model.Status;
import CentralSync.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired //ingect the repository
    private ReservationRepository reservationRepository;


    @Override
    public Reservation saveReservation(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long resId) {
        return reservationRepository.findById(resId)
                .orElseThrow(()-> new ReservationNotFoundException(resId));
    }

    @Override
    public Reservation updateReservationById(Reservation newReservation, Long resId){
        return reservationRepository.findById(resId)
                .map(reservation -> {
                    reservation.setStatus(newReservation.getStatus());
                    reservation.setReason(newReservation.getReason());
                    reservation.setDate(newReservation.getDate());
                    reservation.setQuantity(newReservation.getQuantity());

                    return reservationRepository.save(reservation);
                }).orElseThrow(()->new ReservationNotFoundException(resId));
    }
    @Override
    public String deleteReservationById(Long resId){
        if(!reservationRepository.existsById(resId)){
            throw new ReservationNotFoundException(resId);
        }
        reservationRepository.deleteById(resId);
        return  "Reservation with id "+resId+" has been deleted successfully.";
    }
    @Override
    public Reservation updateResStatusAccept(Long resId) {
        return reservationRepository.findById(resId)
                .map(reservation -> {
                    reservation.setStatus(Status.ACCEPTED);
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(()->new ReservationNotFoundException(resId));
    }

    @Override
    public Reservation updateResStatusReject(Long resId) {
        return reservationRepository.findById(resId)
                .map(reservation -> {
                    reservation.setStatus(Status.REJECTED);
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(()->new ReservationNotFoundException(resId));
    }
}*/