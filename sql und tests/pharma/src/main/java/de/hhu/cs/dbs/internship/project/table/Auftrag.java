package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Auftrag extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("Auftrag-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT * FROM Auftrag ";

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("Auftrag-getSelectQueryForRowWithData: " + data);

        String sql = "SELECT Auftrag_id, Konto_id, Mitarbeiter_id, EinVerkauf, Erstellungsdatum, Liefertermin FROM Auftrag WHERE Auftrag_id=" + data.get("Auftrag.Auftrag_id");
        System.out.println(sql);
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("Auftrag-insertRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("INSERT INTO Auftrag (Konto_id, Mitarbeiter_id, EinVerkauf, Liefertermin) VALUES (?, ?, ?, ?)");
        preparedStatement.setObject(1, data.get("Auftrag.Konto_id"));
        preparedStatement.setObject(2, data.get("Auftrag.Mitarbeiter_id"));
        preparedStatement.setObject(3, data.get("Auftrag.EinVerkauf"));

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            Date date = formatter.parse((String) data.get("Auftrag.Liefertermin"));
            preparedStatement.setObject(4, data.get("Auftrag.Liefertermin"));
        } catch (ParseException e) {
            throw new SQLException("Bitte, Liefertermin als \"yyyy-mm-dd\"");
        }

        preparedStatement.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("Auftrag-updateRowWithData: " + oldData + "  :  " + newData);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection()
                .prepareStatement(("UPDATE Auftrag SET Konto_id = ?, Mitarbeiter_id = ?, EinVerkauf = ?, Liefertermin = ?  WHERE Auftrag_id = ?"));
        preparedStatement.setObject(1, newData.get("Auftrag.Konto_id"));
        preparedStatement.setObject(2, newData.get("Auftrag.Mitarbeiter_id"));
        preparedStatement.setObject(3, newData.get("Auftrag.EinVerkauf"));
        preparedStatement.setObject(4, newData.get("Auftrag.Liefertermin"));
        preparedStatement.setObject(5, oldData.get("Auftrag.Auftrag_id"));
        preparedStatement.executeUpdate();

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("Auftrag-deleteRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }


        deleteAuftragBuchtMedikament((int)data.get("Auftrag.Auftrag_id"));

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM Auftrag WHERE Auftrag_id = ?");
        preparedStatement.setObject(1, data.get("Auftrag.Auftrag_id"));
        preparedStatement.executeUpdate();

    }

    private void deleteAuftragBuchtMedikament(int auftragId) throws SQLException {
        Data data = new Data();
        data.put("AuftragBuchtMedikament.Auftrag_id", auftragId);

        Table auftragBuchtMedikament = new AuftragBuchtMedikament();
        auftragBuchtMedikament.deleteRowWithData(data);

    }
}
