/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListingEntity;
import java.util.List;
import util.exception.AuctionListingNotFoundException;

public interface AuctionListingControllerRemote {

    public AuctionListingEntity createNewAuctionListing(AuctionListingEntity auctionListingEntity);

    public AuctionListingEntity retrieveAuctionListingByAuctionListingId(Long auctionListingId) throws AuctionListingNotFoundException;

    public List<AuctionListingEntity> retrieveActiveAuctionListings();

    // public AuctionListingEntity updateAuctionListing(Long auctionListingId) throws AuctionListingNotFoundException;
    public List<AuctionListingEntity> retrieveAllListing();

    public void updateAuctionListing(AuctionListingEntity auctionListing);

    public void deleteAuctionListing(AuctionListingEntity auctionListing);

    public List<AuctionListingEntity> retrieveAllListingBelowReservePrice();

    public List<AuctionListingEntity> retrieveWonAuctionListings(Long customerId) throws AuctionListingNotFoundException;

    public void closeWinningAuctionListing (Long auctionListingId);
    
    public void manualIntervention(Long auctionListingId);
}
