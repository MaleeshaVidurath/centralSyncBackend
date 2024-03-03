package CentralSync.demo.Services;

import CentralSync.demo.Model.Request;
import CentralSync.demo.exception.RequestNotFoundException;
import CentralSync.demo.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void saveRequest(Request request) {
        requestRepository.save(request);
    }

    @Override
    public List<Request>getAllRequests(){
        return requestRepository.findAll();
    }


    @Override
    public Request updateRequestById(@RequestBody Request newRequest, @PathVariable long id){
        return requestRepository.findById((int) id)
                .map(request -> {
                    request.setReqStatus(newRequest.getReqStatus());
                    request.setReqQuantity(newRequest.getReqQuantity());
                    request.setReason(newRequest.getReason());
                    request.setDescription(newRequest.getDescription());
                    request.setDate(newRequest.getDate());
                    request.setDepName(newRequest.getDepName());
                    request.setRole(newRequest.getRole());
                    return requestRepository.save(newRequest);
                })
                .orElseThrow(() -> new RequestNotFoundException(id));
    }
    @Override
    public String deleteRequestById(long reqId){
        if(!requestRepository.existsById((int) reqId)){
            throw new RequestNotFoundException(reqId);
        }
        requestRepository.deleteById((int) reqId);
        return "Request with id "+reqId+" deleted successfully";
    }
}