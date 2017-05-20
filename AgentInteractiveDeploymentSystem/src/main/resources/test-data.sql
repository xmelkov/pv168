-- my-test-data.sql
INSERT INTO agents(name, gender, age, phoneNumber, alive) VALUES ('Janko', 2, 33, '999999999', TRUE);

INSERT INTO agents(name, gender, age, phoneNumber, alive) VALUES ('Risko', 1, 34, '888888888', TRUE);

INSERT INTO agents(name, gender, age, phoneNumber, alive) VALUES ('Petko', 3, 35, '777777777', TRUE);

INSERT INTO missions(description, numberOfRequiredAgents, difficulty, place, successful) VALUES ('Zberanie jablk', 1, 3, 'babkina zahrada', TRUE);

INSERT INTO assignments(AgentId, MissionId, startdate, endDate) VALUES (1, 1, '2013-04-01', '2013-05-20');

