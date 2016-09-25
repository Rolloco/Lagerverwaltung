CREATE TABLE LAGERVERWALTUNG (
	  BARCODE VARCHAR(20) NOT NULL PRIMARY KEY
    , BEZEICHNUNG VARCHAR(20)
    , STUECKZAHL INTEGER
    , DATUM DATE
    , ABLAUFDATUM DATE
    , PREIS DOUBLE
	, KUNDENNUMMER INTEGER
);

CREATE TABLE LIEFERANT (
	  NAME VARCHAR(20)
	, KUNDENNUMMER INTEGER NOT NULL PRIMARY KEY
);