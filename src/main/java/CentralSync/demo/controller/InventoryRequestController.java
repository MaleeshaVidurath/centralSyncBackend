package CentralSync.demo.controller;


import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.InventoryRequestService;
import CentralSync.demo.service.UserActivityLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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


   // @GetMapping("/getAll")
    //public ResponseEntity<?> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year) {
       // List<InventoryRequest> requests;
        //if (itemGroup != null && year != null) {
            //requests = requestService.getItemsByGroup_Year(itemGroup, year);
        //} else {
        //    requests = requestService.getAllRequests();
       // }

     //   if (!requests.isEmpty()) {
          //  return ResponseEntity.ok(requests);
     //   } else {
       //     return ResponseEntity.noContent().build();
      //  }
 //   }


//    //add new inventory request API
//    @PostMapping("/add")
//    public ResponseEntity<?> addUserRequest(
//            @RequestBody
//            @Valid InventoryRequest request,
//            BindingResult bindingResult,
//    @RequestParam("file") MultipartFile file) {
//        try {
//            // Save the file to a designated folder
//            String uploadFolder = "uploads/";
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(uploadFolder + file.getOriginalFilename());
//            Files.write(path, bytes);
//            InventoryRequest.setFilePath(path.toString());
//        if (bindingResult.hasErrors()) {
//            Map<String, String> errors = bindingResult.getFieldErrors().stream()
//                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//            return ResponseEntity.badRequest().body(errors);
//        }
//        InventoryRequest req =requestService.saveRequest(request);
//        userActivityLogService.logUserActivity(req.getReqId(), "New Inventory request added ");
//        return ResponseEntity.ok("New Inventory request is added");
//    }
@PostMapping("/add")
public ResponseEntity<?> addUserRequest(
        @RequestParam ("itemName") @Valid String itemName,
        @RequestParam("quantity") @Valid String quantity,
        @RequestParam("reason") @Valid String reason,
        //BindingResult bindingResult,
        @RequestParam("description") String description,
        @RequestParam("file") MultipartFile file) {
    try {
      //  if (bindingResult.hasErrors()) {
         //   Map<String, String> errors = bindingResult.getFieldErrors().stream()
           //         .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
         //   return ResponseEntity.badRequest().body(errors);
       // }

        // Save the file to a designated folder
        String uploadFolder = "uploads/";
        byte[] bytes = file.getBytes();
        Path path = Paths.get(uploadFolder + file.getOriginalFilename());
        Files.write(path, bytes);

        InventoryRequest request = new InventoryRequest();
        request.setItemName(itemName);
        request.setQuantity(quantity);
        request.setReason(reason);
        request.setDescription(description);
      request.setFilePath(path.toString());

        // Save the request
        InventoryRequest savedRequest = requestService.saveRequest(request);

        // Log user activity
        userActivityLogService.logUserActivity(savedRequest.getReqId(), "New Inventory request added");

        // Return a more descriptive response
        return ResponseEntity.ok().body(savedRequest);
    } catch (IOException e) {
        // Handle file writing errors
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to save file: " + e.getMessage());
    }
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
            userActivityLogService.logUserActivity(updatedRequest.getReqId(), "Inventory request updated");
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