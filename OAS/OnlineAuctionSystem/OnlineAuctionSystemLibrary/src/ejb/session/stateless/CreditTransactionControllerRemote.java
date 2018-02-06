/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListingEntity;
import entity.CreditTransactionEntity;
import entity.CustomerEntity;
import java.math.BigDecimal;
import java.util.List;
import util.exception.CreditPackageNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidTransactionException;

public interface CreditTransactionControllerRemote {

    public CustomerEntity createNewDebitTransaction(CreditTransactionEntity creditTransactionEntity, Long customerId, Long packageId, BigDecimal amount) throws InvalidTransactionException;

    public List<CreditTransactionEntity> retrieveTransaction(Long customerId);

    public void createNewCreditTransaction( Long customerId, BigDecimal winningBid);

    public AuctionListingEntity refundCreditToCustomer(Long auctionListingId) throws InvalidTransactionException;
}
