package CentralSync.demo.exception;

public class InventoryItemNotFoundException extends RuntimeException {
    public InventoryItemNotFoundException(Long id) {
        super("Could not found the inventory item with id " + id);
    }
}
