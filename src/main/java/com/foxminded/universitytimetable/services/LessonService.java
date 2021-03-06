package com.foxminded.universitytimetable.services;

import com.foxminded.universitytimetable.database.GroupDAO;
import com.foxminded.universitytimetable.database.LessonDAO;
import com.foxminded.universitytimetable.database.ProfessorDAO;
import com.foxminded.universitytimetable.services.exceptions.DAOException;
import com.foxminded.universitytimetable.services.exceptions.ValidationException;
import com.foxminded.universitytimetable.services.exceptions.NotFoundEntityException;
import com.foxminded.universitytimetable.models.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service("lessonServiceBean")
public class LessonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonService.class);
    private final LessonDAO lessonDAO;
    private GroupDAO groupDAO;
    private ProfessorDAO professorDAO;

    @Autowired
    public LessonService(LessonDAO lessonDAO, ProfessorDAO professorDAO, GroupDAO groupDAO) {
        this.lessonDAO = lessonDAO;
        this.professorDAO = professorDAO;
        this.groupDAO = groupDAO;
    }

    @Transactional
    public Lesson add(Lesson lesson) throws NotFoundEntityException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to add lesson " + lesson);
        }

        Lesson returnedLesson;

        try {
            checkLesson(lesson);

            if (lesson.getId() != 0) {
                String exMessage = "New lesson id is not 0. Actual value is: " + lesson.getId();
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            returnedLesson = lessonDAO.add(lesson);
        } catch (DataAccessException ex) {
            String exMessage = "Cant add lesson: " + ex;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully add lesson: " + lesson);
        }

        return returnedLesson;
    }

    public List<Lesson> getAll() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get all from table lessons");
        }

        List<Lesson> lessons = null;

        try {
            lessons = lessonDAO.getAll();

            if (lessons.isEmpty()) {
                String exMessage = "Table lessons is empty";
                NotFoundEntityException ex = new NotFoundEntityException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }
        } catch (DataAccessException ex) {
            String exMessage = "Cant get all from table lessons: " + lessons;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + lessons);
        }

        return lessons;
    }

    public Lesson getById(int id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get lesson by id = " + id);
        }

        Lesson lesson = null;

        try {
            if (id < 1) {
                String exMessage = "Lesson id cant be < 1. Input lesson: " + lesson;
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            lesson = lessonDAO.getById(id);
        } catch (EmptyResultDataAccessException ex) {
            String exMessage = "Table lessons have not lessons with id = " + id;
            LOGGER.warn(exMessage);
            throw new NotFoundEntityException(exMessage, ex);
        } catch (DataAccessException ex) {
            String exMessage = "Cant get lesson by id from DB with id = " + id;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + lesson);
        }

        return lesson;
    }

    public Lesson update(Lesson lesson) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to update lesson: " + lesson);
        }

        Lesson returnedLesson;

        try {
            checkLesson(lesson);

            if (lesson.getId() < 1) {
                String exMessage = "Lesson id cant be < 1. Input lesson: " + lesson;
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            returnedLesson = lessonDAO.update(lesson);

            if (returnedLesson.getId() == 0) {
                String exMessage = "Lesson with input id doesnt exist. Id is: " + lesson.getId();
                NotFoundEntityException ex = new NotFoundEntityException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }
        } catch (DataAccessException ex) {
            String exMessage = "Cant update table lessons. Input lesson: " + lesson;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        } catch (IllegalArgumentException e) {
            throw new NotFoundEntityException(e.getMessage());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully update lesson: " + lesson);
        }

        return returnedLesson;
    }

    public Integer remove(int lessonId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to remove lesson with id = " + lessonId);
        }

        Integer status;

        if(lessonId < 1) {
            String exMessage = "Id cunt be < 1! Input id = " + lessonId;
            ValidationException ex = new ValidationException(exMessage);
            LOGGER.warn(exMessage);
            throw ex;
        }

        try {
            status = lessonDAO.remove(lessonId);
        } catch (DataAccessException ex) {
            String exMessage = "Cant remove from table lessons. Group id: " + lessonId;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        } catch (IllegalArgumentException e) {
            throw new NotFoundEntityException(e.getMessage());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully remove lesson with id: " + lessonId);
        }

        return status;
    }

    private void checkLesson(Lesson lesson) {
        if (lesson == null) {
            String exMessage = "Lesson is null";
            IllegalArgumentException ex = new IllegalArgumentException(exMessage);
            LOGGER.warn(exMessage);
            throw ex;
        }

        Date date = lesson.getDate();
        int lessonNumber = lesson.getLessonNumber();
        int groupId = lesson.getGroupId();
        int professorId = lesson.getProfessorId();
        String building = lesson.getBuilding();
        String classroom = lesson.getClassroom();

        if (date == null || building == null || classroom == null) {
            String exMessage = "Lesson date, building or classroom is null: " + lesson;
            ValidationException ex = new ValidationException(exMessage);
            LOGGER.warn(exMessage);
            throw ex;
        }

        if (building.trim().isEmpty() || classroom.trim().isEmpty()) {
            String exMessage = "Lesson building or classroom is empty: " + lesson;
            ValidationException ex = new ValidationException(exMessage);
            LOGGER.warn(exMessage);
            throw ex;
        }

        if (lessonNumber == 0 || groupId == 0 || professorId == 0) {
            String exMessage = "lessonNumber, groupId or professorId is zero: " + lesson;
            ValidationException ex = new ValidationException(exMessage);
            LOGGER.warn(exMessage);
            throw ex;
        }
    }
}
