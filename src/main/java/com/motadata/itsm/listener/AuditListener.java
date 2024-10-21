package com.motadata.itsm.listener;

import com.motadata.itsm.entity.Audit;
import com.motadata.itsm.repository.AuditRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Log4j2
public class AuditListener {

    @Autowired
    private AuditRepository auditRepository;

    //We will listen message from topic and saving into audit table

    @KafkaListener(id = "audit-save", topics = "audit-save")
    public void save(String message) {
        Audit audit = new Audit();
        audit.setOperationName("SAVE");
        audit.setMessage(message);
        audit.setLocalDate(LocalDate.now());
log.info("message from save : {}",message);
        auditRepository.save(audit);
    }

    @KafkaListener(id = "audit-update", topics = "audit-update")
    public void userUpdate(String message) {

        Audit audit = new Audit();
        audit.setOperationName("UPDATE");
        audit.setMessage(message);
        audit.setLocalDate(LocalDate.now());

        auditRepository.save(audit);
        log.info("message from update : {}",message);
    }

    @KafkaListener(id = "audit-delete", topics = "audit-delete")
    public void delete(String message) {

        Audit audit = new Audit();
        audit.setOperationName("DELETE");
        audit.setMessage(message);
        audit.setLocalDate(LocalDate.now());

        log.info("message from delete : {}",message);

        auditRepository.save(audit);
    }

}
