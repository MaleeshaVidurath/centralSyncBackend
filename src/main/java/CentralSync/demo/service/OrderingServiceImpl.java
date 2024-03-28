package CentralSync.demo.service;

<<<<<<< HEAD

import CentralSync.demo.model.OrderStatus;
=======
>>>>>>> origin
import CentralSync.demo.model.Ordering;
import CentralSync.demo.exception.OrderNotFoundException;
import CentralSync.demo.repository.OrderingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderingServiceImpl implements OrderingService {

    @Autowired
    private OrderingRepository orderingRepository;

    @Override
    public Ordering saveNewOrder(Ordering order) {
        return orderingRepository.save(order);
    }

    @Override
    public List<Ordering> getAllOrders() {
        return  orderingRepository.findAll();
    }

    @Override
    public Ordering getOrderById(long orderId) {
        return orderingRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException(orderId));
    }

    @Override
    public Ordering updateOrderById(Ordering newOrder, long orderId) {
        return orderingRepository.findById(orderId)
                .map(ordering -> {
                    ordering.setVendorName(newOrder.getVendorName());
                    ordering.setCompanyName(newOrder.getCompanyName());
                    ordering.setVendorEmail(newOrder.getVendorEmail());
                    ordering.setItemName(newOrder.getItemName());
                    ordering.setDate(newOrder.getDate());
                    ordering.setBrandName(newOrder.getBrandName());
                    ordering.setQuantity(newOrder.getQuantity());
                    ordering.setDescription(newOrder.getDescription());
                    ordering.setMobile(newOrder.getMobile());


                    return orderingRepository.save(ordering);
                })
                .orElseThrow(()->new OrderNotFoundException(orderId));
    }
    @Override
    public Ordering updateOrderStatus(long orderId) {
        return orderingRepository.findById(orderId)
                .map(ordering -> {
                    ordering.setStatus(OrderStatus.REVIEWED);
                    return orderingRepository.save(ordering);
                })
                .orElseThrow(()->new OrderNotFoundException(orderId));
    }

    @Override
    public String deleteOrderById(long orderId) {
        if(! orderingRepository.existsById(orderId)){
            throw new OrderNotFoundException(orderId);
        }
        orderingRepository.deleteById(orderId);
        return "Order with id"+ orderId + "deleted successfully";
    }
}
