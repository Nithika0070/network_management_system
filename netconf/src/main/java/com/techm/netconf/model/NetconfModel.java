package com.techm.netconf.model;

import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "netconfmodel")
public class NetconfModel {

	    @Id
	    private String nodeId;
//	    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	    private Long id;

	    private Boolean tcpOnly;
	    private Integer port;
	    private String host;
	    private String connectionStatus;
	    private Integer keepaliveDelay;
	    private Integer sessionId;
}
