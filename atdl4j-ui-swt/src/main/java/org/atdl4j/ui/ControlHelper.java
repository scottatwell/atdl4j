/*
 * Created on Feb 21, 2010
 *
 */
package org.atdl4j.ui;

import java.math.BigInteger;

import org.atdl4j.atdl.layout.CheckBoxListT;
import org.atdl4j.atdl.layout.CheckBoxT;
import org.atdl4j.atdl.layout.ClockT;
import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.atdl.layout.DoubleSpinnerT;
import org.atdl4j.atdl.layout.DropDownListT;
import org.atdl4j.atdl.layout.EditableDropDownListT;
import org.atdl4j.atdl.layout.HiddenFieldT;
import org.atdl4j.atdl.layout.LabelT;
import org.atdl4j.atdl.layout.MultiSelectListT;
import org.atdl4j.atdl.layout.RadioButtonListT;
import org.atdl4j.atdl.layout.RadioButtonT;
import org.atdl4j.atdl.layout.SingleSelectListT;
import org.atdl4j.atdl.layout.SingleSpinnerT;
import org.atdl4j.atdl.layout.SliderT;
import org.atdl4j.atdl.layout.TextFieldT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.Atdl4jConstants;

/**
 * 
 *  =====================================================================*
 *  Copyright Notice 
 *  This file contains proprietary information of American Century.
 *  Copying or reproduction without prior written approval is prohibited.
 *  Copyright (c) 2010
 *  =====================================================================*
 * 
 * 
 * This class contains the data associated with the <code>ControlHelper</code>.
 * 
 * Creation date: (Feb 21, 2010 5:44:02 PM)
 * @author SWL
 * @version 1.0, Feb 21, 2010
 */
public class ControlHelper
{
	/**
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return
	 */
	public static BigInteger getIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		if ( aControl instanceof SingleSpinnerT )
		{
			return determineIncrementValue( ((SingleSpinnerT) aControl).getIncrement(), 
													  ((SingleSpinnerT) aControl).getIncrementPolicy(),
													  aAtdl4jConfig );
		}
		
		return null;
	}
	
	/**
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return
	 */
	public static BigInteger getInnerIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		if ( aControl instanceof DoubleSpinnerT )
		{
			return determineIncrementValue( ((DoubleSpinnerT) aControl).getInnerIncrement(), 
													  ((DoubleSpinnerT) aControl).getInnerIncrementPolicy(),
													  aAtdl4jConfig );
		}
		
		return null;
	}
	
	/**
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return
	 */
	public static BigInteger getOuterIncrementValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		if ( aControl instanceof DoubleSpinnerT )
		{
			return determineIncrementValue( ((DoubleSpinnerT) aControl).getOuterIncrement(), 
													  ((DoubleSpinnerT) aControl).getOuterIncrementPolicy(),
													  aAtdl4jConfig );
		}
		
		return null;
	}
	

	/**
	 * @param aIncrement
	 * @param aIncrementPolicy
	 * @param aAtdl4jConfig
	 * @return
	 */
	public static BigInteger determineIncrementValue( Double aIncrement, String aIncrementPolicy, Atdl4jConfig aAtdl4jConfig )
	{
		if ( Atdl4jConstants.INCREMENT_POLICY_LOT_SIZE.equals( aIncrementPolicy ) ) 
		{
			if ( ( aAtdl4jConfig != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData() != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData().getInputInitPolicy_LotSize() != null ) )
			{
				return aAtdl4jConfig.getInputAndFilterData().getInputInitPolicy_LotSize();
			}
			else
			{
				throw new IllegalArgumentException( "LotSize for security was not specified.  Unable to support IncrementPolicy=" + aIncrementPolicy );
			}
		}
		else if ( Atdl4jConstants.INCREMENT_POLICY_TICK.equals( aIncrementPolicy ) ) 
		{
			if ( ( aAtdl4jConfig != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData() != null ) &&
				  ( aAtdl4jConfig.getInputAndFilterData().getInputInitPolicy_Tick() != null ) )
			{
				return aAtdl4jConfig.getInputAndFilterData().getInputInitPolicy_Tick();
			}
			else
			{
				throw new IllegalArgumentException( "Tick size for security was not specified.  Unable to support IncrementPolicy=" + aIncrementPolicy );
			}
		} 
		else  // -- Use aIncrement value when aIncrementPolicy null or Atdl4jConstants.INCREMENT_POLICY_STATIC --
		{
			if ( aIncrement != null )
			{
				return BigInteger.valueOf( aIncrement.longValue() ); 
			}
			else
			{
				return null;
			}
		}
	}
	
	/**
	 * Handles ControlT/@initPolicy ("UseValue" or "UseFixField") logic in conjunction with ControlT/@initValue and ControlT/@initFixField
	 * Returns null if ControlT/@initValue is the special null indicator: VALUE_NULL_INDICATOR
	 * @param aControl
	 * @param aAtdl4jConfig
	 * @return 
	 */
	public static Object getInitValue( ControlT aControl, Atdl4jConfig aAtdl4jConfig )
	{
		// INIT_POLICY_USE_VALUE = "UseValue";  // -- use value from ControlT/@initValue --
		// INIT_POLICY_USE_FIX_FIELD = "UseFixField";  // -- use value from ControlT/@initFixField if available, else ControlT/@initValue --		
		
		if ( Atdl4jConstants.INIT_POLICY_USE_FIX_FIELD.equals( aControl.getInitPolicy() ) )
		{
			if ( aControl.getInitFixField() == null )
			{
				throw new IllegalArgumentException( "ERROR: Control: " + aControl.getID() + " has initPolicy=\"" + aControl.getInitPolicy() + "\" but does not have initFixField set." );
			}
			
			if ( ( aAtdl4jConfig == null ) || ( aAtdl4jConfig.getInputAndFilterData() == null ) )
			{
				throw new IllegalArgumentException( "ERROR: Control: " + aControl.getID() + " has initPolicy=\"" + aControl.getInitPolicy() + "\" but Atdl4jConfig is null." );
			}
			
			String tempFixFieldValue = aAtdl4jConfig.getInputAndFilterData().getInputHiddenFieldValue( aControl.getInitFixField().toString() );
			
			if ( tempFixFieldValue != null )
			{
				// -- "FIX_[fieldname]" value found, return it --
				return tempFixFieldValue;
			}
		}

		// -- Get the 'raw' ControlT/@initValue --
		Object tempInitValueRaw = getInitValueRaw( aControl );
		
		// -- Special handling to check for VALUE_NULL_INDICATOR -- 
		if ( Atdl4jConstants.VALUE_NULL_INDICATOR.equals( tempInitValueRaw ) )
		{
			return null;
		}
		else
		{
			return tempInitValueRaw;
		}
	}
	
	/**
	 * Returns raw/actual value (could be "{NULL}" string) from ControlT/@initValue
	 * @param aControl
	 * @return 
	 */
	public static Object getInitValueRaw( ControlT aControl )
	{
		// -- initPolicy is INIT_POLICY_USE_VALUE  or  no input field value supplied/found for ControlT/@initFixField --
		if ( aControl instanceof CheckBoxT )
		{
//			return ((CheckBoxT) aControl).getInitValue();
		}
		else if ( aControl instanceof CheckBoxListT )
		{
			return ((CheckBoxListT) aControl).getInitValue();
		}
		else if ( aControl instanceof ClockT )
		{
			return ((ClockT) aControl).getInitValue();
		}
		else if ( aControl instanceof DoubleSpinnerT )
		{
			return ((DoubleSpinnerT) aControl).getInitValue();
		}
		else if ( aControl instanceof DropDownListT )
		{
			return ((DropDownListT) aControl).getInitValue();
		}
		else if ( aControl instanceof EditableDropDownListT )
		{
			return ((EditableDropDownListT) aControl).getInitValue();
		}
		else if ( aControl instanceof HiddenFieldT )
		{
			return ((HiddenFieldT) aControl).getInitValue();
		}
		else if ( aControl instanceof LabelT )
		{
			return ((LabelT) aControl).getInitValue();
		}
		else if ( aControl instanceof MultiSelectListT )
		{
			return ((MultiSelectListT) aControl).getInitValue();
		}
		else if ( aControl instanceof RadioButtonT )
		{
//			return ((RadioButtonT) aControl).getInitValue();
		}
		else if ( aControl instanceof RadioButtonListT )
		{
			return ((RadioButtonListT) aControl).getInitValue();
		}
		else if ( aControl instanceof SingleSelectListT )
		{
			return ((SingleSelectListT) aControl).getInitValue();
		}
		else if ( aControl instanceof SingleSpinnerT )
		{
			return ((SingleSpinnerT) aControl).getInitValue();
		}
		else if ( aControl instanceof SliderT )
		{
			return ((SliderT) aControl).getInitValue();
		}
		else if ( aControl instanceof TextFieldT )
		{
			return ((TextFieldT) aControl).getInitValue();
		}
		
		return null;
	}

}
