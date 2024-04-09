package CentralSync.demo.exception;

public class StockOutNotFoundException extends RuntimeException{
    public StockOutNotFoundException (long sinId){
        super("Could not found the stockIn with id "+ sinId);
    }

}
