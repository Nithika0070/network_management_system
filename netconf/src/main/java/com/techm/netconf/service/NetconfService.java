//package com.techm.netconf.service;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.techm.netconf.model.NetconfModel;
//import com.techm.netconf.repository.NetconfRepository;
//
//@Service
//public class NetconfService {
//
//    @Autowired
//    private NetconfRepository repository;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    public void fetchAndStoreNodes() {
//        String url = "http://localhost:8181/rests/data/network-topology:network-topology/topology=topology-netconf";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBasicAuth("admin", "admin");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
//        System.out.println("yeah"+response);
////        JsonNode body = response.getBody();
////        System.out.println("lalala"+body);
//        JsonNode nodes = response.getBody().path("network-topology:topology").path("node");
//        System.out.println("lalala"+nodes);
//
//        for (JsonNode node : nodes) {
//        	
//        	String nodeId = node.path("node-id").asText();
//        	System.out.println("omg" + nodeId);
//        	
//            JsonNode netconf = node.path("netconf-node-topology:netconf-node");
//            
//            NetconfModel n = new NetconfModel();
//            n.setNodeId(node.path("node-id").asText());
//            n.setTcpOnly(netconf.path("tcp-only").asBoolean());
//            n.setPort(netconf.path("port").asInt());
//            n.setHost(netconf.path("host").asText());
//            n.setConnectionStatus(netconf.path("connection-status").asText());
//            n.setKeepaliveDelay(netconf.path("keepalive-delay").asInt());
//            n.setSessionId(netconf.path("session-id").asInt());
//
//            repository.save(n);
//        }
//    }
//}

package com.techm.netconf.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.techm.netconf.model.NetconfModel;
import com.techm.netconf.repository.NetconfRepository;

@Service
public class NetconfService {

    @Autowired
    private NetconfRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    public List<NetconfModel> fetchAndStoreNodes() {
        String url = "http://localhost:8181/rests/data/network-topology:network-topology/topology=topology-netconf";
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        JsonNode topologies = response.getBody().path("network-topology:topology");
        
        List<NetconfModel> result = new ArrayList<>();

        if (topologies.isArray()) {
            for (JsonNode topology : topologies) {
                JsonNode nodes = topology.path("node");

                if (nodes.isArray()) {
                    for (JsonNode node : nodes) {
                        JsonNode netconf = node.path("netconf-node-topology:netconf-node");

                        NetconfModel model = new NetconfModel();
                        model.setNodeId(node.path("node-id").asText());
                        model.setTcpOnly(netconf.path("tcp-only").asBoolean());
                        model.setPort(netconf.path("port").asInt());
                        model.setHost(netconf.path("host").asText());
                        model.setConnectionStatus(netconf.path("connection-status").asText());
                        model.setKeepaliveDelay(netconf.path("keepalive-delay").asInt());
                        model.setSessionId(netconf.path("session-id").asInt());
                        
//                        Optional<NetconfModel> existing = repository.findByNodeIdAndSessionId(
//                                model.getNodeId(), model.getSessionId()
//                            );
//                        
                        if (!repository.existsByNodeId(model.getNodeId())) {
                            repository.save(model);
                            result.add(model);
                        } else {
                            System.out.println("Skipping duplicate: " + model.getNodeId() + ", session " + model.getSessionId());
                        }

                    }
                }
            }
        }
        return result;
    }
}