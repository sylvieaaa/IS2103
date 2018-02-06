
package ws.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for creditTransactionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="creditTransactionType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEBIT"/&gt;
 *     &lt;enumeration value="CREDIT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "creditTransactionType")
@XmlEnum
public enum CreditTransactionType {

    DEBIT,
    CREDIT;

    public String value() {
        return name();
    }

    public static CreditTransactionType fromValue(String v) {
        return valueOf(v);
    }

}
