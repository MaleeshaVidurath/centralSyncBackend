package CentralSync.demo.service;

import CentralSync.demo.Model.InventoryItem;
import CentralSync.demo.Model.Ordering;

import java.util.List;

public interface OrderingService {
    public Ordering saveNewOrder(Ordering order);

    public List<Ordering> getAllOrders();

    public Ordering getOrderById(long orderId);

    public Ordering updateOrderById(Ordering newOrder,long orderId);
    public  Ordering updateOrderStatus( long orderId);

    public String deleteOrderById(long orderId);
}
