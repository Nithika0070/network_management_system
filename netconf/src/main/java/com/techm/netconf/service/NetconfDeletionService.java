package com.techm.netconf.service;

import com.techm.netconf.model.NetconfModel;
import com.techm.netconf.repository.NetconfRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NetconfDeletionService {

    private final NetconfRepository repository;

    @Transactional
    public boolean deleteFromDatabase(String nodeId) {
        Optional<NetconfModel> model = repository.findByNodeId(nodeId);
        if (model.isPresent()) {
            repository.deleteByNodeId(nodeId); 
            System.out.println("Deleted from DB: " + nodeId);
            return true;
        } else {
            System.out.println("Node not found in DB: " + nodeId);
            return false;
        }
    }

    public boolean deleteFromSimulator(String nodeId) {
        String url = "http://localhost:8181/rests/data/network-topology:network-topology"
                   + "/topology=topology-netconf/node=" + nodeId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            System.out.println("Deleted from Simulator: " + nodeId);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Simulator node not found: " + nodeId);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNodeEverywhere(String nodeId) {
        boolean sim = deleteFromSimulator(nodeId);
        boolean db = deleteFromDatabase(nodeId);
        return sim || db; //considers ok if any one runs perfectly
    }
    
    @Transactional
    public void deleteAllNodes() {
        List<NetconfModel> allModels = repository.findAll();

        for (NetconfModel model : allModels) {
            String nodeId = model.getNodeId();
            String url = "http://localhost:8181/rests/data/network-topology:network-topology"
                       + "/topology=topology-netconf/node=" + nodeId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth("admin", "admin");
            HttpEntity<Void> request = new HttpEntity<>(headers);

            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
                System.out.println("Deleted from Simulator: " + nodeId);
            } catch (HttpClientErrorException.NotFound e) {
                System.err.println("Simulator node not found: " + nodeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        repository.deleteAll();
        System.out.println("Deleted all nodes from database.");
    }
}
