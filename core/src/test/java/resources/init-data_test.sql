INSERT INTO gift_certificate (name, description, price, duration)
VALUES ('certificate one', 'description', 12.5, 5),
       ('certificate two', 'some text', 8.5,9),
       ('certificate three', 'third row', 2.5,18),
       ('certificate four', 'fourth row', 3.5,17);

INSERT INTO tag (id_tag, name)
VALUES (1,'tag one'),(2,'tag two'),(3,'tag three'),(4,'fourth tag'),(5,'fifth tag'),(6,'tag six');

INSERT INTO certificates_tags (id_certificate, id_tag)
VALUES
       (1,1),
       (1,2),
       (1,3),
       (2,3),
       (2,4);