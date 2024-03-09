package CentralSync.demo.controller;

import CentralSync.demo.Model.Request;
import CentralSync.demo.Services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
@CrossOrigin
public class RequestController {
    @Autowired
    private RequestService requestService;

    @PostMapping("/add")
    public String addRequest(@RequestBody Request request) {
        requestService.saveRequest(request);
        //response entity should be added
        return "New request added successfully";
    }

    @GetMapping("/getAll")
    public List<Request>getAllRequests(){
        return requestService.getAllRequests();
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