package com.foxminded.universitytimetable.api.restcontrollers;

import com.foxminded.universitytimetable.api.constants.Urls;
import com.foxminded.universitytimetable.services.exceptions.NotFoundEntityException;
import com.foxminded.universitytimetable.services.exceptions.ValidationException;
import com.foxminded.universitytimetable.models.Lesson;
import com.foxminded.universitytimetable.services.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LessonRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonRestController.class);
    private final LessonService lessonService;
    private final ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "\n                       SORRY :( \n" +
                    "We know about this trouble and will correct it soon");

    @Autowired
    public LessonRestController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @RequestMapping(value = Urls.API_REST_POST_LESSON_JSON, method = RequestMethod.POST)
    public Lesson add(@RequestParam Date date, @RequestParam int lessonNumber, @RequestParam int groupId,
                      @RequestParam int professorId, @RequestParam String building, @RequestParam String classroom) {

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Try to add lesson with: " + "date = " + date
                + " lesson number = " + lessonNumber + " group id = " + groupId + " professor id = " + professorId
                + " building = " + building + " classroom = " + classroom);

        Lesson lesson = new Lesson(date, lessonNumber, groupId, professorId, building, classroom);

        try {
            lesson = lessonService.add(lesson);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw responseStatusException;
        }

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Successfully add lesson with id = " + lesson);

        return lesson;
    }

    @RequestMapping(value = Urls.API_REST_GET_LESSONS_JSON, method = RequestMethod.GET)
    public List<Lesson> getAll() {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Try get all lessons");
        List<Lesson> lessons = new ArrayList<>();

        try {
            lessons = lessonService.getAll();
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundEntityException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw responseStatusException;
        }

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Successfully got " + lessons.size() + " lessons");

        return lessons;
    }

    @RequestMapping(value = Urls.API_REST_GET_LESSON_JSON, method = RequestMethod.GET)
    public Lesson getById(@PathVariable int id) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Try get lesson with id = " + id);
        Lesson lesson = new Lesson();

        try {
            lesson = lessonService.getById(id);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundEntityException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw responseStatusException;
        }

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Successfully got lesson: " + lesson);

        return lesson;
    }

    @RequestMapping(value = Urls.API_REST_PUT_LESSON_JSON, method = RequestMethod.PUT)
    public Lesson update(@RequestParam(value = "id", defaultValue = "0") int id, @RequestParam Date date,
                         @RequestParam int lessonNumber, @RequestParam int groupId, @RequestParam int professorId,
                         @RequestParam String building, @RequestParam String classroom) {

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Try to update lesson with: id = " + id + " date = " + date
                + " lesson number = " + lessonNumber + " group id = " + groupId + " professor id = " + professorId
                + " building = " + building + " classroom = " + classroom);

        Lesson lesson = new Lesson(date, lessonNumber, groupId, professorId, building, classroom);

        try {
            lesson.setId(id);

            lesson = lessonService.update(lesson);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundEntityException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw responseStatusException;
        }

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Successfully update lesson: " + lesson);

        return lesson;
    }

    @RequestMapping(value = Urls.API_REST_DELETE_LESSON_JSON, method = RequestMethod.DELETE)
    public Integer remove(@RequestParam int lessonId) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Try to remove lesson with id = " + lessonId);
        Integer status;

        try {
            status = lessonService.remove(lessonId);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundEntityException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw responseStatusException;
        }

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Successfully remove lesson with id: " + lessonId);

        return status;
    }
}
