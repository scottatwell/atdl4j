
package org.atdl4j.ui;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;

/**
 * An interface for an UI widget which will be 
 * stores its underlying value in a parameter field.
 */
public interface ControlUI<E extends Comparable<?>> 
{
// 2/9/2010 Scott Atwell added
	public void init(ControlT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig);

// 3/8/2010 Scott Atwell added
	public void reinit();

// 2/10/2010 Scott Atwell added	
	public boolean isNullValue();
	public void setNullValue(Boolean aNullValue);
// 2/11/2010 Scott Atwell added	
	public Boolean getNullValue();

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
	
	public Comparable<?> getControlValueAsComparable();
	
	// Parameter value accessor methods
	
	public Object getParameterValue();
	
// 3/10/2010 Scott Atwell	public String getParameterValueAsString() throws JAXBException;
	public String getParameterFixWireValue();
	
	public Comparable<?> getParameterValueAsComparable();
	
	// Value mutator methods
	
	public void setValue(E value);
		
	/* 
	 * This method handles string matching Atdl4jConstants.VALUE_NULL_INDICATOR and invoking setNullValue().
	 */
	public void setValueAsString(String value);

	// Helper methods
	
	public Comparable<?> convertStringToControlComparable(String string);
	
// 3/10/2010 Scott Atwell	public Comparable<?> convertStringToParameterComparable(String string) throws JAXBException;
	public Comparable<?> convertParameterStringToParameterComparable(String aParameterString );

	// FIX methods
	
	public String getFIXValue();
	
	public void getFIXValue(FIXMessageBuilder builder);

	// UI methods
	
	public void setVisible(boolean visible);
	public boolean isVisible();
	
	public void setEnabled(boolean enabled);	
	public boolean isEnabled();
	
//TODO Scott Atwell 1/14/2010 Added
	public int getFIXType();
	
	public E getLastNonNullStateControlValueRaw();
	
	/**
	 * Used when pre-populating a control with its FIX message wire value 
	 * For example: PercentageT with isMultiplyBy100() == true would have ".1234" on the wire for "12.34" displayed/stored by the control (for 12.34%). 
	 * @param aFIXValue
	 */
	public void setFIXValue( String aFIXValue );
	
	public void applyConstOrInitValues();
	
	public void processConstValueHasBeenSet();
	
	public void setControlExcludingLabelEnabled(boolean enabled);	
	public boolean isControlExcludingLabelEnabled();
	
	public boolean isHiddenFieldForInputAndFilterData();
	public void setHiddenFieldForInputAndFilterData(boolean aBoolean);
}