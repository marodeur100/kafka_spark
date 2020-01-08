CREATE TABLE IF NOT EXISTS customer_transaction (
    id         integer,
    action varchar(100) ,
    username varchar(100),
    ts timestamp primary key
);

