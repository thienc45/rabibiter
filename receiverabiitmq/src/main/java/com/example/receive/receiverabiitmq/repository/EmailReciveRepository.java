package com.example.receive.receiverabiitmq.repository;

;
import com.example.receive.receiverabiitmq.entity.Email;
import com.example.receive.receiverabiitmq.entity.EmailRecive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailReciveRepository extends JpaRepository<EmailRecive, Long> {

    EmailRecive findByEmailAndState(Email email,Integer state);

}
