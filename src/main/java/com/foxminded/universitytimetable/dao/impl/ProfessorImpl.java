package com.foxminded.universitytimetable.dao.impl;

import com.foxminded.universitytimetable.dao.ProfessorDAO;
import com.foxminded.universitytimetable.dao.impl.repositories.ProfessorRepository;
import com.foxminded.universitytimetable.models.Professor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("professorImplBean")
public class ProfessorImpl implements ProfessorDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessorImpl.class);
    private static ProfessorRepository professorRepository;

    @Override
    public int add(Professor professor) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to add professor: " + professor);
        }

        int id = professorRepository.save(professor).getId();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully add professor with id = " + id);
        }

        return id;
    }

    @Override
    public List<Professor> getAll() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get all from table professors");
        }

        List<Professor> professors = professorRepository.findAll();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + professors);
        }

        return professors;
    }

    @Override
    public Professor getById(int id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get professor by id = " + id);
        }

        Professor professor = professorRepository.getOne(id);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + professor);
        }

        return professor;
    }

    @Override
    public List<Professor> getBySurname(String surname) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get professor by surname = " + surname);
        }

        List<Professor> professors = professorRepository.getBySurname(surname);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + professors);
        }

        return professors;
    }

    @Override
    public int update(Professor professor) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to update professor: " + professor);
        }

        int id = professorRepository.save(professor).getId();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully update professor with id = " + id);
        }

        return id;
    }

    @Override
    public int remove(int professorId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to remove professor with id = " + professorId);
        }

        int status = professorRepository.remove(professorId);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully remove professor with id: " + professorId);
        }

        return status;
    }
}
