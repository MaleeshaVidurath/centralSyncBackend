package CentralSync.demo.controller;



import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.InventoryRequestService;
import  jakarta.validation.Valid;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import CentralSync.demo.service.UserActivityLogService;

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
    @Autowired
    private UserActivityLogService userActivityLogService;

@GetMapping("/getAll")
    public  List<InventoryRequest> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year){
        if(itemGroup!=null && year!= null){
            return  requestService.getItemsByGroup_Year(itemGroup,year);
        }else{
            return requestService.getAllRequests();
        }

    }
    @PostMapping("/add")
    public ResponseEntity<?> addUserRequest(@RequestBody @Valid  InventoryRequest request, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        InventoryRequest req =requestService.saveRequest(request);
        userActivityLogService.logUserActivity(req.getItemId(), "New Inventory request added ");


        return ResponseEntity.ok("New Inventory request is added");
    }



    @GetMapping("/getById/{reqId}")
    public InventoryRequest listById(@PathVariable long reqId) {
        return requestService.getRequestById(reqId);
    }

    @PutMapping("/updateById/{requestId}")
    public InventoryRequest updateRequest(@RequestBody InventoryRequest newRequest, @PathVariable  long requestId){
        InventoryRequest req=requestService.updateRequestById(newRequest,requestId);
        userActivityLogService.logUserActivity(req.getReqId(), "Inventory request updated");
        return (newRequest);

    }

    @PatchMapping("/updateStatus/accept/{reqId}")
    public InventoryRequest updateStatusAccept(@PathVariable long reqId) {
        return requestService.updateInReqStatusAccept(reqId);
    }

    @PatchMapping("/updateStatus/reject/{reqId}")
    public InventoryRequest updateStatusReject(@PathVariable long reqId) {
        return requestService.updateInReqStatusReject(reqId);
    }

   /* @PostMapping("/mailing")
    public String mailNote(@RequestBody EmailRequest emailRequest) {
        String toEmail = emailRequest.getToEmail();
        String body = emailRequest.getBody();
        String subject = emailRequest.getSubject();

        emailSenderService.sendNoteEmail(toEmail, body, subject);
        return "Email sent successfully";
    }*/
   @PatchMapping("/updateStatus/sendToAdmin/{reqId}")
   public InventoryRequest updateStatusSendToAdmin(@PathVariable long reqId) {
       return requestService.updateInReqStatusSendToAdmin(reqId);
   }
    @DeleteMapping("/deleteRequest/{requestId}")
    public String deleteRequest(@PathVariable long requestId){
        return requestService.deleteRequestById(requestId);
    }
}