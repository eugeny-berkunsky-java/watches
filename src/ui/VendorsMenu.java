package ui;

import manage.VendorManager;
import model.Country;
import model.Vendor;
import utils.UserInput;

import java.util.Optional;
import java.util.function.Function;

public class VendorsMenu {
    private final static int SHOW_ALL_VENDORS = 1;
    private final static int ADD_VENDOR = 2;
    private final static int UPDATE_VENDOR = 3;
    private final static int DELETE_VENDOR = 4;
    private final static int PREVIOUS_MENU = 0;

    private VendorManager vendorManager;

    public VendorsMenu() {
        this.vendorManager = new VendorManager();
    }

    private void printMenu() {
        System.out.println("--------- Vendors menu ---------");
        System.out.format("%d. Show all vendors%n", SHOW_ALL_VENDORS);
        System.out.format("%d. Add vendor%n", ADD_VENDOR);
        System.out.format("%d. Update vendor%n", UPDATE_VENDOR);
        System.out.format("%d. Delete vendor%n", DELETE_VENDOR);
        System.out.format("%d. return to main menu%n", PREVIOUS_MENU);
    }

    public void show(UserInput userInput) {
        int answer;

        do {
            printMenu();
            answer = userInput.getNumber("your choice", -1);

            switch (answer) {
                case SHOW_ALL_VENDORS: {
                    showVendors();
                    break;
                }

                case ADD_VENDOR: {
                    addNewVendor(userInput);
                    break;
                }

                case UPDATE_VENDOR: {
                    updateVendor(userInput);
                    break;
                }

                case DELETE_VENDOR: {
                    deleteVendor(userInput);
                    break;
                }
            }

        } while (answer != PREVIOUS_MENU);
    }

    private void showVendors() {
        Function<Vendor, String> vendorView = v -> String.format("%d - %s [%s]",
                v.getId(), v.getVendorName(), v.getCountry().getName());

        System.out.println("------------------------Vendors-----------------------");
        vendorManager.getAll().stream().map(vendorView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
    }

    private void addNewVendor(UserInput userInput) {
        String vendorName = userInput.getString("write new vendor name");

        final int countryId = userInput.getNumber("write country ID", -1);

        final Optional<Vendor> vendor = vendorManager.addVendor(vendorName, countryId);
        if (vendor.isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void updateVendor(UserInput userInput) {
        System.out.println("what vendor do you want to update?");
        int vendorId = userInput.getNumber("id", -1);

        String vendorName = userInput.getString("NEW name");

        int countryId = userInput.getNumber("NEW country ID", -1);

        Vendor vendor = new Vendor(vendorId, vendorName, new Country(countryId, null));

        if (vendorManager.updateVendor(vendor)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void deleteVendor(UserInput userInput) {
        System.out.println("what vendor do you want to delete?");
        int vendorId = userInput.getNumber("id", -1);

        if (vendorManager.deleteVendor(vendorId)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }

}
