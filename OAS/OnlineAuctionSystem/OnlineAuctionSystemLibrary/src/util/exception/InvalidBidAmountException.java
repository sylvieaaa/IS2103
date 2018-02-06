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
public class InvalidBidAmountException extends Exception {

    /**
     * Creates a new instance of <code>InvalidBidAmountException</code> without
     * detail message.
     */
    public InvalidBidAmountException() {
    }

    /**
     * Constructs an instance of <code>InvalidBidAmountException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidBidAmountException(String msg) {
        super(msg);
    }
}
