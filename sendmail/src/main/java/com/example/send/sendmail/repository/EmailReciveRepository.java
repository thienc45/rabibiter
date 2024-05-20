package com.example.send.sendmail.repository;

import com.example.send.sendmail.entity.Email;
import com.example.send.sendmail.entity.EmailRecive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailReciveRepository extends JpaRepository<EmailRecive, Long> {
    EmailRecive findByEmail(Email email);
}
