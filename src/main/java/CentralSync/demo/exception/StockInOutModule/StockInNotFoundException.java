package CentralSync.demo.exception.StockInOutModule;

public class StockInNotFoundException extends RuntimeException{
    public StockInNotFoundException (long sinId){
        super("Could not found the stockIn with id "+ sinId);
    }

}
