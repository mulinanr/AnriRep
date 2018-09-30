package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.Application;
import com.alexanderthelen.applicationkit.database.Data;
import de.hhu.cs.dbs.internship.project.table.Mitarbeiter;

import java.io.IOException;
import java.sql.SQLException;

public class AuthenticationViewController extends com.alexanderthelen.applicationkit.gui.AuthenticationViewController {
    protected AuthenticationViewController(String name) {
        super(name);
    }

    public static AuthenticationViewController createWithName(String name) throws IOException {
        AuthenticationViewController viewController = new AuthenticationViewController(name);
        viewController.loadView();
        return viewController;
    }

    @Override
    public void loginUser(Data data) throws SQLException {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        if (!mitarbeiter.isCorrectLogin(Application.getInstance().getConnection(), (String)data.get("email"), (String)data.get("password"))) {
            throw new SQLException(getClass().getName() + ".loginUser(Data) nicht implementiert.");
        }
    }

    @Override
    public void registerUser(Data data) throws SQLException {
        System.out.println(data);

        if (!isValidPassword(data)) {
            new Exception("Password don*t match!");
        }

        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.insertRowWithData(data);

        //throw new SQLException(getClass().getName() + ".registerUser(Data) nicht implementiert.++++++");
    }

    private boolean isValidPassword(Data data) throws SQLException {
        if (((String)data.get("password1")).equals((String)data.get("password2"))) {
            return true;
        }
        return false;
    }
}
