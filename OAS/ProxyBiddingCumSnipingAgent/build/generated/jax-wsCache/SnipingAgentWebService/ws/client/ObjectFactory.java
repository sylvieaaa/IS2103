
package ws.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ws.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AuctionListingNotFoundException_QNAME = new QName("http://ws.session.ejb/", "AuctionListingNotFoundException");
    private final static QName _CustomerNotFoundException_QNAME = new QName("http://ws.session.ejb/", "CustomerNotFoundException");
    private final static QName _CustomerNotPremiumException_QNAME = new QName("http://ws.session.ejb/", "CustomerNotPremiumException");
    private final static QName _InvalidLoginCredentialException_QNAME = new QName("http://ws.session.ejb/", "InvalidLoginCredentialException");
    private final static QName _CreateNewSnipingBid_QNAME = new QName("http://ws.session.ejb/", "createNewSnipingBid");
    private final static QName _CreateNewSnipingBidResponse_QNAME = new QName("http://ws.session.ejb/", "createNewSnipingBidResponse");
    private final static QName _PremiumRegistration_QNAME = new QName("http://ws.session.ejb/", "premiumRegistration");
    private final static QName _PremiumRegistrationResponse_QNAME = new QName("http://ws.session.ejb/", "premiumRegistrationResponse");
    private final static QName _RemoteLogin_QNAME = new QName("http://ws.session.ejb/", "remoteLogin");
    private final static QName _RemoteLoginResponse_QNAME = new QName("http://ws.session.ejb/", "remoteLoginResponse");
    private final static QName _RetrieveAuctionListing_QNAME = new QName("http://ws.session.ejb/", "retrieveAuctionListing");
    private final static QName _RetrieveAuctionListingResponse_QNAME = new QName("http://ws.session.ejb/", "retrieveAuctionListingResponse");
    private final static QName _RetrieveWonListing_QNAME = new QName("http://ws.session.ejb/", "retrieveWonListing");
    private final static QName _RetrieveWonListingResponse_QNAME = new QName("http://ws.session.ejb/", "retrieveWonListingResponse");
    private final static QName _ViewAuctionListings_QNAME = new QName("http://ws.session.ejb/", "viewAuctionListings");
    private final static QName _ViewAuctionListingsResponse_QNAME = new QName("http://ws.session.ejb/", "viewAuctionListingsResponse");
    private final static QName _ViewCreditBalance_QNAME = new QName("http://ws.session.ejb/", "viewCreditBalance");
    private final static QName _ViewCreditBalanceResponse_QNAME = new QName("http://ws.session.ejb/", "viewCreditBalanceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ws.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AuctionListingNotFoundException }
     * 
     */
    public AuctionListingNotFoundException createAuctionListingNotFoundException() {
        return new AuctionListingNotFoundException();
    }

    /**
     * Create an instance of {@link CustomerNotFoundException }
     * 
     */
    public CustomerNotFoundException createCustomerNotFoundException() {
        return new CustomerNotFoundException();
    }

    /**
     * Create an instance of {@link CustomerNotPremiumException }
     * 
     */
    public CustomerNotPremiumException createCustomerNotPremiumException() {
        return new CustomerNotPremiumException();
    }

    /**
     * Create an instance of {@link InvalidLoginCredentialException }
     * 
     */
    public InvalidLoginCredentialException createInvalidLoginCredentialException() {
        return new InvalidLoginCredentialException();
    }

    /**
     * Create an instance of {@link CreateNewSnipingBid }
     * 
     */
    public CreateNewSnipingBid createCreateNewSnipingBid() {
        return new CreateNewSnipingBid();
    }

    /**
     * Create an instance of {@link CreateNewSnipingBidResponse }
     * 
     */
    public CreateNewSnipingBidResponse createCreateNewSnipingBidResponse() {
        return new CreateNewSnipingBidResponse();
    }

    /**
     * Create an instance of {@link PremiumRegistration }
     * 
     */
    public PremiumRegistration createPremiumRegistration() {
        return new PremiumRegistration();
    }

    /**
     * Create an instance of {@link PremiumRegistrationResponse }
     * 
     */
    public PremiumRegistrationResponse createPremiumRegistrationResponse() {
        return new PremiumRegistrationResponse();
    }

    /**
     * Create an instance of {@link RemoteLogin }
     * 
     */
    public RemoteLogin createRemoteLogin() {
        return new RemoteLogin();
    }

    /**
     * Create an instance of {@link RemoteLoginResponse }
     * 
     */
    public RemoteLoginResponse createRemoteLoginResponse() {
        return new RemoteLoginResponse();
    }

    /**
     * Create an instance of {@link RetrieveAuctionListing }
     * 
     */
    public RetrieveAuctionListing createRetrieveAuctionListing() {
        return new RetrieveAuctionListing();
    }

    /**
     * Create an instance of {@link RetrieveAuctionListingResponse }
     * 
     */
    public RetrieveAuctionListingResponse createRetrieveAuctionListingResponse() {
        return new RetrieveAuctionListingResponse();
    }

    /**
     * Create an instance of {@link RetrieveWonListing }
     * 
     */
    public RetrieveWonListing createRetrieveWonListing() {
        return new RetrieveWonListing();
    }

    /**
     * Create an instance of {@link RetrieveWonListingResponse }
     * 
     */
    public RetrieveWonListingResponse createRetrieveWonListingResponse() {
        return new RetrieveWonListingResponse();
    }

    /**
     * Create an instance of {@link ViewAuctionListings }
     * 
     */
    public ViewAuctionListings createViewAuctionListings() {
        return new ViewAuctionListings();
    }

    /**
     * Create an instance of {@link ViewAuctionListingsResponse }
     * 
     */
    public ViewAuctionListingsResponse createViewAuctionListingsResponse() {
        return new ViewAuctionListingsResponse();
    }

    /**
     * Create an instance of {@link ViewCreditBalance }
     * 
     */
    public ViewCreditBalance createViewCreditBalance() {
        return new ViewCreditBalance();
    }

    /**
     * Create an instance of {@link ViewCreditBalanceResponse }
     * 
     */
    public ViewCreditBalanceResponse createViewCreditBalanceResponse() {
        return new ViewCreditBalanceResponse();
    }

    /**
     * Create an instance of {@link AuctionListingEntity }
     * 
     */
    public AuctionListingEntity createAuctionListingEntity() {
        return new AuctionListingEntity();
    }

    /**
     * Create an instance of {@link AddressEntity }
     * 
     */
    public AddressEntity createAddressEntity() {
        return new AddressEntity();
    }

    /**
     * Create an instance of {@link BidEntity }
     * 
     */
    public BidEntity createBidEntity() {
        return new BidEntity();
    }

    /**
     * Create an instance of {@link CustomerEntity }
     * 
     */
    public CustomerEntity createCustomerEntity() {
        return new CustomerEntity();
    }

    /**
     * Create an instance of {@link CreditTransactionEntity }
     * 
     */
    public CreditTransactionEntity createCreditTransactionEntity() {
        return new CreditTransactionEntity();
    }

    /**
     * Create an instance of {@link CreditPackageEntity }
     * 
     */
    public CreditPackageEntity createCreditPackageEntity() {
        return new CreditPackageEntity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuctionListingNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "AuctionListingNotFoundException")
    public JAXBElement<AuctionListingNotFoundException> createAuctionListingNotFoundException(AuctionListingNotFoundException value) {
        return new JAXBElement<AuctionListingNotFoundException>(_AuctionListingNotFoundException_QNAME, AuctionListingNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "CustomerNotFoundException")
    public JAXBElement<CustomerNotFoundException> createCustomerNotFoundException(CustomerNotFoundException value) {
        return new JAXBElement<CustomerNotFoundException>(_CustomerNotFoundException_QNAME, CustomerNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerNotPremiumException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "CustomerNotPremiumException")
    public JAXBElement<CustomerNotPremiumException> createCustomerNotPremiumException(CustomerNotPremiumException value) {
        return new JAXBElement<CustomerNotPremiumException>(_CustomerNotPremiumException_QNAME, CustomerNotPremiumException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidLoginCredentialException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "InvalidLoginCredentialException")
    public JAXBElement<InvalidLoginCredentialException> createInvalidLoginCredentialException(InvalidLoginCredentialException value) {
        return new JAXBElement<InvalidLoginCredentialException>(_InvalidLoginCredentialException_QNAME, InvalidLoginCredentialException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewSnipingBid }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "createNewSnipingBid")
    public JAXBElement<CreateNewSnipingBid> createCreateNewSnipingBid(CreateNewSnipingBid value) {
        return new JAXBElement<CreateNewSnipingBid>(_CreateNewSnipingBid_QNAME, CreateNewSnipingBid.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewSnipingBidResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "createNewSnipingBidResponse")
    public JAXBElement<CreateNewSnipingBidResponse> createCreateNewSnipingBidResponse(CreateNewSnipingBidResponse value) {
        return new JAXBElement<CreateNewSnipingBidResponse>(_CreateNewSnipingBidResponse_QNAME, CreateNewSnipingBidResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PremiumRegistration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "premiumRegistration")
    public JAXBElement<PremiumRegistration> createPremiumRegistration(PremiumRegistration value) {
        return new JAXBElement<PremiumRegistration>(_PremiumRegistration_QNAME, PremiumRegistration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PremiumRegistrationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "premiumRegistrationResponse")
    public JAXBElement<PremiumRegistrationResponse> createPremiumRegistrationResponse(PremiumRegistrationResponse value) {
        return new JAXBElement<PremiumRegistrationResponse>(_PremiumRegistrationResponse_QNAME, PremiumRegistrationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "remoteLogin")
    public JAXBElement<RemoteLogin> createRemoteLogin(RemoteLogin value) {
        return new JAXBElement<RemoteLogin>(_RemoteLogin_QNAME, RemoteLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "remoteLoginResponse")
    public JAXBElement<RemoteLoginResponse> createRemoteLoginResponse(RemoteLoginResponse value) {
        return new JAXBElement<RemoteLoginResponse>(_RemoteLoginResponse_QNAME, RemoteLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveAuctionListing }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "retrieveAuctionListing")
    public JAXBElement<RetrieveAuctionListing> createRetrieveAuctionListing(RetrieveAuctionListing value) {
        return new JAXBElement<RetrieveAuctionListing>(_RetrieveAuctionListing_QNAME, RetrieveAuctionListing.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveAuctionListingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "retrieveAuctionListingResponse")
    public JAXBElement<RetrieveAuctionListingResponse> createRetrieveAuctionListingResponse(RetrieveAuctionListingResponse value) {
        return new JAXBElement<RetrieveAuctionListingResponse>(_RetrieveAuctionListingResponse_QNAME, RetrieveAuctionListingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveWonListing }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "retrieveWonListing")
    public JAXBElement<RetrieveWonListing> createRetrieveWonListing(RetrieveWonListing value) {
        return new JAXBElement<RetrieveWonListing>(_RetrieveWonListing_QNAME, RetrieveWonListing.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveWonListingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "retrieveWonListingResponse")
    public JAXBElement<RetrieveWonListingResponse> createRetrieveWonListingResponse(RetrieveWonListingResponse value) {
        return new JAXBElement<RetrieveWonListingResponse>(_RetrieveWonListingResponse_QNAME, RetrieveWonListingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewAuctionListings }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "viewAuctionListings")
    public JAXBElement<ViewAuctionListings> createViewAuctionListings(ViewAuctionListings value) {
        return new JAXBElement<ViewAuctionListings>(_ViewAuctionListings_QNAME, ViewAuctionListings.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewAuctionListingsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "viewAuctionListingsResponse")
    public JAXBElement<ViewAuctionListingsResponse> createViewAuctionListingsResponse(ViewAuctionListingsResponse value) {
        return new JAXBElement<ViewAuctionListingsResponse>(_ViewAuctionListingsResponse_QNAME, ViewAuctionListingsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewCreditBalance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "viewCreditBalance")
    public JAXBElement<ViewCreditBalance> createViewCreditBalance(ViewCreditBalance value) {
        return new JAXBElement<ViewCreditBalance>(_ViewCreditBalance_QNAME, ViewCreditBalance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewCreditBalanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "viewCreditBalanceResponse")
    public JAXBElement<ViewCreditBalanceResponse> createViewCreditBalanceResponse(ViewCreditBalanceResponse value) {
        return new JAXBElement<ViewCreditBalanceResponse>(_ViewCreditBalanceResponse_QNAME, ViewCreditBalanceResponse.class, null, value);
    }

}
