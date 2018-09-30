package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import java.sql.SQLException;

public class AuftraegeErstellungsdatum extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("AuftraegeErstellungsdatum-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT Auftrag.Auftrag_id, Auftrag.Mitarbeiter_id, " +
                "Auftrag.EinVerkauf, Auftrag.Erstellungsdatum, Auftrag.Liefertermin " +
                "FROM Auftrag ";

        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE Auftrag.Erstellungsdatum > date('" + filter + "') " +
                    "AND Auftrag.Erstellungsdatum < date('" + filter + "', '+1 day')";
        }

        System.out.println(sql);
        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeErstellungsdatum-getSelectQueryForRowWithData: " + data);

        String sql = "SELECT Auftrag_id, Konto_id, Mitarbeiter_id, EinVerkauf, Erstellungsdatum, Liefertermin FROM Auftrag WHERE Auftrag_id=" + data.get("Auftrag.Auftrag_id");
        System.out.println(sql);
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeErstellungsdatum-insertRowWithData: " + data);

        Auftrag newAuftrag = new Auftrag();
        newAuftrag.insertRowWithData(data);

    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("AuftraegeErstellungsdatum-updateRowWithData: " + oldData + "  :  " + newData);

        Auftrag updateAuftrag = new Auftrag();
        updateAuftrag.updateRowWithData(oldData, newData);
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeErstellungsdatum-deleteRowWithData: " + data);

        Auftrag deleteAuftrag = new Auftrag();
        deleteAuftrag.deleteRowWithData(data);
    }
}
