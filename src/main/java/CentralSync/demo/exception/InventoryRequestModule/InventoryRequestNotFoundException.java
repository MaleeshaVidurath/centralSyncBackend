package CentralSync.demo.exception.InventoryRequestModule;

public class InventoryRequestNotFoundException extends RuntimeException{
    public InventoryRequestNotFoundException(Long id){
        super("Could not found the user with id "+id);
    }
}
