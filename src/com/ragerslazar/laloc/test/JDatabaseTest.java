package com.ragerslazar.laloc.test;

import com.ragerslazar.laloc.model.JDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDatabaseTest {
    JDatabase db = new JDatabase();
    Dotenv dotenv = Dotenv.configure().load();

    @Test
    public void testLoginAdmin() {
        String dbAdminUser = dotenv.get("DB_ADMIN_USER");
        String dbAdminPassword = dotenv.get("DB_ADMIN_PASSWORD");
        String login = db.loginDB(dbAdminUser, dbAdminPassword);
        assertEquals("approved", login);
    }

    @Test
    public void testLoginFail() {
        String login = db.loginDB("test", "j'existe_pas");
        assertEquals("not_authenticated", login);
    }

    @Test
    public void testLoginNotAdmin() {
        String username = dotenv.get("USERNAME_TEST");
        String password = dotenv.get("PASSWORD_TEST");
        String login = db.loginDB(username, password);
        assertEquals("admin_perm_missing", login);
    }

    @Test
    public void testInsertCar() {
        boolean insert = db.queryInsert("BMW", "bmw.jpg", "modele test", "102-145-5D4", "7", "100000", "1", "280000", "2");
        assertTrue(insert);
    }

    @Test
    public void testInsertCarFail() {
        boolean insert = db.queryInsert("BMW", "bmw.jpg", "modele test", "102-145-5D4", "7", "100000", "1", "280000", "7");
        assertFalse(insert);
    }

}
