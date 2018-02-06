    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListingEntity;
import entity.CustomerEntity;
import java.math.BigDecimal;
import java.util.Calendar;


public interface EJBTimerSessionBeanLocal {
   
    public void createTimer(AuctionListingEntity auctionListingEntity);
    
    public void createWebTimer(AuctionListingEntity auctionListing, Long customerId, BigDecimal maxAmount, Calendar cal);
}
