CREATE TABLE registration (
		registration_id BIGINT NOT NULL,
		email_address CHARACTER VARYING(500) NOT NULL,
		name character varying(200),
		subject character varying(200),
		website character varying(200),
		message TEXT,
		created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
		updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,		
	    CONSTRAINT registration_id_pk PRIMARY KEY (registration_id)
);

CREATE TABLE role (
		role_id BIGINT NOT NULL,
		description CHARACTER VARYING(100) NOT NULL,
		display_order INTEGER NOT NULL,
	    CONSTRAINT role_pk PRIMARY KEY (role_id)
);

CREATE TABLE account (
	    account_id BIGINT NOT NULL,	
	    username CHARACTER VARYING(100),
	    password CHARACTER VARYING(500),
	    remember_me_token CHARACTER VARYING(500) NOT NULL,
	    first_name CHARACTER VARYING(200) NOT NULL,
	    last_name CHARACTER VARYING(200) NOT NULL,
		email CHARACTER VARYING(200),
	    phone_number CHARACTER VARYING(10) NOT NULL,
	    api_token CHARACTER VARYING(500) NOT NULL,
	    time_zone_identifier CHARACTER VARYING(100) DEFAULT 'US/Eastern',
		country_identifier CHARACTER VARYING(100) DEFAULT 'US', -- ISO-3166
		language_identifier CHARACTER VARYING(100) DEFAULT 'en', -- ISO-639
		created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
		updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	    CONSTRAINT account_pk PRIMARY KEY (account_id)
);

CREATE TABLE trak (
	    trak_id BIGINT NOT NULL,	
	    title CHARACTER VARYING(100),
	    description TEXT,
		active BOOLEAN NOT NULL DEFAULT TRUE,  
		inUse BOOLEAN NOT NULL DEFAULT FALSE,
		isSpeechVisible BOOLEAN NOT NULL DEFAULT TRUE,
		created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
		updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	    CONSTRAINT trak_pk PRIMARY KEY (trak_id)
);

CREATE TABLE account_role_trak (
	    account_id BIGINT NOT NULL,		
		role_id BIGINT NOT NULL,
	    trak_id BIGINT NOT NULL,
		CONSTRAINT account_role_trak_pk PRIMARY KEY (account_id, role_id, trak_id)
);

CREATE TABLE point (
	    point_id BIGINT NOT NULL,	
	    trak_id BIGINT NOT NULL,
	    sort_order BIGINT NOT NULL,
    	title CHARACTER VARYING(100),
	    description TEXT,
	    active BOOLEAN NOT NULL DEFAULT TRUE,  
	    created_by BIGINT NOT NULL,
		created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
		updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	    CONSTRAINT point_pk PRIMARY KEY (point_id)
);

CREATE TABLE highlighted_point(
		highlighted_point_id BIGINT NOT NULL,
	    point_id BIGINT NOT NULL,	
		account_id BIGINT NOT NULL,
        read_flag BOOLEAN NOT NULL DEFAULT FALSE,                
		created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	    CONSTRAINT highlighted_point_pk PRIMARY KEY (highlighted_point_id)		
);

-- Indexes --
CREATE UNIQUE INDEX account_username_idx ON account (username);

-- Functions --

CREATE OR REPLACE FUNCTION set_updated_date() RETURNS TRIGGER AS $$
BEGIN        
	NEW.updated_date := 'now';
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION is_favorite (the_account_id bigint, the_video_id bigint) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
	RETURN (
		SELECT COUNT(f.favorite_id)
		FROM favorite f
		WHERE f.account_id = the_account_id
		AND f.video_id = the_video_id		
	) > 0;
END;
$$;

-- Triggers --
CREATE TRIGGER set_updated_date BEFORE INSERT OR UPDATE ON registration FOR EACH ROW EXECUTE PROCEDURE set_updated_date();
CREATE TRIGGER set_updated_date BEFORE INSERT OR UPDATE ON point FOR EACH ROW EXECUTE PROCEDURE set_updated_date();
CREATE TRIGGER set_updated_date BEFORE INSERT OR UPDATE ON trak FOR EACH ROW EXECUTE PROCEDURE set_updated_date();

-- Sequences --
CREATE SEQUENCE account_seq START WITH 1000;
CREATE SEQUENCE trak_seq START WITH 1000;
CREATE SEQUENCE point_seq START WITH 1000;
CREATE SEQUENCE highlighted_point_seq START WITH 1000;
CREATE SEQUENCE registration_seq START WITH 1000;

-- VIEWS --
CREATE VIEW v_point as
select p.*, count(hp.highlighted_point_id) > 0 as highlighted, hp.read_flag is true as read_flag 
from point p LEFT OUTER JOIN highlighted_point hp ON (p.point_id = hp.point_id and p.created_by = hp.account_id)
where active = true
group by p.point_id, hp.read_flag order by point_id;

CREATE VIEW trak_detail as
select t.trak_id, t.title, t.description, t.created_date, t.updated_date, art.role_id, a.email, r.description as role, a.username, art.account_id, 
	(select count(account_id) from account_role_trak art1 where art1.trak_id = t.trak_id and art1.account_id != art.account_id ) as collaborator_cnt 
from trak t, account_role_trak art, account a, role r 
where t.active = true and t.trak_id = art.trak_id and art.account_id = a.account_id and art.role_id = r.role_id;


