/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author SYLVIA
 */
public class CreditTransactionNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CreditTransactionNotFoundException</code>
     * without detail message.
     */
    public CreditTransactionNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CreditTransactionNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreditTransactionNotFoundException(String msg) {
        super(msg);
    }
}
