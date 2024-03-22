package CentralSync.demo.service;

import CentralSync.demo.model.Request;

import java.util.List;

public interface RequestService {
    public void saveRequest(Request request);

    public List<Request> getAllRequests();

    public Request updateRequestById(Request newRequest, long requestId);

    public String deleteRequestById(long requestId);

}