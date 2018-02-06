/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackageEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreditPackageNotFoundException;

/**
 *
 * @author SYLVIA
 */
@Stateless
@Local(CreditPackageControllerLocal.class)
@Remote(CreditPackageControllerRemote.class)
public class CreditPackageController implements CreditPackageControllerRemote, CreditPackageControllerLocal {

    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    @Override
    public CreditPackageEntity createNewCreditPackage(CreditPackageEntity creditPackageEntity){
        em.persist(creditPackageEntity);
        em.flush();
        em.refresh(creditPackageEntity);
        return creditPackageEntity;
    }

    
    @Override
    public List<CreditPackageEntity> retrieveAllActivePackage(){
        
         Query query = em.createQuery("SELECT s FROM CreditPackageEntity s WHERE s.enable = 1");
        
        return query.getResultList();
    }
    
    @Override
    public CreditPackageEntity retrievePackage(Long packageId)throws CreditPackageNotFoundException{
        Query query = em.createQuery("SELECT p FROM CreditPackageEntity p WHERE p.creditPackageId = :inPackageId");
        query.setParameter("inPackageId", packageId);
        
        
        try {
            return (CreditPackageEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CreditPackageNotFoundException("Credit Package " + packageId + " does not exist!");
        }
                
    }
    
     @Override
    public CreditPackageEntity retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException
    {
        CreditPackageEntity creditPackageEntity = em.find(CreditPackageEntity.class, creditPackageId);
        
        if(creditPackageEntity != null)
        {
            creditPackageEntity.getTransactions().size();
            return creditPackageEntity;
        }
        else
        {
            throw new CreditPackageNotFoundException("Credit Package ID " + creditPackageId + " does not exist!");
        }
    }
    
    @Override
    public void updateCreditPackage(CreditPackageEntity creditPackage)
    {
        em.merge(creditPackage);
    }
    
    @Override
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException
    {
        CreditPackageEntity creditPackageToRemove = retrieveCreditPackageByCreditPackageId(creditPackageId);
        em.remove(creditPackageToRemove);
    }
    
    @Override
    public List<CreditPackageEntity> retrieveAllCreditPackage()
    {
        Query query = em.createQuery("SELECT c FROM CreditPackageEntity c");
        
        return query.getResultList();
    }
    
    
}
