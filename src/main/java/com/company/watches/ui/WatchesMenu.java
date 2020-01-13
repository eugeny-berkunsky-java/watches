package com.company.watches.ui;

import com.company.watches.manage.ManagersContainer;
import com.company.watches.manage.WatchManager;
import com.company.watches.model.Watch;
import com.company.watches.utils.UserInput;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

public class WatchesMenu {
    private final static int SHOW_ALL_WATCHES = 1;
    private final static int ADD_WATCH = 2;
    private final static int UPDATE_WATCH = 3;
    private final static int DELETE_WATCH = 4;
    private final static int REPORTS = 5;
    private final static int PREVIOUS_MENU = 0;

    private WatchManager watchManager;
    private WatchesReportsMenu watchesReportsMenu;

    public WatchesMenu(ManagersContainer managersContainer) {
        this.watchManager = managersContainer.getWatchManager();
        watchesReportsMenu = new WatchesReportsMenu(managersContainer);
    }

    private void printMenu() {
        System.out.println("---------- Watches menu ----------");
        System.out.format("%d. Show all watches%n", SHOW_ALL_WATCHES);
        System.out.format("%d. Add watch%n", ADD_WATCH);
        System.out.format("%d. Update watch%n", UPDATE_WATCH);
        System.out.format("%d. Delete watch%n", DELETE_WATCH);
        System.out.format("%d. Reports%n", REPORTS);
        System.out.format("%d. return to main menu%n", PREVIOUS_MENU);
    }

    public void show(UserInput userInput) {
        int answer;

        do {
            printMenu();
            answer = userInput.getNumber("your choice", -1);

            switch (answer) {
                case SHOW_ALL_WATCHES: {
                    showWatches();
                    break;
                }

                case ADD_WATCH: {
                    addWatch(userInput);
                    break;
                }

                case UPDATE_WATCH: {
                    updateWatch(userInput);
                    break;
                }

                case DELETE_WATCH: {
                    deleteWatch(userInput);
                    break;
                }

                case REPORTS: {
                    watchesReportsMenu.show(userInput);
                    break;
                }
            }
        } while (answer != PREVIOUS_MENU);
    }

    private void showWatches() {

        Function<Watch, String> watchView = w -> String.format("%d - %s - %s [%s]",
                w.getId(), w.getBrand(), w.getVendor().getVendorName(),
                w.getVendor().getCountry().getName());

        System.out.println("----------------------- Watches ----------------------");
        watchManager.getAll().stream().map(watchView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
    }

    private void addWatch(UserInput userInput) {
        String brandName = userInput.getString("brand name");

        String watchTypeString = userInput.getString("watch type (analogue, digital)").toUpperCase();
        Watch.WatchType watchType = Watch.WatchType.valueOf(watchTypeString);

        BigDecimal price = new BigDecimal(userInput.getString("price"));

        int qty = userInput.getNumber("quantity", -1);

        int vendorId = userInput.getNumber("vendor ID", -1);

        final Optional<Watch> watch = watchManager.addWatch(brandName, watchType, price, qty, vendorId);
        if (watch.isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void updateWatch(UserInput userInput) {
        System.out.println("what watch do you want to update?");
        int watchId = userInput.getNumber("id", -1);

        String brandName = userInput.getString("NEW brand name");

        String watchTypeString = userInput.getString("watch type (analogue, digital)").toUpperCase();
        Watch.WatchType watchType = Watch.WatchType.valueOf(watchTypeString);

        BigDecimal price = new BigDecimal(userInput.getString("NEW price"));

        int qty = userInput.getNumber("NEW quantity", -1);

        int vendorId = userInput.getNumber("NEW vendor ID", -1);

        if (watchManager.updateWatch(watchId, brandName, watchType, price, qty, vendorId)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void deleteWatch(UserInput userInput) {
        System.out.println("what watch do you want to delete?");
        int watchId = userInput.getNumber("id", -1);

        if (watchManager.deleteWatch(watchId)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
