package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import java.sql.SQLException;

public class AuftragKunden extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("AuftraegeKunden-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT Auftrag.Auftrag_id,Kunde.Kunde_id, Kunde.Bezeichnung, " +
                "Auftrag.EinVerkauf, Auftrag.Erstellungsdatum, Auftrag.Liefertermin " +
                "FROM Auftrag " +
                "JOIN Konto ON Auftrag.Konto_id = Konto.Konto_id " +
                "JOIN Kunde ON Konto.Kunde_id = Kunde.Kunde_id ";

        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE Kunde.Bezeichnung LIKE '%" + filter + "%'";
        }
        System.out.println(sql);
        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeKunden-getSelectQueryForRowWithData: " + data);

        Auftrag auftrag = new Auftrag();
        return auftrag.getSelectQueryForRowWithData(data);
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeKunden-insertRowWithData: " + data);

        Auftrag auftrag = new Auftrag();
        auftrag.insertRowWithData(data);
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("AuftraegeKunden-updateRowWithData: " + oldData + "  :  " + newData);

        Auftrag auftrag = new Auftrag();
        auftrag.updateRowWithData(oldData, newData);
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeKunden-deleteRowWithData: " + data);

        Auftrag auftrag = new Auftrag();
        auftrag.deleteRowWithData(data);
    }
}
