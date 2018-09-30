

INSERT INTO Person VALUES (11, 'VornameA', 'NachnameA');
INSERT INTO Person VALUES (12, 'VornameB', 'NachnameB');
INSERT INTO Person VALUES (13, 'VornameC', 'NachnameC');
INSERT INTO Person VALUES (21, 'VornameD', 'NachnameD');
INSERT INTO Person VALUES (22, 'VornameE', 'NachnameE');
INSERT INTO Person VALUES (23, 'VornameF', 'NachnameF');
INSERT INTO Person VALUES (24, 'VornameG', 'NachnameG');
INSERT INTO Person VALUES (31, 'VornameH', 'NachnameH');
INSERT INTO Person VALUES (32, 'VornameI', 'NachnameI');

;

INSERT INTO Mitarbeiter VALUES (21, 212121, 'password');
INSERT INTO Mitarbeiter VALUES (22, 222222, 'password');
INSERT INTO Mitarbeiter VALUES (23, 232323, 'password');
INSERT INTO Mitarbeiter VALUES (24, 242424, 'password');
INSERT INTO Mitarbeiter VALUES (31, 313131, 'password');
INSERT INTO Mitarbeiter VALUES (32, 323232, 'password');

;

INSERT INTO Kundenbetreuer VALUES (21, 'Fach1', 'Bezirk1');
INSERT INTO Kundenbetreuer VALUES (22, 'Fach2', 'Bezirk2');
INSERT INTO Kundenbetreuer VALUES (23, 'Fach3', 'Bezirk3');
INSERT INTO Kundenbetreuer VALUES (24, 'Fach4', 'Bezirk4');

;

INSERT INTO CallcenterMitarbeiter VALUES (31, 'Englisch');
INSERT INTO CallcenterMitarbeiter VALUES (32, 'Swedisch');

;

INSERT INTO istVorgesetzter VALUES (21, 22);
INSERT INTO istVorgesetzter VALUES (21, 31);

;

INSERT INTO Medikament VALUES (11, 'Medikament_Name11', 'freiverkäuflich');
INSERT INTO Medikament VALUES (12, 'Medikament_Name12', 'freiverkäuflich');
INSERT INTO Medikament VALUES (13, 'Medikament_Name13', 'freiverkäuflich');
INSERT INTO Medikament VALUES (21, 'Medikament_Name21', 'verschreibungspflichtig');
INSERT INTO Medikament VALUES (22, 'Medikament_Name22', 'verschreibungspflichtig');
INSERT INTO Medikament VALUES (23, 'Medikament_Name23', 'verschreibungspflichtig');

;

INSERT INTO Kunde VALUES (11, 'Kunde mit Schein', 0x0123456789ABCDEF);
INSERT INTO Kunde VALUES (12, 'Kunde mit Schein', 0x0123456789ABCDEF);
INSERT INTO Kunde VALUES (13, 'Kunde mit Schein', 0x0123456789ABCDEF);
INSERT INTO Kunde VALUES (21, 'Kunde ohne Schein', null);
INSERT INTO Kunde VALUES (22, 'Kunde ohne Schein', null);
INSERT INTO Kunde VALUES (23, 'Kunde ohne Schein', null);

;

INSERT INTO Adresse VALUES (111, 'for Kunde', 11, '11111', 'Ort');
INSERT INTO Adresse VALUES (112, 'for Kunde', 12, '11111', 'Ort');
INSERT INTO Adresse VALUES (113, 'for Kunde', 13, '11111', 'Ort');
INSERT INTO Adresse VALUES (121, 'for Person', 21, '11111', 'Ort');
INSERT INTO Adresse VALUES (122, 'for Person', 22, '11111', 'Ort');
INSERT INTO Adresse VALUES (123, 'for Person', 23, '11111', 'Ort');

;

INSERT INTO Ansprechpartner VALUES (11, 11, 'abteilung1');
INSERT INTO Ansprechpartner VALUES (12, 22, 'abteilung1');
INSERT INTO Ansprechpartner VALUES (13, 23, 'abteilung1');

;

INSERT INTO Konto VALUES (11, 11, 'Konto Kunde 11', '5t', 'sepa');
INSERT INTO Konto VALUES (12, 12, 'Konto Kunde 12', '5t', 'karte');
INSERT INTO Konto VALUES (13, 13, 'Konto Kunde 13', '5t', 'Lastschrift');
INSERT INTO Konto VALUES (14, 21, 'Konto Kunde 21', '5t', 'sepa');
INSERT INTO Konto VALUES (15, 22, 'Konto Kunde 22', '5t', 'karte');
INSERT INTO Konto VALUES (16, 23, 'Konto Kunde 23', '5t', 'Überweisung');

;

INSERT INTO Kontakteintrag VALUES (111111, 121, 'person11@aaa.bb', 111111);
INSERT INTO Kontakteintrag VALUES (111112, 122, 'person12@aaa.bb', 121212);
INSERT INTO Kontakteintrag VALUES (111113, 123, 'person13@aaa.bb', 131313);
INSERT INTO Kontakteintrag VALUES (111121, 121, 'person21@aaa.bb', 212121);
INSERT INTO Kontakteintrag VALUES (111122, 122, 'person22@aaa.bb', 222222);
INSERT INTO Kontakteintrag VALUES (111123, 123, 'person23@aaa.bb', 232323);
INSERT INTO Kontakteintrag VALUES (111124, 121, 'person24@aaa.bb', 242424);
INSERT INTO Kontakteintrag VALUES (111131, 122, 'person31@aaa.bb', 313131);
INSERT INTO Kontakteintrag VALUES (111132, 123, 'person32@aaa.bb', 323232);

INSERT INTO Kontakteintrag VALUES (222211, 111, 'kunde11@aaa.bb', 11111111);
INSERT INTO Kontakteintrag VALUES (222212, 112, 'kunde12@aaa.bb', 12121212);
INSERT INTO Kontakteintrag VALUES (222213, 113, 'kunde13@aaa.bb', 13131313);
INSERT INTO Kontakteintrag VALUES (222221, 111, 'kunde21@aaa.bb', 21212121);
INSERT INTO Kontakteintrag VALUES (222222, 112, 'kunde22@aaa.bb', 22222222);
INSERT INTO Kontakteintrag VALUES (222223, 113, 'kunde23@aaa.bb', 23232323);

;

INSERT INTO Auftrag VALUES (111, 11, 21, 'Einkauf', date('now','-7 days'), date('now','-5 days'));
INSERT INTO Auftrag VALUES (112, 11, 21, 'Verkauf', date('now','-7 days'), date('now','-5 days'));
INSERT INTO Auftrag VALUES (113, 12, 22, 'Einkauf', date('now','-7 days'), date('now','-5 days'));
INSERT INTO Auftrag VALUES (114, 12, 24, 'Einkauf', date('now','-7 days'), date('now','-5 days'));

INSERT INTO Auftrag VALUES (121, 14, 24, 'Einkauf', date('now','-7 days'), date('now','-5 days'));
INSERT INTO Auftrag VALUES (122, 14, 24, 'Verkauf', date('now','-7 days'), date('now','-5 days'));
INSERT INTO Auftrag VALUES (123, 15, 24, 'Einkauf', date('now','-7 days'), date('now','-5 days'));
INSERT INTO Auftrag VALUES (124, 16, 31, 'Einkauf', date('now','-7 days'), date('now','-5 days'));

;

INSERT INTO AuftragBuchtMedikament VALUES (111, 21, 3);
INSERT INTO AuftragBuchtMedikament VALUES (112, 21, 7);
INSERT INTO AuftragBuchtMedikament VALUES (113, 22, 12);
INSERT INTO AuftragBuchtMedikament VALUES (114, 23, 17);
INSERT INTO AuftragBuchtMedikament VALUES (114, 22, 16);

INSERT INTO AuftragBuchtMedikament VALUES (121, 11, 2);
INSERT INTO AuftragBuchtMedikament VALUES (122, 11, 6);
INSERT INTO AuftragBuchtMedikament VALUES (123, 12, 13);
INSERT INTO AuftragBuchtMedikament VALUES (124, 13, 19);

;

INSERT INTO Staffelpreis VALUES (111, 11, 1, 'Einkauf', 1.40);
INSERT INTO Staffelpreis VALUES (112, 11, 5, 'Einkauf', 1.20);
INSERT INTO Staffelpreis VALUES (113, 11, 10, 'Einkauf', 1.00);
INSERT INTO Staffelpreis VALUES (114, 11, 1, 'Verkauf', 1.50);
INSERT INTO Staffelpreis VALUES (115, 11, 5, 'Verkauf', 1.40);
INSERT INTO Staffelpreis VALUES (116, 11, 10, 'Verkauf', 1.20);

INSERT INTO Staffelpreis VALUES (121, 12, 1, 'Einkauf', 25.00);
INSERT INTO Staffelpreis VALUES (122, 12, 5, 'Einkauf', 24.00);
INSERT INTO Staffelpreis VALUES (123, 12, 10, 'Einkauf', 23.00);
INSERT INTO Staffelpreis VALUES (124, 12, 1, 'Verkauf', 25.50);
INSERT INTO Staffelpreis VALUES (125, 12, 5, 'Verkauf', 24.50);
INSERT INTO Staffelpreis VALUES (126, 12, 10, 'Verkauf', 23.50);

INSERT INTO Staffelpreis VALUES (131, 13, 1, 'Einkauf', 35.00);
INSERT INTO Staffelpreis VALUES (132, 13, 5, 'Einkauf', 34.00);
INSERT INTO Staffelpreis VALUES (133, 13, 10, 'Einkauf', 33.00);
INSERT INTO Staffelpreis VALUES (134, 13, 1, 'Verkauf', 35.50);
INSERT INTO Staffelpreis VALUES (135, 13, 5, 'Verkauf', 34.50);
INSERT INTO Staffelpreis VALUES (136, 13, 10, 'Verkauf', 33.50);

INSERT INTO Staffelpreis VALUES (211, 21, 1, 'Einkauf', 55.00);
INSERT INTO Staffelpreis VALUES (212, 21, 5, 'Einkauf', 54.00);
INSERT INTO Staffelpreis VALUES (213, 21, 10, 'Einkauf', 53.00);
INSERT INTO Staffelpreis VALUES (214, 21, 1, 'Verkauf', 55.50);
INSERT INTO Staffelpreis VALUES (215, 21, 5, 'Verkauf', 54.50);
INSERT INTO Staffelpreis VALUES (216, 21, 10, 'Verkauf', 53.50);

INSERT INTO Staffelpreis VALUES (221, 22, 1, 'Einkauf', 65.00);
INSERT INTO Staffelpreis VALUES (222, 22, 5, 'Einkauf', 64.00);
INSERT INTO Staffelpreis VALUES (223, 22, 10, 'Einkauf', 63.00);
INSERT INTO Staffelpreis VALUES (224, 22, 1, 'Verkauf', 65.50);
INSERT INTO Staffelpreis VALUES (225, 22, 5, 'Verkauf', 64.50);
INSERT INTO Staffelpreis VALUES (226, 22, 10, 'Verkauf', 63.50);

INSERT INTO Staffelpreis VALUES (231, 23, 1, 'Einkauf', 75.00);
INSERT INTO Staffelpreis VALUES (232, 23, 5, 'Einkauf', 74.00);
INSERT INTO Staffelpreis VALUES (233, 23, 10, 'Einkauf', 73.00);
INSERT INTO Staffelpreis VALUES (234, 23, 1, 'Verkauf', 75.50);
INSERT INTO Staffelpreis VALUES (235, 23, 5, 'Verkauf', 74.50);
INSERT INTO Staffelpreis VALUES (236, 23, 10, 'Verkauf', 73.50);

;

INSERT INTO KundeHatKontakteintrag VALUES (222211, 11);
INSERT INTO KundeHatKontakteintrag VALUES (222212, 12);
INSERT INTO KundeHatKontakteintrag VALUES (222213, 13);
INSERT INTO KundeHatKontakteintrag VALUES (222221, 21);
INSERT INTO KundeHatKontakteintrag VALUES (222222, 22);
INSERT INTO KundeHatKontakteintrag VALUES (222223, 23);

;

INSERT INTO PersonHatKontakteintrag VALUES (111111, 11);
INSERT INTO PersonHatKontakteintrag VALUES (111112, 12);
INSERT INTO PersonHatKontakteintrag VALUES (111113, 13);
INSERT INTO PersonHatKontakteintrag VALUES (111121, 21);
INSERT INTO PersonHatKontakteintrag VALUES (111122, 22);
INSERT INTO PersonHatKontakteintrag VALUES (111123, 23);
INSERT INTO PersonHatKontakteintrag VALUES (111124, 24);
INSERT INTO PersonHatKontakteintrag VALUES (111131, 31);
INSERT INTO PersonHatKontakteintrag VALUES (111132, 32);

;

INSERT INTO KundenbetreuerBetreutKunde VALUES (11, 21);
INSERT INTO KundenbetreuerBetreutKunde VALUES (12, 22);
INSERT INTO KundenbetreuerBetreutKunde VALUES (13, 23);
INSERT INTO KundenbetreuerBetreutKunde VALUES (21, 24);

;
