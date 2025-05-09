package com.ragerslazar.laloc.test;

import com.ragerslazar.laloc.model.JDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class JDatabaseTest {
    private final JDatabase db = new JDatabase();
    private final Dotenv dotenv = Dotenv.configure().load();

    @Test
    public void testObject() {
        assertNotNull(db);
    }

    @Test
    public void testConnection() {
        Connection cx = this.db.connection();
        assertNotNull(cx);
    }

    @Test
    public void testLoginAdmin() {
        String dbAdminUser = this.dotenv.get("DB_ADMIN_USER");
        String dbAdminPassword = this.dotenv.get("DB_ADMIN_PASSWORD");
        String login = this.db.loginDB(dbAdminUser, dbAdminPassword);
        assertEquals("approved", login);
    }

    @Test
    public void testLoginFail() {
        String login = this.db.loginDB("test", "j'existe_pas");
        assertEquals("not_authenticated", login);
    }

    @Test
    public void testLoginNotAdmin() {
        String username = this.dotenv.get("USERNAME_TEST");
        String password = this.dotenv.get("PASSWORD_TEST");
        String login = this.db.loginDB(username, password);
        assertEquals("admin_perm_missing", login);
    }

    @Test
    public void testInsertCar() {
        boolean insert = this.db.queryInsert("BMW", "bmw.jpg", "modele test", "102-145-5D4", "7", "100000", "1", "280000", "2");
        assertTrue(insert);
    }

    @Test
    public void testInsertCarFail() {
        boolean insert = this.db.queryInsert("BMW", "bmw.jpg", "modele test", "102-145-5D4", "7", "100000", "1", "280000", "7");
        assertFalse(insert);
    }
}
