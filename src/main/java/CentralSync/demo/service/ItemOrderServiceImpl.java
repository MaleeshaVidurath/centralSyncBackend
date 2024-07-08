package CentralSync.demo.service;

import CentralSync.demo.controller.InventoryItemController;
import CentralSync.demo.model.OrderStatus;
import CentralSync.demo.model.ItemOrder;
import CentralSync.demo.exception.OrderNotFoundException;
import CentralSync.demo.repository.ItemOrderRepository;
import CentralSync.demo.util.PdfGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;


@Service
public class ItemOrderServiceImpl implements ItemOrderService {

    private final static Logger logger = LoggerFactory.getLogger(InventoryItemController.class);
    private final ItemOrderRepository itemOrderRepository;
    private final PdfGenerator pdfGenerator;
    private final EmailSenderService emailSenderService;

    @Autowired
    public ItemOrderServiceImpl(
            ItemOrderRepository itemOrderRepository,
            PdfGenerator pdfGenerator,
            EmailSenderService emailSenderService) {
        this.itemOrderRepository = itemOrderRepository;
        this.pdfGenerator = pdfGenerator;
        this.emailSenderService = emailSenderService;
    }


    @Override
    public ItemOrder saveNewOrder(ItemOrder itemOrder) {

        ItemOrder savedItemOrder = itemOrderRepository.save(itemOrder);
        String pdfFilePath = "uploads/ItemOrder" + savedItemOrder.getOrderId() + ".pdf";

        try {
            pdfGenerator.generateOrderPdf(pdfFilePath, savedItemOrder);
        } catch (FileNotFoundException e) {
            logger.error("Failed to generate PDF for order ID: {}", savedItemOrder.getOrderId(), e);
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

        return itemOrderRepository.findAll();

    }

    @Override
    public ItemOrder getOrderById(long orderId) {

        return itemOrderRepository.findById(orderId)

                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }


//    public ItemOrder updateOrderById(ItemOrder newItemOrder, long orderId) {
//
//        return itemOrderRepository.findById(orderId)
//
//                .map(itemOrder -> {
//                    itemOrder.setVendorName(newItemOrder.getVendorName());
//                    itemOrder.setCompanyName(newItemOrder.getCompanyName());
//                    itemOrder.setVendorEmail(newItemOrder.getVendorEmail());
//                    itemOrder.setItemName(newItemOrder.getItemName());
//                    itemOrder.setDate(newItemOrder.getDate());
//                    itemOrder.setBrandName(newItemOrder.getBrandName());
//                    itemOrder.setQuantity(newItemOrder.getQuantity());
//                    itemOrder.setDescription(newItemOrder.getDescription());
//                    itemOrder.setMobile(newItemOrder.getMobile());
//
//
//                    return itemOrderRepository.save(itemOrder);
//
//                })
//                .orElseThrow(() -> new OrderNotFoundException(orderId));
//    }

    public ItemOrder markAsReviewed(long orderId) {

        return itemOrderRepository.findById(orderId)
                .map(itemOrder -> {
                    itemOrder.setStatus(OrderStatus.REVIEWED);
                    return itemOrderRepository.save(itemOrder);

                })
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public ItemOrder markAsCompleted(long orderId) {
        return itemOrderRepository.findById(orderId)
                .map(itemOrder -> {
                    itemOrder.setStatus(OrderStatus.COMPLETED);
                    return itemOrderRepository.save(itemOrder);

                })
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }



}

