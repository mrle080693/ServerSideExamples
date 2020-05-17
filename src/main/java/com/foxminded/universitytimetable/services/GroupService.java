package com.foxminded.universitytimetable.services;

import com.foxminded.universitytimetable.dao.impl.GroupImpl;
import com.foxminded.universitytimetable.exceptions.DAOException;
import com.foxminded.universitytimetable.exceptions.EntityValidationException;
import com.foxminded.universitytimetable.exceptions.NotFoundEntityException;
import com.foxminded.universitytimetable.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("groupServiceBean")
public class GroupService {
    @Autowired
    private GroupImpl groupImpl;

    public int add(Group group) {
        int groupIdInTable;

        try {
            checkGroup(group);

            if (group.getId() != 0) {
                throw new EntityValidationException("New group id must be 0. \n" +
                        "If you want update group you have to use update method");
            }

            groupIdInTable = groupImpl.add(group);
        } catch (DataAccessException ex) {
            throw new DAOException("Cant add group", ex);
        }

        return groupIdInTable;
    }

    public List<Group> getAll() {
        List<Group> groups;

        try {
            groups = groupImpl.getAll();

            if (groups.isEmpty()) {
                throw new EntityValidationException("Table groups is empty");
            }
        } catch (DataAccessException ex) {
            throw new DAOException("Cant get all from table groups", ex);
        }

        return groups;
    }

    public Group getById(int id) {
        Group group;

        try {
            if (id == 0) {
                throw new EntityValidationException("Group id cant be 0");
            }

            group = groupImpl.getById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundEntityException("Table groups have not rows with input id", ex);
        } catch (DataAccessException ex) {
            throw new DAOException("Cant get by id from table groups", ex);
        }

        return group;
    }

    public List<Group> getByName(String name) {
        List<Group> groups;

        try {
            if (name == null) {
                throw new EntityValidationException("Group must have name");
            }

            if (name.trim().equals("")) {
                throw new EntityValidationException("Group name must not be empty");
            }

            groups = groupImpl.getByName(name);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundEntityException("Table groups have not rows with input name", ex);
        } catch (DataAccessException ex) {
            throw new DAOException("Cant get by name from table groups", ex);
        }

        return groups;
    }

    public int update(Group group) {
        int status;

        try {
            checkGroup(group);

            if (group.getId() == 0) {
                throw new EntityValidationException("New group id must not be 0. \n" +
                        "If you want add new group you have to use add method");
            }

            status = groupImpl.update(group);

            if (status != 1) {
                throw new IllegalArgumentException("Group with input id doesnt exist");
            }
        } catch (DataAccessException ex) {
            throw new DAOException("Cant update table groups", ex);
        }

        return status;
    }

    public int remove(int groupId) {
        int status;

        try {
            status = groupImpl.remove(groupId);

            if (status != 1) {
                throw new EntityValidationException("Group with input id doesnt exist");
            }
        } catch (DataAccessException ex) {
            throw new DAOException("Cant remove element of table groups", ex);
        }

        return status;
    }

    private void checkGroup(Group group) {
        String name = group.getName();

        if (group == null) {
            throw new IllegalArgumentException("Group for update cant be null");
        }

        if (name == null) {
            throw new EntityValidationException("Group must have name");
        }

        if (name.isEmpty()) {
            throw new EntityValidationException("Group name must not be empty");
        }
    }
}