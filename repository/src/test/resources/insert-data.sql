INSERT INTO tag (name)
VALUES ('tag one'),('tag two'),('tag three'),('fourth tag'),('fifth tag');

INSERT INTO certificate (name, description, price, creation_date, modification_date, duration)
VALUES ('certificate one', 'description', 12.5, '2020-06-09 00:00', null, 5),
       ('certificate two', 'some text', 8.5, '2018-07-19 12:30', null, 9),
       ('certificate three', 'third row', 2.5, '2021-09-17 10:10', null, 18);

INSERT INTO certificate_tag (certificate_id, tag_id)
VALUES (1,1),
       (1,2),
       (1,3),
       (2,3),
       (2,4),
       (3,5);
