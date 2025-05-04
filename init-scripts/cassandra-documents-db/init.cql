CREATE KEYSPACE IF NOT EXISTS crawler WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };

CREATE TABLE IF NOT EXISTS crawler.url (
    url text PRIMARY KEY,
    content text,
    last_update_timestamp timestamp
);