/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadminpanelclient;

import ejb.session.stateless.AddressControllerRemote;
import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import java.text.ParseException;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRight;
import util.exception.AuctionListingNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author KERK
 */
public class MainApp {

    private EmployeeControllerRemote employeeControllerRemote;
    private CustomerControllerRemote customerControllerRemote;
    private CreditTransactionControllerRemote creditTransactionControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private BidControllerRemote bidControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private AddressControllerRemote addressControllerRemote;

    private SalesStaffModule salesStaffModule;
    private FinanceStaffModule financeStaffModule;
    private SystemAdminModule systemAdminModule;

    private EmployeeEntity currentEmployee;

    public MainApp() {
    }

    public MainApp(EmployeeControllerRemote employeeControllerRemote, CustomerControllerRemote customerControllerRemote, CreditTransactionControllerRemote creditTransactionControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote, BidControllerRemote bidControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, AddressControllerRemote addressControllerRemote) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.customerControllerRemote = customerControllerRemote;
        this.creditTransactionControllerRemote = creditTransactionControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.bidControllerRemote = bidControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.addressControllerRemote = addressControllerRemote;
    }

    

    public void run() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("* Welcome to Crazy Auctions Admin Panel *\n");

            //System.out.println("1: create");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    try {
                        doLogin();
                        salesStaffModule = new SalesStaffModule(employeeControllerRemote, bidControllerRemote, auctionListingControllerRemote, creditTransactionControllerRemote);
                        financeStaffModule = new FinanceStaffModule(employeeControllerRemote, creditTransactionControllerRemote, creditPackageControllerRemote);
                        systemAdminModule = new SystemAdminModule(employeeControllerRemote);
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Login failed: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    public void doLogin() throws InvalidLoginCredentialException {

        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Crazy Auction :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            try {
                currentEmployee = employeeControllerRemote.staffLogin(username, password);
                System.out.println("Login successful!\n");
            } catch (InvalidLoginCredentialException ex) {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");

                throw new InvalidLoginCredentialException();
            }
        } else {
            System.out.println("Invalid login credential!");
        }

    }

    public void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Crazy Bid Auction ***\n");
            System.out.println("You are login as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getAccessRight().toString() + " rights\n");
            System.out.println("1: Change Password");
            System.out.println("2: " + currentEmployee.getAccessRight().toString() + " Admin Panel");
            System.out.println("3: Logout");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    changePassword();
                } else if (response == 2) {
                    if (currentEmployee.getAccessRight().toString().equals("SALESSTAFF")) {
                        try {
                            salesStaffModule.menuOperation(currentEmployee);
                        } catch (InvalidAccessRightException ex) {
                            System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                        } catch (AuctionListingNotFoundException ex)
                        {
                            System.out.println("Invalid option, please try again!: " + ex.getMessage());
                        }
                        
                        
                    } else if (currentEmployee.getAccessRight().toString().equals("FINANCESTAFF")) {
                        try {
                            financeStaffModule.menuOperation(currentEmployee);
                        } catch (InvalidAccessRightException ex) {
                            System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                        }
                    } else if (currentEmployee.getAccessRight().toString().equals("SYSTEMADMIN")) {
                        try {
                            systemAdminModule.menuOperation(currentEmployee);
                        } catch (InvalidAccessRightException ex) {
                            System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                        }
                    }
                } else if (response == 3) {
                    break;
                } 
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    public void changePassword() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Crazy Auction :: Change Password ***\n");
            System.out.println("Enter Current Password>");
            String currentPassword = scanner.nextLine().trim();
            if (currentPassword.equals(currentEmployee.getPassword())) {
                while (true) {
                    System.out.println("Enter new password>");
                    String newPassword = scanner.nextLine().trim();
                    System.out.println("Re-enter new password>");
                    String reEnterPassword = scanner.nextLine().trim();
                    if (reEnterPassword.equals(newPassword)) {
                        currentEmployee.setPassword(newPassword);
                        employeeControllerRemote.updateEmployee(currentEmployee);
                        System.out.println("Password changed successfully!");
                        break;

                    } else {
                        System.out.println("Wrong Password entered. Please re-enter a new password.");
                    }
                }
            } else {
                System.out.println("Wrong password. Please re-enter your old password again.");
            }
    }
}

