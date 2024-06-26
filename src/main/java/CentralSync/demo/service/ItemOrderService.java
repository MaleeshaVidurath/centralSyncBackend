package CentralSync.demo.service;

import CentralSync.demo.model.ItemOrder;

import java.util.List;

public interface ItemOrderService {
    ItemOrder saveNewOrder(ItemOrder itemOrder);

    List<ItemOrder> getAllOrders();

    ItemOrder getOrderById(long orderId);

    ItemOrder updateOrderById(ItemOrder newItemOrder, long orderId);

    ItemOrder updateOrderStatus(long orderId);

    String deleteOrderById(long orderId);
}
