DROP TABLE IF EXISTS groups;
CREATE TABLE groups (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	name VARCHAR(45) NULL ,
	created_at BIGINT NULL ,
	pass_phrase VARCHAR(45) NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS photos;
CREATE TABLE photos (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	user_id BIGINT NULL ,
	create_at BIGINT NULL ,
	latitude DOUBLE NULL ,
	longitude DOUBLE NULL ,
	city_id BIGINT NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS photo_entrys;
CREATE TABLE photo_entrys (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	photo_id BIGINT NULL ,
	group_id BIGINT NULL ,
	user_id BIGINT NULL ,
	create_at BIGINT NULL ,
	acknowledged TINYINT NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS users;
CREATE TABLE users (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	name VARCHAR(45) NULL ,
	joint_at BIGINT NULL ,
	api_key VARCHAR(45) NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS group_members;
CREATE TABLE group_members (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	group_id BIGINT NULL ,
	user_id BIGINT NULL ,
	join_at BIGINT NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS devices;
CREATE TABLE devices (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	device_type VARCHAR(45) NULL ,
	udid VARCHAR(45) NULL ,
	user_id BIGINT NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS citys;
CREATE TABLE citys (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	name VARCHAR(45) NULL ,
	latitude DOUBLE NULL ,
	longitude DOUBLE NULL ,
	county VARCHAR(45) NULL ,
	state_abbreviation VARCHAR(2) NULL ,
	state VARCHAR(45) NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS faces;
CREATE TABLE faces (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	photo_id BIGINT NULL ,
	face_index INT NULL ,
	x INT NULL ,
	y INT NULL ,
	width INT NULL ,
	height INT NULL ,
	PRIMARY KEY (id));

DROP TABLE IF EXISTS face_detection_logs;
CREATE TABLE face_detection_logs (
	id BIGINT NOT NULL AUTO_INCREMENT ,
	photo_id BIGINT NULL ,
	create_at BIGINT NULL ,
	num_faces INT NULL ,
	PRIMARY KEY (id));
