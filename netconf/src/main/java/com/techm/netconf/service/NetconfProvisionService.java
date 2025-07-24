package com.techm.netconf.service;

import com.techm.netconf.model.NetconfModel;
import com.techm.netconf.repository.NetconfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NetconfProvisionService {

    private final NetconfRepository repository;

    public NetconfModel createDevice(NetconfModel input) {
        NetconfModel saved = repository.save(input);
        System.out.println(saved);
        postToSimulator(saved);
        return saved;
    }

//    private void postToSimulator(NetconfModel model) {
//        String url = "http://localhost:8181/rests/data/networktopology:networktopology/topology=topology-netconf";
//
//        Map<String, Object> login = Map.of(
//                "netconf-node-topology:username", "admin",
//                "netconf-node-topology:password", "admin"
//        );
//
//        Map<String, Object> netconfNode = new HashMap<>();
//        netconfNode.put("netconf-node-topology:port", model.getPort());
//        netconfNode.put("netconf-node-topology:tcp-only", model.getTcpOnly());
//        netconfNode.put("netconf-node-topology:host", model.getHost());
//        netconfNode.put("netconf-node-topology:keepalive-delay", model.getKeepaliveDelay());
//        netconfNode.put("netconf-node-topology:reconnect-on-changed-schema", false);
//        netconfNode.put("netconf-node-topology:connection-timeout-millis", 20000);
//        netconfNode.put("netconf-node-topology:max-connection-attempts", 0);
//        netconfNode.put("netconf-node-topology:min-backoff-millis", 2000);
//        netconfNode.put("netconf-node-topology:max-backoff-millis", 1800000);
//        netconfNode.put("netconf-node-topology:backoff-multiplier", 1.5);
//        netconfNode.put("netconf-node-topology:login-password-unencrypted", login);
//
//        Map<String, Object> node = Map.of(
//                "node-id", model.getNodeId(),
//                "netconf-node", netconfNode
//        );
//
//        Map<String, Object> payload = Map.of("node", List.of(node));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBasicAuth("admin", "admin");
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
//
//        new RestTemplate().exchange(url, HttpMethod.PUT, request, String.class);
//    }
    
    private void postToSimulator(NetconfModel model) {
        String url = "http://localhost:8181/rests/data/network-topology:network-topology/topology=topology-netconf";

        Map<String, Object> login = Map.of(
            "netconf-node-topology:username", "admin",
            "netconf-node-topology:password", "admin"
        );

        Map<String, Object> netconfNode = new LinkedHashMap<>();
        netconfNode.put("netconf-node-topology:port", model.getPort());
        netconfNode.put("netconf-node-topology:reconnect-on-changed-schema", false);
        netconfNode.put("netconf-node-topology:connection-timeout-millis", 20000);
        netconfNode.put("netconf-node-topology:tcp-only", model.getTcpOnly());
        netconfNode.put("netconf-node-topology:max-connection-attempts", 0);
        netconfNode.put("netconf-node-topology:login-password-unencrypted", login);
        netconfNode.put("netconf-node-topology:host", model.getHost());
        netconfNode.put("netconf-node-topology:min-backoff-millis", 2000);
        netconfNode.put("netconf-node-topology:max-backoff-millis", 1800000);
        netconfNode.put("netconf-node-topology:backoff-multiplier", 1.5);
        netconfNode.put("netconf-node-topology:keepalive-delay", model.getKeepaliveDelay());

        //linkedhashmap is used to maintain the order of insertion
        Map<String, Object> node = new LinkedHashMap<>();
        node.put("node-id", model.getNodeId());
        node.put("netconf-node", netconfNode);

        Map<String, Object> payload = Map.of("node", List.of(node));

        //Basic Auth
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("admin", "admin");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            System.out.println("Simulator response: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Simulator responded with error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
//    private void postToSimulator(NetconfModel model) {
//        String url = "http://localhost:8181/rests/data/networktopology:networktopology/topology=topology-netconf";
//
//        Map<String, Object> login = Map.of(
//            "username", "admin",
//            "password", "admin"
//        );
//
//        Map<String, Object> netconfNode = new LinkedHashMap<>();
//        netconfNode.put("port", model.getPort());
//        netconfNode.put("tcp-only", model.getTcpOnly());
//        netconfNode.put("host", model.getHost());
//        netconfNode.put("login-password-unencrypted", login);
//        netconfNode.put("keepalive-delay", model.getKeepaliveDelay());
//        netconfNode.put("connection-timeout-millis", 20000);
//        netconfNode.put("max-connection-attempts", 0);
//        netconfNode.put("min-backoff-millis", 2000);
//        netconfNode.put("max-backoff-millis", 1800000);
//        netconfNode.put("backoff-multiplier", 1.5);
//        netconfNode.put("reconnect-on-changed-schema", false);
//
//        Map<String, Object> node = new LinkedHashMap<>();
//        node.put("node-id", model.getNodeId());
//        node.put("netconf-node-topology:netconf-node", netconfNode);
//
//        Map<String, Object> topology = Map.of(
//            "topology-id", "topology-netconf",
//            "node", List.of(node)
//        );
//
//        Map<String, Object> payload = Map.of(
//            "network-topology:topology", List.of(topology)
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBasicAuth("admin", "admin");
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
//
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
//        } catch (HttpClientErrorException e) {
//            System.err.println("Error from simulator: " + e.getResponseBodyAsString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}