SELECT *
    FROM Kunde
    WHERE Kunde_id NOT IN (
            SELECT Kunde.Kunde_id
                FROM Konto
                JOIN Kunde ON Konto.Kunde_id = Kunde.Kunde_id
                WHERE Zahlungsart = 'Lastschrift'
                    OR Zahlungsart = 'Überweisung'
        );