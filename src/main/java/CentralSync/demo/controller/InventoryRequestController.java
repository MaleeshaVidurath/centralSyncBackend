package CentralSync.demo.controller;



import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.service.InventoryRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
@CrossOrigin
public class InventoryRequestController {
    @Autowired
    private InventoryRequestService requestService;

@GetMapping("/getAll")
    public  List<InventoryRequest> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year){
        if(itemGroup!=null && year!= null){
            return  requestService.getItemsByGroup_Year(itemGroup,year);
        }else{
            return requestService.getAllRequests();
        }

    }
    @PostMapping("/add")
    public String addUserRequest(@RequestBody InventoryRequest request) {
        requestService.saveRequest(request);
        //response entity should be added
        return "New request added successfully";
    }



    @GetMapping("/getById/{reqId}")
    public InventoryRequest listById(@PathVariable long reqId) {
        return requestService.getRequestById(reqId);
    }

    @PutMapping("/updateById/{requestId}")
    public InventoryRequest updateRequest(@RequestBody InventoryRequest newRequest, @PathVariable  long requestId){
        return requestService.updateRequestById(newRequest,requestId);
    }

    @PatchMapping("/updateStatus/accept/{reqId}")
    public InventoryRequest updateStatusAccept(@PathVariable long reqId) {
        return requestService.updateInReqStatusAccept(reqId);
    }

    @PatchMapping("/updateStatus/reject/{reqId}")
    public InventoryRequest updateStatusReject(@PathVariable long reqId) {
        return requestService.updateInReqStatusReject(reqId);
    }
    @DeleteMapping("/deleteRequest/{requestId}")
    public String deleteRequest(@PathVariable long requestId){
        return requestService.deleteRequestById(requestId);
    }
}