package CentralSync.demo.controller;

import CentralSync.demo.Model.Request;
import CentralSync.demo.exception.RequestNotFoundException;
import CentralSync.demo.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestController {
    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/request")
    Request newRequest(@RequestBody Request newRequest) {
        return requestRepository.save(newRequest);

    }

    @GetMapping("/requests")
    List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @GetMapping("request/{id}")
    Request getUserById(@PathVariable Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }


    @PutMapping("/request/{id}")
    public Request updateRequest(@RequestBody Request newRequest, @PathVariable Long id) {
        return requestRepository.findById(id)
                .map(request -> {
                    request.setReqStatus(newRequest.getReqStatus());
                    request.setReqQuantity(newRequest.getReqQuantity());
                    request.setReason(newRequest.getReason());
                    request.setDate(newRequest.getDate());
                    request.setDepName(newRequest.getDepName());
                    return requestRepository.save(request);
                })
                .orElseThrow(() -> new RequestNotFoundException(id));

    }

    @DeleteMapping("/request/{id}")
    String deleteRequest(@PathVariable Long id) {
        if (!requestRepository.existsById(id)) {
            throw new RequestNotFoundException(id);
        }
        requestRepository.deleteById(id);
        return "User with id " + id + " has been deleted success";
    }

}



