package CentralSync.demo.controller;


import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.InventoryRequestService;
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


    //get Item group by year API
    @GetMapping("/getAll")
    public List<InventoryRequest> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year) {
        if (itemGroup != null && year != null) {
            return requestService.getItemsByGroup_Year(itemGroup, year);
        } else {
            return requestService.getAllRequests();
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
        requestService.saveRequest(request);

        return ResponseEntity.ok("New Inventory request is added");
    }


    //get inventory request by id API
    @GetMapping("/getById/{reqId}")
    public InventoryRequest listById(@PathVariable long reqId) {
        return requestService.getRequestById(reqId);
    }

    //update inventory request by id API
    @PutMapping("/updateById/{requestId}")
    public InventoryRequest updateRequest(@RequestBody InventoryRequest newRequest, @PathVariable long requestId) {
        return requestService.updateRequestById(newRequest, requestId);
    }

    //update inventory request status to accept API
    @PatchMapping("/updateStatus/accept/{reqId}")
    public InventoryRequest updateStatusAccept(@PathVariable long reqId) {
        return requestService.updateInReqStatusAccept(reqId);
    }

    //update inventory request status to reject API
    @PatchMapping("/updateStatus/reject/{reqId}")
    public InventoryRequest updateStatusReject(@PathVariable long reqId) {
        return requestService.updateInReqStatusReject(reqId);
    }

    //update inventory request status to send to admin API
    @PatchMapping("/updateStatus/sendToAdmin/{reqId}")
    public InventoryRequest updateStatusSendToAdmin(@PathVariable long reqId) {
        return requestService.updateInReqStatusSendToAdmin(reqId);
    }

    //delete inventory request by id API
    @DeleteMapping("/deleteRequest/{requestId}")
    public String deleteRequest(@PathVariable long requestId) {
        return requestService.deleteRequestById(requestId);
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