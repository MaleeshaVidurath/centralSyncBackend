package CentralSync.demo.exception;

public class AdjustmentNotFoundException extends RuntimeException{
    public AdjustmentNotFoundException (Long adjId){
        super("Could not found the adjustment with id "+ adjId);
    }

}
