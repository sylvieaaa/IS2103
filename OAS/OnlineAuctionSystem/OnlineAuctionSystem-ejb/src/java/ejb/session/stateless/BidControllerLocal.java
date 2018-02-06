/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BidEntity;
import entity.CustomerEntity;
import java.math.BigDecimal;
import java.util.List;
import util.exception.InvalidBidException;

public interface BidControllerLocal {

   public CustomerEntity createNewBid(Long customerId, Long auctionListingId, BigDecimal increment, BigDecimal bidAmount) throws InvalidBidException;

    public List<BidEntity> retrieveBids(Long auctionListingId);

    public BidEntity retrieveBidByCurrentBidAmount (BigDecimal bidAmount);
    
    public BigDecimal bidTable(BigDecimal currentBid);
}
