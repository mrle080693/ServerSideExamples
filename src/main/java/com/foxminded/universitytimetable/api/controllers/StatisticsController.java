package com.foxminded.universitytimetable.api.controllers;

import com.foxminded.universitytimetable.services.StatisticsService;
import com.foxminded.universitytimetable.services.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public String getStatisticsPage() {
        LOGGER.debug("Get statistics.html page");

        return "statistics";
    }

    @GetMapping("/get_group_employment")
    @ResponseBody
    public long getGroupEmployment(@RequestParam int groupId,
                                   @RequestParam("from") Date from,
                                   @RequestParam("till") Date till) {
        LOGGER.debug("Try get group employment with id = " + groupId + " from: " + from + " till: " + till);

        long lessonsQuantity = 0;

        try {
            lessonsQuantity = statisticsService.getGroupEmployment(groupId, from, till);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        LOGGER.debug("Successfully got with lessons quantity: " + lessonsQuantity);

        return lessonsQuantity;
    }

    @GetMapping("/get_professor_employment")
    @ResponseBody
    public long getProfessorEmployment(@RequestParam int professorId,
                                       @RequestParam("from") Date from,
                                       @RequestParam("till") Date till) {
        LOGGER.debug("Try get professor employment with id = " + professorId + " from: " + from + " till: " + till);

        long lessonsQuantity = 0;

        try {
            lessonsQuantity = statisticsService.getProfessorEmployment(professorId, from, till);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        LOGGER.debug("Successfully got with lessons quantity: " + lessonsQuantity);

        return lessonsQuantity;
    }
}
