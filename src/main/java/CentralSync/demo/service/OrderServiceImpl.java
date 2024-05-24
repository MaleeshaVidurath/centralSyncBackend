package CentralSync.demo.service;



import CentralSync.demo.model.OrderStatus;

import CentralSync.demo.model.Order;
import CentralSync.demo.exception.OrderNotFoundException;
import CentralSync.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order saveNewOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return  orderRepository.findAll();
    }

    @Override
    public Order getOrderById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException(orderId));
    }

    @Override
    public Order updateOrderById(Order newOrder, long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setVendorName(newOrder.getVendorName());
                    order.setCompanyName(newOrder.getCompanyName());
                    order.setVendorEmail(newOrder.getVendorEmail());
                    order.setItemName(newOrder.getItemName());
                    order.setDate(newOrder.getDate());
                    order.setBrandName(newOrder.getBrandName());
                    order.setQuantity(newOrder.getQuantity());
                    order.setDescription(newOrder.getDescription());
                    order.setMobile(newOrder.getMobile());


                    return orderRepository.save(order);
                })
                .orElseThrow(()->new OrderNotFoundException(orderId));
    }
    @Override
    public Order updateOrderStatus(long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(OrderStatus.REVIEWED);
                    return orderRepository.save(order);
                })
                .orElseThrow(()->new OrderNotFoundException(orderId));
    }

    @Override
    public String deleteOrderById(long orderId) {
        if(! orderRepository.existsById(orderId)){
            throw new OrderNotFoundException(orderId);
        }
        orderRepository.deleteById(orderId);
        return "Order with id"+ orderId + "deleted successfully";
    }
}
