package com.example.receive.receiverabiitmq.repository;


import com.example.receive.receiverabiitmq.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
   Email findByName(String email);


}
