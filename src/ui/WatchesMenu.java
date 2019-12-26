package ui;

import manage.WatchManager;
import model.Watch;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class WatchesMenu {
    private final static int SHOW_ALL_WATCHES = 1;
    private final static int ADD_WATCH = 2;
    private final static int UPDATE_WATCH = 3;
    private final static int DELETE_WATCH = 4;

    private WatchManager watchManager = new WatchManager();

    private void printMenu() {
        System.out.println("---------- Watches menu ----------");
        System.out.println("1. Show all watches");
        System.out.println("2. Add watch");
        System.out.println("3. Update watch");
        System.out.println("4. Delete watch");
        System.out.println("0. return to main menu");
    }

    public void show(Scanner scanner) {
        int answer;

        do {
            printMenu();
            answer = scanner.nextInt();

            switch (answer) {
                case SHOW_ALL_WATCHES: {
                    showWatches();
                    break;
                }

                case ADD_WATCH: {
                    addWatch(scanner);
                    break;
                }

                case UPDATE_WATCH: {
                    updateWatch(scanner);
                    break;
                }

                case DELETE_WATCH: {
                    deleteWatch(scanner);
                    break;
                }
            }
        } while (answer != 0);
    }

    private void showWatches() {

        Function<Watch, String> watchView = w -> String.format("%d - %s - %s [%s]",
                w.getId(), w.getBrand(), w.getVendor().getVendorName(),
                w.getVendor().getCountry().getName());

        System.out.println("----------------------- Watches ----------------------");
        watchManager.getAll().stream().map(watchView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");

    }

    private void addWatch(Scanner scanner) {
        System.out.print("brand name: ");
        String brandName = scanner.next();

        System.out.print("watch type (analogue, digital): ");
        Watch.WatchType watchType = Watch.WatchType.valueOf(scanner.next().trim().toUpperCase());

        System.out.print("price: ");
        BigDecimal price = new BigDecimal(scanner.next().trim());

        System.out.print("quantity: ");
        int qty = scanner.nextInt();

        System.out.print("vendor ID: ");
        int vendorId = scanner.nextInt();

        final Optional<Watch> watch = watchManager.addWatch(brandName, watchType, price, qty, vendorId);
        if (watch.isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void updateWatch(Scanner scanner) {
        System.out.println("what watch do you want to update?");
        System.out.print("id: ");
        int watchId = scanner.nextInt();

        System.out.print("NEW brand name: ");
        String brandName = scanner.next();

        System.out.print("NEW watch type (analogue, digital): ");
        Watch.WatchType watchType = Watch.WatchType.valueOf(scanner.next().trim().toUpperCase());

        System.out.print("NEW price: ");
        BigDecimal price = new BigDecimal(scanner.next().trim());

        System.out.print("NEW quantity: ");
        int qty = scanner.nextInt();

        System.out.print("NEW vendor ID: ");
        int vendorId = scanner.nextInt();

        if (watchManager.updateWatch(watchId, brandName, watchType, price, qty, vendorId)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void deleteWatch(Scanner scanner) {
        System.out.println("what watch do you want to delete?");
        System.out.print("id: ");
        int watchId = scanner.nextInt();
        if (watchManager.deleteWatch(watchId)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
