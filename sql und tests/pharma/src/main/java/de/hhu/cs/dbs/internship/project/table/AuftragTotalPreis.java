package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import java.sql.SQLException;

public class AuftragTotalPreis extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("AuftragTotalPreis-getSelectQueryForTableWithFilter: " + filter);

        String sql =
                "SELECT aa.a_id as Auftrag_id, count( m_id) as Medikament_count, sum(gp) as Auftrag_total_Preis " +
                "FROM ( " +
                "SELECT abm.Auftrag_id as a_id, " +
                "Medikament_id as m_id, " +
                "Anzahl, " +
                "a.EinVerkauf, " +
                "(SELECT SUM(lineprice) " +
                "FROM ( " +
                "WITH RECURSIVE cnt(m, x, y, z) AS ( " +
                "VALUES(0, 0, abm.Anzahl, 0) " +
                "UNION ALL " +
                "SELECT " +
                "(SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl < y) as m, " +
                "y / (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl < y), " +
                "y % (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl < y), " +
                "(SELECT Preis FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl = ( " +
                "SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = abm.Medikament_id AND EinVerkauf = a.EinVerkauf AND Anzahl < y )) " +
                "FROM cnt " +
                "WHERE  y > 0) " +
                "SELECT y, x, m, z, (x * m * z) as lineprice FROM cnt) ) as gp " +
                "FROM AuftragBuchtMedikament abm  " +
                "JOIN Auftrag a ON abm.Auftrag_id = a.Auftrag_id " +
                ") as aa ";

        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE aa.a_id LIKE '%" + filter + "%'  ";
        }

        sql += " GROUP BY aa.a_id ";

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("AuftragTotalPreis-getSelectQueryForRowWithData: " + data);

        Object auftragId = data.get("AuftragBuchtMedikament.Auftrag_id");
        data.put("Auftrag.Auftrag_id", auftragId);

        Auftrag auftrag = new Auftrag();
        return auftrag.getSelectQueryForRowWithData(data);
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("AuftragTotalPreis-insertRowWithData: " + data);

        Object auftragId = data.get("AuftragBuchtMedikament.Auftrag_id");
        data.put("Auftrag.Auftrag_id", auftragId);

        Auftrag auftrag = new Auftrag();
        auftrag.insertRowWithData(data);
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("AuftragTotalPreis-updateRowWithData: " + oldData + "  :  " + newData);

        Object auftragId = oldData.get("AuftragBuchtMedikament.Auftrag_id");
        oldData.put("Auftrag.Auftrag_id", auftragId);
        newData.put("Auftrag.Auftrag_id", auftragId);

        Auftrag auftrag = new Auftrag();
        auftrag.updateRowWithData(oldData, newData);
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("AuftragTotalPreis-deleteRowWithData: " + data);

        Object auftragId = data.get("AuftragBuchtMedikament.Auftrag_id");
        data.put("Auftrag.Auftrag_id", auftragId);

        Auftrag auftrag = new Auftrag();
        auftrag.deleteRowWithData(data);
    }
}
