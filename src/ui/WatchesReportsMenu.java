package ui;

import manage.WatchManager;
import model.Vendor;
import model.Watch;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.function.Function;

public class WatchesReportsMenu {

    private final static int REPORT_1 = 1;
    private final static int REPORT_2 = 2;
    private final static int REPORT_3 = 3;
    private final static int REPORT_4 = 4;

    private WatchManager watchManager = new WatchManager();

    private void printMenu() {
        System.out.println("------ Watches reports menu ------");
        System.out.println("1. show all brands for specified type");
        System.out.println("2. show analogue watches the price for which does not exceed " +
                "specified value");
        System.out.println("3. show all watches produced in specified country");
        System.out.println("4. show vendors total price for which does not exceed specified value");
        System.out.println("0. return to watches menu");
    }

    public void show(Scanner scanner) {
        int answer;

        do {
            printMenu();
            answer = scanner.nextInt();

            switch (answer) {
                case REPORT_1: {
                    report1(scanner);
                    break;
                }

                case REPORT_2: {
                    report2(scanner);
                    break;
                }

                case REPORT_3: {
                    report3(scanner);
                    break;
                }

                case REPORT_4: {
                    report4(scanner);
                    break;
                }
            }

        } while (answer != 0);
    }

    private void report1(Scanner scanner) {
        System.out.print("watch type (analogue, digital): ");
        Watch.WatchType watchType = Watch.WatchType.valueOf(scanner.next().trim().toUpperCase());

        Function<Watch, String> watchView = w -> String.format("%d - %s", w.getId(), w.getBrand());

        System.out.println("----------------------- Result ----------------------");
        watchManager.getByType(watchType).stream().map(watchView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");


    }

    private void report2(Scanner scanner) {
        System.out.print("upper limit price: ");
        BigDecimal upperLimit = new BigDecimal(scanner.next().trim());

        System.out.println("----------------------- Result ----------------------");
        watchManager.getAnalogueWatchNotGreaterByPriceThan(upperLimit).stream().map(this::defaultWatchView).forEach(System.out::println);
        System.out.println("-----------------------------------------------------");

    }

    private void report3(Scanner scanner) {
        System.out.print("country name: ");
        String countryName = scanner.next();

        System.out.println("----------------------- Result ----------------------");
        watchManager.getByCountry(countryName).stream().map(this::defaultWatchView).forEach(System.out::println);
        System.out.println("-----------------------------------------------------");

    }

    private void report4(Scanner scanner) {
        System.out.print("upper limit sum: ");
        BigDecimal upperLimit = new BigDecimal(scanner.next().trim());

        System.out.println("----------------------- Result ----------------------");
        watchManager.getVendorByTotalSumNotGreaterThan(upperLimit).stream().map(Vendor::getVendorName).forEach(System.out::println);
        System.out.println("-----------------------------------------------------");

    }

    private String defaultWatchView(Watch watch) {
        return String.format("%d - %s - %s [%s]",
                watch.getId(), watch.getBrand(), watch.getVendor().getVendorName(),
                watch.getVendor().getCountry().getName());
    }
}
