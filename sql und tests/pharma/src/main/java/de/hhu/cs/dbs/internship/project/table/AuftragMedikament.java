package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AuftragMedikament extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("AuftraegeMedikament-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT Auftrag.Auftrag_id, Produktname, Anzahl, Kennzeichnung FROM Auftrag " +
                "JOIN AuftragBuchtMedikament ON Auftrag.Auftrag_id = AuftragBuchtMedikament.Auftrag_id " +
                "JOIN Medikament ON AuftragBuchtMedikament.Medikament_id = Medikament.Medikament_id ";

        if (filter != null && !filter.isEmpty()) {
            List<String> filterItems = Arrays.asList(filter.split(","));
            sql += " WHERE Medikament.Produktname LIKE '%" + filterItems.get(0) + "%' ";

            for (int i = 1; i < filterItems.size(); i++) {
                sql += " OR Medikament.Produktname LIKE '%" + filterItems.get(i).trim() + "%' ";
            }
        }

        System.out.println(sql);
        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeMedikament-getSelectQueryForRowWithData: " + data);

        Auftrag auftrag = new Auftrag();
        return auftrag.getSelectQueryForRowWithData(data);
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeMedikament-insertRowWithData: " + data);

        Auftrag auftrag = new Auftrag();
        auftrag.insertRowWithData(data);
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("AuftraegeMedikament-updateRowWithData: " + oldData + "  :  " + newData);

        Auftrag auftrag = new Auftrag();
        auftrag.updateRowWithData(oldData, newData);
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("AuftraegeMedikament-deleteRowWithData: " + data);

        Auftrag auftrag = new Auftrag();
        auftrag.deleteRowWithData(data);
    }
}
