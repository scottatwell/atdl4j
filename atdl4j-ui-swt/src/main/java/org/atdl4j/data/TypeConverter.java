
package org.atdl4j.data;

import javax.xml.bind.JAXBException;
import org.atdl4j.atdl.core.ParameterT;

/**
 * An interface for an algorithmic parameter container class. Classes which implement
 * this interface hold parameter descriptor data but do not store a value (see the 
 * ControlUI class which stores the underlying FIX value.)
 */
public interface TypeConverter<E extends Comparable<?>> {

// 2/12/2010 Scott Atwell - differentiate Parameter value (0.25) vs. Control (25%)	public E convertValueToComparable(Object value) throws JAXBException;
	public E convertValueToParameterComparable(Object value) throws JAXBException;
	public E convertValueToControlComparable(Object value) throws JAXBException;
	
// 2/12/2010 Scott Atwell - differentiate Parameter value (0.25) vs. Control (25%)	public String convertValueToString(Object value) throws JAXBException;
	public String convertValueToParameterString(Object value) throws JAXBException;
	public String convertValueToControlString(Object value) throws JAXBException;
	
	public ParameterT getParameter();
}