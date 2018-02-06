/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxybiddingcumsnipingagent;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.AuctionListingEntity;
import ws.client.AuctionListingNotFoundException_Exception;
import ws.client.BidEntity;
import ws.client.CreditTransactionEntity;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.CustomerNotPremiumException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;

/**
 *
 * @author SYLVIA
 */
public class MainApp {

    private CustomerEntity customer;

    public MainApp() {
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

        while (true) {
            System.out.println("*** Welcome to Crazy Bid Auction Remote System (v4.2) ***");
            System.out.println("*** Remote Checkout Client***\n");
            System.out.println("1: Remote Register");
            System.out.println("2: Remote Login\n");

            System.out.println("3. Exit");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doRegister();
                } else if (response == 2) {

                    try {
                        if (doLogin()) {
                            menuMain();
                        }
                    } catch (InvalidLoginCredentialException_Exception | CustomerNotPremiumException_Exception ex) {
                        // System.out.println("An error " + ex.getMessage());
                    }

                } else if (response == 3) {
                    break;
                } else {
                    System.out.print("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    private static void doRegister() {
        Scanner scanner = new Scanner(System.in);
        CustomerEntity customer = new CustomerEntity();

        System.out.println("***Remote Web Service :: Create Customer ***\n\n");
        System.out.println("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.println("Enter password> ");
        String password = (scanner.nextLine().trim());

        try {
            premiumRegistration(username, password);
            System.out.println("Customer has successfully registered");
        } catch (CustomerNotFoundException_Exception ex) {
            System.err.println("An error trying to register: " + ex.getMessage());
        }

    }

    private boolean doLogin() throws InvalidLoginCredentialException_Exception, CustomerNotPremiumException_Exception {
        Scanner scanner = new Scanner(System.in);
        customer = new CustomerEntity();

        System.out.println("***Remote Crazy Auctions :: Create Customer ***\n\n");
        System.out.println("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.println("Enter password> ");
        String password = (scanner.nextLine().trim());
        while (true) {

            if (username.length() > 0 && password.length() > 0) {
                try {
                    customer = remoteLogin(username, password);
                    System.out.println("Customer has successfully logged in remotely");
                    return true;

                } catch (InvalidLoginCredentialException_Exception | CustomerNotPremiumException_Exception ex) {
                    System.out.println("An error: " + ex.getMessage());
                    return false;
                }

            } else {
                System.out.println("Invalid login credential!");
                return false;

            }
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("***Remote Crazy Auctions: Remote Auction Client ***\n");
            System.out.println("1. Remote View Credit Balance");
            System.out.println("2. Remote View Auction Listing");
            System.out.println("3. Remote Browse All Auction Listing");
            System.out.println("4. Remote View Won Auction Listing");
            System.out.println("5. Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.println("> ");

                response = scanner.nextInt();
                if (response == 1) {
                    doViewCreditBalance();
                } else if (response == 2) {
                    viewAuctionListingDetail();
                } else if (response == 3) {
                    viewAllAuctionListings();
                } else if (response == 4) {
                    viewWonAuctionListing();
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

    private void doViewCreditBalance() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***Remote Crazy Auctions :: View Credit Balance ***\n");
        try {
            BigDecimal creditBal =  viewCreditBalance(customer.getCustomerId());
            System.out.printf("%20s%20s\n", "Credit Balance", "Holding Balance");
            System.out.printf("%20s%20s\n", creditBal, customer.getHoldingBalance());
            
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        } catch (CustomerNotFoundException_Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void viewAuctionListingDetail() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Remote Crazy Auctions :: View Auction Listing ***\n");
        System.out.printf("%8s%20s%20s\n", "Index", "Name", "Description");

        List<AuctionListingEntity> listings = viewAuctionListings();
        int row = 1;
        for (AuctionListingEntity listing : listings) {
            System.out.printf("%8s%20s%20s\n", row, listing.getName(), listing.getDescription());
            row++;
        }

        System.out.println("Enter row number> ");
        int input = scanner.nextInt();
        try {
            Long auctionId = listings.get(input - 1).getAuctionListingId();
            AuctionListingEntity auctionListing = retrieveAuctionListing(auctionId);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            XMLGregorianCalendar startDate = auctionListing.getStartBidDate();
            GregorianCalendar startDateCalendar = startDate.toGregorianCalendar();
            XMLGregorianCalendar endDate = auctionListing.getEndBidDate();
            GregorianCalendar endDateCalendar = endDate.toGregorianCalendar();

            System.out.printf("%8s%20s%20s%30s%40s%30s%27s\n", "Row", "Start Bid Amount", "Current Bid", "Start Bid Date", "End Bid Date", "Name", "Description");
            System.out.printf("%8s%20s%20s%30s%40s%30s%27s\n", input, auctionListing.getStartBidAmount(), auctionListing.getCurrentBidAmount(), sdf.format(startDateCalendar.getTime()), sdf.format(endDateCalendar.getTime()), auctionListing.getName(), auctionListing.getDescription());
            System.out.println("------------------------");
            System.out.println("1. Proxy Bidding");
            System.out.println("2. Sniping Agent");
            System.out.println("3. Exit");
            System.out.print("> ");
            int response = scanner.nextInt();
            if (response == 1) {
                System.out.println("We did not complete proxy bidding use case!");
            } else if (response == 2) {
                snipingAuctionListing(auctionListing);
            }

        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Row index not found, please try again!");
        } catch (AuctionListingNotFoundException_Exception ex) {
            System.out.println("An error occured trying to retrieve address: " + ex.getMessage());
        }

    }

    private void snipingAuctionListing(AuctionListingEntity auctionListing) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Remote Crazy Auctions :: Auction Sniping***\n");
        System.out.println("Enter specific time to snipe before listing expiration (YYYY-MM-DD HH:MM:SS)> ");
        String startDate = scanner.nextLine();

        // snippingDateTime.set(year, month, day, hour, min, sec);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(sdf.parse(startDate));
        } catch (ParseException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print("Enter maximum bid amount> ");
        BigDecimal maxAmount = scanner.nextBigDecimal();
        XMLGregorianCalendar xmlGregorianCalendar;

        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            createNewSnipingBid(customer, auctionListing, xmlGregorianCalendar, maxAmount);
        } catch (DatatypeConfigurationException ex) {

        }

    }

    // java.sql.Timestamp timeStamp = new java.sql.Timestamp(System.currentTimeMillis());
    private void viewAllAuctionListings() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Remote Crazy Auctions :: View All Auction Listings ***\n");
        System.out.printf("%8s%20s%20s\n", "Index", "Name", "Description");

        List<AuctionListingEntity> listings = viewAuctionListings();
        int row = 1;
        for (AuctionListingEntity listing : listings) {
            System.out.printf("%8s%20s%20s\n", row, listing.getName(), listing.getDescription());
            row++;
        }

        System.out.println("Press any key to continue");
        scanner.nextLine();
    }

    public void viewWonAuctionListing() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Remote Crazy Auctions :: View Won Auction Listings ***\n");
        try {
            List<AuctionListingEntity> listings = retrieveWonListing(customer.getCustomerId());
            int row = 1;
            for (AuctionListingEntity listing : listings) {
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                XMLGregorianCalendar startDate = listing.getStartBidDate();
                GregorianCalendar startDateTime = startDate.toGregorianCalendar();
                XMLGregorianCalendar endDate = listing.getEndBidDate();
                GregorianCalendar endDateTime = endDate.toGregorianCalendar();
                System.out.printf("%8s%13s%20s%30s%40s%30s%27s\n", "Row", "Name", "Description", "Start Bid Date", "End Bid Date", "Start Bid Amount", "Current Bid Amount");
                System.out.printf("%8s%13s%15s%40s%40s%23s%23s\n", row, listing.getName(), listing.getDescription(), sdf.format(startDateTime.getTime()), sdf.format(endDateTime.getTime()),listing.getStartBidAmount(), listing.getCurrentBidAmount());
                row++;
            }

        } catch (AuctionListingNotFoundException_Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static CustomerEntity remoteLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception, CustomerNotPremiumException_Exception {
        ws.client.SnipingAgentWebService_Service service = new ws.client.SnipingAgentWebService_Service();
        ws.client.SnipingAgentWebService port = service.getSnipingAgentWebServicePort();
        return port.remoteLogin(username, password);
    }

    private static BigDecimal viewCreditBalance(long customerId) throws CustomerNotFoundException_Exception {
        ws.client.SnipingAgentWebService_Service service = new ws.client.SnipingAgentWebService_Service();
        ws.client.SnipingAgentWebService port = service.getSnipingAgentWebServicePort();
        return port.viewCreditBalance(customerId);
    }

    private static java.util.List<ws.client.AuctionListingEntity> viewAuctionListings() {
        ws.client.SnipingAgentWebService_Service service = new ws.client.SnipingAgentWebService_Service();
        ws.client.SnipingAgentWebService port = service.getSnipingAgentWebServicePort();
        return port.viewAuctionListings();
    }

    private static AuctionListingEntity retrieveAuctionListing(long auctionListingId) throws AuctionListingNotFoundException_Exception {
        ws.client.SnipingAgentWebService_Service service = new ws.client.SnipingAgentWebService_Service();
        ws.client.SnipingAgentWebService port = service.getSnipingAgentWebServicePort();
        return port.retrieveAuctionListing(auctionListingId);
    }

    private static void premiumRegistration(java.lang.String username, java.lang.String password) throws CustomerNotFoundException_Exception {
        ws.client.SnipingAgentWebService_Service service = new ws.client.SnipingAgentWebService_Service();
        ws.client.SnipingAgentWebService port = service.getSnipingAgentWebServicePort();
        port.premiumRegistration(username, password);
    }

    private static void createNewSnipingBid(ws.client.CustomerEntity customer, ws.client.AuctionListingEntity auctionListing, javax.xml.datatype.XMLGregorianCalendar cal, java.math.BigDecimal maxAmount) {
        ws.client.SnipingAgentWebService_Service service = new ws.client.SnipingAgentWebService_Service();
        ws.client.SnipingAgentWebService port = service.getSnipingAgentWebServicePort();
        port.createNewSnipingBid(customer, auctionListing, cal, maxAmount);
    }

    private static java.util.List<ws.client.AuctionListingEntity> retrieveWonListing(long customerId) throws AuctionListingNotFoundException_Exception {
        ws.client.SnipingAgentWebService_Service service = new ws.client.SnipingAgentWebService_Service();
        ws.client.SnipingAgentWebService port = service.getSnipingAgentWebServicePort();
        return port.retrieveWonListing(customerId);
    }

    

}
