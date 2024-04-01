package CentralSync.demo.exception;

public class StockOutNotFoundException extends RuntimeException{
    public StockOutNotFoundException (long soutId){
        super("Could not found the stockOut with id "+ soutId);
    }

}
