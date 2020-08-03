/**
 * Sparta Software Co.
 * 2017
 */
package com.github.spartatech.testutils.exception;

/**
 * @author Daniel Conde Diehl - Sparta Technology
 *
 * History:
 *  Apr 3, 2017 - Daniel Conde Diehl
 */
@FunctionalInterface
public interface Processable {
	
	/**
	 * Process the method and allows exception to be thrown
	 * 
	 * @throws Exception externalized exception if happened.
	 */
	public void process() throws Exception;
}
