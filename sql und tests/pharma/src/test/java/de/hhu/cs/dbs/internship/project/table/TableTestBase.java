package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.Application;
import com.alexanderthelen.applicationkit.database.Connection;
import de.hhu.cs.dbs.internship.project.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableTestBase {

    int countRows(Connection connection, String table) throws SQLException {
        ResultSet resultSet = connection.executeQuery("SELECT count(*) FROM " + table);
        return resultSet.getInt(1);
    }

    Connection prepareMemeoryDbConnection() throws Exception {
        //Connection connection = new Connection("jdbc:sqlite::memory:");
        Connection connection = new Connection("jdbc:sqlite:project.db");
        Statement stmt = connection.createStatement();
        initDatabase(stmt, "create.sql", "----");
        initDatabase(stmt, "insert.sql", ";");
        stmt.close();
        return connection;
    }


    void prepareEnvironment(Connection connection) {
        Application app = new Application() {
            @Override
            public void start() throws Exception {}
        };

        Project.getInstance().getData().put("permission", 10);
        Project.getInstance().setConnection(connection);
    }


    void initDatabase(Statement stmt, String createSql, String delimiter) throws Exception {

        StringBuffer sb = readFile(createSql);
        String[] inst = sb.toString().split(delimiter);

        for(int i = 0; i<inst.length; i++) {
            if(!inst[i].trim().equals("")) {
                stmt.executeUpdate(inst[i]);
                System.out.println(">> "+inst[i]);
            }
        }

    }

    private StringBuffer readFile(String filename) throws Exception {
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try (FileReader fr = new FileReader(new File(filename));
             BufferedReader br = new BufferedReader(fr);
        ) {

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        }
        return sb;
    }

}
