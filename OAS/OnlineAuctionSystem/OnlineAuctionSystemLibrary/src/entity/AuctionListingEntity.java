/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author SYLVIA
 */
@Entity
public class AuctionListingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionListingId;
    @Column(precision = 11, scale = 2)
    private BigDecimal reservedPrice;
    @Column(precision = 11, scale = 2)
    private BigDecimal startBidAmount;
    @Column(precision = 11, scale = 2)
    private BigDecimal currentBidAmount;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar startBidDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar endBidDate;
    @Column(length = 32, nullable = false)
    private String name;
    @Column(length = 32, nullable = false)
    private String description;
    private Boolean enable;
    @ManyToOne
    @JoinColumn (nullable= true)
    private CustomerEntity customer;
    @OneToMany(mappedBy="auctionListing")
    private List<BidEntity> bids;
    @ManyToOne
    @JoinColumn (nullable = true)
    private AddressEntity address;
    
    public AuctionListingEntity() {
        this.bids = new ArrayList<>();
        
    }

    public AuctionListingEntity(BigDecimal reservedPrice, BigDecimal startBidAmount, BigDecimal currentBidAmount, Calendar startBidDate, Calendar endBidDate, String name, String description, Boolean enable) {
        
        this.reservedPrice = reservedPrice;
        this.startBidAmount = startBidAmount;
        this.currentBidAmount = currentBidAmount;
        this.startBidDate = startBidDate;
        this.endBidDate = endBidDate;
        this.name = name;
        this.description = description;
        this.enable = enable;
    }
    

    public Long getAuctionListingId() {
        return auctionListingId;
    }

    public void setAuctionListingId(Long auctionListingId) {
        this.auctionListingId = auctionListingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (auctionListingId != null ? auctionListingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the auctionListingId fields are not set
        if (!(object instanceof AuctionListingEntity)) {
            return false;
        }
        AuctionListingEntity other = (AuctionListingEntity) object;
        if ((this.auctionListingId == null && other.auctionListingId != null) || (this.auctionListingId != null && !this.auctionListingId.equals(other.auctionListingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AuctionListingEntity[ id=" + auctionListingId + " ]";
    }

    /**
     * @return the reservedPrice
     */
    public BigDecimal getReservedPrice() {
        return reservedPrice;
    }

    /**
     * @param reservedPrice the reservedPrice to set
     */
    public void setReservedPrice(BigDecimal reservedPrice) {
        this.reservedPrice = reservedPrice;
    }

    /**
     * @return the startBidAmount
     */
    public BigDecimal getStartBidAmount() {
        return startBidAmount;
    }

    /**
     * @param startBidAmount the startBidAmount to set
     */
    public void setStartBidAmount(BigDecimal startBidAmount) {
        this.startBidAmount = startBidAmount;
    }

    /**
     * @return the currentBidAmount
     */
    public BigDecimal getCurrentBidAmount() {
        return currentBidAmount;
    }

    /**
     * @param currentBidAmount the currentBidAmount to set
     */
    public void setCurrentBidAmount(BigDecimal currentBidAmount) {
        this.currentBidAmount = currentBidAmount;
    }

    /**
     * @return the startBidDate
     */
    public Calendar getStartBidDate() {
        return startBidDate;
    }

    /**
     * @param startBidDate the startBidDate to set
     */
    public void setStartBidDate(Calendar startBidDate) {
        this.startBidDate = startBidDate;
    }

    /**
     * @return the endBidDate
     */
    public Calendar getEndBidDate() {
        return endBidDate;
    }

    /**
     * @param endBidDate the endBidDate to set
     */
    public void setEndBidDate(Calendar endBidDate) {
        this.endBidDate = endBidDate;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the enable
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * @param enable the enable to set
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    /**
     * @return the customer
     */
    public CustomerEntity getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    /**
     * @return the bids
     */
    public List<BidEntity> getBids() {
        return bids;
    }

    /**
     * @param bids the bids to set
     */
    public void setBids(List<BidEntity> bids) {
        this.bids = bids;
    }

    /**
     * @return the address
     */
    public AddressEntity getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(AddressEntity address) {
        this.address = address;
    }
    
}
