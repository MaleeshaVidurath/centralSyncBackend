package CentralSync.demo.exception;

public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(Long id){
        super("Could not found the user with id "+id);
    }
}
