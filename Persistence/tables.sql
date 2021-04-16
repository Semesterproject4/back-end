CREATE DATABASE brewmes;

CREATE TABLE Machine (
    id UUID PRIMARY KEY,
    ip varchar(100)
);

CREATE TABLE Batch (
    id UUID PRIMARY KEY,
    machine_id UUID REFERENCES Machine(id),
    product_type INT,
    normalized_machine_speed FLOAT,
    machine_speed FLOAT,
    processed_products INT,
    acceptable_products INT,
    defect_products INT,
    total_products INT
);

CREATE TABLE Vibration (
    id SERIAL PRIMARY KEY,
    batch_id UUID REFERENCES Batch(id),
    time_stamp TIME,
    vibration FLOAT
);

CREATE TABLE Temperature (
    id SERIAL PRIMARY KEY,
    batch_id UUID REFERENCES Batch(id),
    time_stamp TIME,
    temperature FLOAT
);

CREATE TABLE Humidity (
    id SERIAL PRIMARY KEY,
    batch_id UUID REFERENCES Batch(id),
    time_stamp TIME,
    humidity FLOAT
);

CREATE TABLE Time_in_states (
    id SERIAL PRIMARY KEY,
    machine_state INT,
    time FLOAT,
    batch_id UUID references Batch(id)
);