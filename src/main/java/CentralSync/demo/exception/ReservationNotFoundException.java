package CentralSync.demo.exception;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(Long id){
        super("Could not found the user with id "+id);
    }
}
