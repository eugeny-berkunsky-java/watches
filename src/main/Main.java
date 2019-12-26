package main;

import ui.MainMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new MainMenu().show(new Scanner(System.in));
        Settings.shutDown();
    }
}
