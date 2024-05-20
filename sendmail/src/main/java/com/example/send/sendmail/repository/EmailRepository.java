package com.example.send.sendmail.repository;

import com.example.send.sendmail.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
   Email findByName(String email);
}
