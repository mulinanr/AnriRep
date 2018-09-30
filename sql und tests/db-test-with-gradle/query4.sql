SELECT Konto.Konto_id
    FROM Auftrag
    JOIN Konto ON Auftrag.Konto_id = Konto.Konto_id
    ORDER BY Erstellungsdatum ASC
    LIMIT 1;
