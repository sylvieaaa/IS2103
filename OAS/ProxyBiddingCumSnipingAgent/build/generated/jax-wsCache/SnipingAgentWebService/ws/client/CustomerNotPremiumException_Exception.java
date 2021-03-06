
package ws.client;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.11-b150120.1832
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "CustomerNotPremiumException", targetNamespace = "http://ws.session.ejb/")
public class CustomerNotPremiumException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private CustomerNotPremiumException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public CustomerNotPremiumException_Exception(String message, CustomerNotPremiumException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public CustomerNotPremiumException_Exception(String message, CustomerNotPremiumException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: ws.client.CustomerNotPremiumException
     */
    public CustomerNotPremiumException getFaultInfo() {
        return faultInfo;
    }

}
