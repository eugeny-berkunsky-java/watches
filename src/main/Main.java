package main;

import ui.Menu;

public class Main {

    private Menu menu = new Menu();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        int selection;
        while ((selection = menu.menu())!=0) {
            System.out.println("--------------");
        }
    }

}
