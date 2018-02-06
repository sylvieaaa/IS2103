/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListingEntity;
import entity.BidEntity;
import entity.CreditTransactionEntity;
import entity.CustomerEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AuctionListingNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidBidException;


 
@Stateless
@Local(BidControllerLocal.class)
@Remote(BidControllerRemote.class)
public class BidController implements BidControllerRemote, BidControllerLocal {

    @EJB(name = "AuctionListingControllerLocal")
    private AuctionListingControllerLocal auctionListingControllerLocal;

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    @Override
    public CustomerEntity createNewBid(Long customerId, Long auctionListingId, BigDecimal increment, BigDecimal bidAmount) throws InvalidBidException {
        try {
         
            CustomerEntity customer = customerControllerLocal.retrieveCustomerByCustomerId(customerId);
            AuctionListingEntity auctionListing = auctionListingControllerLocal.retrieveAuctionListingByAuctionListingId(auctionListingId);
            BigDecimal creditWallet = customer.getCreditWallet();
            BigDecimal currentBid = auctionListing.getCurrentBidAmount();

            if (creditWallet.compareTo(bidAmount) < 0) {
                throw new InvalidBidException("You have insufficient money, please try again! ");
            } else if ((bidAmount.subtract(currentBid)).compareTo(increment) < 0) {
                throw new InvalidBidException("Insufficient bid amount entered, please try again!");
            } else {
                customer.setCreditWallet(creditWallet.subtract(bidAmount));
                BigDecimal holdingBalance = customer.getHoldingBalance();
                customer.setHoldingBalance(holdingBalance.add(bidAmount));
                auctionListing.setCurrentBidAmount(bidAmount);
                //checks if my auction listing has previous bids, if yes refund to previous highest bidder
                if (auctionListing.getBids().size() != 0) {
                    BidEntity previousBid = auctionListing.getBids().get(auctionListing.getBids().size() - 1);
                    CustomerEntity previousCustomer = previousBid.getCustomer();

                    if (previousCustomer.equals(customer)) {
                        throw new InvalidBidException("You are the highest bidder currently, please retry later!");
                    } else {
                        previousCustomer.setCreditWallet(previousCustomer.getCreditWallet().add(previousBid.getBidAmount()));
                        previousCustomer.setHoldingBalance(previousCustomer.getHoldingBalance().subtract(previousBid.getBidAmount()));
                        em.merge(previousCustomer);
                    }
                }

                BidEntity bidEntity = new BidEntity();
                bidEntity.setBidAmount(bidAmount);
                bidEntity.setAuctionListing(auctionListing);
                bidEntity.setCustomer(customer);
                customer.getBids().add(bidEntity);
                auctionListing.getBids().add(bidEntity);
                em.persist(bidEntity);
                em.flush();
                em.refresh(bidEntity);
                em.merge(auctionListing);
                em.merge(customer);

                return customer;
            }

        } catch (CustomerNotFoundException | AuctionListingNotFoundException ex) {
            throw new InvalidBidException("Customer or AuctionListing not found");
        }
    }

    @Override
    public List<BidEntity> retrieveBids(Long auctionListingId) {
        Query query = em.createQuery("SELECT a from AuctionListingEntity a WHERE a.auctionListingId = :inAuctionListingId ");
        query.setParameter("inAuctionListingId", auctionListingId);
        AuctionListingEntity auctionListing = (AuctionListingEntity) query.getSingleResult();

        List<BidEntity> bids = auctionListing.getBids();

        for (BidEntity bid : bids) {
            bid.getAuctionListing();
        }

        return bids;

    }

    @Override
    public BidEntity retrieveBidByCurrentBidAmount(BigDecimal bidAmount) {

        Query query = em.createQuery("SELECT b from BidEntity b WHERE b.bidAmount := inBidAmount");
        query.setParameter("inBidAmount", bidAmount);

        return (BidEntity) query.getSingleResult();
    }
    
    @Override
     public BigDecimal bidTable(BigDecimal currentBid) {
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

}
