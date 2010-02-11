
package org.atdl4j.ui;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.FIXMessageBuilder;

/**
 * An interface for an UI widget which will be 
 * stores its underlying value in a parameter field.
 */
public interface ControlUI<E extends Comparable<?>> 
{
// 2/9/2010 Scott Atwell added
	public void init(ControlT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException;

// 2/10/2010 Scott Atwell added	
	public boolean isNullValue();
	public void setNullValue(boolean aNullValue);
	
	public ParameterT getParameter();
	
	public ControlT getControl();
		
	// Control value accessor methods
	
	/**
	 * Will return null if isNullValue() is true, otherwise returns getControlValueRaw()
	 * @return
	 */
	public E getControlValue();
	
	/**
	 * Will return whatever value the Control has regardless of isNullValue()
	 * @return
	 */
	public E getControlValueRaw();
	
// 2/10/2010 Scott Atwell not used		public String getControlValueAsString() throws JAXBException;
	
	public Comparable<?> getControlValueAsComparable() throws JAXBException;
	
	// Parameter value accessor methods
	
	public Object getParameterValue();
	
	public String getParameterValueAsString() throws JAXBException;
	
	public Comparable<?> getParameterValueAsComparable() throws JAXBException;
	
	// Value mutator methods
	
	public void setValue(E value);
		
	/* 
	 * This method handles string matching Atdl4jConstants.VALUE_NULL_INDICATOR and invoking setNullValue().
	 */
	public void setValueAsString(String value) throws JAXBException;

	// Helper methods
	
	public Comparable<?> convertStringToControlComparable(String string) throws JAXBException;
	
	public Comparable<?> convertStringToParameterComparable(String string) throws JAXBException;
	
	// FIX methods
	
	public String getFIXValue() throws JAXBException;
	
	public void getFIXValue(FIXMessageBuilder builder) throws JAXBException;

	// UI methods
	
	public void setVisible(boolean visible);
	public boolean isVisible();
	
	public void setEnabled(boolean enabled);	
	public boolean isEnabled();
	
//TODO Scott Atwell 1/14/2010 Added
	public int getFIXType() throws JAXBException;
}