package CentralSync.demo.service;

import CentralSync.demo.model.ItemOrder;
import CentralSync.demo.model.OrderStatus;

import java.util.List;

public interface ItemOrderService {
    ItemOrder saveNewOrder(ItemOrder itemOrder);

    List<ItemOrder> getAllOrders();

    ItemOrder getOrderById(long orderId);

    ItemOrder markAsReviewed(long orderId, String prevStatus);

    ItemOrder markAsCompleted(long orderId);
    ItemOrder markAsProblemReported(long orderId,String note);

    ItemOrder markAsCancelled(long orderId);

}
