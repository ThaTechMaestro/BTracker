CREATE TABLE IF NOT EXISTS users
(
    user_id              SERIAL PRIMARY KEY,
    user_name            VARCHAR(50) NOT NULL,
    password             VARCHAR(255) NOT NULL,
    role                 VARCHAR(20) NOT NULL,
    created_time         TIMESTAMP NOT NULL DEFAULT clock_timestamp(),
    updated_time         TIMESTAMP NOT NULL DEFAULT clock_timestamp()
    );

INSERT INTO users (user_name,password,role) VALUES
('Admin','{bcrypt}$2a$12$BRX2ORh/JDobQW6lw5hwjembVPZoRvulHujXvTRtI7r1xQAQusskC','ADMIN'),
('User','{bcrypt}$2a$12$sFmo39TT3oP4KjdVzvpMvelGhCUWxkid.Mo3B2XtVZAP5WV//62xO','USER');