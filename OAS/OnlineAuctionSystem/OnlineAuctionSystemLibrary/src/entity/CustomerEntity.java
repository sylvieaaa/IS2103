/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author SYLVIA
 */
@Entity


public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Column(length = 32, nullable = false)
    private String username;
    @Column(length = 32, nullable = false)
    private String password;
    @Column(length = 32, nullable = false, unique = true)
    private String contactNumber;
    @Column(precision = 11, scale = 2)
    private BigDecimal creditWallet;
    @Column(precision = 11, scale = 2)
    private BigDecimal holdingBalance;
    
    private Boolean premiumAccount;
    @OneToMany
    private List<AddressEntity> addresses;
    @OneToMany (mappedBy="customer")
    private List<AuctionListingEntity> auctions;
    @OneToMany (mappedBy= "customer")
    private List<BidEntity> bids;
    @OneToMany (mappedBy= "customer")
    private List<CreditTransactionEntity> transactions;
    
    
    
    public CustomerEntity() {
        this.auctions = new ArrayList<>();
        this.bids = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public CustomerEntity(String firstName, String lastName, String username, String password, String contactNumber, BigDecimal creditWallet, BigDecimal bidLedgerBalance, Boolean premiumAccount) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.contactNumber = contactNumber;
        this.creditWallet = creditWallet;
        this.holdingBalance = bidLedgerBalance;
        this.premiumAccount = premiumAccount;
    }
    

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + customerId + " ]";
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
  
    /**
     * @return the contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * @param contactNumber the contactNumber to set
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * @return the creditWallet
     */
    public BigDecimal getCreditWallet() {
        return creditWallet;
    }

    /**
     * @param creditWallet the creditWallet to set
     */
    public void setCreditWallet(BigDecimal creditWallet) {
        this.creditWallet = creditWallet;
    }

    /**
     * @return the premiumAccount
     */
    public Boolean getPremiumAccount() {
        return premiumAccount;
    }

    /**
     * @param premiumAccount the premiumAccount to set
     */
    public void setPremiumAccount(Boolean premiumAccount) {
        this.premiumAccount = premiumAccount;
    }

    /**
     * @return the addresses
     */
    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return the auctions
     */
    public List<AuctionListingEntity> getAuctions() {
        return auctions;
    }

    /**
     * @param auctions the auctions to set
     */
    public void setAuctions(List<AuctionListingEntity> auctions) {
        this.auctions = auctions;
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
     * @return the transactions
     */
    public List<CreditTransactionEntity> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<CreditTransactionEntity> transactions) {
        this.transactions = transactions;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the holdingBalance
     */
    public BigDecimal getHoldingBalance() {
        return holdingBalance;
    }

    /**
     * @param holdingBalance the holdingBalance to set
     */
    public void setHoldingBalance(BigDecimal holdingBalance) {
        this.holdingBalance = holdingBalance;
    }
    
}
