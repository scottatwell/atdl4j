package org.atdl4j.ui.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.EnumPairT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.CheckBoxListT;
import org.atdl4j.atdl.layout.CheckBoxT;
import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.atdl.layout.DropDownListT;
import org.atdl4j.atdl.layout.EditableDropDownListT;
import org.atdl4j.atdl.layout.ListItemT;
import org.atdl4j.atdl.layout.MultiSelectListT;
import org.atdl4j.atdl.layout.RadioButtonListT;
import org.atdl4j.atdl.layout.RadioButtonT;
import org.atdl4j.atdl.layout.SingleSelectListT;
import org.atdl4j.atdl.layout.SliderT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.TypeConverterFactory;
import org.atdl4j.data.converter.AbstractTypeConverter;
import org.atdl4j.data.fix.PlainFIXMessageBuilder;
import org.atdl4j.data.fix.Tag959Helper;
import org.atdl4j.ui.ControlUI;

/**
 * Abstract class that represents a Parameter SWT Widget. Implements the FIX
 * value getters's methods.
 */
public abstract class AbstractControlUI<E extends Comparable<?>>
		implements ControlUI<E> {

	protected ParameterT parameter;
	protected ControlT control;
	protected AbstractTypeConverter<E> controlConverter;
	protected AbstractTypeConverter<?> parameterConverter;
	
// 2/9/2010 Scott Atwell added
	private Atdl4jConfig atdl4jConfig;
	
// 2/10/2010 Scott Atwell added
// 2/11/2010 (use Boolean to differentiate between null (unknown) vs. true and false)	boolean nullValue = false;
	Boolean nullValue = null;  // undefined state
	
	public void init(ControlT aControl, ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	{
		control = aControl;
		parameter = aParameter;
		setAtdl4jConfig( aAtdl4jConfig );
		
		// -- This method can be overriden/implemented --
		initPreCheck();
		
		//TODO: do a safe cast AbstractAdapter<?> adapter = (AbstractAdapter<E>) TypeAdapterFactory.create(control);
		controlConverter = (AbstractTypeConverter<E>) TypeConverterFactory.create(control, parameter);
		if (parameter != null)
		{
			parameterConverter = (AbstractTypeConverter<?>) TypeConverterFactory.create(parameter);
		}
		validateEnumPairs();
		
		// -- This method can be overriden/implemented --
		initPostCheck();
	}
	
	// -- Can be overriden --
	protected void initPreCheck() throws JAXBException
	{
	}
	
	// -- Can be overriden --
	protected void initPostCheck() throws JAXBException
	{
	}
	
/** 2/9/2010 Scott Atwell	@see AbstractControlUI.init(ControlT aControl, ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	@SuppressWarnings("unchecked")
	protected void init() throws JAXBException{
		//TODO: do a safe cast AbstractAdapter<?> adapter = (AbstractAdapter<E>) TypeAdapterFactory.create(control);
		controlConverter = (AbstractTypeConverter<E>) TypeConverterFactory.create(control, parameter);
		if (parameter != null) parameterConverter = (AbstractTypeConverter<?>) TypeConverterFactory.create(parameter);
		validateEnumPairs();
	}
**/
	
/** 2/10/2010 Scott Atwell not used	
	public String getControlValueAsString() throws JAXBException {
		return controlConverter.convertValueToString(getControlValue());
	}
**/
	/**
	 * Will return null if isNullValue() is true, otherwise returns getControlValueRaw()
	 * @return
	 */
	public E getControlValue()
	{
		if ( isNullValue() )
		{
			return null;
		}
		else
		{
			return getControlValueRaw();
		}
	}

	
	public Comparable<?> getControlValueAsComparable() throws JAXBException
	{
		return controlConverter.convertValueToComparable(getControlValue());
	}
	
	public String getParameterValueAsString() throws JAXBException {
		return parameter == null ? null : parameterConverter.convertValueToString(getParameterValue());
	}

	public Comparable<?> getParameterValueAsComparable() throws JAXBException
	{
		return parameter == null ? null : parameterConverter.convertValueToComparable(getParameterValue());
	}
	
	/* 
	 * This method handles string matching Atdl4jConstants.VALUE_NULL_INDICATOR and invoking setNullValue().
	 */
	public void setValueAsString(String string) throws JAXBException 
	{
// 2/10/2010 Scott Atwell
//		 TODO: do i also need a Param adapter pass here?
//		setValue(controlConverter.convertValueToComparable(string));
		if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( string ) )
		{
//			setNullValue( true );
			setNullValue( Boolean.TRUE );
			// -- note that this has no effect on the internal value which may have already been set --
		}
		else  // -- not null --
		{
			// TODO: do i also need a Param adapter pass here?
			E tempValue = controlConverter.convertValueToComparable(string);
			setValue( tempValue );
			
//			if ( tempValue == null )
//			{
//				setNullValue( true );
//			}
//			else
//			{
//				setNullValue( false );
//			}
			if ( ( tempValue == null ) && ( getNullValue() != null ) )
			{
				setNullValue( Boolean.TRUE );
			}
			else
			{
				setNullValue( Boolean.FALSE );
			}
		}
	}
	
// 2/1/2010 Scott Atwell differentiate between ControlComparable and ParameterComparable	
//	public Comparable<?> convertStringToComparable(String string) throws JAXBException
//	{
//		if (parameterConverter != null) return parameterConverter.convertValueToComparable(string);
//		else return controlConverter.convertValueToComparable(string);
//	}
	
	public Comparable<?> convertStringToControlComparable(String string) throws JAXBException
	{
		return controlConverter.convertValueToComparable(string);
	}

	public Comparable<?> convertStringToParameterComparable(String string) throws JAXBException
	{
		if (parameterConverter != null) 
			return parameterConverter.convertValueToComparable(string);
		else 
			return null;
	}

	public ParameterT getParameter() {
		return parameter;
	}
	
	public boolean hasParameter() {
		return (this.parameter != null);
	}
		
	public ControlT getControl() {
		return control;
	}
	
	public String getFIXValue() throws JAXBException {
		PlainFIXMessageBuilder builder = new PlainFIXMessageBuilder();
		builder.onStart();
		getFIXValue(builder);
		builder.onEnd();
		return builder.getMessage();
	}
	
	// 2/1/2010 John Shields added
	public String getTooltip() {
	    if (control.getTooltip() != null) return control.getTooltip();
	    else if (parameter != null && parameter.getDescription() != null) return parameter.getDescription();
	    return null;
	}
	
	// Scott Atwell 1/14/2010 made public
	public int getFIXType() throws JAXBException {
		return Tag959Helper.toInteger(getParameter());
	}

	public void getFIXValue(FIXMessageBuilder builder) throws JAXBException {
		String value = getParameterValueAsString();
		if (value != null) {
			if (getParameter().getFixTag() != null) {
//TODO 				builder.onField(getParameter().getFixTag().intValue(), value.toString());
//TODO Scott Atwell 1/31/2010 added (FixTag=0 to indicate valid parameter but DO NOT INCLUDE in FIX Message)
				if ( getParameter().getFixTag().intValue() == 0 )	{
					// ignore
				}
				else {
					builder.onField(getParameter().getFixTag().intValue(), value.toString());
				}
							
			} else {
/***
//TODO Scott Atwell 1/18/2010 BEFORE				
				String name = getParameter().getName();
				String type = Integer.toString(getFIXType());
				builder.onField(Atdl4jConstants.TAG_STRATEGY_PARAMETER_NAME, name);
				builder.onField(Atdl4jConstants.TAG_STRATEGY_PARAMETER_TYPE, type);
				builder.onField(Atdl4jConstants.TAG_STRATEGY_PARAMETER_VALUE, value.toString());
***/				
				if ( getParameter().getName().startsWith( InputAndFilterData.FIX_DEFINED_FIELD_PREFIX ) )
				{
					// bypass Hidden "standard fields" (eg "FIX_OrderQty")
				}
				else
				{
					String name = getParameter().getName();
					String type = Integer.toString(getFIXType());
					builder.onField(Atdl4jConstants.TAG_STRATEGY_PARAMETER_NAME, name);
					builder.onField(Atdl4jConstants.TAG_STRATEGY_PARAMETER_TYPE, type);
					builder.onField(Atdl4jConstants.TAG_STRATEGY_PARAMETER_VALUE, value.toString());
				}
			}
		}
	}
	
    // Helper method to validate that EnumPairs and ListItems match for
    // the given Parameter and Control pair.
    protected void validateEnumPairs() throws JAXBException
    {
	if (parameter != null)
	{
	    List<EnumPairT> enumPairs = parameter.getEnumPair();
	    List<ListItemT> listItems = getListItems();

	    if (control instanceof RadioButtonT || control instanceof CheckBoxT)
	    {
		// validate checkedEnumRef and uncheckedEnumRef
	    }
	    else if (listItems == null || listItems.size() == 0)
	    {
		if (enumPairs != null && enumPairs.size() > 0)
		{
		    throw new JAXBException("Parameter \""
			    + parameter.getName()
			    + "\" has EnumPairs but Control \""
			    + control.getID() + "\" does not have ListItems.");
		}
	    }
	    else if (parameter.getEnumPair() != null)
	    {
		if (listItems.size() != enumPairs.size())
		    throw new JAXBException("Parameter \""
			    + parameter.getName() + "\" has "
			    + enumPairs.size() + " EnumPairs but Control \""
			    + control.getID() + "\" has " + listItems.size()
			    + " ListItems.");

		for (ListItemT listItem : listItems)
		{
		    boolean match = false;
		    for (EnumPairT enumPair : enumPairs)
		    {
			if (listItem.getEnumID().equals(enumPair.getEnumID()))
			{
			    match = true;
			    break;
			}
		    }
		    if (!match) 
			throw new JAXBException("ListItem \""
				    + listItem.getEnumID()
				    + "\" on Control \""
				    + control.getID() + 
				    "\" does not have a matching EnumPair on Parameter \""
				    + parameter.getName() + "\".");
		}
	    }
	}
    }
	
	// Helper method to lookup a parameter string where the EnumID is matched
	// across the ListItemTs and EnumPairTs
	protected String getEnumWireValue(String enumID){
		if (parameter != null) {
			java.util.List<EnumPairT> enumPairs = parameter.getEnumPair();
			for (EnumPairT enumPair : enumPairs) {
				if (enumPair.getEnumID().equals(enumID)) return enumPair.getWireValue();	
			}
			// throw error?
		}
		return null;
	}
	
	// Helper method to lookup a parameter string where the EnumID is matched
	// across the ListItemTs and EnumPairTs
	protected String getParameterValueAsEnumWireValue(){
		if ( getControlValue() != null )
		{
			return getEnumWireValue(getControlValue().toString());
		}
		else
		{
			return null;
		}
	}
	
	// Helper method to convert MultipleValueChar / MultipleValueString Control values
	// to ParameterValues
	protected String getParameterValueAsMultipleValueString(){
		String value = "";
		if (getControlValue() != null)
		{
			String enumIDs = getControlValue().toString();
			if (enumIDs != null && parameter != null)
			{
				for (String enumID : enumIDs.split("\\s")) {
					if ("".equals(value)) {
						value += getEnumWireValue(enumID);
					} else {
						value += " " + getEnumWireValue(enumID);
					}
				}
			}
		}
		return "".equals(value) ? null : value;
	}
	
	// Helper method to get control ListItems
	protected List<ListItemT> getListItems()
	{
		if (control instanceof DropDownListT) {
			return ((DropDownListT)control).getListItem();
		} else if (control instanceof EditableDropDownListT) {
			return ((EditableDropDownListT)control).getListItem();
		} else if (control instanceof RadioButtonListT) {
			return ((RadioButtonListT) control).getListItem();
		} else if (control instanceof CheckBoxListT) {
			return ((CheckBoxListT)control).getListItem();
		} else if (control instanceof SingleSelectListT) {
			return ((SingleSelectListT)control).getListItem();
		} else if (control instanceof MultiSelectListT) {
			return ((MultiSelectListT)control).getListItem();
		} else if (control instanceof SliderT) {
			return ((SliderT)control).getListItem();
		} else {
			// TODO: this should maybe throw a runtime error???
			// return an empty list
			//return new Vector<ListItemT>();
		    return null;
		}
	}

	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return this.atdl4jConfig;
	}

	/**
	 * @param aAtdl4jConfig the atdl4jConfig to set
	 */
	protected void setAtdl4jConfig(Atdl4jConfig aAtdl4jConfig)
	{
		this.atdl4jConfig = aAtdl4jConfig;
	}

	/**
	 * Note contains special logic to support returning false if:
	 * 	( getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( ! isVisible() ) 
	 * or
	 * 	( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( ! isEnabled() ) )
	 * if those configs are set and nullValue is false.
	 * 
	 * @return the nullValue
	 */
// 2/11/2010 Scott Atwell Boolean vs. boolean	
//	public boolean isNullValue()
//	{
//// 2/10/2010 Scott Atwell 		return this.nullValue;
//		// -- Special logic to treat non-visible and/or non-enabled as "null" if nullValue is false --
//		if ( ! this.nullValue )
//		{
//			if ( getAtdl4jConfig() != null )
//			{
//				if ( ( getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( ! isVisible() ) )
//				{
//					return false;
//				}
//				
//				if ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( ! isEnabled() ) )
//				{
//					return false;
//				}
//			}
//		}
//		
//		return this.nullValue;
//	}
	public boolean isNullValue()
	{
		if ( getNullValue() != null )
		{
			// -- If we have it set, use it --
			return getNullValue().booleanValue();
		}
		else  // -- our nullValue is in an undefined state --
		{
			// -- Special logic to treat non-visible and/or non-enabled as "null" if nullValue is false --
			if ( getAtdl4jConfig() != null )
			{
				if ( ( ( getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( ! isVisible() ) )   ||
					  ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( ! isEnabled() ) ) )
				{
					return false;
				}
				else if ( ( ( getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( isVisible() ) )   ||
						    ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( isEnabled() ) ) )
				{
					return true;
				}
			}

			
			// -- Treat getNullValue() == null as FALSE --
			return false;
		}
	}

	/**
	 * @return
	 */
	public Boolean getNullValue()
	{
		return this.nullValue;
	}
	
	/**
	 * 
	 */
	abstract protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd);

	/**
	 * @param aNullValue the nullValue to set
	 */
//	2/11/2010 Scott Atwell public void setNullValue(boolean aNullValue)
	public void setNullValue(Boolean aNullValue)
	{
// 2/11/2010 Scott Atwell		this.nullValue = aNullValue;
		Boolean tempPreExistingNullValue = this.nullValue;

		// -- Assign the value --
		this.nullValue = aNullValue;

		// -- Check to see if aNullValue provided is different than the pre-existing value --
		if ( ( ( aNullValue != null ) &&
			    ( ! aNullValue.equals( tempPreExistingNullValue ) ) ) ||
		     ( ( tempPreExistingNullValue != null ) &&
				 ( ! tempPreExistingNullValue.equals( aNullValue ) ) ) )
		{
			// -- value has changed, notify --
			processNullValueIndicatorChange( tempPreExistingNullValue, aNullValue );
		}
	}
}