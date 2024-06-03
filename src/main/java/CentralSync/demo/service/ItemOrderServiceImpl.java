package CentralSync.demo.service;



import CentralSync.demo.model.OrderStatus;
import CentralSync.demo.model.ItemOrder;
import CentralSync.demo.exception.OrderNotFoundException;
import CentralSync.demo.repository.ItemOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemOrderServiceImpl implements ItemOrderService {

    @Autowired

    private ItemOrderRepository itemOrderRepository;

    @Override
    public ItemOrder saveNewOrder(ItemOrder itemOrder) {
        return itemOrderRepository.save(itemOrder);

    }

    @Override
    public List<ItemOrder> getAllOrders() {

        return  itemOrderRepository.findAll();

    }

    @Override
    public ItemOrder getOrderById(long orderId) {

        return itemOrderRepository.findById(orderId)

                .orElseThrow(()-> new OrderNotFoundException(orderId));
    }

    @Override
    public ItemOrder updateOrderById(ItemOrder newItemOrder, long orderId) {

        return itemOrderRepository.findById(orderId)

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



                    return itemOrderRepository.save(itemOrder);

                })
                .orElseThrow(()->new OrderNotFoundException(orderId));
    }
    @Override
    public ItemOrder updateOrderStatus(long orderId) {

        return itemOrderRepository.findById(orderId)
                .map(itemOrder -> {
                    itemOrder.setStatus(OrderStatus.REVIEWED);
                    return itemOrderRepository.save(itemOrder);

                })
                .orElseThrow(()->new OrderNotFoundException(orderId));
    }

    @Override
    public String deleteOrderById(long orderId) {

        if(! itemOrderRepository.existsById(orderId)){
            throw new OrderNotFoundException(orderId);
        }
        itemOrderRepository.deleteById(orderId);

        return "Order with id"+ orderId + "deleted successfully";
    }
}
