package CentralSync.demo.controller;

import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.service.InventoryRequestService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/request")
public class InventoryRequestViewController {

    private final InventoryRequestService inventoryRequestService;
    private final UserActivityLogService userActivityLogService;
    private final LoginService loginService;

    @Autowired
    public InventoryRequestViewController(InventoryRequestService inventoryRequestService, UserActivityLogService userActivityLogService, LoginService loginService) {
        this.inventoryRequestService = inventoryRequestService;
        this.userActivityLogService = userActivityLogService;
        this.loginService = loginService;
    }

    @GetMapping("/updateStatus/delivered/{reqId}")
    public String updateStatusDeliver(@PathVariable long reqId, Model model) {
        try {
            InventoryRequest updatedRequest = inventoryRequestService.updateInReqStatusDeliver(reqId);
            if (updatedRequest != null) {
                Long actorId = loginService.userId;
                userActivityLogService.logUserActivity(actorId, updatedRequest.getReqId(), "Item delivered successfully");
                model.addAttribute("message", "Delivery confirmed successfully!");
                return "delivery_confirmation";
            }
        } catch (Exception e) {
            model.addAttribute("message", "Failed to confirm delivery.");
            return "error";
        }
        model.addAttribute("message", "Failed to confirm delivery.");
        return "error";
    }
}
