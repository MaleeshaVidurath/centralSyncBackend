package CentralSync.demo.exception;

public class InventoryRequestNotFoundException extends RuntimeException{
    public InventoryRequestNotFoundException(Long id){
        super("Could not found the user with id "+id);
    }
}
