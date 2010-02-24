package org.atdl4j.ui.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.fixatdl.layout.SliderT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.ParameterHelper;
import org.atdl4j.data.converter.AbstractTypeConverter;
import org.atdl4j.data.fix.PlainFIXMessageBuilder;
import org.atdl4j.data.fix.Tag959Helper;
import org.atdl4j.ui.ControlUI;

/**
 * Abstract class that represents a Parameter SWT Widget. Implements the FIX
 * value getters's methods.
 */
public abstract class AbstractControlUI<E extends Comparable<?>>
		implements ControlUI<E>
{
	private final Logger logger = Logger.getLogger( AbstractControlUI.class );

	protected ParameterT parameter;
	protected ControlT control;
	protected AbstractTypeConverter<E> controlConverter;
	protected AbstractTypeConverter<?> parameterConverter;

	private Atdl4jConfig atdl4jConfig;

	// 2/10/2010 Scott Atwell added
	Boolean nullValue = null; // undefined state

	// 2/11/2010 Scott Atwell
	E lastNonNullStateControlValueRaw;

	public void init(ControlT aControl, ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	{
		control = aControl;
		parameter = aParameter;
		setAtdl4jConfig( aAtdl4jConfig );

		// -- This method can be overridden/implemented --
		initPreCheck();

		controlConverter = (AbstractTypeConverter<E>) getAtdl4jConfig().getTypeConverterFactory( getAtdl4jConfig() ).create( control, parameter );

		if ( parameter != null )
		{
			parameterConverter = (AbstractTypeConverter<?>) getAtdl4jConfig().getTypeConverterFactory( getAtdl4jConfig() ).create( parameter );
		}

		validateEnumPairs();
		
// too early in process, Control does not yet have widget built		applyConstValue( parameter );
		
		// -- This method can be overridden/implemented --
		initPostCheck();
	}

	// -- Can be overridden --
	protected void initPreCheck() throws JAXBException
	{
	}

	// -- Can be overridden --
	protected void initPostCheck() throws JAXBException
	{
	}

	/**
	 * Should be invoked after Control's Widget has been fully established.  Applies Parameter's constValue to the Control
	 * @throws JAXBException
	 */
	private void applyConstValue()
		throws JAXBException
	{
// Parameter/@const has been removed		
//		if ( ( getParameter() != null ) && ( getParameter().isConst() ) )
		if ( getParameter() != null ) 
		{
			Object tempConstValue = ParameterHelper.getConstValue( getParameter() );
			
			if ( tempConstValue != null )
			{
				E tempComparable = controlConverter.convertValueToControlComparable( tempConstValue );
				if ( tempComparable != null )
				{
					setValue( tempComparable );
					processConstValueHasBeenSet();
				}
				else
				{
//					throw new IllegalArgumentException( "Unable to convert constValue or dailyConstValue [" + tempConstValue + "] -- required when Parameter@const=true [Parameter: " + parameter.getName() + "]");
					throw new IllegalArgumentException( "Unable to convert constValue [" + tempConstValue + "] -- required when Parameter@constValue is non-null [Parameter: " + parameter.getName() + "]");
				}
			}
//			else
//			{
//				throw new IllegalArgumentException( "constValue or dailyConstValue is required when Parameter@const=true [Parameter: " + parameter.getName() + "]");
//			}
		}
	
	}
	
	/**
	 * Should be invoked after Control's Widget has been fully established.  Applies Parameter's constValue to the Control
	 * @throws JAXBException
	 */
	private void applyInitValue()
		throws JAXBException
	{
//		if ( getInitValue() != null )
//		{
//			E tempComparable = controlConverter.convertValueToControlComparable( tempInitValue );
//			setValue( tempComparable );
//			processInitValueHasBeenSet();
//		}
	}

	public void applyConstOrInitValues()
		throws JAXBException
	{
		applyConstValue();
		applyInitValue();
	}
	
	/**
	 * Will return null if isNullValue() is true, otherwise returns
	 * getControlValueRaw()
	 * 
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
		return controlConverter.convertValueToControlComparable( getControlValue() );
	}

	public String getParameterValueAsString() throws JAXBException
	{
		return parameter == null ? null : parameterConverter.convertValueToParameterString( getParameterValue() );
	}

	public Comparable<?> getParameterValueAsComparable() throws JAXBException
	{
		return parameter == null ? null : parameterConverter.convertValueToParameterComparable( getParameterValue() );
	}

	/*
	 * This method handles string matching Atdl4jConstants.VALUE_NULL_INDICATOR
	 * and invoking setNullValue().
	 */
	public void setValueAsString(String string) throws JAXBException
	{
		if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( string ) )
		{
			setNullValue( Boolean.TRUE );
			// -- note that this has no effect on the internal value which may have already been set --
		}
		else
		// -- not null --
		{
			E tempValue = controlConverter.convertValueToControlComparable( string );
			setValue( tempValue );
			
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

	public Comparable<?> convertStringToControlComparable(String string) throws JAXBException
	{
		return controlConverter.convertValueToControlComparable( string );
	}

	public Comparable<?> convertStringToParameterComparable(String string) throws JAXBException
	{
		if ( parameterConverter != null )
			return parameterConverter.convertValueToParameterComparable( string );
		else
			return null;
	}

	public ParameterT getParameter()
	{
		return parameter;
	}

	public boolean hasParameter()
	{
		return ( this.parameter != null );
	}

	public ControlT getControl()
	{
		return control;
	}

	public String getFIXValue() throws JAXBException
	{
		PlainFIXMessageBuilder builder = new PlainFIXMessageBuilder();
		builder.onStart();
		getFIXValue( builder );
		builder.onEnd();
		return builder.getMessage();
	}

	// 2/1/2010 John Shields added
	public String getTooltip()
	{
		if ( control.getTooltip() != null )
			return control.getTooltip();
		else if ( parameter != null && parameter.getDescription() != null )
			return parameter.getDescription();
		return null;
	}

	public int getFIXType() throws JAXBException
	{
		return Tag959Helper.toInteger( getParameter() );
	}

	public void getFIXValue(FIXMessageBuilder builder) throws JAXBException
	{
		String value = getParameterValueAsString();
		if ( value != null )
		{
			if ( getParameter().getFixTag() != null )
			{
// Scott Atwell 1/31/2010 added (FixTag=0 to indicate valid parameter but DO NOT INCLUDE in FIX Message)
// 2/15/2010 Scott Atwell -- ???? remove the FixTag=0 part				
//				if ( getParameter().getFixTag().intValue() == 0 )
//				{
//					// ignore
//				}
//				else
//				{
					builder.onField( getParameter().getFixTag().intValue(), value.toString() );
//				}

			}
			else
			{
				if ( getParameter().getName().startsWith( InputAndFilterData.FIX_DEFINED_FIELD_PREFIX ) )
				{
					// bypass Hidden "standard fields" (eg "FIX_OrderQty")
				}
				else
				{
					String name = getParameter().getName();
					String type = Integer.toString( getFIXType() );
					builder.onField( Atdl4jConstants.TAG_STRATEGY_PARAMETER_NAME, name );
					builder.onField( Atdl4jConstants.TAG_STRATEGY_PARAMETER_TYPE, type );
					builder.onField( Atdl4jConstants.TAG_STRATEGY_PARAMETER_VALUE, value.toString() );
				}
			}
		}
	}

	// Helper method to validate that EnumPairs and ListItems match for
	// the given Parameter and Control pair.
	protected void validateEnumPairs() throws JAXBException
	{
		if ( parameter != null )
		{
			List<EnumPairT> enumPairs = parameter.getEnumPair();
			List<ListItemT> listItems = getListItems();

			if ( control instanceof RadioButtonT || control instanceof CheckBoxT )
			{
				// validate checkedEnumRef and uncheckedEnumRef
			}
			else if ( listItems == null || listItems.size() == 0 )
			{
				if ( enumPairs != null && enumPairs.size() > 0 )
				{
					throw new JAXBException( "Parameter \"" + parameter.getName() + "\" has EnumPairs but Control \"" + control.getID()
							+ "\" does not have ListItems." );
				}
			}
			else if ( parameter.getEnumPair() != null )
			{
				if ( listItems.size() != enumPairs.size() )
					throw new JAXBException( "Parameter \"" + parameter.getName() + "\" has " + enumPairs.size() + " EnumPairs but Control \""
							+ control.getID() + "\" has " + listItems.size() + " ListItems." );

				for ( ListItemT listItem : listItems )
				{
					boolean match = false;
					for ( EnumPairT enumPair : enumPairs )
					{
						if ( listItem.getEnumID().equals( enumPair.getEnumID() ) )
						{
							match = true;
							break;
						}
					}
					if ( !match )
						throw new JAXBException( "ListItem \"" + listItem.getEnumID() + "\" on Control \"" + control.getID()
								+ "\" does not have a matching EnumPair on Parameter \"" + parameter.getName() + "\"." );
				}
			}
		}
	}

	// Helper method to lookup a parameter string where the EnumID is matched
	// across the ListItemTs and EnumPairTs
	protected String getEnumWireValue(String enumID)
	{
		if ( parameter != null )
		{
			java.util.List<EnumPairT> enumPairs = parameter.getEnumPair();
			for ( EnumPairT enumPair : enumPairs )
			{
				if ( enumPair.getEnumID().equals( enumID ) )
					return enumPair.getWireValue();
			}
			// throw error?
		}
		return null;
	}

	// Helper method to lookup a parameter string where the EnumID is matched
	// across the ListItemTs and EnumPairTs
	protected String getParameterValueAsEnumWireValue()
	{
		if ( getControlValue() != null )
		{
			return getEnumWireValue( getControlValue().toString() );
		}
		else
		{
			return null;
		}
	}

	// Helper method to convert MultipleValueChar / MultipleValueString Control
	// values to ParameterValues
	protected String getParameterValueAsMultipleValueString()
	{
		String value = "";
		if ( getControlValue() != null )
		{
			String enumIDs = getControlValue().toString();
			if ( enumIDs != null && parameter != null )
			{
				for ( String enumID : enumIDs.split( "\\s" ) )
				{
					if ( "".equals( value ) )
					{
						value += getEnumWireValue( enumID );
					}
					else
					{
						value += " " + getEnumWireValue( enumID );
					}
				}
			}
		}
		return "".equals( value ) ? null : value;
	}

	// Helper method to get control ListItems
	protected List<ListItemT> getListItems()
	{
		if ( control instanceof DropDownListT )
		{
			return ( (DropDownListT) control ).getListItem();
		}
		else if ( control instanceof EditableDropDownListT )
		{
			return ( (EditableDropDownListT) control ).getListItem();
		}
		else if ( control instanceof RadioButtonListT )
		{
			return ( (RadioButtonListT) control ).getListItem();
		}
		else if ( control instanceof CheckBoxListT )
		{
			return ( (CheckBoxListT) control ).getListItem();
		}
		else if ( control instanceof SingleSelectListT )
		{
			return ( (SingleSelectListT) control ).getListItem();
		}
		else if ( control instanceof MultiSelectListT )
		{
			return ( (MultiSelectListT) control ).getListItem();
		}
		else if ( control instanceof SliderT )
		{
			return ( (SliderT) control ).getListItem();
		}
		else
		{
			// TODO: this should maybe throw a runtime error???
			// return an empty list
			// return new Vector<ListItemT>();
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
	 * @param aAtdl4jConfig
	 *           the atdl4jConfig to set
	 */
	protected void setAtdl4jConfig(Atdl4jConfig aAtdl4jConfig)
	{
		this.atdl4jConfig = aAtdl4jConfig;
	}

	/**
	 * Note contains special logic to support returning false if: (
	 * getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( ! isVisible()
	 * ) or ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( !
	 * isEnabled() ) ) if those configs are set and nullValue is false.
	 * 
	 * @return the nullValue
	 */
	public boolean isNullValue()
	{
		if ( getNullValue() != null )
		{
			// -- If we have it set, use it --
			return getNullValue().booleanValue();
		}
		else
		// -- our nullValue is in an undefined state --
		{
			// -- Special logic to treat non-visible and/or non-enabled as "null"
			// if nullValue is false --
			if ( getAtdl4jConfig() != null )
			{
				if ( ( ( getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( !isVisible() ) )
// 2/15/2010 Scott Atwell						|| ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( !isEnabled() ) ) )
						|| ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( !isControlExcludingLabelEnabled() ) ) )
				{
					return false;
				}
				else if ( ( ( getAtdl4jConfig().isTreatControlVisibleFalseAsNull() ) && ( isVisible() ) )
// 2/15/2010 Scott Atwell						|| ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( isEnabled() ) ) )
						|| ( ( getAtdl4jConfig().isTreatControlEnabledFalseAsNull() ) && ( isControlExcludingLabelEnabled() ) ) )
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
	 * @param aNullValue
	 *           the nullValue to set
	 */
	// 2/11/2010 Scott Atwell public void setNullValue(boolean aNullValue)
	public void setNullValue(Boolean aNullValue)
	{
		// 2/11/2010 Scott Atwell this.nullValue = aNullValue;
		Boolean tempPreExistingNullValue = this.nullValue;

		// -- Assign the value --
		this.nullValue = aNullValue;

		logger.debug( "setNullValue() control ID:" + getControl().getID() + " tempPreExistingNullValue: " + tempPreExistingNullValue + " aNullValue: "
				+ aNullValue );

		// -- Check to see if aNullValue provided is different than the
		// pre-existing value --
		if ( ( ( aNullValue != null ) && ( !aNullValue.equals( tempPreExistingNullValue ) ) )
				|| ( ( tempPreExistingNullValue != null ) && ( !tempPreExistingNullValue.equals( aNullValue ) ) ) )
		{
			// -- "retain" the Control's last non-null raw value when changing to
			// aNullValue of true --
			if ( ( ( tempPreExistingNullValue == null ) || ( Boolean.FALSE.equals( tempPreExistingNullValue ) ) )
					&& ( Boolean.TRUE.equals( aNullValue ) ) )
			{
				logger.debug( "setNullValue() control ID:" + getControl().getID() + " tempPreExistingNullValue: " + tempPreExistingNullValue
						+ " aNullValue: " + aNullValue + " invoking setLastNonNullStateControlValueRaw( " + getControlValueRaw() + " )" );
				setLastNonNullStateControlValueRaw( getControlValueRaw() );
			}

			// -- value has changed, notify --
			processNullValueIndicatorChange( tempPreExistingNullValue, aNullValue );

			// -- "restore" the Control's raw value if so configured when going
			// from aNullValue of true to non-null --
			if ( ( Boolean.FALSE.equals( aNullValue ) ) && ( getLastNonNullStateControlValueRaw() != null ) )
			{
				if ( getAtdl4jConfig().isRestoreLastNonNullStateControlValueBehavior() )
				{
					logger
							.debug( "setNullValue() control ID:" + getControl().getID() + " tempPreExistingNullValue: " + tempPreExistingNullValue
									+ " aNullValue: " + aNullValue + " invoking restoreLastNonNullStateControlValue( " + getLastNonNullStateControlValueRaw()
									+ " )" );
					restoreLastNonNullStateControlValue( getLastNonNullStateControlValueRaw() );
				}
			}
		}
	}

	/**
	 * This method could be overridden for specific controls, if so desired.
	 * 
	 * @param aLastNonNullStateControlValue
	 */
	protected void restoreLastNonNullStateControlValue(E aLastNonNullStateControlValue)
	{
		setValue( aLastNonNullStateControlValue );
	}

	/**
	 * @return the lastNonNullStateControlValueRaw
	 */
	public E getLastNonNullStateControlValueRaw()
	{
		return this.lastNonNullStateControlValueRaw;
	}

	/**
	 * @param aLastNonNullStateControlValueRaw
	 *           the lastNonNullStateControlValueRaw to set
	 */
	protected void setLastNonNullStateControlValueRaw(E aLastNonNullStateControlValueRaw)
	{
		this.lastNonNullStateControlValueRaw = aLastNonNullStateControlValueRaw;
	}
	
	/**
	 * Used when pre-populating a control with its FIX message wire value 
	 * For example: PercentageT with isMultiplyBy100() == true would have ".1234" on the wire for "12.34" displayed/stored by the control (for 12.34%). 
	 * @param aFIXValue
	 * @throws JAXBException
	 */
	public void setFIXValue( String aFIXValue )
		throws JAXBException
	{
		// -- Must use parameterConverter's coonvertToControlString (eg TextField's controlConverter is a StringConverter, not a DecimalConverter like the Parameter's would be) --
		setValueAsString( parameterConverter.convertValueToControlString( aFIXValue ) );
	}
	

	/**
	 * Default implementation.  Can be overridden if so desired.
	 */
	public void processConstValueHasBeenSet()
	{
//		setEnabled( false );
		setControlExcludingLabelEnabled( false );
	}
}