package main;

import manage.ManagersContainer;
import model.DAOContainer;
import ui.MainMenu;
import utils.Settings;
import utils.UserInput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            Settings.init();
            logger.info("program started");

            new MainMenu(
                    ManagersContainer.getInstance(DAOContainer.getInstance())
            ).show(new UserInput(reader));

        } catch (Exception e) {
            logger.log(Level.SEVERE, "buffered reader error", e);
        } finally {
            Settings.shutDown();
            logger.info("program stopped");
        }
    }
}
