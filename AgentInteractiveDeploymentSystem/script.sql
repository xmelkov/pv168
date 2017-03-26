 create table agents (
   id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
   name VARCHAR(50),
   gender SMALLINT,
   age SMALLINT,
   phone VARCHAR(20),
   alive BIT);

 create table assignments (
   id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
   agentId BIGINT REFERENCES agents(id) on delete cascade,
   missionId BIGINT REFERENCES missions(id) on delete cascade,
   start TIMESTAMP,
   end TIMESTAMP);

 create table missions (
   id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
   description VARCHAR(100),
   requiredAgents SMALLINT,
   difficulty INT,
   place VARCHAR(30),
   successful BIT);