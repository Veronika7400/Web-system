CREATE TABLE DNEVNIK (
  id integer not null generated always as identity (start with 1, increment by 1),
  vrsta varchar(10) NOT NULL,
  zahtjev varchar(60) NOT NULL,
  vrijeme TIMESTAMP NOT NULL, 
  PRIMARY KEY (id)
);

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DNEVNIK TO APLIKACIJA;

DELETE * FROM DNEVNIK; 
SELECT COUNT(*) FROM DNEVNIK; 
