package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import java.sql.SQLException;

public class Person extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        return null;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        return null;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {

    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {

    }
}
