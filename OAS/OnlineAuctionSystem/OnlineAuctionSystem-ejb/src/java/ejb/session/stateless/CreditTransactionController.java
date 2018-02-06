/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListingEntity;
import entity.BidEntity;
import entity.CreditPackageEntity;
import entity.CreditTransactionEntity;
import entity.CustomerEntity;
import java.math.BigDecimal;
import java.util.Calendar;
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
import util.enumeration.CreditTransactionType;
import util.exception.AuctionListingNotFoundException;
import util.exception.CreditPackageNotFoundException;
import util.exception.CreditTransactionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidTransactionException;

/**
 *
 * @author SYLVIA
 */
@Stateless
@Local(CreditTransactionControllerLocal.class)
@Remote(CreditTransactionControllerRemote.class)
public class CreditTransactionController implements CreditTransactionControllerRemote, CreditTransactionControllerLocal {

    @EJB(name = "AuctionListingControllerLocal")
    private AuctionListingControllerLocal auctionListingControllerLocal;

    @EJB(name = "CreditPackageControllerLocal")
    private CreditPackageControllerLocal creditPackageControllerLocal;

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    @Override
    public CustomerEntity createNewDebitTransaction(CreditTransactionEntity creditTransactionEntity, Long customerId, Long packageId, BigDecimal amount) throws InvalidTransactionException {
        try {
            creditTransactionEntity = em.merge(creditTransactionEntity);
            CustomerEntity customer = customerControllerLocal.retrieveCustomerByCustomerId(customerId);
            CreditPackageEntity cPackage = creditPackageControllerLocal.retrieveCreditPackageByCreditPackageId(packageId);
            BigDecimal balance = cPackage.getCreditBalance();
            BigDecimal current = customer.getCreditWallet();
            customer.setCreditWallet(current.add(balance.multiply(amount)));

            Calendar cal = Calendar.getInstance();
            creditTransactionEntity.setTransactionDate(cal);
            creditTransactionEntity.setType(CreditTransactionType.DEBIT);
            creditTransactionEntity.setAmount(cPackage.getPackagePrice());
            creditTransactionEntity.setCustomer(customer);
            creditTransactionEntity.setCustomer(customer);
            creditTransactionEntity.setCreditPackage(cPackage);

            customer.getTransactions().add(creditTransactionEntity);
            cPackage.getTransactions().add(creditTransactionEntity);

            em.persist(creditTransactionEntity);
            em.flush();
            em.merge(customer);
            em.refresh(creditTransactionEntity);

            return customer;
        } catch (CustomerNotFoundException | CreditPackageNotFoundException ex) {

            throw new InvalidTransactionException("Invalid Tranaction, Customer does not exist!");
        }

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<CreditTransactionEntity> retrieveTransaction(Long customerId) {

        Query query = em.createQuery("SELECT c FROM CreditTransactionEntity c JOIN c.customer b WHERE b.customerId = :inCustomerId ");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();

    }

    @Override
    public void createNewCreditTransaction(Long customerId, BigDecimal winningBid) {
        try {
            CreditTransactionEntity creditTransaction = new CreditTransactionEntity();
            creditTransaction = em.merge(creditTransaction);
            CustomerEntity customer = customerControllerLocal.retrieveCustomerByCustomerId(customerId);
            Calendar cal = Calendar.getInstance();
            creditTransaction.setTransactionDate(cal);
            creditTransaction.setType(CreditTransactionType.CREDIT);
            creditTransaction.setAmount(winningBid);
            creditTransaction.setCustomer(customer);
            customer.getTransactions().add(creditTransaction);
            BigDecimal holdingBalance = customer.getHoldingBalance();
            customer.setHoldingBalance(holdingBalance.subtract(winningBid));
            //customer.getHoldingBalance().subtract(winningBid);
         //   System.out.println("testing in credit trans control" + customer.getHoldingBalance());

            em.persist(creditTransaction);
            em.flush();
            em.merge(customer);
            em.refresh(creditTransaction);

        } catch (CustomerNotFoundException ex) {
            System.out.println("Customer does not exist, please try again! ");
        }

    }

    public AuctionListingEntity refundCreditToCustomer(Long auctionListingId) throws InvalidTransactionException {
        try {
            AuctionListingEntity listing = auctionListingControllerLocal.retrieveAuctionListingByAuctionListingId(auctionListingId);
            Query query = em.createQuery("SELECT b FROM BidEntity b WHERE b.auctionListing.auctionListingId = :inAuctionListingId ORDER BY b.bidAmount DESC");
            query.setParameter("inAuctionListingId", auctionListingId);
            List<BidEntity> bids = query.getResultList();
            if (bids.size() != 0) {
                BidEntity bid = bids.get(0);
                CustomerEntity customer = bid.getCustomer();
                BigDecimal amountRefunded = bid.getBidAmount();
                customer.setHoldingBalance(customer.getHoldingBalance().subtract(amountRefunded));
                customer.setCreditWallet(customer.getCreditWallet().add(amountRefunded));
                em.refresh(customer);
                return listing;
            } else {
                throw new InvalidTransactionException("No customer to refund back to!");
            }

        } catch (AuctionListingNotFoundException ex) {
            throw new InvalidTransactionException("Auction Listing does not exist!");
        }

    }

}
