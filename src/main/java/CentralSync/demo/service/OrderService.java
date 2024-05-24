package CentralSync.demo.service;

import CentralSync.demo.model.Order;

import java.util.List;

public interface OrderService {
    Order saveNewOrder(Order order);

    List<Order> getAllOrders();

    Order getOrderById(long orderId);

    Order updateOrderById(Order newOrder, long orderId);

    Order updateOrderStatus(long orderId);

    String deleteOrderById(long orderId);
}
