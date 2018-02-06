/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasauctionclient;

import ejb.session.stateless.AddressControllerRemote;
import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import javax.ejb.EJB;

/**
 *
 * @author SYLVIA
 */
public class Main {

    @EJB
    private static CustomerControllerRemote customerControllerRemote;

    @EJB
    private static CreditTransactionControllerRemote creditTransactionControllerRemote;

    @EJB
    private static CreditPackageControllerRemote creditPackageControllerRemote;

    @EJB
    private static BidControllerRemote bidControllerRemote;

    @EJB
    private static AuctionListingControllerRemote auctionListingControllerRemote;

    @EJB
    private static AddressControllerRemote addressControllerRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerControllerRemote, creditTransactionControllerRemote, creditPackageControllerRemote, bidControllerRemote, auctionListingControllerRemote, addressControllerRemote);
        mainApp.run();
    }
    
}
