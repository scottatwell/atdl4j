/*
 * Created on Feb 21, 2010
 *
 */
package org.atdl4j.ui;

import java.math.BigInteger;

import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.atdl.layout.DoubleSpinnerT;
import org.atdl4j.atdl.layout.SingleSpinnerT;
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
	
}
