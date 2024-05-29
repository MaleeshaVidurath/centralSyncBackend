package CentralSync.demo.exception.StockInOutModule;

public class StockOutNotFoundException extends RuntimeException{
    public StockOutNotFoundException (long sinId){
        super("Could not found the stockIn with id "+ sinId);
    }

}
