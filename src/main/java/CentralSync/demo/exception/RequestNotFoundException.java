package CentralSync.demo.exception;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(Long id){
        super("Could not found the user with id "+id);
    }
}
