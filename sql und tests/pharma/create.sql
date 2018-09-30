----

CREATE TABLE Adresse (
    addr_id   INTEGER PRIMARY KEY    CHECK (addr_id >= 0),
    Strasse   VARCHAR(200)  NOT NULL CHECK (Strasse NOT GLOB '*[0-9]*'),
    Hausnr    INTEGER       NOT NULL CHECK (Hausnr >= 0), 
    PLZ       VARCHAR(10)   NOT NULL CHECK (length(PLZ) > 0), 
    Ort       VARCHAR(50)   NOT NULL CHECK (Ort NOT GLOB '*[0-9]*')
);

----

CREATE TABLE Ansprechpartner (
    Ansprechpartner_id  INTEGER PRIMARY KEY  CHECK (Ansprechpartner_id >= 0),
    Kunde_id            INTEGER       NOT NULL,
    Fachabteilung       VARCHAR(30)   NOT NULL,
    FOREIGN KEY (Ansprechpartner_id) REFERENCES Person (Person_id),
    FOREIGN KEY (Kunde_id) REFERENCES Kunde (Kunde_id)
);

----

CREATE TABLE AuftragBuchtMedikament (
Auftrag_id      INTEGER   NOT NULL,
Medikament_id   INTEGER   NOT NULL,
Anzahl          INTEGER   NOT NULL CHECK (Anzahl > 0),
PRIMARY KEY (Auftrag_id, Medikament_id),
FOREIGN KEY (Auftrag_id) REFERENCES Auftrag (Auftrag_id),
FOREIGN KEY (Medikament_id)   REFERENCES Medikament (Medikament_id)
);

----

CREATE TABLE Auftrag (
    Auftrag_id        INTEGER PRIMARY KEY  CHECK (Auftrag_id >= 0),
    Konto_id          INTEGER     NOT NULL,
    Mitarbeiter_id    INTEGER     NOT NULL,
    EinVerkauf        VARCHAR(50) NOT NULL CHECK (EinVerkauf IN ('Einkauf', 'Verkauf')),
    Erstellungsdatum  DATE        NOT NULL DEFAULT (date('now','localtime')),
    Liefertermin      DATE DEFAULT (date('now', '+7 days')),
    FOREIGN KEY (Mitarbeiter_id) REFERENCES Mitarbeiter (Mitarbeiter_id),
    FOREIGN KEY (Konto_id) REFERENCES Konto (Konto_id)
);

----

CREATE TRIGGER auftrag_trig AFTER insert ON Auftrag 
BEGIN 
    update Auftrag SET Erstellungsdatum = datetime('now','localtime') WHERE Auftrag_id = NEW.Auftrag_id; 
END;

----

CREATE TABLE CallcenterMitarbeiter (
    CallcenterMitarbeiter_id  INTEGER PRIMARY KEY,
    Sprachzertifikat          VARCHAR(30) NOT NULL  CHECK (Sprachzertifikat NOT GLOB '*[0-9]*'),
    FOREIGN KEY (CallcenterMitarbeiter_id) REFERENCES Mitarbeiter (Mitarbeiter_id)
);

----

CREATE TABLE istVorgesetzter (
    Vorgesetzter INTEGER NOT NULL,
    Untergebener INTEGER NOT NULL,
    PRIMARY KEY (Vorgesetzter, Untergebener),
    FOREIGN KEY (Vorgesetzter) REFERENCES Mitarbeiter (Mitarbeiter_id),
    FOREIGN KEY (Untergebener) REFERENCES Mitarbeiter (Mitarbeiter_id)
);

----

CREATE TABLE Kontakteintrag (
    TelefonNr   INTEGER PRIMARY KEY CHECK (TelefonNr > 0),
    addr_id     INTEGER   NOT NULL,
    EMail       VARCHAR(200) CHECK ((length(EMail) > 0) 
	AND (EMail GLOB '?*@?*.?*') 
	AND (EMail NOT GLOB '?*@?*.*[0-9]*')),
    Fax         INTEGER CHECK (Fax >= 0),
    FOREIGN KEY (addr_id) REFERENCES Adresse (addr_id)
);

----

CREATE TABLE Konto (
Konto_id      INTEGER PRIMARY KEY CHECK (Konto_id >= 0),
Kunde_id      INTEGER     NOT NULL,
Bezeichnung   VARCHAR(30) NOT NULL,
Zahlungsziel  VARCHAR(3) NOT NULL 
    CHECK(	(substr(Zahlungsziel,-1) in ('t', 'T'))
	AND ( ((length(Zahlungsziel) == 2) 
		AND (CAST(substr(Zahlungsziel, 1, 3) AS INTEGER) > 0) )
	    OR ((length(Zahlungsziel) == 3) 
		AND (CAST(substr(Zahlungsziel, 1, 3) AS INTEGER) > 10) 
		AND (CAST(substr(Zahlungsziel, 1, 3) AS INTEGER) < 31))) ),
    Zahlungsart   VARCHAR(30) NOT NULL,
    FOREIGN KEY (Kunde_id) REFERENCES Kunde( Kunde_id)
);

----

CREATE TABLE KundeHatKontakteintrag (
    TelefonNr   INTEGER   NOT NULL,
    Kunde_id    INTEGER   NOT NULL,
    PRIMARY KEY (TelefonNr),
    FOREIGN KEY (TelefonNr) REFERENCES Kontakteintrag (TelefonNr),
    FOREIGN KEY (Kunde_id) REFERENCES Kunde (Kunde_id)
);

----

CREATE TRIGGER kunde_kontakt_trig AFTER insert ON KundeHatKontakteintrag 
BEGIN 
    SELECT RAISE(FAIL, "Kontakteintrag schon gemacht") 
    FROM PersonHatKontakteintrag 
    WHERE TelefonNr = NEW.TelefonNr; 
END;

----

CREATE TABLE KundenbetreuerBetreutKunde (
    Kunde_id            INTEGER   NOT NULL,
    Kundenbetreuer_id   INTEGER   NOT NULL,
    PRIMARY KEY (Kunde_id, Kundenbetreuer_id),
    FOREIGN KEY (Kunde_id)          REFERENCES Kunde (Kunde_id),
    FOREIGN KEY (Kundenbetreuer_id) REFERENCES Kundenbetreuer (Kundenbetreuer_id)
);

----

CREATE TRIGGER zu_viele_kunden BEFORE INSERT ON KundenbetreuerBetreutKunde 
BEGIN 
    SELECT RAISE(FAIL, "zu_viele_kunden") 
    FROM (	SELECT count(*) AS c 
	    FROM KundenbetreuerBetreutKunde 
	    WHERE Kundenbetreuer_id = NEW.Kundenbetreuer_id) 
    WHERE c > 2; 
END;

----

CREATE TABLE Kundenbetreuer (
    Kundenbetreuer_id   INTEGER PRIMARY KEY,
    Fachgebiet          VARCHAR(30) NOT NULL,
    Bezirk              VARCHAR(30) NOT NULL CHECK(length(Bezirk) >= 0),
    FOREIGN KEY (Kundenbetreuer_id) REFERENCES Mitarbeiter (Mitarbeiter_id)
);

----

CREATE TRIGGER zu_viele_fachgebiten BEFORE INSERT ON Kundenbetreuer 
BEGIN 
    SELECT RAISE(FAIL, "zu_viele_fachgebieten") 
    FROM (  SELECT count(*) AS c 
	    FROM Kundenbetreuer 
	    GROUP BY Fachgebiet) 
    WHERE c > 1; 
END;

----

CREATE TABLE Kunde (
    Kunde_id      INTEGER PRIMARY KEY CHECK (Kunde_id >= 0),
    Bezeichnung   VARCHAR(30) NOT NULL,
    ApothekenSchein BLOB
);

----

CREATE TABLE Medikament (
    Medikament_id   INTEGER PRIMARY KEY 		CHECK (Medikament_id >= 0),
    Produktname     VARCHAR(50)   NOT NULL,
    Kennzeichnung   VARCHAR(50) 	COLLATE NOCASE 	CHECK (Kennzeichnung in ('freiverkäuflich', 'verschreibungspflichtig'))
);

----

CREATE TABLE Mitarbeiter (
    Mitarbeiter_id  INTEGER PRIMARY KEY,
    Mitarbeiter_nr  INTEGER       NOT NULL UNIQUE CHECK (Mitarbeiter_nr > 0),
    Passwort        VARCHAR(30)   NOT NULL CHECK (length(Passwort) > 0),
    FOREIGN KEY (Mitarbeiter_id) REFERENCES Person (Person_id)
);

----

CREATE TABLE PersonHatKontakteintrag (
    TelefonNr INTEGER NOT NULL,
    Person_id INTEGER  NOT NULL,
    PRIMARY KEY (TelefonNr),
    FOREIGN KEY (TelefonNr) REFERENCES Kontakteintrag (TelefonNr),
    FOREIGN KEY (Person_id) REFERENCES Person (Person_id)
);

----

CREATE TRIGGER person_kontakt_trig AFTER insert ON PersonHatKontakteintrag 
BEGIN 
    SELECT RAISE(FAIL, "Kontakteintrag schon gemacht") 
    FROM KundeHatKontakteintrag 
    WHERE TelefonNr = NEW.TelefonNr; 
END;

----

CREATE TABLE Person (
    Person_id   INTEGER PRIMARY KEY CHECK (Person_id >= 0),
    Vorname     VARCHAR(30)  NOT NULL CHECK (Vorname NOT GLOB '*[0-9]*'),
    Nachname    VARCHAR(30)  NOT NULL CHECK (Nachname NOT GLOB '*[0-9]*')
);

----

CREATE TABLE Staffelpreis (
    Staffelpreis_id   INTEGER PRIMARY KEY CHECK (Staffelpreis_id >= 0),
    Medikament_id     INTEGER     NOT NULL,
    Anzahl            INTEGER     NOT NULL CHECK (Anzahl > 0),
    EinVerkauf        VARCHAR(50) NOT NULL CHECK (EinVerkauf IN ('Einkauf', 'Verkauf')),
    Preis             DOUBLE      NOT NULL ,
    FOREIGN KEY (Medikament_id) REFERENCES Medikament (Medikament_id)
);

----
