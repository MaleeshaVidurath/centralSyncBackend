package CentralSync.demo.exception.OrderModule;

public class OrderingNotFoundException extends RuntimeException{
    public OrderingNotFoundException(Long id){
        super("Could not found the order with id "+ id);
    }
}
