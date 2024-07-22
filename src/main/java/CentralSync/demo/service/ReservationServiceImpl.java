package CentralSync.demo.service;

import CentralSync.demo.exception.ReservationNotFoundException;
import CentralSync.demo.model.Reservation;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.Status;
import CentralSync.demo.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired // Inject the repository
    private ReservationRepository reservationRepository;

    @Autowired
    private InventoryItemService inventoryItemService;

    @Override
    public Reservation saveReservation(Reservation reservation) {

        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long resId) {
        return reservationRepository.findById(resId)
                .orElseThrow(() -> new ReservationNotFoundException(resId));
    }

    public List<Reservation> getReservationByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public Reservation updateReservationById(Reservation newReservation, Long resId) {
        return reservationRepository.findById(resId)
                .map(reservation -> {
                    reservation.setStatus(newReservation.getStatus());
                    reservation.setReason(newReservation.getReason());
                    reservation.setStartDate(newReservation.getStartDate());
                    reservation.setEndDate(newReservation.getEndDate());
                    reservation.setReservationQuantity(newReservation.getReservationQuantity());

                    return reservationRepository.save(reservation);
                }).orElseThrow(() -> new ReservationNotFoundException(resId));
    }

    @Override
    public String deleteReservationById(Long resId) {
        if (!reservationRepository.existsById(resId)) {
            throw new ReservationNotFoundException(resId);
        }
        reservationRepository.deleteById(resId);
        return "Reservation with id " + resId + " has been deleted successfully.";
    }

    @Transactional
    public Reservation updateResStatusAccept(Long resId) {
        Reservation reservation = reservationRepository.findById(resId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + resId));

        reservation.setStatus(Status.ACCEPTED);

        // Fetch the corresponding InventoryItem
        InventoryItem inventoryItem = inventoryItemService.getItemById(reservation.getItemId());

        // Check if the item exists and update quantity
        if (inventoryItem != null) {
            inventoryItem.setQuantity(inventoryItem.getQuantity() + reservation.getReservationQuantity());

            // Save the updated InventoryItem
            inventoryItemService.updateItemById(inventoryItem, inventoryItem.getItemId());
        } else {
            throw new EntityNotFoundException("Inventory item not found with id: " + reservation.getItemId());
        }

        // Save the updated reservation
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateResStatusReject(Long resId) {
        return reservationRepository.findById(resId)
                .map(reservation -> {
                    reservation.setStatus(Status.REJECTED);
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new ReservationNotFoundException(resId));
    }

    @Override
    public Long countByStatusAndUserId(Status status, Long userId) {
        return reservationRepository.countByStatusAndUserId(status, userId);
    }
}
