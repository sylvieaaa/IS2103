    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.AuctionListingEntity;
import entity.BidEntity;
import entity.CreditPackageEntity;
import entity.CreditTransactionEntity;
import entity.CustomerEntity;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
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
import util.exception.CreditPackageNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author KERK
 */
@Stateless
@Local(AuctionListingControllerLocal.class)
@Remote(AuctionListingControllerRemote.class)
public class AuctionListingController implements AuctionListingControllerRemote, AuctionListingControllerLocal {

    @EJB
    private EJBTimerSessionBeanLocal ejbTimerSessionBeanLocal;

    @EJB(name = "CreditTransactionControllerLocal")
    private CreditTransactionControllerLocal creditTransactionControllerLocal;

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    public AuctionListingEntity createNewAuctionListing(AuctionListingEntity auctionListingEntity) {
        System.out.println(auctionListingEntity);
        em.persist(auctionListingEntity);
        em.flush();
        em.refresh(auctionListingEntity);
        ejbTimerSessionBeanLocal.createTimer(auctionListingEntity);

        return auctionListingEntity;
    }

    @Override
    public AuctionListingEntity retrieveAuctionListingByAuctionListingId(Long auctionListingId) throws AuctionListingNotFoundException {
        AuctionListingEntity auctionListingEntity = em.find(AuctionListingEntity.class, auctionListingId);

        if (auctionListingEntity != null) {
            auctionListingEntity.getBids().size();
            return auctionListingEntity;
        } else {
            throw new AuctionListingNotFoundException("Auction Listing ID " + auctionListingId + " does not exist!");
        }
    }

    @Override
    public List<AuctionListingEntity> retrieveActiveAuctionListings() {
        Query query = em.createQuery("SELECT a from AuctionListingEntity a WHERE a.enable = 1");
        return query.getResultList();

    }

    @Override
    public List<AuctionListingEntity> retrieveAllListing() {
        Query query = em.createQuery("SELECT a FROM AuctionListingEntity a");

        return query.getResultList();
    }

    @Override
    public void updateAuctionListing(AuctionListingEntity auctionListing) {
        auctionListing = em.merge(auctionListing);
        ejbTimerSessionBeanLocal.createTimer(auctionListing);
    }

    @Override
    public void deleteAuctionListing(AuctionListingEntity auctionListing) {
        auctionListing = em.merge(auctionListing);
        if (auctionListing.getBids().size() != 0) {
            auctionListing.setEnable(Boolean.FALSE);
        } else {
            em.remove(auctionListing);
        }
    }

    @Override
    public List<AuctionListingEntity> retrieveAllListingBelowReservePrice() {
        Query query = em.createQuery("SELECT a FROM AuctionListingEntity a WHERE a.reservedPrice >= a.currentBidAmount AND a.enable = 0 AND a.customer is NULL AND SIZE(a.bids) >0");
        return query.getResultList();
    }

    @Override
    public List<AuctionListingEntity> retrieveWonAuctionListings(Long customerId) throws AuctionListingNotFoundException {

        Query query = em.createQuery("SELECT a FROM AuctionListingEntity a WHERE a.customer.customerId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        List<AuctionListingEntity> wonListings = query.getResultList();
        if (wonListings.size() == 0) {
            throw new AuctionListingNotFoundException("No Won Auction Listing Found!");
        } else {
            return wonListings;
        }

    }

    @Override
    public void closeWinningAuctionListing(Long auctionListingId) {

        AuctionListingEntity auctionListing = em.find(AuctionListingEntity.class, auctionListingId);
        Query query = em.createQuery("SELECT b FROM BidEntity b WHERE b.auctionListing.auctionListingId = :inAuctionListingId ORDER BY b.bidAmount DESC");
        query.setParameter("inAuctionListingId", auctionListingId);
        List<BidEntity> bidEntities = query.getResultList();
        if (bidEntities.size() > 0) {
            BidEntity bid = bidEntities.get(0);
            if (bid.getBidAmount().compareTo(auctionListing.getReservedPrice()) > 0) {
                BigDecimal winningBidAmount = bid.getBidAmount();
                CustomerEntity customer = bid.getCustomer();
                creditTransactionControllerLocal.createNewCreditTransaction(customer.getCustomerId(), winningBidAmount);
                auctionListing.setCustomer(customer);
                customer.getAuctions().add(auctionListing);
                bid.setAuctionListing(auctionListing);
                auctionListing.getBids().add(bid);
                em.merge(customer);
            }
        }

    }

    @Override
    public void manualIntervention(Long auctionListingId) {
        AuctionListingEntity auctionListing = em.find(AuctionListingEntity.class, auctionListingId);
        Query query = em.createQuery("SELECT b FROM BidEntity b WHERE b.auctionListing.auctionListingId = :inAuctionListingId ORDER BY b.bidAmount DESC");
        query.setParameter("inAuctionListingId", auctionListingId);
        List<BidEntity> bidEntities = query.getResultList();
        BidEntity bid = bidEntities.get(0);
        BigDecimal winningBidAmount = bid.getBidAmount();
        CustomerEntity customer = bid.getCustomer();
        CreditTransactionEntity creditTransaction = new CreditTransactionEntity();
        creditTransactionControllerLocal.createNewCreditTransaction(customer.getCustomerId(), winningBidAmount);
        auctionListing.setCustomer(customer);
        customer.getAuctions().add(auctionListing);
        bid.setAuctionListing(auctionListing);
        auctionListing.getBids().add(bid);
    }
}
