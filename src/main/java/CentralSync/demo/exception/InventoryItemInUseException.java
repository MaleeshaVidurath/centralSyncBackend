package CentralSync.demo.exception;

public class InventoryItemInUseException  extends RuntimeException{
    public InventoryItemInUseException(long itemId) {
        super("Inventory Item with id " + itemId + " is currently in use and cannot be deleted.");
    }
}
