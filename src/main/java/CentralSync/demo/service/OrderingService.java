package CentralSync.demo.service;

import CentralSync.demo.model.Ordering;

import java.util.List;

public interface OrderingService {
     Ordering saveNewOrder(Ordering order);

     List<Ordering> getAllOrders();

     Ordering getOrderById(long orderId);

     Ordering updateOrderById(Ordering newOrder,long orderId);
      Ordering updateOrderStatus( long orderId);

     String deleteOrderById(long orderId);
}
