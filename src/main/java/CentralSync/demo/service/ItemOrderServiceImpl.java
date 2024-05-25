package CentralSync.demo.service;



import CentralSync.demo.model.OrderStatus;

import CentralSync.demo.model.ItemOrder;
import CentralSync.demo.exception.OrderNotFoundException;
import CentralSync.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemOrderServiceImpl implements ItemOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ItemOrder saveNewOrder(ItemOrder itemOrder) {
        return orderRepository.save(itemOrder);
    }

    @Override
    public List<ItemOrder> getAllOrders() {
        return  orderRepository.findAll();
    }

    @Override
    public ItemOrder getOrderById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException(orderId));
    }

    @Override
    public ItemOrder updateOrderById(ItemOrder newItemOrder, long orderId) {
        return orderRepository.findById(orderId)
                .map(itemOrder -> {
                    itemOrder.setVendorName(newItemOrder.getVendorName());
                    itemOrder.setCompanyName(newItemOrder.getCompanyName());
                    itemOrder.setVendorEmail(newItemOrder.getVendorEmail());
                    itemOrder.setItemName(newItemOrder.getItemName());
                    itemOrder.setDate(newItemOrder.getDate());
                    itemOrder.setBrandName(newItemOrder.getBrandName());
                    itemOrder.setQuantity(newItemOrder.getQuantity());
                    itemOrder.setDescription(newItemOrder.getDescription());
                    itemOrder.setMobile(newItemOrder.getMobile());


                    return orderRepository.save(itemOrder);
                })
                .orElseThrow(()->new OrderNotFoundException(orderId));
    }
    @Override
    public ItemOrder updateOrderStatus(long orderId) {
        return orderRepository.findById(orderId)
                .map(itemOrder -> {
                    itemOrder.setStatus(OrderStatus.REVIEWED);
                    return orderRepository.save(itemOrder);
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
