package com.foxminded.universitytimetable.services;

import com.foxminded.universitytimetable.database.ProfessorDAO;
import com.foxminded.universitytimetable.services.exceptions.DAOException;
import com.foxminded.universitytimetable.services.exceptions.ValidationException;
import com.foxminded.universitytimetable.services.exceptions.NotFoundEntityException;
import com.foxminded.universitytimetable.models.Professor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("professorServiceBean")
public class ProfessorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessorService.class);
    private final ProfessorDAO professorDAO;

    @Autowired
    public ProfessorService(ProfessorDAO professorDAO) {
        this.professorDAO = professorDAO;
    }

    public Professor add(Professor professor) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to add professor: " + professor);
        }

        Professor returnedProfessor;

        try {
            checkProfessor(professor);

            if (professor.getId() != 0) {
                String exMessage = "New professor id is not 0. Actual value is: " + professor.getId();
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            returnedProfessor = professorDAO.add(professor);
        } catch (DataAccessException ex) {
            String exMessage = "Cant add professor: " + professor;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully add professor: " + professor);
        }

        return returnedProfessor;
    }

    public List<Professor> getAll() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get all from table professors");
        }

        List<Professor> professors;

        try {
            professors = professorDAO.getAll();

            if (professors.isEmpty()) {
                String exMessage = "Table professors is empty";
                NotFoundEntityException ex = new NotFoundEntityException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }
        } catch (DataAccessException ex) {
            String exMessage = "Cant get all from table professors";
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + professors);
        }

        return professors;
    }

    public Professor getById(int id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get professor by id = " + id);
        }

        Professor professor;

        try {
            if (id < 1) {
                String exMessage = "Input id cant be < 1. Input is: " + id;
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            professor = professorDAO.getById(id);
        } catch (EmptyResultDataAccessException ex) {
            String exMessage = "Table professors have not professors with id = " + id;
            LOGGER.warn(exMessage);
            throw new NotFoundEntityException(exMessage, ex);
        } catch (DataAccessException ex) {
            String exMessage = "Cant get professor from DB with id = " + id;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + professor);
        }

        return professor;
    }

    public List<Professor> getBySurname(String surname) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get professor by surname = " + surname);
        }

        List<Professor> professors;

        try {
            if (surname == null) {
                String exMessage = "Surname is null";
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            if (surname.trim().isEmpty()) {
                String exMessage = "Surname is empty";
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            professors = professorDAO.getBySurname(surname);
        } catch (EmptyResultDataAccessException ex) {
            String exMessage = "Table professors have not professors with surname = " + surname;
            LOGGER.warn(exMessage);
            throw new NotFoundEntityException(exMessage, ex);
        } catch (DataAccessException ex) {
            String exMessage = "Cant get professor from DB with surname = " + surname;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result is: " + professors);
        }

        return professors;
    }

    public Professor update(Professor professor) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to update professor: " + professor);
        }

        Professor returnedProfessor;

        try {
            checkProfessor(professor);

            if (professor.getId() < 1) {
                String exMessage = "Input id cant be < 1. Input is: " + professor;
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            returnedProfessor = professorDAO.update(professor);

            if (returnedProfessor.getId() == 0) {
                String exMessage = "Professor with input id doesnt exist. " + professor;
                NotFoundEntityException ex = new NotFoundEntityException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }
        } catch (DataAccessException ex) {
            String exMessage = "Cant update table professors. Input group: " + professor;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        } catch (IllegalArgumentException e) {
            throw new NotFoundEntityException(e.getMessage());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully update professor: " + professor);
        }

        return returnedProfessor;
    }

    public Integer remove(int professorId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to remove professor with id = " + professorId);
        }

        Integer status;

        try {
            if (professorId < 1) {
                String exMessage = "Input id cant be < 1. Input is: " + professorId;
                ValidationException ex = new ValidationException(exMessage);
                LOGGER.warn(exMessage);
                throw ex;
            }

            status = professorDAO.remove(professorId);
        } catch (DataAccessException ex) {
            String exMessage = "Cant remove from table professors. Professor id: " + professorId;
            LOGGER.error(exMessage);
            throw new DAOException(exMessage, ex);
        } catch (IllegalArgumentException e) {
            throw new NotFoundEntityException(e.getMessage());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully remove professor with id: " + professorId);
        }

        return status;
    }

    private void checkProfessor(Professor professor) {
        if (professor == null) {
            String exMessage = "Professor is null";
            IllegalArgumentException ex = new IllegalArgumentException(exMessage);
            LOGGER.warn(exMessage);
            throw ex;
        }

        String name = professor.getName();
        String surname = professor.getSurname();
        String patronymic = professor.getPatronymic();
        String subject = professor.getSubject();

        if (name == null || surname == null || patronymic == null || subject == null) {
            String exMessage = "Professor name, surname, patronymic or subject is null. " + professor;
            ValidationException ex = new ValidationException(exMessage);
            LOGGER.warn(exMessage);
            throw ex;
        }

        if (name.trim().isEmpty() || surname.trim().isEmpty() || patronymic.trim().isEmpty() || subject.trim().isEmpty()) {
            String exMessage = "Professor name, surname, patronymic or subject is empty";
            ValidationException ex = new ValidationException(exMessage);
            LOGGER.warn(exMessage);
            throw ex;
        }
    }
}
