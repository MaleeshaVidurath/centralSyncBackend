package CentralSync.demo.exception;

public class UserActivityLogNotFoundException extends RuntimeException{
    public UserActivityLogNotFoundException(Long id){
        super("Could not found the user with id "+id);
    }
}
