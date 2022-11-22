CREATE TABLE users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    password VARCHAR(255),
    is_admin BOOLEAN,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE railway_stations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_railway_stations PRIMARY KEY (id)
);

CREATE TABLE trains
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    number BIGINT,
    number_of_seats BIGINT,
    CONSTRAINT pk_trains PRIMARY KEY (id)
);

CREATE TABLE railway_routes
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_station_id BIGINT,
    finish_station_id BIGINT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    train_id BIGINT,
    CONSTRAINT pk_railway_routes PRIMARY KEY (id)
);

CREATE TABLE tickets
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    railway_route_id BIGINT,
    seat_number BIGINT,
    user_id BIGINT,
    CONSTRAINT pk_tickets PRIMARY KEY (id)
);

SELECT * FROM railway_stations;
INSERT INTO railway_stations(name) VALUES ('Railway Station 1');
INSERT INTO railway_stations(name) VALUES ('Railway Station 2');

SELECT * FROM users;
INSERT INTO users(name, password, is_admin) VALUES ('user', 'pass', false);
INSERT INTO users(name, password, is_admin) VALUES ('admin', 'pass', true);

SELECT * FROM trains;
INSERT INTO trains(number, number_of_seats) values (101, 35);
INSERT INTO trains(number, number_of_seats) values (78, 50);

SELECT * FROM railway_routes;
DROP TABLE railway_routes;

SELECT COUNT(id) AS count FROM tickets WHERE railway_route_id = 1