/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadminpanelclient;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.CreditPackageEntity;
import entity.EmployeeEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRight;
import util.exception.CreditPackageNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidAccessRightException;


public class FinanceStaffModule {

    private EmployeeControllerRemote employeeControllerRemote;
    private CreditTransactionControllerRemote creditTransactionControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;

    public FinanceStaffModule() {
    }

    public FinanceStaffModule(EmployeeControllerRemote employeeControllerRemote, CreditTransactionControllerRemote creditTransactionControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.creditTransactionControllerRemote = creditTransactionControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
    }

    public void menuOperation(EmployeeEntity currentEmployee) throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() != EmployeeAccessRight.FINANCESTAFF) {
            throw new InvalidAccessRightException("You don't have FINANCESTAFF rights to access the system administration module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("* Crazy Auction :: Finance Administration *\n");
            System.out.println("1: Create Credit Package");
            System.out.println("2: View Individual Credit Package Details");
            System.out.println("3: View All Credit Packages");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    createCreditPackage();
                } else if (response == 2) {
                    viewIndividualCreditPackageDetails();
                } else if (response == 3) {
                    viewAllCreditPackageDetails();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    public void createCreditPackage() {
        Scanner scanner = new Scanner(System.in);
        CreditPackageEntity newCreditPackage = new CreditPackageEntity();

        System.out.println("* Crazy Auction :: Finance Administration :: Create New Credit Package *\n");
        System.out.print("Enter Credit Amount> ");
        newCreditPackage.setCreditBalance(scanner.nextBigDecimal());
        System.out.print("Enter Package Price> ");
        newCreditPackage.setPackagePrice(scanner.nextBigDecimal());
        newCreditPackage.setEnable(true);

        newCreditPackage = creditPackageControllerRemote.createNewCreditPackage(newCreditPackage);
        System.out.println("New credit package created successfully!: " + newCreditPackage.getCreditPackageId() + "\n");

    }

    public void viewIndividualCreditPackageDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("* Crazy Auction :: Finance Administration :: View Credit Package Details *\n");
        System.out.print("Enter Credit Package ID> ");
        Long creditPackageId = scanner.nextLong();

        try {
            CreditPackageEntity creditPackage = creditPackageControllerRemote.retrieveCreditPackageByCreditPackageId(creditPackageId);
            System.out.printf("%8s%20s%20s%20s\n", "Credit Package ID", "Balance", "Price", "Enabled");
            System.out.printf("%8s%28s%20s%20s\n", creditPackage.getCreditPackageId().toString(), creditPackage.getCreditBalance(), creditPackage.getPackagePrice(), creditPackage.getEnable().toString());
            System.out.println("1: Update Credit Package");
            System.out.println("2: Delete Credit Package");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                updateCreditPackage(creditPackage);
            } else if (response == 2) {
                deleteCreditPackage(creditPackage);
            }
        } catch (CreditPackageNotFoundException ex) {
            System.out.println("An error has occurred while retrieving credit package. " + ex.getMessage() + "\n");
        }

    }

    public void updateCreditPackage(CreditPackageEntity creditPackage) {

        Scanner scanner = new Scanner(System.in);
        BigDecimal input;

        System.out.println("* Crazy Auction :: Finance Administration :: View Credit Package Details :: Update Credit Package *\n");
        System.out.print("Enter Credit Balance (Enter 0 if no change)> ");
        input = scanner.nextBigDecimal();
        if (input.doubleValue() != 0) {
            creditPackage.setCreditBalance(input);
        }

        System.out.print("Enter Credit Package Price (Enter 0 if no change)> ");
        input = scanner.nextBigDecimal();
        if (input.doubleValue() != 0) {
            creditPackage.setPackagePrice(input);
        }

        creditPackage.setEnable(true);
        scanner.nextLine();

        creditPackageControllerRemote.updateCreditPackage(creditPackage);
        System.out.println("Credit Package updated successfully!\n");
    }

    public void deleteCreditPackage(CreditPackageEntity creditPackage) {

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("* Crazy Auction :: Finance Administration :: View Credit Package Details :: Delete Credit Package *\n");
        System.out.printf("Confirm Delete Credit Package? (Credit Package ID: %d) (Enter 'Y' to Delete)> ", creditPackage.getCreditPackageId());
        input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("y")) {
            if (creditPackage.getTransactions().size() != 0) {
                creditPackage.setEnable(false);
                creditPackageControllerRemote.updateCreditPackage(creditPackage);
                System.out.println("Credit Package is disabled.");
            } else {
                try {
                    creditPackageControllerRemote.deleteCreditPackage(creditPackage.getCreditPackageId());
                    System.out.println("Credit Package deleted successfully!\n");
                } catch (CreditPackageNotFoundException ex) {
                    System.out.println("An error has occurred while deleting credit package: " + ex.getMessage() + "\n");
                }
            }
        } else {
            System.out.println("Credit Package NOT deleted!\n");
        }
    }

    public void viewAllCreditPackageDetails() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("* Crazy Auction :: Finance Administration :: View All Credit Package ***\n");

        List<CreditPackageEntity> creditPackages = creditPackageControllerRemote.retrieveAllCreditPackage();
        System.out.printf("%8s%20s%20s%20s\n", "Credit Package ID", "Balance", "Price", "Enabled");
 for (CreditPackageEntity creditPackage : creditPackages) {
            System.out.printf("%8s%28s%20s%20s\n", creditPackage.getCreditPackageId().toString(), creditPackage.getCreditBalance(), creditPackage.getPackagePrice(), creditPackage.getEnable().toString());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}