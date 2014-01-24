DROP DATABASE rebar;
DROP ROLE rebar;

CREATE ROLE rebar LOGIN PASSWORD 'test123' NOSUPERUSER NOINHERIT NOCREATEROLE;
CREATE DATABASE rebar WITH OWNER = rebar ENCODING = 'UTF8';

\c rebar;
	
CREATE SCHEMA rebar 
AUTHORIZATION rebar;	

ALTER USER rebar SET search_path TO rebar;

ALTER DATABASE rebar SET timezone TO 'UTC';

-- Use backcompat bytea mode for Postgres 9+
ALTER DATABASE rebar SET bytea_output = 'escape'; 