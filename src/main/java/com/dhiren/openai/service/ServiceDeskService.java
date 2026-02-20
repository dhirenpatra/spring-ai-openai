package com.dhiren.openai.service;

import com.dhiren.openai.entity.ServiceDeskTicketDetails;
import com.dhiren.openai.model.TicketRequest;
import com.dhiren.openai.repository.ServiceDeskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceDeskService {

    private final ServiceDeskRepository serviceDeskRepository;

    public ServiceDeskTicketDetails createServiceDeskTicket(TicketRequest ticketRequest, String userName) {

        ServiceDeskTicketDetails serviceDeskTicketDetails =
                ServiceDeskTicketDetails.builder()
                        .issue(ticketRequest.issue())
                        .serviceDeskTicketId(UUID.randomUUID().toString()+"SD")
                        .status("OPEN")
                        .userName(userName)
                        .etaDate(LocalDate.now().plusDays(7).toString())
                        .build();

        return serviceDeskRepository.save(serviceDeskTicketDetails);

    }

    public List<ServiceDeskTicketDetails> getTicketsByUsername(String username) {
        return serviceDeskRepository.findByUserName(username);
    }

}
