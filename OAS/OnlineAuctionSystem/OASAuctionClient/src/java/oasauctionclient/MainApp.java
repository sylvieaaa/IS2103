/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasauctionclient;

import ejb.session.stateless.AddressControllerRemote;
import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import entity.AddressEntity;
import entity.AuctionListingEntity;
import entity.BidEntity;
import entity.CreditPackageEntity;
import entity.CreditTransactionEntity;
import entity.CustomerEntity;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import util.enumeration.CreditTransactionType;
import util.exception.AddressNotFoundException;
import util.exception.AuctionListingNotFoundException;
import util.exception.CreditPackageNotFoundException;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidBidException;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidTransactionException;

/**
 *
 * @author SYLVIA
 */
public class MainApp {

    private CustomerControllerRemote customerControllerRemote;
    private CreditTransactionControllerRemote creditTransactionControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private BidControllerRemote bidControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private AddressControllerRemote addressControllerRemote;

    private CustomerEntity customerEntity;
    private AddressEntity addressEntity;

    public MainApp() {
    }

    public MainApp(CustomerControllerRemote customerControllerRemote, CreditTransactionControllerRemote creditTransactionControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote, BidControllerRemote bidControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, AddressControllerRemote addressControllerRemote) {
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
            System.out.println("* Welcome to Crazy Auctions! *\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    doRegister();
                } else if (response == 2) {

                    try {
                        if (doLogin()) // Reserved for future use                        
                        {
                            menuMain();
                        }

                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Login failed: " + ex.getMessage() + "\n");
                    }

                } else if (response == 3) {
                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    private void doRegister() {

        try {
            Scanner scanner = new Scanner(System.in);
            CustomerEntity newCustomer = new CustomerEntity();

            System.out.println("*** Crazy Auctions :: Create Customer ***\n\n");
            System.out.print("Enter First Name> ");
            newCustomer.setFirstName(scanner.nextLine().trim());
            System.out.print("Enter Last Name> ");
            newCustomer.setLastName(scanner.nextLine().trim());
            System.out.print("Enter Contact Number> ");
            newCustomer.setContactNumber(scanner.nextLine().trim());
            System.out.print("Enter username> ");
            newCustomer.setUsername(scanner.nextLine().trim());
            System.out.print("Enter password> ");
            newCustomer.setPassword(scanner.nextLine().trim());
            newCustomer.setPremiumAccount(Boolean.FALSE);
            newCustomer.setCreditWallet(new BigDecimal(0.00));
            newCustomer.setHoldingBalance(new BigDecimal(0.00));

            newCustomer = customerControllerRemote.createNewCustomer(newCustomer);
            System.out.println("New customer created successfully!: " + newCustomer.getCustomerId() + "\n");
        } catch (CustomerExistException | GeneralException ex) {
            System.out.println("An error has occurred while creating the new customer: " + ex.getMessage() + "!\n");
        }
    }

    private boolean doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password = "";

        while (true) {

            System.out.println("*** Crazy Auctions :: Login ***\n");
            System.out.print("Enter username> ");
            username = scanner.nextLine().trim();
            System.out.print("Enter password> ");
            password = scanner.nextLine().trim();

            if (username.length() > 0 && password.length() > 0) {
                try {
                    customerEntity = customerControllerRemote.customerLogin(username, password);
                    System.out.println("Login successful!\n");
                    return true;
                } catch (InvalidLoginCredentialException ex) {
                    System.out.println("Invalid login credential: " + ex.getMessage() + "\n");

                    throw new InvalidLoginCredentialException();
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
            System.out.println("*** Crazy Auctions: Auction Client ***\n");
            System.out.println("You are logged in\n");
            System.out.println("1. View profile");
            System.out.println("2: Update profile");
            System.out.println("3: Create address");
            System.out.println("4: View Address Details");
            System.out.println("5: View All Address");
            System.out.println("6: View Credit Balance");
            System.out.println("7: Purchase Credit Package");
            System.out.println("8: Browse all Auction Listings");
            System.out.println("9: View Auction Listing");
            System.out.println("10: View Credit Transaction");
            System.out.println("11. Browse Won Auction Listings");
            System.out.println("12: Logout\n");
            response = 0;

            while (response < 1 || response > 12) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doViewCustomerDetails();
                } else if (response == 2) {
                    doUpdateCustomer();
                } else if (response == 3) {
                    doCreateAddress();
                } else if (response == 4) {
                    viewAddressDetails();
                } else if (response == 5) {
                    doViewAllAddress();
                } else if (response == 6) {
                    doViewCreditBalance();
                } else if (response == 7) {
                    doPurchaseCreditPackage();
                } else if (response == 8) {
                    browseAuctionListings();
                } else if (response == 9) {
                    viewAuctionListing();
                } else if (response == 10) {
                    viewCreditTransactionHistory();
                } else if (response == 11) {
                    browseWonAuctionlisting();
                } else if (response == 12) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 12) {
                break;
            }
        }
    }

    private void doViewCustomerDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Crazy Auctions :: View Customer Details ***\n");
        Long customerId = customerEntity.getCustomerId();

        try {
            customerEntity = customerControllerRemote.retrieveCustomerByCustomerId(customerId);
            System.out.printf("%8s%20s%20s%15s%20s%20s%20s%20s\n", "Customer ID", "First Name", "Last Name", "Contact Number", "Username", "Password", "Credit Balance", "Holding Balance");
            System.out.printf("%8s%20s%20s%15s%20s%20s%20s%20s\n", customerEntity.getCustomerId().toString(), customerEntity.getFirstName(), customerEntity.getLastName(), customerEntity.getContactNumber(), customerEntity.getUsername(), customerEntity.getPassword(), customerEntity.getCreditWallet(), customerEntity.getHoldingBalance());
            System.out.println("------------------------");

            System.out.print("Press any key to continue...> ");
            scanner.nextLine();

        } catch (CustomerNotFoundException ex) {
            System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateCustomer() {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** Crazy Auctions :: View Customer Details :: Update Customer ***\n");
        //System.out.print("Enter Customer ID> ");
        //Long customerId = customerEntity.getCustomerId();

        try {
            CustomerEntity customerEntity = customerControllerRemote.retrieveCustomerByCustomerId(this.customerEntity.getCustomerId());
            System.out.print("Enter First Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                customerEntity.setFirstName(input);
            }

            System.out.print("Enter Last Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                customerEntity.setLastName(input);
            }

            System.out.print("Enter Username (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                customerEntity.setUsername(input);
            }

            System.out.print("Enter Password (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                customerEntity.setPassword(input);
            }

            customerControllerRemote.updateCustomer(customerEntity);
            System.out.println("Customer Details Updated Successfully!\n");

        } catch (CustomerNotFoundException ex) {
            System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }

    }

    private void doCreateAddress() {
        Scanner scanner = new Scanner(System.in);
        AddressEntity addressEntity = new AddressEntity();
        System.out.println("*** Crazy Auctions :: View Customer Details :: Create Address ***\n");

        try {

            System.out.println("Enter address line> ");
            addressEntity.setAddressLine(scanner.nextLine().trim());
            System.out.println("Enter postal code> ");
            addressEntity.setPostalCode(scanner.nextLine().trim());
            addressEntity.setActive(Boolean.TRUE);

            addressEntity = addressControllerRemote.createNewAddress(addressEntity, customerEntity.getCustomerId());
            System.out.println("Address is Successfully Created!");

        } catch (CustomerNotFoundException ex) {
            System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }

    }

    private void doViewAllAddress() {
        Scanner scanner = new Scanner(System.in);
        Long customerId = customerEntity.getCustomerId();

        System.out.println("*** Crazy Auctions :: View Customer Details :: View Address ***\n");
        List<AddressEntity> addressEntities;
        try {
            addressEntities = addressControllerRemote.retrieveAllAddresses(customerId);

            System.out.printf("%8s%40s%40s\n", "row", "Address Line", "Postal Code");
            int row = 1;
            for (AddressEntity addressEntity : addressEntities) {
                System.out.printf("%8s%40s%40s\n", row, addressEntity.getAddressLine(), addressEntity.getPostalCode());
                row++;
            }

            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        } catch (AddressNotFoundException ex) {
            System.out.println("An error has occured while trying to retrieve address: " + ex.getMessage());
        }

    }

    private void viewAddressDetails() {
        Scanner scanner = new Scanner(System.in);
        Long customerId = customerEntity.getCustomerId();

        List<AddressEntity> addressEntities;
        try {
            addressEntities = addressControllerRemote.retrieveAllAddresses(customerId);

            System.out.printf("%8s%40s%40s\n", "row", "Address Line", "Postal Code");
            int row = 1;
            for (AddressEntity addressEntity : addressEntities) {
                System.out.printf("%8s%40s%40s\n", row, addressEntity.getAddressLine(), addressEntity.getPostalCode());
                row++;
            }

            System.out.println("Enter Row> ");
            row = scanner.nextInt();

            Long addressId = addressEntities.get(row - 1).getAddressId();
            System.out.println("*** Crazy Auctions :: View Customer Details :: View Address ***\n");
            AddressEntity addressEntity = addressControllerRemote.retrieveAddress(customerId, addressId);
            System.out.printf("%8s%40s%40s\n", "Address ID", "Address Line", "Postal Code");
            System.out.printf("%8s%40s%40s\n", addressEntity.getAddressId().toString(), addressEntity.getAddressLine(), addressEntity.getPostalCode());
            System.out.println("------------------------");
            System.out.println("1: Update Address");
            System.out.println("2: Delete Address");
            System.out.println("3: Back\n");
            System.out.print("> ");
            Integer response = scanner.nextInt();
            if (response == 1) {
                doUpdateAddress(addressEntity);
            } else if (response == 2) {
                doDeleteAddress(addressEntity);
            }

        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Index does not exist, please try again");
        } catch (AddressNotFoundException ex) {
            System.out.println("An error occured: " + ex.getMessage());
        }

    }

    private void doUpdateAddress(AddressEntity addressEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** Crazy Auctions :: View Address Details :: Update Address ***\n");

        System.out.print("Enter AddressLine (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            addressEntity.setAddressLine(input);
        }

        System.out.print("Enter Postal Code (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            addressEntity.setPostalCode(input);
        }

        addressControllerRemote.updateAddress(addressEntity);
        System.out.println("Address is Updated Successfully!\n");

    }

    private void doDeleteAddress(AddressEntity addressEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** Crazy Auctions :: View Address Details :: Delete Address ***\n");
        System.out.printf("Confirm Delete Address (Address ID: %d) (Enter 'Y' to Delete)> ", addressEntity.getAddressId());
        input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("Y")) {
            if (addressEntity.getListings().size() == 0) {
                customerEntity.getAddresses().remove(addressEntity);
                customerEntity = customerControllerRemote.mergeCustomer(customerEntity);
                addressControllerRemote.deleteAddress(addressEntity);
                System.out.println("Address deleted successfully!\n");
            } else {
                addressEntity.setActive(false);
                addressEntity = addressControllerRemote.mergeAddress(addressEntity);
                System.out.println("Address is NOT deleted! ");
                //merge
            }
        } else if (input.equalsIgnoreCase("N")) {
            System.out.println("Address is not removed!");
        }

    }

    private void doViewCreditBalance() {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("*** Crazy Auctions :: View Credit Balance ***\n");
        System.out.printf("%20s%20s\n", "Credit Balance", "Holding Balance");
        System.out.printf("%20s%20s\n", customerEntity.getCreditWallet(), customerEntity.getHoldingBalance());
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();

    }

    private void viewCreditTransactionHistory() {
        Scanner scanner = new Scanner(System.in);
        Long customerId = customerEntity.getCustomerId();

        System.out.println("*** Crazy Auctions :: View Customer Details :: View Tramsactions ***\n");
        List<CreditTransactionEntity> creditTransactionEntities = creditTransactionControllerRemote.retrieveTransaction(customerId);
        System.out.printf("%8s%30s%30s%35s\n", "Row", "Transaction Date", "Type", "Amount");

        for (CreditTransactionEntity creditTransactionEntity : creditTransactionEntities) {
            Calendar cal = creditTransactionEntity.getTransactionDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.printf("%8s%32s%29s%33s\n", creditTransactionEntity.getCreditTransactionId().toString(), sdf.format(cal.getTime()), creditTransactionEntity.getType().toString(), creditTransactionEntity.getAmount());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doPurchaseCreditPackage() {
        Scanner scanner = new Scanner(System.in);
        Long customerId = customerEntity.getCustomerId();

        List<CreditPackageEntity> creditPackages = creditPackageControllerRemote.retrieveAllActivePackage();
        if (creditPackages.isEmpty()) {
            System.out.println("There are no credit packages avaliable to purchase!");
            System.out.println("Enter any key to continue...");
            scanner.nextLine();
        } else {
            System.out.println("*** Crazy Auctions :: View Customer Details :: Buy Credit Package ***\n");
            System.out.printf("%8s%28s%20s%20s\n", "Row", "Credit Balance", "Enable", "Package Price");

            try {
                //List<CreditPackageEntity> creditPackages = creditPackageControllerRemote.retrieveAllActivePackage();
                int row = 1;
                for (CreditPackageEntity creditPackage : creditPackages) {
                    System.out.printf("%8s%28s%20s%20s\n", row, creditPackage.getCreditBalance().toString(), creditPackage.getEnable(), creditPackage.getPackagePrice());
                    row++;
                }

                System.out.println("Enter Credit Package Row to purchase (Only ONE type is allowed) > ");
                int input = scanner.nextInt();
                Long packageId = creditPackages.get(input - 1).getCreditPackageId();
                System.out.println("Enter Quantity to purchase");
                BigDecimal amount = scanner.nextBigDecimal();
                CreditPackageEntity creditPackage = creditPackageControllerRemote.retrieveCreditPackageByCreditPackageId(packageId);
                CreditTransactionEntity transaction = new CreditTransactionEntity();
                customerEntity = creditTransactionControllerRemote.createNewDebitTransaction(transaction, customerId, creditPackage.getCreditPackageId(), amount);
                System.out.println("Credit Package is Successfully Bought");

            } catch (IndexOutOfBoundsException ex) {
                System.out.println("Row Index does not exist, please try again!");
            } catch (CreditPackageNotFoundException ex) {
                System.out.println("An error has occurred while trying to retrieve package: " + ex.getMessage() + "\n");
            } catch (InvalidTransactionException ex) {
                System.out.println("An error has occured while trying to make transaction: " + ex.getMessage());
            }
        }
    }

    private void browseAuctionListings() {

        Scanner scanner = new Scanner(System.in);
        List<AuctionListingEntity> listings = auctionListingControllerRemote.retrieveActiveAuctionListings();
        if (listings.isEmpty()) {
            System.out.println("No active auction listings are avalible for viewing!");
            scanner.nextLine();
        } else {
            System.out.println("*** Crazy Auctions :: Browse Auction Listings ***\n");
         System.out.printf("%8s%13s%20s%30s%40s%30s%27s\n", "Row", "Name", "Description", "Start Bid Date", "End Bid Date", "Start Bid Amount", "Current Bid Amount");

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int row = 1;
            for (AuctionListingEntity listing : listings) {
                Calendar startDate = listing.getStartBidDate();
                Calendar endDate = listing.getEndBidDate();
               System.out.printf("%8s%13s%15s%40s%40s%23s%23s\n", row, listing.getName(), listing.getDescription(), sdf.format(startDate.getTime()), sdf.format(endDate.getTime()), listing.getStartBidAmount(), listing.getCurrentBidAmount());
                row++;
            }
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();

    }

    private void viewAuctionListing() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Crazy Auctions :: View Auction Listings ***\n");
        System.out.printf("%8s%20s%20s\n", "Index", "Name", "Description");

        List<AuctionListingEntity> listings = auctionListingControllerRemote.retrieveActiveAuctionListings();
        int row = 1;

        for (AuctionListingEntity listing : listings) {
            System.out.printf("%8s%20s%20s\n", row, listing.getName(), listing.getDescription());
            row++;
        }

        System.out.println("Enter row number> ");
        int input = scanner.nextInt();
        try {
            Long auctionId = listings.get(input - 1).getAuctionListingId();
            AuctionListingEntity auctionListing = auctionListingControllerRemote.retrieveAuctionListingByAuctionListingId(auctionId);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar startDate = auctionListing.getStartBidDate();
            Calendar endDate = auctionListing.getEndBidDate();
            System.out.printf("%8s%13s%20s%30s%40s%30s%27s\n", "Row", "Name", "Description", "Start Bid Date", "End Bid Date", "Start Bid Amount", "Current Bid Amount");
            System.out.printf("%8s%13s%15s%40s%40s%23s%23s\n", row, auctionListing.getName(), auctionListing.getDescription(), sdf.format(startDate.getTime()), sdf.format(endDate.getTime()), auctionListing.getStartBidAmount(), auctionListing.getCurrentBidAmount());
            System.out.println("------------------------");
            System.out.println("Current credit balance: " + customerEntity.getCreditWallet());
            System.out.println("1: Place New Bid");
            System.out.println("2: Refresh Auction Listing Bids");
            System.out.println("3. Back");
            System.out.print("> ");
            Integer response = scanner.nextInt();
            if (response == 1) {
                doPlaceNewBid(auctionListing);
            } else if (response == 2) {
                refreshAuctionListing(auctionListing);
            }

        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Row Index does not exist, please try again!");
        } catch (AuctionListingNotFoundException ex) {
            System.out.println("An error occured trying to retrieve auction listing: " + ex.getMessage());
        }

    }

    private void doPlaceNewBid(AuctionListingEntity auctionListing) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal currentBid = auctionListing.getCurrentBidAmount();
        BigDecimal increment = bidControllerRemote.bidTable(currentBid);
        System.out.println("*** Crazy Auctions :: Place New Bid ***\n");
        System.out.print("Current Highest Bid > " + auctionListing.getCurrentBidAmount() + "\n");
        System.out.println("Next increment level  > " + increment);
        System.out.print("Your Current Amount> " + customerEntity.getCreditWallet() + "\n");
        System.out.print("Enter your bid amount> ");
        BigDecimal bidAmount = scanner.nextBigDecimal();

        try {
            customerEntity = bidControllerRemote.createNewBid(customerEntity.getCustomerId(), auctionListing.getAuctionListingId(), increment, bidAmount);
            System.out.println("You have successfully bidded!");

        } catch (InvalidBidException ex) {
            System.out.println("An error has occured while trying to create a new bid: " + ex.getMessage());
        }
    }

    private void refreshAuctionListing(AuctionListingEntity auctionListing) {
        Long auctionListingId = auctionListing.getAuctionListingId();

        List<BidEntity> bidEntities = bidControllerRemote.retrieveBids(auctionListingId);
        System.out.printf("%8s%20s\n", "Row", "Bid Amount");

        for (BidEntity bidEntity : bidEntities) {
            System.out.printf("%8s%20s\n", bidEntity.getBidId(), bidEntity.getBidAmount());
        }

    }

    private void browseWonAuctionlisting() {
        Scanner scanner = new Scanner(System.in);
        Long customerId = customerEntity.getCustomerId();

        try {
            System.out.println("*** Crazy Auctions :: View Won Auction Listings ***\n");
            System.out.printf("%8s%13s%20s%30s%40s%30s%27s\n", "Row", "Name", "Description", "Start Bid Date", "End Bid Date", "Start Bid Amount", "Current Bid Amount");
            int row = 1;
            List<AuctionListingEntity> wonListings = auctionListingControllerRemote.retrieveWonAuctionListings(customerId);
            for (AuctionListingEntity listing : wonListings) {
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar startDate = listing.getStartBidDate();
                Calendar endDate = listing.getEndBidDate();
                System.out.printf("%8s%13s%15s%40s%40s%23s%23s\n", row, listing.getName(), listing.getDescription(), sdf.format(startDate.getTime()), sdf.format(endDate.getTime()), listing.getStartBidAmount(), listing.getCurrentBidAmount());
                row++;
            }
            System.out.println("------------------------");
            System.out.println("1: Select delivery address for won listing");
            System.out.println("2. Back");
            System.out.print("> ");
            int input = scanner.nextInt();
            if (input == 1) {
                System.out.println("Select Won Listing ");
                System.out.print("> ");
                row = scanner.nextInt();
                Long auctionId = wonListings.get(row - 1).getAuctionListingId();
                AuctionListingEntity listing = auctionListingControllerRemote.retrieveAuctionListingByAuctionListingId(auctionId);
                selectAddress(listing);

            }

        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Row index does not exist, please try again!");
        } catch (AuctionListingNotFoundException ex) {
            System.out.println("An error has occured trying to retrieve won listings: " + ex.getMessage());
        }
    }

    private void selectAddress(AuctionListingEntity listing) {
        Scanner scanner = new Scanner(System.in);
        Long auctionListingId = listing.getAuctionListingId();
        Long customerId = customerEntity.getCustomerId();
        List<AddressEntity> addresses;
        try {
            addresses = addressControllerRemote.retrieveAllAddresses(customerId);

            System.out.printf("%8s%40s%40s\n", "row", "Address Line", "Postal Code");
            int row = 1;
            for (AddressEntity address : addresses) {

                System.out.printf("%8s%40s%40s\n", row, address.getAddressLine(), address.getPostalCode());
                row++;
            }
            System.out.println("Select Address");
            System.out.print("> : ");
            int input = scanner.nextInt();

            Long addressId = addresses.get(input - 1).getAddressId();
            AddressEntity address = addressControllerRemote.retrieveAddress(customerId, addressId);
            System.out.printf("%8s%40s%40s\n", "Row", "Address Line", "Postal Code");
            System.out.printf("%8s%40s%40s\n", input, address.getAddressLine(), address.getPostalCode());
            scanner.nextLine();
            System.out.println("Enter Y if confirm this address");
            String response = scanner.nextLine().trim();

            if (response.equalsIgnoreCase("Y")) {
                addressControllerRemote.setAddressToListing(addressId, auctionListingId);
                System.out.println("Address is set to your auction listing!");
            } else {
                System.out.println("Address is NOT set to your auction listing");
            }

        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Index does not exist, please try again!");
        } catch (AddressNotFoundException ex) {
            System.out.println("An error has occured while trying to retrieve address: " + ex.getMessage());
        }

    }

}
