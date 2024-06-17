package CentralSync.demo.service;

import CentralSync.demo.model.OrderStatus;
import CentralSync.demo.model.ItemOrder;
import CentralSync.demo.exception.OrderNotFoundException;
import CentralSync.demo.repository.ItemOrderRepository;
import CentralSync.demo.util.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.keygen.BytesKeyGenerator;
    import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class ItemOrderServiceImpl implements ItemOrderService {

    @Autowired
    private ItemOrderRepository itemOrderRepository;
    @Autowired
    private PdfGenerator pdfGenerator;
    @Autowired
    private EmailSenderService emailSenderService;


   // private static final BytesKeyGenerator TOKEN_GENERATOR = KeyGenerators.secureRandom(15);

    @Override
    public ItemOrder saveNewOrder(ItemOrder itemOrder) {
//        String token = new String(TOKEN_GENERATOR.generateKey());
//        itemOrder.setToken(token);
        ItemOrder savedItemOrder = itemOrderRepository.save(itemOrder);
        String pdfFilePath = "uploads/ItemOrder" + savedItemOrder.getOrderId() + ".pdf";

        try {
            pdfGenerator.generateOrderPdf(pdfFilePath, savedItemOrder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Send email to the vendor
        String subject = "Order Request  " + savedItemOrder.getItemName();
        String body = "Dear Vendor,\n\n"
                + "We are interested in placing an order :\n\n"
                + "You can download the order details PDF from the following link:\n"
                + "http://localhost:8080/orders/download/ItemOrder" + savedItemOrder.getOrderId() + ".pdf";
        emailSenderService.sendSimpleEmail(savedItemOrder.getVendorEmail(), subject, body);
        return savedItemOrder;



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
