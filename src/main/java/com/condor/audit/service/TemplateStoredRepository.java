package com.condor.audit.service;

import com.condor.audit.model.TemplateStored;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemplateStoredRepository extends JpaRepository<TemplateStored, Integer> {

    Optional<TemplateStored> findByPmid(UUID pmid);

}
