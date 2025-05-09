CREATE TABLE IF NOT EXISTS BWS_USER (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	PASSWORD CHARACTER VARYING(255) NOT NULL,
	PERMISSIONS BIGINT NOT NULL,
	USERNAME CHARACTER VARYING(255) NOT NULL,
	CONSTRAINT CONSTRAINT_A PRIMARY KEY (ID),
	CONSTRAINT UK_USER_USERNAME UNIQUE (USERNAME)
);
CREATE INDEX IF NOT EXISTS IDX_USER_USERNAME ON BWS_USER (USERNAME);
CREATE UNIQUE INDEX IF NOT EXISTS UK_USER_USERNAME_INDEX_A ON BWS_USER (USERNAME);