package com.foxminded.universitytimetable.dao.impl.jdbctemplate;

import com.foxminded.universitytimetable.dao.LessonDAO;
import com.foxminded.universitytimetable.dao.queries.SQLQueries;
import com.foxminded.universitytimetable.dao.impl.jdbctemplate.rowmappers.LessonMapper;
import com.foxminded.universitytimetable.models.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

@Repository("lessonImplBean")
public class LessonImpl implements LessonDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LessonImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int add(Lesson lesson) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to add lesson " + lesson);
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(SQLQueries.ADD_LESSON, new String[]{"id"});
                    ps.setDate(1, lesson.getDate());
                    ps.setInt(2, lesson.getLessonNumber());
                    ps.setInt(3, lesson.getGroupId());
                    ps.setInt(4, lesson.getProfessorId());
                    ps.setString(5, lesson.getBuilding());
                    ps.setString(6, lesson.getClassroom());
                    return ps;
                }
                , keyHolder);

        Number id = keyHolder.getKey();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully add lesson with id = " + id);
        }

        return (int) id;
    }

    public List<Lesson> getAll() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get all from table lessons");
        }

        List<Lesson> lessons = jdbcTemplate.query(SQLQueries.GET_ALL_LESSONS, new LessonMapper());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + lessons);
        }

        return lessons;
    }

    public Lesson getById(int id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get lesson by id = " + id);
        }

        Lesson lesson = jdbcTemplate.queryForObject(SQLQueries.GET_LESSON_BY_ID, new Object[]{id}, new LessonMapper());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + lesson);
        }

        return lesson;
    }

    public int update(Lesson lesson) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to update lesson: " + lesson);
        }

        int id = lesson.getId();
        Date date = lesson.getDate();
        int lessonNumber = lesson.getLessonNumber();
        int groupId = lesson.getGroupId();
        int professorId = lesson.getProfessorId();
        String building = lesson.getBuilding();
        String classroom = lesson.getClassroom();

        int status = jdbcTemplate.update(SQLQueries.UPDATE_LESSON, date, lessonNumber, groupId, professorId,
                building, classroom, id);

        if (status != 1) {
            throw new IllegalArgumentException("Row with input id doesnt exist");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully update lesson with status = " + status);
        }

        return status;
    }

    public int remove(int lessonId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to remove lesson with id = " + lessonId);
        }

        int status = jdbcTemplate.update(SQLQueries.DELETE_LESSON, lessonId);

        if (status != 1) {
            throw new IllegalArgumentException("Row with input id doesnt exist");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully remove lesson with id: " + lessonId);
        }

        return status;
    }
}