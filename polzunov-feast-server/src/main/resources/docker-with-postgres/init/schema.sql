--TODO make sure CASCADE is correct
CREATE TABLE users
(
    user_id integer NOT NULL,
    username character varying(25) NOT NULL,
    password character varying(25) NOT NULL,
    name character varying(15) NOT NULL,
    phone character varying(11) NOT NULL,
    email text NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE places
(
    place_id integer NOT NULL,
    name text NOT NULL,
    address text NOT NULL,
    location_description text NOT NULL,
    PRIMARY KEY (place_id)
);

CREATE TABLE events
(
    event_id integer NOT NULL,
    place_id integer NOT NULL,
    name character varying(25) NOT NULL,
    time_start time without time zone NOT NULL,
    time_end time without time zone NOT NULL,
    path_description text NOT NULL,
    age integer NOT NULL,
    PRIMARY KEY (event_id),
    --update/delete?
    FOREIGN KEY (place_id) REFERENCES places (place_id)
);

CREATE TABLE routes
(
    route_id integer NOT NULL,
    user_id integer NOT NULL,
    PRIMARY KEY (route_id),
    --update/delete?
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE transport
(
    transport_id integer NOT NULL,
    name character varying(15) NOT NULL,
    PRIMARY KEY (transport_id)
);

CREATE TABLE route_nodes
(
    route_node_id integer,
    route_id integer NOT NULL,
    event_id integer NOT NULL,
    current_transport_id integer NOT NULL,
    moving_time integer NOT NULL,
    estimate_arriving_time integer NOT NULL,
    PRIMARY KEY (route_node_id),
    --update/delete?
    FOREIGN KEY (route_id) REFERENCES routes (route_id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE CASCADE,
    FOREIGN KEY (current_transport_id) REFERENCES transport (transport_id)
);

CREATE TABLE route_nodes_possible_transport
(
    route_node_id integer NOT NULL,
    possible_transport_id integer NOT NULL,
    --update/delete?
    FOREIGN KEY (route_node_id) REFERENCES route_nodes (route_node_id) ON DELETE CASCADE,
    FOREIGN KEY (possible_transport_id) REFERENCES transport (transport_id) ON DELETE CASCADE
)