/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadminpanelclient;

import static com.sun.xml.bind.util.CalendarConv.formatter;
import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import ejb.session.stateless.EJBTimerSessionBeanRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.AuctionListingEntity;
import entity.BidEntity;
import entity.CreditPackageEntity;
import entity.CreditTransactionEntity;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.xml.datatype.DatatypeConstants.DATETIME;
import util.enumeration.EmployeeAccessRight;
import util.exception.AuctionListingNotFoundException;
import util.exception.CreditPackageNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidTransactionException;
import util.exception.StaffNotFoundException;

public class SalesStaffModule {

    private EmployeeControllerRemote employeeControllerRemote;
    private BidControllerRemote bidControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private CreditTransactionControllerRemote creditTransactionControllerRemote;

    private EmployeeEntity currentEmployee;

    public SalesStaffModule() {
    }

    public SalesStaffModule(EmployeeControllerRemote employeeControllerRemote, BidControllerRemote bidControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, CreditTransactionControllerRemote creditTransactionControllerRemote) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.bidControllerRemote = bidControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.creditTransactionControllerRemote = creditTransactionControllerRemote;
    }

    public void menuOperation(EmployeeEntity currentEmployee) throws InvalidAccessRightException, AuctionListingNotFoundException {
        if (!currentEmployee.getAccessRight().equals(EmployeeAccessRight.SALESSTAFF)) {
            throw new InvalidAccessRightException("You don't have SALESSTAFF rights to access the system administration module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("* Crazy Auction :: Sales Administration *\n");
            System.out.println("1: Create Auction Listing");
            System.out.println("2: View Individual Auction Listing");
            System.out.println("3: View All Auction Listing");
            System.out.println("4: View All Closed Auction Listing With Bids But Below Reserve Price");
            System.out.println("5: Back\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    createNewAuctionListing();
                } else if (response == 2) {
                    viewIndividualAuctionListing();
                } else if (response == 3) {
                    viewAllAuctionListing();
                } else if (response == 4) {
                    viewAllAuctionListingBelowReservePrice();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    public void createNewAuctionListing() {

        Scanner scanner = new Scanner(System.in);
        AuctionListingEntity newAuctionListing = new AuctionListingEntity();

        System.out.println("* Crazy Auction :: Sales Administration :: Create New Auction Listing *\n");
        System.out.print("Enter Name Of Auction Listing> ");
        newAuctionListing.setName(scanner.nextLine().trim());
        System.out.print("Enter Description> ");
        newAuctionListing.setDescription(scanner.nextLine());

        System.out.println("Enter Start Bid Date (YYYY-MM-DD HH:MM:SS)> ");
        String startDate = scanner.nextLine();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(startDate));
        } catch (ParseException ex) {
            Logger.getLogger(SalesStaffModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        newAuctionListing.setStartBidDate(cal);

        System.out.println("Enter End Bid Date (YYYY-MM-DD HH:MM:SS)> ");
        String endDate = scanner.nextLine();
        Calendar calEnd = Calendar.getInstance();
        try {
            calEnd.setTime(sdf.parse(endDate));
        } catch (ParseException ex) {
            Logger.getLogger(SalesStaffModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        newAuctionListing.setEndBidDate(calEnd);

        System.out.println("Enter Reserved Price> ");
        BigDecimal reservedPrice = scanner.nextBigDecimal();
        newAuctionListing.setReservedPrice(reservedPrice);

        System.out.println("Enter Start Bid Amount> ");
        BigDecimal startBidAmount = scanner.nextBigDecimal();
        newAuctionListing.setStartBidAmount(startBidAmount);

        newAuctionListing.setCurrentBidAmount(startBidAmount);
        newAuctionListing.setEnable(false);
        newAuctionListing = auctionListingControllerRemote.createNewAuctionListing(newAuctionListing);
        System.out.println("New Auction Listing created successfully!: " + newAuctionListing.getAuctionListingId() + "\n");

    }

    public void viewIndividualAuctionListing() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("* Crazy Auction :: System Administration :: View Auction Listing Details *\n");
        System.out.print("Enter Auction Listing ID> ");
        Long auctionListingId = scanner.nextLong();

        try {
            AuctionListingEntity auctionListing = auctionListingControllerRemote.retrieveAuctionListingByAuctionListingId(auctionListingId);
            System.out.printf("%8s%20s%20s%30s%40s%30s%27s%18s%12s\n", "Auction Listing ID", "Name", "Description", "Start Bid Date", "End bid Date",
                    "Current Bid Amount", "Start Bid Amount", "Reserved Price", "Enable");
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar startCal = auctionListing.getStartBidDate();
            Calendar endCal = auctionListing.getEndBidDate();
            System.out.printf("%8s%30s%15s%40s%40s%23s%23s%20s%16s\n", auctionListing.getAuctionListingId(), auctionListing.getName(), auctionListing.getDescription(),
                    sdf.format(startCal.getTime()), sdf.format(endCal.getTime()), auctionListing.getCurrentBidAmount(), auctionListing.getStartBidAmount(),
                    auctionListing.getReservedPrice(), auctionListing.getEnable().toString());

            System.out.println();
            System.out.println("1: Update Auction Listing");
            System.out.println("2: Delete Auction Listing");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                updateAuctionListing(auctionListing);
            } else if (response == 2) {
                deleteAuctionListing(auctionListing);
            }
        } catch (AuctionListingNotFoundException ex) {
            System.out.println("An error has occurred while retrieving auction listing. " + ex.getMessage() + "\n");
        }

    }

    public void updateAuctionListing(AuctionListingEntity auctionListing) {

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("* Crazy Auction :: Sales Administration :: View Auction Listing Details :: Update Auction Listing *\n");
        System.out.print("Enter Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            auctionListing.setName(input);
        }

        System.out.print("Enter Description (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            auctionListing.setDescription(input);
        }

        System.out.print("Enter Start Bid Date (YYYY-MM-DD HH:MM:SS) (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar startCal = Calendar.getInstance();
            try {
                startCal.setTime(sdf.parse(input));
            } catch (ParseException ex) {
                System.out.println("Error Has Occured In Setting Time");
            }
            auctionListing.setStartBidDate(startCal);
        }

        System.out.print("Enter End Bid Date (YYYY-MM-DD HH:MM:SS) (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar endCal = Calendar.getInstance();
            try {
                endCal.setTime(sdf.parse(input));
            } catch (ParseException ex) {
                System.out.println("Error has occured in setting time");
            }
            auctionListing.setEndBidDate(endCal);
        }

        System.out.print("Enter Reserved Price (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            auctionListing.setReservedPrice(new BigDecimal(input));
        }

        auctionListingControllerRemote.updateAuctionListing(auctionListing);
        System.out.println("Auction Listing updated successfully!\n");

    }

    public void deleteAuctionListing(AuctionListingEntity auctionListing) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("* Crazy Auction :: Sales Administration :: View Auction Listing :: Delete Auction Listing *\n");
        System.out.printf("Confirm Delete Auction Listing (Auction Listing ID: %d) (Enter 'Y' to Delete)> ", auctionListing.getAuctionListingId());
        input = scanner.nextLine().trim();

        try {
            if (input.equalsIgnoreCase("Y")) {

                if (auctionListing.getEnable().equals(Boolean.FALSE)) {
                    auctionListingControllerRemote.deleteAuctionListing(auctionListing);
                    System.out.println("Auction Listing deleted successfully!\n");
                } else {
                    auctionListing.setEnable(Boolean.FALSE);
                    auctionListingControllerRemote.updateAuctionListing(auctionListing);
                    creditTransactionControllerRemote.refundCreditToCustomer(auctionListing.getAuctionListingId());
                    System.out.println("Auction Listing has been disabled successfully!\n");
                }
            } else {
                System.out.println("Auction Listing NOT deleted!\n");
            }
        } catch (InvalidTransactionException ex) {
            System.out.println("error: " + ex.getMessage());
        }
    }

    private void viewAllAuctionListing() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("* Crazy Auction :: Sales Administration :: View All Auction Listing***\n");

        List<AuctionListingEntity> auctionListings = auctionListingControllerRemote.retrieveAllListing();
        System.out.printf("%8s%20s%20s%30s%40s%30s%27s%18s%12s\n", "Auction Listing ID", "Name", "Description", "Start Bid Date", "End bid Date",
                "Current Bid Amount", "Start Bid Amount", "Reserved Price", "Enable");

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (AuctionListingEntity auctionListing : auctionListings) {
            Calendar startCal = auctionListing.getStartBidDate();
            Calendar endCal = auctionListing.getEndBidDate();
            System.out.printf("%8s%30s%15s%40s%40s%23s%23s%20s%16s\n", auctionListing.getAuctionListingId(), auctionListing.getName(), auctionListing.getDescription(),
                    sdf.format(startCal.getTime()), sdf.format(endCal.getTime()), auctionListing.getCurrentBidAmount(), auctionListing.getStartBidAmount(),
                    auctionListing.getReservedPrice(), auctionListing.getEnable().toString());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();

    }

    public void viewAllAuctionListingBelowReservePrice() throws AuctionListingNotFoundException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Crazy Auction :: Sales Administration :: View All Auction Listing Below Reserved Price***\n");
        List<AuctionListingEntity> auctionListings = auctionListingControllerRemote.retrieveAllListingBelowReservePrice();

        if (auctionListings.isEmpty()) {
            System.out.println("There are no manual intervention required for auction listings!");
        } else {
            try {
                System.out.printf("%8s%20s%20s%30s%40s%30s%27s\n", "Auction Listing ID", "Name", "Description",
                        "Start Bid Date", "End Bid Date", "Current Bid Amount", "Reserved Price");

                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (AuctionListingEntity auctionListing : auctionListings) {
                    Calendar startCal = auctionListing.getStartBidDate();
                    Calendar endCal = auctionListing.getEndBidDate();
                    System.out.printf("%8s%20s%20s%40s%40s%23s%23s\n", auctionListing.getAuctionListingId(), auctionListing.getName(), auctionListing.getDescription(),
                            sdf.format(startCal.getTime()), sdf.format(endCal.getTime()), auctionListing.getCurrentBidAmount(), auctionListing.getReservedPrice());

                }
                System.out.println();
                System.out.println(" —------------------------------------------------------- \n");
                System.out.print("Enter row Number for manual intervention> ");
                //int input = scanner.nextInt();
                Long response = scanner.nextLong();
                scanner.nextLine();
                //  Long auctionId = auctionListings.get(row - 1).getAuctionListingId();
                AuctionListingEntity auctionListingIntervention = auctionListingControllerRemote.retrieveAuctionListingByAuctionListingId(response);
                System.out.println("Current Highest Bid for Selected Listing: " + auctionListingIntervention.getCurrentBidAmount());
                System.out.println("Reserved Price for Selected Listing: " + auctionListingIntervention.getReservedPrice());
                System.out.println("Mark the current highest bid as winning bid? Y/N");

                String option = scanner.nextLine().trim();
                if (option.equalsIgnoreCase("y")) {
                    auctionListingControllerRemote.manualIntervention(response);
                    System.out.println("Current Highest Bid Successfully Marked as Winning Bid!");
                } else if (option.equalsIgnoreCase("n")) {
                    auctionListingIntervention.setCustomer(null);
                    System.out.println("No Winning Bid!");
                } //got to catch some exception here
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("Index does not exist, please try again!");
            }
        }
    }

}
