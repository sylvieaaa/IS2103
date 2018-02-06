/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.AuctionListingControllerLocal;
import ejb.session.stateless.CreditTransactionControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.EJBTimerSessionBeanLocal;
import ejb.session.stateless.EJBTimerSessionBeanRemote;
import entity.AuctionListingEntity;
import entity.BidEntity;
import entity.CreditTransactionEntity;
import entity.CustomerEntity;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.AuctionListingNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerNotPremiumException;
import util.exception.InvalidBidException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author SYLVIA
 */
@WebService(serviceName = "SnipingAgentWebService")
@Stateless()
public class SnipingAgentWebService {

    @EJB(name = "CreditTransactionControllerLocal")
    private CreditTransactionControllerLocal creditTransactionControllerLocal;

    @EJB
    private EJBTimerSessionBeanLocal eJBTimerSessionBeanLocal;

    @EJB(name = "AuctionListingControllerLocal")
    private AuctionListingControllerLocal auctionListingControllerLocal;

    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @WebMethod(operationName = "remoteLogin")
    public CustomerEntity remoteLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException, CustomerNotPremiumException {
        //TODO write your implementation code here:

        try {
            CustomerEntity customer = customerControllerLocal.customerLogin(username, password);
            ;
            if (customer.getPremiumAccount() == false) {

                throw new CustomerNotPremiumException("Customer is not yet a premium account");
            } else {

                em.detach(customer);
                customer.setAuctions(null);
                customer.setBids(null);
                customer.setTransactions(null);
                return customer;
            }

        } catch (InvalidLoginCredentialException ex) {
            throw new InvalidLoginCredentialException("Incorrect Password or Username");
        }

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "premiumRegistration")
    public void premiumRegistration(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws CustomerNotFoundException {
        //TODO write your implementation code here:
        try {
            CustomerEntity customer = customerControllerLocal.retrieveCustomerByUsername(username);
            System.out.println("customer is true " + customer);
            customer.setPremiumAccount(Boolean.TRUE);
            System.out.println("Customer is registered successfully");

        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Remote Registration fail, customer not found!");
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "viewCreditBalance")
    public BigDecimal viewCreditBalance(@WebParam(name = "customerId") long customerId) throws CustomerNotFoundException {
        //TODO write your implementation code here:
        try {
            CustomerEntity customer = customerControllerLocal.retrieveCustomerByCustomerId(customerId);
            
            return customer.getCreditWallet();
        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer Not Found!");
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "viewAuctionListings")
    public List<AuctionListingEntity> viewAuctionListings() {
        //TODO write your implementation code here:
        List<AuctionListingEntity> listings = auctionListingControllerLocal.retrieveActiveAuctionListings();

        for (AuctionListingEntity listing : listings) {
            em.detach(listing);
            listing.setBids(null);
            listing.setCustomer(null);
        }

        return listings;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retrieveAuctionListing")
    public AuctionListingEntity retrieveAuctionListing(@WebParam(name = "auctionListingId") long auctionListingId) throws AuctionListingNotFoundException {
        try {
            AuctionListingEntity auctionListing = auctionListingControllerLocal.retrieveAuctionListingByAuctionListingId(auctionListingId);
            em.detach(auctionListing);
            auctionListing.setBids(null);
            auctionListing.setCustomer(null);
            return auctionListing;
        } catch (AuctionListingNotFoundException | ArrayIndexOutOfBoundsException ex) {
            throw new AuctionListingNotFoundException("Remote: Auction Listing ID " + auctionListingId + " does not exist!");
        }

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retrieveWonListing")
    public List<AuctionListingEntity> retrieveWonListing(@WebParam(name = "customerId") long customerId) throws AuctionListingNotFoundException {
        //TODO write your implementation code here:
       try {
            List<AuctionListingEntity> listings = auctionListingControllerLocal.retrieveWonAuctionListings(customerId);
            for (AuctionListingEntity listing: listings)
            {
                em.detach(listing);
                listing.setAddress(null);
                listing.setCustomer(null);
                listing.setBids(null);
            }
            return listings;
        } catch (AuctionListingNotFoundException ex) {
            throw new AuctionListingNotFoundException("Remote: No Winning Auction Listing");
        }
    }

    private static BigDecimal bidTable(BigDecimal currentBid) {
        if (currentBid.compareTo(BigDecimal.valueOf(1.00)) < 0) {
            return BigDecimal.valueOf(0.05);
        } else if (currentBid.compareTo(BigDecimal.valueOf(5.00)) < 0) {
            return BigDecimal.valueOf(0.25);
        } else if (currentBid.compareTo(BigDecimal.valueOf(25.00)) < 0) {
            return BigDecimal.valueOf(0.50);
        } else if (currentBid.compareTo(BigDecimal.valueOf(100.00)) < 0) {
            return BigDecimal.valueOf(1.00);
        } else if (currentBid.compareTo(BigDecimal.valueOf(250.00)) < 0) {
            return BigDecimal.valueOf(2.50);
        } else if (currentBid.compareTo(BigDecimal.valueOf(500.00)) < 0) {
            return BigDecimal.valueOf(5.00);
        } else if (currentBid.compareTo(BigDecimal.valueOf(1000.00)) < 0) {
            return BigDecimal.valueOf(10.00);
        } else if (currentBid.compareTo(BigDecimal.valueOf(2500.00)) < 0) {
            return BigDecimal.valueOf(25.00);
        } else if (currentBid.compareTo(BigDecimal.valueOf(5000.00)) < 0) {
            return BigDecimal.valueOf(50.00);
        } else {
            return BigDecimal.valueOf(100.00);
        }

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "createNewSnipingBid")
    public void createNewSnipingBid(@WebParam(name = "customer") CustomerEntity customer, @WebParam(name = "auctionListing") AuctionListingEntity auctionListing, @WebParam(name = "cal") Calendar cal, @WebParam(name = "maxAmount") BigDecimal maxAmount) {
        eJBTimerSessionBeanLocal.createWebTimer(auctionListing, customer.getCustomerId(), maxAmount, cal);
    }

    /**
     * Web service operation
     */
 

    /**
     * Web service operation
     */
    //@WebMethod(operationName = "createSnipingBid")
    //public void createSnipingBid(@WebParam(name = "auctionListing") AuctionListingEntity auctionListing, @WebParam(name = "customer") CustomerEntity customer, @WebParam(name = "maxAmount") BigDecimal maxAmount, @WebParam(name = "cal") Calendar cal) {
    //  eJBTimerSessionBeanLocal.createWebTimer(auctionListing, customer.getCustomerId(), maxAmount, cal);
    //}
}
