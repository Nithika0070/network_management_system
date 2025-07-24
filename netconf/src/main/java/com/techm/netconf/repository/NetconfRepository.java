package com.techm.netconf.repository;

import com.techm.netconf.model.*;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NetconfRepository extends JpaRepository<NetconfModel, String>{
	Optional<NetconfModel> findByNodeId(String nodeId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM NetconfModel n WHERE n.nodeId = :nodeId")
    void deleteByNodeId(String nodeId);
    
    void deleteAll();
    List<NetconfModel> findAll();
    
    Optional<NetconfModel> findByNodeIdAndSessionId(String nodeId, int sessionId);
    
    boolean existsByNodeId(String nodeId);
}

