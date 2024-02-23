package CentralSync.demo.service;

import CentralSync.demo.Model.InventoryItem;
import CentralSync.demo.Model.Ordering;
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
                    ordering.setDate(newOrder.getDate());
                    ordering.setBrandName(newOrder.getBrandName());
                    ordering.setDescription(newOrder.getDescription());
                    ordering.setMobile(newOrder.getMobile());
                    ordering.setVendorEmail(newOrder.getVendorEmail());

                    return orderingRepository.save(newOrder);
                })
                .orElseThrow(()->new OrderNotFoundException(orderId));
    }

    @Override
    public String deleteOrderById(long orderId) {
        if(! orderingRepository.existsById(orderId)){
            throw new OrderNotFoundException(orderId);
        }
        orderingRepository.deleteById(orderId);
        return "Order with id"+ orderId + "has been deleted successfully";
    }
}
