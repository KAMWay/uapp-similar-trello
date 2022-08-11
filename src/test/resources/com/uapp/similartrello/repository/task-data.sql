INSERT INTO groups (id, name, position) VALUES (1, 'To Do', 1);
INSERT INTO groups (id, name, position) VALUES (2, 'June 23rd', 2);

INSERT INTO task (id, name, description, date_create, position, group_id) VALUES (1, 'Task 1.1', 'Something to do 1', '2012-06-30', 1, 1);
INSERT INTO task (id, name, description, date_create, position, group_id) VALUES (2, 'Task 1.2', 'Something to do 2', '2012-05-10', 2, 1);
