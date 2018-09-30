package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Kontakteintrag extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("Kontakteintrag-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT * " +
                "FROM Kontakteintrag ";

        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE Kontakteintrag.TelefonNr LIKE '%" + filter + "%'";
        }
        System.out.println(sql);

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("Kontakteintrag-getSelectQueryForRowWithData: " + data);

        String sql = "SELECT * FROM Kontakteintrag WHERE TelefonNr=" + data.get("Kontakteintrag.TelefonNr");
        System.out.println(sql);
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("Kontakteintrag-insertRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("INSERT INTO Kontakteintrag (TelefonNr, addr_id, EMail, Fax) VALUES (?, ?, ?, ?)");
        preparedStatement.setObject(1, data.get("Kontakteintrag.TelefonNr"));
        preparedStatement.setObject(2, data.get("Kontakteintrag.addr_id"));
        preparedStatement.setObject(3, data.get("Kontakteintrag.EMail"));
        preparedStatement.setObject(4, data.get("Kontakteintrag.Fax"));
        preparedStatement.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("Kontakteintrag-updateRowWithData: " + oldData + "  :  " + newData);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection()
                .prepareStatement(("UPDATE Kontakteintrag SET EMail = ?, Fax = ?  WHERE TelefonNr = ?"));
        preparedStatement.setObject(1, newData.get("Kontakteintrag.EMail"));
        preparedStatement.setObject(2, newData.get("Kontakteintrag.Fax"));
        preparedStatement.setObject(3, newData.get("Kontakteintrag.TelefonNr"));
        preparedStatement.executeUpdate();

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("Kontakteintrag-deleteRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        if(hasAuftrag((int) data.get("Kontakteintrag.TelefonNr"))) {
            throw new SQLException("Es gibt einen Auftrag, nicht erlaubt!!");
        }

        PreparedStatement preparedStatement1 = Project.getInstance().getConnection().prepareStatement("DELETE FROM KundeHatKontakteintrag WHERE TelefonNr = ?");
        preparedStatement1.setObject(1, data.get("Kontakteintrag.TelefonNr"));
        preparedStatement1.executeUpdate();

        PreparedStatement preparedStatement2 = Project.getInstance().getConnection().prepareStatement("DELETE FROM PersonHatKontakteintrag WHERE TelefonNr = ?");
        preparedStatement2.setObject(1, data.get("Kontakteintrag.TelefonNr"));
        preparedStatement2.executeUpdate();

        PreparedStatement preparedStatement3 = Project.getInstance().getConnection().prepareStatement("DELETE FROM Kontakteintrag WHERE TelefonNr = ?");
        preparedStatement3.setObject(1, data.get("Kontakteintrag.TelefonNr"));
        preparedStatement3.executeUpdate();
    }

    private boolean hasAuftrag(int telefonNr) {
        String sql = "SELECT *" +
                "FROM KundeHatKontakteintrag " +
                "JOIN Konto ON KundeHatKontakteintrag.Kunde_id = Konto.Kunde_id " +
                "JOIN Auftrag ON Konto.Konto_id = Auftrag.Konto_id " +
                "WHERE KundeHatKontakteintrag.TelefonNr = " + telefonNr;

        try {
            Statement stmt = Project.getInstance().getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return  resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
