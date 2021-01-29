USE `test_db` ;
INSERT INTO gift_certificate (name, description, price, duration)
VALUES ('certificate one', 'description', 12.5, 5),
       ('certificate two', 'some text', 8.5,9),
       ('certificate three', 'third row', 2.5,18),
       ('certificate four', 'fourth row', 3.5,17);

INSERT INTO tag (id_tag, name)
VALUES (1,'tag one'),
(2,'tag two'),
(3,'tag three'),
(4,'fourth tag'),
(5,'fifth tag'),
(6,'tag six');

INSERT INTO certificates_tags (id_certificate, id_tag)
VALUES
       (1,1),
       (1,2),
       (1,3),
       (2,3),
       (2,4);

INSERT INTO order_table (id_order,price,time_of_puchase,id_user)
VALUES
(1,55,2021-01-25 08:19:58,1),
(2,40,2021-01-25 08:19:58,2),
(3,22,2021-01-25 08:19:58,3);

INSERT INTO user (id_user,login,password,role)
VALUES
(1,'user1','passwword1',USER),
(2,'user2','passwword2',USER),
(3,'user3','passwword3',USER);

INSERT INTO orders_certificates (id_order,id_certificate)
VALUES
(1,1),
(1,2),
(1,3),
(2,1),
(2,2),
(2,3),
(3,1),
(3,2),
(3,3);

