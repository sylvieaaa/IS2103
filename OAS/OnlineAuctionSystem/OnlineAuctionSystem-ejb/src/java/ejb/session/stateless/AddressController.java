/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.AuctionListingEntity;
import entity.CustomerEntity;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import util.exception.AddressNotFoundException;
import util.exception.AuctionListingNotFoundException;

import util.exception.CustomerNotFoundException;


/**
 *
 * @author SYLVIA
 */
@Stateless
@Local(AddressControllerLocal.class)
@Remote(AddressControllerRemote.class)
public class AddressController implements AddressControllerRemote, AddressControllerLocal {

    @EJB(name = "AuctionListingControllerLocal")
    private AuctionListingControllerLocal auctionListingControllerLocal;

    @EJB
    private CustomerControllerLocal customerControllerLocal;

    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    @Override
    public AddressEntity createNewAddress(AddressEntity addressEntity, Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = customerControllerLocal.retrieveCustomerByCustomerId(customerId);
        em.persist(addressEntity);
        em.flush();
        
        customerEntity.getAddresses().add(addressEntity);
        em.refresh(addressEntity);
        addressEntity.getListings().size();
        return addressEntity;
    }

    @Override
    public List<AddressEntity> retrieveAllAddresses(Long customerId)throws AddressNotFoundException{
    
        Query query = em.createQuery("SELECT a FROM CustomerEntity c JOIN c.addresses a WHERE c.customerId = :inCustomerId ");
        query.setParameter("inCustomerId", customerId);
        List<AddressEntity> addresses = query.getResultList();
        
        if (addresses.size() ==0 ){
            throw new AddressNotFoundException("Address does not exist, please create address!");
        } else {
        return addresses;
        }
       
    }
    
    @Override
    public AddressEntity retrieveAddress(Long customerId, Long addressId) {
     
        Query query= em.createQuery("SELECT DISTINCT a FROM CustomerEntity c JOIN c.addresses b JOIN AddressEntity a WHERE c.customerId = :inCustomerId AND a.addressId = :inAddressId");
        query.setParameter("inCustomerId", customerId);
        query.setParameter("inAddressId", addressId);
        AddressEntity addressEntity = (AddressEntity) query.getSingleResult();
        addressEntity.getListings().size();
        
        return addressEntity ;        
        
    }
    
    @Override
    public void updateAddress(AddressEntity addressEntity){
        em.merge(addressEntity);
    }
    
    
    @Override
    public void deleteAddress(AddressEntity addressEntity){
        addressEntity= em.merge(addressEntity);
        em.remove(addressEntity);
    }
    
    
    @Override
    public AddressEntity mergeAddress (AddressEntity addressEntity){
        addressEntity = em.merge(addressEntity);
        addressEntity.getListings().size(); 
        return addressEntity;
    }
    
    @Override
    public void setAddressToListing(Long addressId, Long auctionListingId)
    {
        Query query = em.createQuery("SELECT a from AddressEntity a WHERE a.addressId =:inAddressId");
        query.setParameter("inAddressId", addressId);
        AddressEntity address = (AddressEntity) query.getSingleResult();
        query = em.createQuery("SELECT b from AuctionListingEntity b WHERE b.auctionListingId =:inAuctionListingId");
        query.setParameter("inAuctionListingId", auctionListingId);
        AuctionListingEntity listing = (AuctionListingEntity) query.getSingleResult();
          
        address.getListings().add(listing);
        listing.setAddress(address);
        em.merge(address);
        em.merge(listing);       
        

    }    

}
