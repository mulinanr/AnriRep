
SELECT * 
	FROM (
		SELECT aa.a_id, count( m_id) as c, sum(gp) as s 
			FROM (
				SELECT 	abm.Auftrag_id as a_id, 
						Medikament_id as m_id, 
						Anzahl, 
						a.EinVerkauf, 
						(SELECT SUM(lineprice)
							FROM (
								WITH RECURSIVE cnt(m, x, y, z) AS (
								VALUES(0, 0, 10, 0)
								UNION ALL
								SELECT 
									(SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl < y) as m,
									y / (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl < y),
									y % (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl < y),
									(SELECT Preis FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl = (
											SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl < y ))
								  FROM cnt
								  WHERE  y > 0)
							SELECT y, x, m, z, (x * m * z) as lineprice FROM cnt) ) as gp 
					FROM AuftragBuchtMedikament abm  
					JOIN Auftrag a ON abm.Auftrag_id = a.Auftrag_id
				) as aa 
			GROUP BY aa.a_id
		)
	WHERE c > 1 AND s > 1000;
	
