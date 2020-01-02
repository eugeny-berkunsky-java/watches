package main;

import ui.MainMenu;
import utils.Settings;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Settings.init();
            new MainMenu().show(new Scanner(System.in));
        } finally {
            Settings.shutDown();
        }
    }
}
