/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

public interface CustomerControllerRemote {

    public CustomerEntity createNewCustomer(CustomerEntity customerEntity) throws CustomerExistException, GeneralException;

    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public CustomerEntity customerLogin(String username, String password) throws InvalidLoginCredentialException;

    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    public void updateCustomer(CustomerEntity customerEntity);
    
    public CustomerEntity mergeCustomer(CustomerEntity customerEntity);
}
