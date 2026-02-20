package com.dhiren.openai.repository;

import com.dhiren.openai.entity.ServiceDeskTicketDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceDeskRepository extends
        JpaRepository<ServiceDeskTicketDetails, Long> {


    List<ServiceDeskTicketDetails> findByUserName(String userName);

    List<ServiceDeskTicketDetails> findAllByUserNameAndStatus(String userName, String status);

    ServiceDeskTicketDetails findByTicketId(String ticketId);

}
