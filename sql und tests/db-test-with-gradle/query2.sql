SELECT Medikament_id,
	(SELECT SUM(lineprice)
		FROM (
		WITH RECURSIVE cnt(m, x, y, z) AS (
			VALUES(0, 0, 10, 0)
			UNION ALL
			SELECT (
				SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl < y) as m,
				y / (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl < y),
				y % (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl < y),
				(SELECT Preis FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl = (
					SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl < y )
				)
			FROM cnt
			WHERE  y > 0)
		SELECT y, x, m, z, (x * m * z) as lineprice
			FROM cnt )) as vp,
	(SELECT SUM(lineprice)
		FROM (
		WITH RECURSIVE cnt(m, x, y, z) AS (
			VALUES(0, 0, 10, 0)
			UNION ALL
			SELECT (
				SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl < y) as m,
				y / (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl < y),
				y % (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl < y),
				(SELECT Preis FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl = (
					SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl < y )
				)
			FROM cnt
			WHERE  y > 0)
		SELECT y, x, m, z, (x * m * z) as lineprice
		FROM cnt )) as ep
	FROM Medikament mm
	WHERE (vp BETWEEN 10 AND 15
		OR vp BETWEEN 10 AND 15);