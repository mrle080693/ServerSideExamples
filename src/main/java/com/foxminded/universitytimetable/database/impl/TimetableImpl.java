package com.foxminded.universitytimetable.database.impl;

import com.foxminded.universitytimetable.database.TimetableDAO;
import com.foxminded.universitytimetable.database.impl.repositories.TimetableRepository;
import com.foxminded.universitytimetable.models.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("timetableImplBean")
public class TimetableImpl implements TimetableDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimetableImpl.class);
    private TimetableRepository timetableRepository;

    @Autowired
    public TimetableImpl(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    @Override
    public List<Lesson> getGroupTimetable(int groupId, Date from, Date till) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get group timetable. Group id: " + groupId + "from: " + from + "till: " + till);
        }

        List<Lesson> groupTimetable = timetableRepository.getGroupTimetable(groupId, from, till);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully get group timetable. Group id = " + groupId + "from: " + from + "till: " + till);
        }

        return groupTimetable;
    }

    @Override
    public List<Lesson> getProfessorTimetable(int professorId, Date from, Date till) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Try to get professor timetable. Professor id: " + professorId + " from: " + from + "till: "
                    + till);
        }

        List<Lesson> professorTimetable = timetableRepository.getGroupTimetable(professorId, from, till);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Successfully get professor timetable. Professor id = " + professorId + "from: " + from +
                    "till: " + till);
        }

        return professorTimetable;
    }
}
