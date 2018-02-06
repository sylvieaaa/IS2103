/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.CreditTransactionType;

/**
 *
 * @author SYLVIA
 */
@Entity
public class CreditTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditTransactionId;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Calendar transactionDate;
    @Enumerated(EnumType.STRING)
    private CreditTransactionType type;
    @Column(precision = 11, scale = 2)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(nullable = true)
    private CustomerEntity customer;
    @ManyToOne
    @JoinColumn(nullable = true)
    private CreditPackageEntity creditPackage;

    public CreditTransactionEntity() {
    }

    public CreditTransactionEntity(Calendar transactionDate, CreditTransactionType type, BigDecimal amount) {
        this.transactionDate = transactionDate;
        this.type = type;
        this.amount = amount;
    }

    public Long getCreditTransactionId() {
        return creditTransactionId;
    }

    public void setCreditTransactionId(Long creditTransactionId) {
        this.creditTransactionId = creditTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditTransactionId != null ? creditTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the creditTransactionId fields are not set
        if (!(object instanceof CreditTransactionEntity)) {
            return false;
        }
        CreditTransactionEntity other = (CreditTransactionEntity) object;
        if ((this.creditTransactionId == null && other.creditTransactionId != null) || (this.creditTransactionId != null && !this.creditTransactionId.equals(other.creditTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditTransactionEntity[ id=" + creditTransactionId + " ]";
    }

    /**
     * @return the transactionDate
     */
    public Calendar getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return the type
     */
    public CreditTransactionType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(CreditTransactionType type) {
        this.type = type;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * @return the creditPackage
     */
    public CreditPackageEntity getCreditPackage() {
        return creditPackage;
    }

    /**
     * @param creditPackage the creditPackage to set
     */
    public void setCreditPackage(CreditPackageEntity creditPackage) {
        this.creditPackage = creditPackage;
    }

}
