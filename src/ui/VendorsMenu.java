package ui;

import manage.VendorManager;
import model.Country;
import model.Vendor;

import java.util.Scanner;
import java.util.function.Function;

public class VendorsMenu {
    private final static int SNOW_ALL_VENDORS = 1;
    private final static int ADD_VENDOR = 2;
    private final static int UPDATE_VENDOR = 3;
    private final static int DELETE_VENDOR = 4;
    private VendorManager vendorManager;

    public VendorsMenu() {
        this.vendorManager = new VendorManager();
    }

    private void printMenu() {
        System.out.println("--------- Vendors menu ---------");
        System.out.println("1. Show all vendors");
        System.out.println("2. Add vendor");
        System.out.println("3. Update vendor");
        System.out.println("4. Delete vendor");
        System.out.println("0. return to main menu");
    }

    public void show(Scanner scanner) {
        int answer;

        do {
            printMenu();
            answer = scanner.nextInt();

            switch (answer) {
                case SNOW_ALL_VENDORS: {
                    showVendors();
                    break;
                }

                case ADD_VENDOR: {
                    addNewVendor(scanner);
                    break;
                }

                case UPDATE_VENDOR: {
                    updateVendor(scanner);
                    break;
                }

                case DELETE_VENDOR: {
                    deleteVendor(scanner);
                    break;
                }
            }

        } while (answer != 0);
    }

    private void showVendors() {
        Function<Vendor, String> vendorView = v -> String.format("%d - %s [%s]",
                v.getId(), v.getVendorName(), v.getCountry().getName());

        System.out.println("------------------------Vendors-----------------------");
        vendorManager.getAll().stream().map(vendorView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
    }

    private void addNewVendor(Scanner scanner) {
        System.out.print("write new vendor name: ");
        String vendorName = scanner.next();

        System.out.print("write country ID: ");
        final int countryId = scanner.nextInt();

        final Vendor vendor = vendorManager.addVendor(vendorName, countryId);
        if (vendor != null) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void updateVendor(Scanner scanner) {
        System.out.println("which vendor do you want to update?");
        System.out.print("id: ");
        int vendorId = scanner.nextInt();

        System.out.print("NEW name: ");
        String vendorName = scanner.next();

        System.out.print("NEW country ID: ");
        int countryId = scanner.nextInt();

        Vendor vendor = new Vendor(vendorId, vendorName, new Country(countryId, null));
        if (vendorManager.updateVendor(vendor)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void deleteVendor(Scanner scanner) {
        System.out.println("which vendor do you want to delete?");
        System.out.print("id: ");
        int vendorId = scanner.nextInt();
        if (vendorManager.deleteVendor(vendorId)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }

}
