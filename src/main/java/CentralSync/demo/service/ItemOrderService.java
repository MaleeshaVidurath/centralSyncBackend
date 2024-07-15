package CentralSync.demo.service;

import CentralSync.demo.model.ItemOrder;

import java.util.List;

public interface ItemOrderService {
    ItemOrder saveNewOrder(ItemOrder itemOrder);

    List<ItemOrder> getAllOrders();

    ItemOrder getOrderById(long orderId);

    ItemOrder markAsReviewed(long orderId);

    ItemOrder markAsCompleted(long orderId);
    ItemOrder markAsProblemReported(long orderId,String note);
    ItemOrder markAsResolved(long orderId);
    ItemOrder markAsCancelled(long orderId);

}
