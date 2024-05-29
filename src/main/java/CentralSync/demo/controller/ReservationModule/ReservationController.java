/*package CentralSync.demo.controller;

import CentralSync.demo.model.ReservationModule.Reservation;
import CentralSync.demo.model.InventoryRequestModule.Status;
import CentralSync.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import CentralSync.demo.service.UserManagementModule.UserActivityLogService;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@CrossOrigin("http://localhost:3000")
public class ReservationController {
    @Autowired
<<<<<<< HEAD
    private ReservationRepository reservationRepository;
    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping("/reservation")
    Reservation newReservation(@RequestBody Reservation newReservation) {
        Reservation reservation=reservationRepository.save(newReservation);
        // Log user activity
        userActivityLogService.logUserActivity(reservation.getResId(), "New Reservation added");
        return(newReservation);
=======
    private ReservationService reservationService;
>>>>>>> origin

    @PostMapping("/add")
    public String add(@RequestBody Reservation reservation){
        reservation.setStatus(Status.PENDING);
        reservationService.saveReservation(reservation);
        return "New reservation is added.";
    }

    @GetMapping("/getAll")
    public List<Reservation> getAllReservations(){
        return reservationService.getAllReservation();
    }

    @GetMapping("/getById/{resId}")
    public Reservation listById (@PathVariable Long resId){
        return reservationService.getReservationById(resId);
    }

    @PutMapping("/updateById/{resId}")
    public Reservation updateReservation (@RequestBody Reservation newReservation,@PathVariable Long resId){
        return reservationService.updateReservationById(newReservation,resId);
    }


    @DeleteMapping("/deleteById/{resId}")
    public String deleteReservation(@PathVariable Long resId){
        return reservationService.deleteReservationById(resId);
    }

    @PatchMapping("/updateStatus/accept/{resId}")
    public Reservation updateStatusAccept(@PathVariable Long resId) {
        return reservationService.updateResStatusAccept(resId);
    }

    @PatchMapping("/updateStatus/reject/{resId}")
    public Reservation updateStatusReject(@PathVariable Long resId) {
        return reservationService.updateResStatusReject(resId);
    }

}*/