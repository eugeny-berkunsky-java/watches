package main;

import ui.MainMenu;
import utils.Settings;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            Settings.init();
            logger.info("program started");
            new MainMenu().show(new Scanner(System.in));
        } finally {
            Settings.shutDown();
            logger.info("program stopped");
        }
    }
}
