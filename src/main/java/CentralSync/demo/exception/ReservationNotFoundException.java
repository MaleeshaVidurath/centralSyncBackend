package CentralSync.demo.exception;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(Long resId){
        super("Could not found the reservation with id "+ resId);
    }
}
