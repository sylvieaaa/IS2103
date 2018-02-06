
package ws.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for retrieveAuctionListing complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="retrieveAuctionListing"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="auctionListingId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retrieveAuctionListing", propOrder = {
    "auctionListingId"
})
public class RetrieveAuctionListing {

    protected long auctionListingId;

    /**
     * Gets the value of the auctionListingId property.
     * 
     */
    public long getAuctionListingId() {
        return auctionListingId;
    }

    /**
     * Sets the value of the auctionListingId property.
     * 
     */
    public void setAuctionListingId(long value) {
        this.auctionListingId = value;
    }

}
