package com.foxminded.universitytimetable.dao.interfaces;

import com.foxminded.universitytimetable.dao.models.Lesson;

import java.util.List;

public interface LessonDAO {
    void add(Lesson lesson);

    List<Lesson> getAll();

    Lesson getById(int id);

    Lesson getByLessonNumber(int lessonNumber);

    void update(Lesson lesson);

    void remove(Lesson lesson);
}
