package com.foxminded.universitytimetable.dao.impl;

import com.foxminded.universitytimetable.dao.ProfessorDAO;
import com.foxminded.universitytimetable.models.Lesson;
import com.foxminded.universitytimetable.models.Professor;
import com.foxminded.universitytimetable.models.Timetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ProfessorImpl implements ProfessorDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProfessorImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Professor professor) {

    }

    public List<Professor> getAll() {
        return null;
    }

    public Professor getById(int id) {
        return null;
    }

    public Professor getByFullName(String fullName) {
        return null;
    }

    public void update(Professor professor) {

    }

    public void remove(Professor professor) {

    }

    public Timetable getTimetable(int professorId, Date from, Date till) {
        return null;
    }

    public void addLesson(Lesson lesson) {

    }
}