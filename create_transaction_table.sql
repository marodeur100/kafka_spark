CREATE TABLE IF NOT EXISTS customer_transaction (
    id         integer,
    action varchar(100) ,
    username varchar(100),
    ts timestamp,
    customername varchar(400),
    customeraddress varchar(400),
    state varchar(100),
    creation_time   timestamp DEFAULT current_timestamp
);

