package CentralSync.demo.exception.MaintenanceTicketModule;

public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(Long id){
        super("Could not found the ticket with id "+id);
    }
}
