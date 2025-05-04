CREATE SCHEMA crawler;

CREATE TABLE crawler.url (
    id SERIAL PRIMARY KEY,
    url VARCHAR(150) NOT NULL,
    creation_date timestamptz DEFAULT now()
);
