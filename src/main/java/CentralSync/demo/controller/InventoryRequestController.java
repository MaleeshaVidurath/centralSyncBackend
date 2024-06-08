package CentralSync.demo.controller;


import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.User;
import CentralSync.demo.model.UserActivityLog;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.InventoryRequestService;
import CentralSync.demo.service.UserActivityLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/request")
@CrossOrigin
public class InventoryRequestController {
    @Autowired
    private InventoryRequestService requestService;
    @Autowired
    private EmailSenderService emailSenderService;




    @GetMapping("/getAll")
    public ResponseEntity<?> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year) {
        List<InventoryRequest> requests;
        if (itemGroup != null && year != null) {
            requests = requestService.getItemsByGroup_Year(itemGroup, year);
        } else {
            requests = requestService.getAllRequests();
        }

        if (!requests.isEmpty()) {
            return ResponseEntity.ok(requests);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    //add new inventory request API
    @PostMapping("/add")
    public ResponseEntity<?> addUserRequest(@RequestBody @Valid InventoryRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        InventoryRequest req =requestService.saveRequest(request);
       // userActivityLogService.logUserActivity(userId,req.getReqId(), "New Inventory request added ");
        return ResponseEntity.ok("New Inventory request is added");
    }


    @GetMapping("/getById/{reqId}")
    public ResponseEntity<?> listById(@PathVariable long reqId) {
        InventoryRequest request = requestService.getRequestById(reqId);
        if (request != null) {
            return ResponseEntity.ok(request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateById/{requestId}")
    public ResponseEntity<?> updateRequest(@RequestBody InventoryRequest newRequest, @PathVariable long requestId){
        InventoryRequest updatedRequest = requestService.updateRequestById(newRequest, requestId);
        if (updatedRequest != null) {
            //userActivityLogService.logUserActivity(userId,updatedRequest.getReqId(), "Inventory request updated");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/updateStatus/accept/{reqId}")
    public ResponseEntity<?> updateStatusAccept(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusAccept(reqId);
        if (updatedRequest != null) {
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/reject/{reqId}")
    public ResponseEntity<?> updateStatusReject(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusReject(reqId);
        if (updatedRequest != null) {
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/sendToAdmin/{reqId}")
    public ResponseEntity<?> updateStatusSendToAdmin(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusSendToAdmin(reqId);
        if (updatedRequest != null) {
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteRequest/{requestId}")
    public ResponseEntity<String> deleteRequest(@PathVariable long requestId) {
        String result = requestService.deleteRequestById(requestId);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

//send email API
   /* @PostMapping("/mailing")
    public String mailNote(@RequestBody EmailRequest emailRequest) {
        String toEmail = emailRequest.getToEmail();
        String body = emailRequest.getBody();
        String subject = emailRequest.getSubject();

        emailSenderService.sendNoteEmail(toEmail, body, subject);
        return "Email sent successfully";
    }*/