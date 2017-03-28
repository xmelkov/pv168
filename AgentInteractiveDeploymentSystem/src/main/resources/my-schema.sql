-- my-schema.sql

CREATE TABLE missions (
  id                     BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  description            VARCHAR(300),
  numberOfRequiredAgents SMALLINT,
  difficulty             INT,
  place                  VARCHAR(100),
  successful             BOOLEAN);

CREATE TABLE agents (
  id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  name        VARCHAR(150),
  gender      SMALLINT,
  age         SMALLINT,
  phoneNumber VARCHAR(20),
  alive       BOOLEAN);

CREATE TABLE assignments (
  id           BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  AgentId      BIGINT REFERENCES agents (id) ON DELETE CASCADE,
  MissionId    BIGINT REFERENCES missions (id) ON DELETE CASCADE,
  startDate    VARCHAR(100),
  endDate      VARCHAR(100));