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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author KERK
 */
@Stateless

public class EJBTimerSessionBean implements EJBTimerSessionBeanLocal, EJBTimerSessionBeanRemote {

    @Resource
    private SessionContext sessionContext;
    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    @EJB
    private AuctionListingControllerLocal auctionListingControllerLocal;

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @EJB(name = "CreditTransactionControllerLocal")
    private CreditTransactionControllerLocal creditTransactionControllerLocal;

    @EJB(name = "BidControllerLocal")
    private BidControllerLocal bidControllerLocal;

    @Override
    public void createTimer(AuctionListingEntity auctionListingEntity) {
        TimerService timerService = sessionContext.getTimerService();
        ScheduleExpression schedule = new ScheduleExpression();
        Calendar date = auctionListingEntity.getStartBidDate();
        schedule.year(date.get(Calendar.YEAR)).month(date.get(Calendar.MONTH) + 1).dayOfMonth(date.get(Calendar.DAY_OF_MONTH)).hour(date.get(Calendar.HOUR_OF_DAY)).minute(date.get(Calendar.MINUTE)).second(date.get(Calendar.SECOND));
        Object[] myObj = {auctionListingEntity.getAuctionListingId(), 0L};
        timerService.createCalendarTimer(schedule, new TimerConfig(myObj, true));

        date = auctionListingEntity.getEndBidDate();
        schedule.year(date.get(Calendar.YEAR)).month(date.get(Calendar.MONTH) + 1).dayOfMonth(date.get(Calendar.DAY_OF_MONTH)).hour(date.get(Calendar.HOUR_OF_DAY)).minute(date.get(Calendar.MINUTE)).second(date.get(Calendar.SECOND));
        Object[] myObj2 = {auctionListingEntity.getAuctionListingId(), 1L};
        timerService.createCalendarTimer(schedule, new TimerConfig(myObj2, true));
    }

    public void createWebTimer(AuctionListingEntity auctionListing, Long customerId, BigDecimal maxAmount, Calendar cal) {
        TimerService timerService = sessionContext.getTimerService();
        ScheduleExpression schedule = new ScheduleExpression();
        schedule.year(cal.get(Calendar.YEAR)).month(cal.get(Calendar.MONTH) + 1).dayOfMonth(cal.get(Calendar.DAY_OF_MONTH)).hour(cal.get(Calendar.HOUR_OF_DAY)).minute(cal.get(Calendar.MINUTE)).second(cal.get(Calendar.SECOND));
        Object[] myObj3 = {auctionListing.getAuctionListingId(), 2L, customerId, maxAmount};
        timerService.createCalendarTimer(schedule, new TimerConfig(myObj3, true));
    }

    @Timeout
    public void handleTimeout(Timer timer) throws CustomerNotFoundException {
        Object[] myObj = (Object[]) timer.getInfo();

        Long id = (Long) myObj[0];
        Long type = (Long) myObj[1];
        Long comparison = 0L;
        Long comparison2 = 1L;

        if (type.equals(comparison)) {
            AuctionListingEntity auctionListing = em.find(AuctionListingEntity.class, id);
            auctionListing.setEnable(true);
        } else if (type.equals(comparison2)) {
            AuctionListingEntity auctionListing = em.find(AuctionListingEntity.class, id);
            auctionListing.setEnable(false);
            auctionListingControllerLocal.closeWinningAuctionListing(id);
        } else {
            Long customerId = (Long) myObj[2];
            BigDecimal maxAmount = (BigDecimal) myObj[3];
            AuctionListingEntity auctionListing = em.find(AuctionListingEntity.class, id);
            CustomerEntity customer = customerControllerLocal.retrieveCustomerByCustomerId(customerId);
            BigDecimal creditWallet = customer.getCreditWallet();
            if (auctionListing.getBids().size() > 0) {
                BigDecimal bidAmount = auctionListing.getBids().get((auctionListing.getBids().size()) - 1).getBidAmount();
                if (maxAmount.compareTo(bidAmount) > 0) {
                    BidEntity bid = new BidEntity();
                    bid.setAuctionListing(auctionListing);
                    BigDecimal bidIncrement = bidControllerLocal.bidTable(bidAmount);
                    bidAmount = bidAmount.add(bidIncrement);
                    customer.setCreditWallet(creditWallet.subtract(bidAmount));
                    BigDecimal holdingBalance = customer.getHoldingBalance();
                    customer.setHoldingBalance(holdingBalance.add(bidAmount));
                    auctionListing.setCurrentBidAmount(bidAmount);
                    bid.setBidAmount(bidAmount);
                    bid.setAuctionListing(auctionListing);
                    bid.setCustomer(customer);
                    customer.getBids().add(bid);
                    em.persist(bid);
                    em.merge(auctionListing);
                }
            } else {
                BidEntity bid = new BidEntity();
                bid.setAuctionListing(auctionListing);
                BigDecimal currentAmount = auctionListing.getCurrentBidAmount();
                BigDecimal bidIncrement = bidControllerLocal.bidTable(currentAmount);
                BigDecimal nextBid = currentAmount.add(bidIncrement);
                if (maxAmount.compareTo(nextBid) > 0) {
                    customer.setCreditWallet(creditWallet.subtract(nextBid));
                    BigDecimal holdingBalance = customer.getHoldingBalance();
                    customer.setHoldingBalance(holdingBalance.add(nextBid));
                    auctionListing.setCurrentBidAmount(nextBid);
                    bid.setBidAmount(nextBid);
                    bid.setAuctionListing(auctionListing);
                    bid.setCustomer(customer);
                    customer.getBids().add(bid);
                    em.persist(bid);
                    em.merge(auctionListing);
                }
            }
        }
    }
}
