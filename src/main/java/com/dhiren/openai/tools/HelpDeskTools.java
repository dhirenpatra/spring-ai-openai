package com.dhiren.openai.tools;

import com.dhiren.openai.entity.ServiceDeskTicketDetails;
import com.dhiren.openai.model.TicketRequest;
import com.dhiren.openai.service.ServiceDeskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {

    private final Logger logger = LoggerFactory.getLogger(HelpDeskTools.class);
    private final ServiceDeskService serviceDeskService;

    @Tool(name = "createServiceDeskTicket" ,
            description = "Create The service desk Ticket")
    public ServiceDeskTicketDetails createServiceDeskTicket(@ToolParam(
            description = "Details to create a service desk ticket")TicketRequest request,
                                                            ToolContext toolContext) {
        logger.info("createServiceDeskTicket called...");
        String userName = toolContext.getContext().get("userName").toString();
        ServiceDeskTicketDetails serviceDeskTicket = serviceDeskService.createServiceDeskTicket(request, userName);
        logger.info("ticket created  {}...", serviceDeskTicket.getServiceDeskTicketId());
        return serviceDeskTicket;
    }

    @Tool(name = "getServiceDeskTicket" ,
            description = "Get the current service desk ticket")
    public List<ServiceDeskTicketDetails> getCurrentTime(ToolContext toolContext) {
        logger.info("getCurrentTime called...");
        String userName = toolContext.getContext().get("userName").toString();
        return serviceDeskService.getTicketsByUsername(userName);
    }

}
