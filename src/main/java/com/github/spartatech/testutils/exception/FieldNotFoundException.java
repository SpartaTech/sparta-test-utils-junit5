package com.github.spartatech.testutils.exception;

/** 
 * 
 * Field not found for DateAssertUtils
 * 
 * @author Daniel Conde Diehl 
 * 
 * History: 
 *    Dec 29, 2016 - Daniel Conde Diehl
 *  
 */
public class FieldNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5417391757338884052L;

    /**
     * Constructor receiving message
     * 
     * @param message error message
     */
    public FieldNotFoundException(String message) {
        super(message);
    }
}
