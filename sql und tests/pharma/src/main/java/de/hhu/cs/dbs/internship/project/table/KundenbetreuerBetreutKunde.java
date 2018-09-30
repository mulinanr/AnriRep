package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KundenbetreuerBetreutKunde extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("KundenbetreuerBetreutKunde-getSelectQueryForTableWithFilter: " + filter);
        return null;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("KundenbetreuerBetreutKunde-getSelectQueryForRowWithData: " + data);
        return null;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("KundenbetreuerBetreutKunde-insertRowWithData: " + data);

    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("KundenbetreuerBetreutKunde-updateRowWithData: " + oldData + "  :  " + newData);

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("KundenbetreuerBetreutKunde-deleteRowWithData: " + data);

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM KundenbetreuerBetreutKunde WHERE Kunde_id = ?");
        preparedStatement.setObject(1, data.get("KundenbetreuerBetreutKunde.Kunde_id"));
        preparedStatement.executeUpdate();

    }
}
