drop table if exists professors cascade;
drop table if exists groups cascade;
drop table if exists lessons cascade;

create table professors
(
  id         serial primary key,
  name       char(100),
  surname    char(100),
  patronymic char(100),
  subject    char(100)
);

create table groups
(
  id   serial primary key,
  name char(100)
);

create table lessons
(
  id           serial primary key,
  date         date,
  lessonNumber integer,
  groupId      integer,
  professorId  integer,
  building     char(100),
  classroom    char(100)
);
