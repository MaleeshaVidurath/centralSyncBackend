package CentralSync.demo.controller;

import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.InventoryRequestStatus;
import CentralSync.demo.service.InventoryRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
@CrossOrigin
public class RequestController {
    @Autowired
    private RequestService requestService;
    @GetMapping("/getAll")
    public List<Request>getAllRequests(){
        return requestService.getAllRequests();
    }



    @PostMapping("/add")
    public String addInventoryRequest(@RequestBody InventoryRequest request) {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setReqStatus(InventoryRequestStatus.PENDING);
        requestService.saveRequest(request);
        //response entity should be added
        return "New request added successfully";
    }
    @PutMapping("/updateById/{requestId}")
    public Request updateRequest(@RequestBody Request newRequest, @PathVariable  long requestId){
        return requestService.updateRequestById(newRequest,requestId);
    }
    @DeleteMapping("/deleteRequest/{requestId}")
    public String deleteRequest(@PathVariable long requestId){
        return requestService.deleteRequestById(requestId);
    }
}