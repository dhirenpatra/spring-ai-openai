package com.dhiren.openai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTools {

    Logger logger = LoggerFactory.getLogger(TimeTools.class);

    @Tool(name = "getCurrentLocalTime" ,
            description = "Get the current time from the users current local time zone")
    public String getCurrentLocalTime() {
        LocalTime localTime = LocalTime.now();
        logger.info("getCurrentLocalTime called {}", localTime);
        return localTime.toString();
    }

    @Tool(name = "getCurrentTime" ,
            description = "Get the current time from the provided time zone")
    public String getCurrentTime(@ToolParam(description = "ZoneId value representing the time zone") String zoneId) {
        LocalTime localTime = LocalTime.now(ZoneId.of(zoneId));
        logger.info("getCurrentTime called {}", localTime);
        return localTime.toString();
    }

}
