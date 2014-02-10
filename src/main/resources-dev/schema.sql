DROP TABLE IF EXISTS users;
CREATE TABLE users (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	name VARCHAR(45) NULL ,
	email VARCHAR(45) NULL ,
	image_url VARCHAR(45) NULL ,
	provider_id VARCHAR(45) NULL ,
	provider_user_id VARCHAR(45) NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS groups;
create table groups (
  id BIGINT AUTO_INCREMENT,
  group_id BIGINT,
  owner_id BIGINT,
  name VARCHAR(512),
  created_at BIGINT,
  primary key (id)
);

DROP TABLE IF EXISTS group_users;
CREATE TABLE group_users (
  id BIGINT AUTO_INCREMENT,
  group_id BIGINT,
  user_id BIGINT,
  primary key (id)
);
