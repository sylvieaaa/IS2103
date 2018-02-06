/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackageEntity;
import java.util.List;
import util.exception.CreditPackageNotFoundException;

public interface CreditPackageControllerRemote {

    public CreditPackageEntity createNewCreditPackage(CreditPackageEntity creditPackageEntity);

    public List<CreditPackageEntity> retrieveAllActivePackage();

    public CreditPackageEntity retrievePackage(Long packageId) throws CreditPackageNotFoundException;

    public CreditPackageEntity retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException;

    public void updateCreditPackage(CreditPackageEntity creditPackage);

    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException;

    public List<CreditPackageEntity> retrieveAllCreditPackage();
}
