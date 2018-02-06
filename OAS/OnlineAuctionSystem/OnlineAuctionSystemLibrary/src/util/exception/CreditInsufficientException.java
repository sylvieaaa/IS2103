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
public class CreditInsufficientException extends Exception {

    /**
     * Creates a new instance of <code>CreditInsufficientException</code>
     * without detail message.
     */
    public CreditInsufficientException() {
    }

    /**
     * Constructs an instance of <code>CreditInsufficientException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreditInsufficientException(String msg) {
        super(msg);
    }
}
