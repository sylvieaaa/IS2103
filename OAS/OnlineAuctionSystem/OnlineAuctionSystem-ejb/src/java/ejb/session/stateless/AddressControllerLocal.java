/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.CustomerEntity;
import java.util.List;
import util.exception.AddressNotFoundException;
import util.exception.CustomerNotFoundException;

public interface AddressControllerLocal {

    public AddressEntity createNewAddress(AddressEntity addressEntity, Long customerId) throws CustomerNotFoundException;

    public List<AddressEntity> retrieveAllAddresses(Long customerId)throws AddressNotFoundException;

    public AddressEntity retrieveAddress(Long customerId, Long addressId);

    public void updateAddress(AddressEntity addressEntity);

    public void deleteAddress(AddressEntity addressEntity);

    public AddressEntity mergeAddress(AddressEntity addressEntity);
    
    public void setAddressToListing(Long addressId, Long auctionListingId);
}
