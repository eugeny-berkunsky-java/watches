package ui;

import manage.WatchManager;
import model.Vendor;
import model.Watch;
import utils.UserInput;

import java.math.BigDecimal;
import java.util.function.Function;

public class WatchesReportsMenu {

    private final static int REPORT_1 = 1;
    private final static int REPORT_2 = 2;
    private final static int REPORT_3 = 3;
    private final static int REPORT_4 = 4;
    private final static int PREVIOUS_MENU = 0;

    private WatchManager watchManager = new WatchManager();

    private void printMenu() {
        System.out.println("------ Watches reports menu ------");
        System.out.format("%d. show all brands for specified type%n", REPORT_1);
        System.out.format("%d. show analogue watches the price for which does not exceed " +
                "specified value%n", REPORT_2);
        System.out.format("%d. show all watches produced in specified country%n", REPORT_3);
        System.out.format("%d. show vendors total price for which does not exceed specified " +
                "value%n", REPORT_4);
        System.out.format("%d. return to watches menu%n", PREVIOUS_MENU);
    }

    public void show(UserInput userInput) {
        int answer;

        do {
            printMenu();
            answer = userInput.getNumber("your choice", -1);

            switch (answer) {
                case REPORT_1: {
                    report1(userInput);
                    break;
                }

                case REPORT_2: {
                    report2(userInput);
                    break;
                }

                case REPORT_3: {
                    report3(userInput);
                    break;
                }

                case REPORT_4: {
                    report4(userInput);
                    break;
                }
            }

        } while (answer != PREVIOUS_MENU);
    }

    private void report1(UserInput userInput) {
        String watchTypeString =
                userInput.getString("watch type (analogue, digital)").toUpperCase();
        Watch.WatchType watchType = Watch.WatchType.valueOf(watchTypeString);

        Function<Watch, String> watchView = w -> String.format("%d - %s", w.getId(), w.getBrand());

        System.out.println("----------------------- Result ----------------------");
        watchManager.getByType(watchType).stream().map(watchView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
    }

    private void report2(UserInput userInput) {
        BigDecimal upperLimit = new BigDecimal(userInput.getString("upper limit price"));

        System.out.println("----------------------- Result ----------------------");
        watchManager.getAnalogueWatchNotGreaterByPriceThan(upperLimit).stream()
                .map(this::defaultWatchView)
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------------");
    }

    private void report3(UserInput userInput) {
        String countryName = userInput.getString("country name");

        System.out.println("----------------------- Result ----------------------");
        watchManager.getByCountry(countryName).stream()
                .map(this::defaultWatchView)
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------------");
    }

    private void report4(UserInput userInput) {
        BigDecimal upperLimit = new BigDecimal(userInput.getString("upper limit sum"));

        System.out.println("----------------------- Result ----------------------");
        watchManager.getVendorByTotalSumNotGreaterThan(upperLimit).stream()
                .map(Vendor::getVendorName)
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------------");
    }

    private String defaultWatchView(Watch watch) {
        return String.format("%d - %s - %s [%s]",
                watch.getId(), watch.getBrand(), watch.getVendor().getVendorName(),
                watch.getVendor().getCountry().getName());
    }
}
