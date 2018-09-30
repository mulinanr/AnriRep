SELECT *
    FROM Kunde
    WHERE ApothekenSchein IS NOT NULL
      AND Kunde_id NOT IN (
	SELECT Kunde.Kunde_id
	FROM Medikament
	INNER JOIN AuftragBuchtMedikament ON Medikament.Medikament_id = AuftragBuchtMedikament.Medikament_id
	JOIN Auftrag ON AuftragBuchtMedikament.Auftrag_id = Auftrag.Auftrag_id
	JOIN Konto ON Auftrag.Konto_id = Konto.Konto_id
	JOIN Kunde ON Konto.Kunde_id = Kunde.Kunde_id
	WHERE Kennzeichnung = 'verschreibungspflichtig'
);
