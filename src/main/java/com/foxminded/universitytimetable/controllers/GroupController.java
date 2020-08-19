package com.foxminded.universitytimetable.controllers;

import com.foxminded.universitytimetable.exceptions.NotFoundEntityException;
import com.foxminded.universitytimetable.exceptions.ValidationException;
import com.foxminded.universitytimetable.models.Group;
import com.foxminded.universitytimetable.services.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

        @RequestMapping(method = RequestMethod.GET)
        public ModelAndView getGroupsViewAndAllGroups() {
            LOGGER.debug("Try get groups.html with all groups");

            ModelAndView modelAndView = new ModelAndView("groups");
            List<Group> groups = null;

            try {
                groups = groupService.getAll();
                modelAndView.addObject("groups", groups);
            } catch (NotFoundEntityException e) {
                LOGGER.warn(e.getEmptyResultExceptionMessage());
            }

            if (groups != null) {
                LOGGER.debug("groups.html successfully got with groups: " + groups.size());
            } else {
                LOGGER.debug("groups.html successfully got without groups");
            }

            return modelAndView;
        }

        @GetMapping("/get_all")
        @ResponseBody
        public List<Group> getAll() {
            LOGGER.debug("Try get all groups");

            List<Group> groups = null;

            try {
                groups = groupService.getAll();
            } catch (NotFoundEntityException e) {
                LOGGER.warn(e.getEmptyResultExceptionMessage());
            }

            if (groups != null) {
                LOGGER.debug("Successfully got with groups: " + groups.size());
            } else {
                LOGGER.debug("Successfully got without groups");
            }

            return groups;
        }

        @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
        public ModelAndView getById(@PathVariable("id") int id) {
            LOGGER.debug("Try get groups.html with group with id = " + id);

            ModelAndView modelAndView = new ModelAndView("groups");

            try {
                Group group = groupService.getById(id);
                modelAndView.addObject("id", group.getId());
                modelAndView.addObject("name", group.getName());
            } catch (NotFoundEntityException e) {
                modelAndView.setStatus(HttpStatus.BAD_REQUEST);
                LOGGER.warn("Try to get group with not existing id = " + id);
            }

            LOGGER.debug("groups.html successfully got with group with id = : " + id);

            return modelAndView;
        }

        @PostMapping("/save")
        public String save(@RequestParam String newName) {
            LOGGER.debug("Try save group with: " + " name = " + newName);

            Group group = new Group(newName);
            int id = groupService.add(group);

            LOGGER.debug("Successfully save group with id = " + id);

            return "redirect:/groups";
        }

        @PostMapping("/update")
        public String update(@RequestParam int id, @RequestParam String newName) {
            LOGGER.debug("Try to update group with id = " + id);
            System.out.println(id);
            try {
                Group group = new Group(newName);
                group.setId(id);

                groupService.update(group);
            } catch (NotFoundEntityException e) {
                LOGGER.warn(e.getEmptyResultExceptionMessage());
            }

            LOGGER.debug("Successfully update group with new name: " + newName);

            return "redirect:/groups";
        }

        @PostMapping("/remove")
        public String remove(@RequestParam int id) {
            LOGGER.debug("Try to remove group with id = " + id);

            try {
                groupService.remove(id);
            } catch (NotFoundEntityException e) {
                LOGGER.warn(e.getEmptyResultExceptionMessage());
            }

            LOGGER.debug("Successfully remove group with id: " + id);

            return "redirect:/groups";
        }
       /*
    @GetMapping
    public String getView() {
        LOGGER.debug("Get index.html page");
        return "groups";
    }

    @GetMapping("/get_all")
    @ResponseBody
    public List<Group> getAll() {
        LOGGER.debug("Try get all groups");

        List<Group> groups = null;

        try {
            groups = groupService.getAll();
        } catch (NotFoundEntityException e) {
            LOGGER.warn(e.getEmptyResultExceptionMessage());
        }

        if (groups != null) {
            LOGGER.debug("Successfully got with groups: " + groups.size());
        } else {
            LOGGER.debug("Successfully got without groups");
        }

        return groups;
    }

    @GetMapping("/getById/{id}")
    @ResponseBody
    public Group getById(@PathVariable("id") int id) {
        LOGGER.debug("Try get group by id = " + id);
        Group group = null;

        try {
            group = groupService.getById(id);

        } catch (NotFoundEntityException e) {
            LOGGER.warn("Try to get group with not existing id = " + id);
        }

        LOGGER.debug("Successfully got group with id = : " + id);
        return group;
    }
     */
}
