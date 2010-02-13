package org.atdl4j.data.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.atdl4j.atdl.core.AmtT;
import org.atdl4j.atdl.core.FloatT;
import org.atdl4j.atdl.core.NumericT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.PercentageT;
import org.atdl4j.atdl.core.PriceOffsetT;
import org.atdl4j.atdl.core.PriceT;
import org.atdl4j.atdl.core.QtyT;

/*
 * Supports BigDecimal-based Parameter types:
 * 		FloatT
 * 		AmtT
 * 		PercentageT
 * 		PriceOffsetT
 * 		PriceT
 * 		QtyT
 *
 * Note that isMultiplyBy100() is only supported for PercentageT
 * 
 * @author Scott Atwell
 */
public class DecimalConverter
		extends AbstractTypeConverter<BigDecimal>
{

	public DecimalConverter()
	{
	}

	public DecimalConverter(ParameterT parameter)
	{
		this.parameter = parameter;
	}

	public static NumberFormat DECIMAL_FORMAT_0dp = new DecimalFormat( "#;-#" );
	public static NumberFormat DECIMAL_FORMAT_1dp = new DecimalFormat( "#.0;-#.0" );
	public static NumberFormat DECIMAL_FORMAT_2dp = new DecimalFormat( "#.00;-#.00" );
	public static NumberFormat DECIMAL_FORMAT_3dp = new DecimalFormat( "#.000;-#.000" );
	public static NumberFormat DECIMAL_FORMAT_4dp = new DecimalFormat( "#.0000;-#.0000" );
	public static NumberFormat DECIMAL_FORMAT_5dp = new DecimalFormat( "#.00000;-#.00000" );
	public static NumberFormat DECIMAL_FORMAT_6dp = new DecimalFormat( "#.000000;-#.000000" );
	public static NumberFormat DECIMAL_FORMAT_7dp = new DecimalFormat( "#.0000000;-#.0000000" );
	public static NumberFormat DECIMAL_FORMAT_8dp = new DecimalFormat( "#.00000000;-#.00000000" );
	public static NumberFormat DECIMAL_FORMAT_9dp = new DecimalFormat( "#.000000000;-#.000000000" );
	public static NumberFormat DECIMAL_FORMAT_10dp = new DecimalFormat( "#.0000000000;-#.0000000000" );
	public static NumberFormat DECIMAL_FORMAT_11dp = new DecimalFormat( "#.00000000000;-#.00000000000" );
	public static NumberFormat DECIMAL_FORMAT_12dp = new DecimalFormat( "#.000000000000;-#.000000000000" );
	public static NumberFormat DECIMAL_FORMAT_13dp = new DecimalFormat( "#.0000000000000;-#.0000000000000" );
	
	/* 
	 * Note that if isMultiplyBy100() is true and value is BigDecial, the value returned will be / 100.d
	 */
	public BigDecimal convertValueToParameterComparable(Object value)
	{
		BigDecimal tempBigDecimal = null;
		
		if ( value instanceof BigDecimal )
		{
		// 2/12/2010			return (BigDecimal) value;
			tempBigDecimal = (BigDecimal) value;
		}
		else if ( value instanceof String )
		{
			String str = (String) value;
// 2/12/2010			try
//			{
//				return new BigDecimal( str );
//			}
//			catch (NumberFormatException e)
//			{
//				return null;
//			}
			if ( ( str == null ) || ( str.trim().length() == 0 ) )
			{
				return null;
			}
			else
			{
				try
				{
					tempBigDecimal = new BigDecimal( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Decimal Number Format: [" + str + "] for Parameter: " + getParameter().getName() );
				}
			}
		}
		else if ( value instanceof Boolean )
		{
			Boolean bool = (Boolean) value;
			if ( bool != null )
			{
				if ( bool )
					tempBigDecimal = new BigDecimal( 1 );
				else
					tempBigDecimal = new BigDecimal( 0 );
			}
			else
				return null;
		}
		
// 2/12/2010 Scott Atwell		
		if ( ( tempBigDecimal != null ) && ( isMultiplyBy100() ) )
		{
// 2/12/2010 avoid stray decimal precision			return new BigDecimal( tempBigDecimal.doubleValue() / 100.0d );
			return tempBigDecimal.scaleByPowerOfTen( -2 );
		}
		else
		{
			return tempBigDecimal;
		}
	}

	/* 
	 * Note that if isMultiplyBy100() is true and value is BigDecial, the value returned will be * 100.d
	 */
	public BigDecimal convertValueToControlComparable(Object value)
	{
		BigDecimal tempBigDecimal = null;
		
		if ( value instanceof BigDecimal )
		{
		// 2/12/2010			return (BigDecimal) value;
			tempBigDecimal = (BigDecimal) value;
		}
		else if ( value instanceof String )
		{
			String str = (String) value;
// 2/12/2010			try
//			{
//				return new BigDecimal( str );
//			}
//			catch (NumberFormatException e)
//			{
//				return null;
//			}
			if ( ( str == null ) || ( str.trim().length() == 0 ) )
			{
				return null;
			}
			else
			{
				try
				{
					tempBigDecimal = new BigDecimal( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Decimal Number Format: [" + str + "] for Parameter: " + getParameter().getName() );
				}
			}
		}
		else if ( value instanceof Boolean )
		{
			Boolean bool = (Boolean) value;
			if ( bool != null )
			{
				if ( bool )
					tempBigDecimal = new BigDecimal( 1 );
				else
					tempBigDecimal = new BigDecimal( 0 );
			}
			else
				return null;
		}
		
// 2/12/2010 Scott Atwell		
		if ( ( tempBigDecimal != null ) && ( isMultiplyBy100() ) )
		{
// 2/12/2010 avoid stray decimal precision			return new BigDecimal( tempBigDecimal.doubleValue() * 100.0d );
			return tempBigDecimal.scaleByPowerOfTen( 2 );
		}
		else
		{
			return tempBigDecimal;
		}
	}

	/* 
	 * Returns string value, formatted with getPrecision() decimal places if so specified and applicable.
	 */
	public String convertValueToParameterString(Object value)
	{
		BigDecimal num = convertValueToParameterComparable( value ); 
/** 2/12/2010 Scott Atwell		
		if ( num == null )
			return null;
		else
			return num.toPlainString();
**/
		return toString( num, getPrecision() );
	}

	/* 
	 * Returns string value, formatted with getPrecision() decimal places if so specified and applicable.
	 */
	public String convertValueToControlString(Object value)
	{
		BigDecimal num = convertValueToControlComparable( value ); 
/** 2/12/2010 Scott Atwell		
		if ( num == null )
			return null;
		else
			return num.toPlainString();
**/
		return toString( num, getPrecision() );
	}

	/**
	 * Applies precision rules, if specified, up to 13 decimal places.
	 * 
	 * @param aValue
	 * @param aPrecision
	 * @return
	 */
	public static String toString( BigDecimal aValue, BigInteger aPrecision )
	{
		if ( aValue != null )
		{
			if ( aPrecision != null )
			{
				switch ( aPrecision.intValue() )
				{
					case 0:
						return DECIMAL_FORMAT_0dp.format( aValue.doubleValue() );
						
					case 1:
						return DECIMAL_FORMAT_1dp.format( aValue.doubleValue() );
						
					case 2:
						return DECIMAL_FORMAT_2dp.format( aValue.doubleValue() );
						
					case 3:
						return DECIMAL_FORMAT_3dp.format( aValue.doubleValue() );
						
					case 4:
						return DECIMAL_FORMAT_4dp.format( aValue.doubleValue() );
						
					case 5:
						return DECIMAL_FORMAT_5dp.format( aValue.doubleValue() );
						
					case 6:
						return DECIMAL_FORMAT_6dp.format( aValue.doubleValue() );
						
					case 7:
						return DECIMAL_FORMAT_7dp.format( aValue.doubleValue() );
						
					case 8:
						return DECIMAL_FORMAT_8dp.format( aValue.doubleValue() );
						
					case 9:
						return DECIMAL_FORMAT_9dp.format( aValue.doubleValue() );
						
					case 10:
						return DECIMAL_FORMAT_10dp.format( aValue.doubleValue() );
						
					case 11:
						return DECIMAL_FORMAT_11dp.format( aValue.doubleValue() );
						
					case 12:
						return DECIMAL_FORMAT_12dp.format( aValue.doubleValue() );
						
					case 13:
						return DECIMAL_FORMAT_13dp.format( aValue.doubleValue() );

					default:
						return aValue.toPlainString();
				}
			}
			else  // -- No precision expressed --
			{
				return aValue.toPlainString();
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the value of Parameter.getPrecision() for NumericT assuming it has been set,
	 * otherwise returns null.
	 * 
	 * @return
	 */
	public BigInteger getPrecision()
	{
		if ( parameter instanceof NumericT )
		{
			return ( (NumericT) parameter ).getPrecision();
		}
		else
		{
			// -- Return null if Parameter does not have this value set --
			return null; 
		}
	}

	/**
	 * Returns the value of Parameter.getMultiplyBy100() for PercentageT assuming it has been set,
	 * otherwise returns false.
	 * 
	 * @return
	 */
	public boolean isMultiplyBy100()
	{
		if ( parameter instanceof PercentageT )
		{
			return ( (PercentageT) parameter ).isMultiplyBy100();
		}
		else
		{
			// -- Return null if Parameter does not have this value set --
			return false; 
		}
	}

	/**
	 * Returns the value of Parameter.getMinValue() for the specific NumericT types for which this is
	 * applicable, assuming it has been set, otherwise returns null.
	 * 
	 * @return
	 */
	public BigDecimal getMinValue()
	{
		if ( parameter instanceof FloatT )
		{
			return ( (FloatT) parameter ).getMinValue();
		}
		else if ( parameter instanceof AmtT )
		{
			return ( (AmtT) parameter ).getMinValue();
		}
		else if ( parameter instanceof PercentageT )
		{
			return ( (PercentageT) parameter ).getMinValue();
		}
		else if ( parameter instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) parameter ).getMinValue();
		}
		else if ( parameter instanceof PriceT )
		{
			return ( (PriceT) parameter ).getMinValue();
		}
		else if ( parameter instanceof QtyT )
		{
			return ( (QtyT) parameter ).getMinValue();
		}
//		else if ( parameter instanceof IntT )
//		{
//			
//			IntT intT = (IntT) parameter;
//
//			if ( intT.getMinValue() != null )
//			{
//				return intT.getMinValue();
//			}
//
//		}
		else
		{
			return null;
		}
	}

	
	/**
	 * Returns the value of Parameter.getMaxValue() for the specific NumericT types for which this is
	 * applicable, assuming it has been set, otherwise returns null.
	 * 
	 * @return
	 */
	public BigDecimal getMaxValue()
	{
		if ( parameter instanceof FloatT )
		{
			return ( (FloatT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof AmtT )
		{
			return ( (AmtT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof PercentageT )
		{
			return ( (PercentageT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof PriceT )
		{
			return ( (PriceT) parameter ).getMaxValue();
		}
		else if ( parameter instanceof QtyT )
		{
			return ( (QtyT) parameter ).getMaxValue();
		}
//		else if ( parameter instanceof IntT )
//		{
//			
//			IntT intT = (IntT) parameter;
//
//			if ( intT.getMaxValue() != null )
//			{
//				return intT.getMinValue();
//			}
//
//		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns the value of Parameter.getConstValue() for the specific NumericT types for which this is
	 * applicable, assuming it has been set, otherwise returns null.
	 * 
	 * @return
	 */
	public BigDecimal getConstValue()
	{
		if ( parameter instanceof FloatT )
		{
			return ( (FloatT) parameter ).getConstValue();
		}
		else if ( parameter instanceof AmtT )
		{
			return ( (AmtT) parameter ).getConstValue();
		}
		else if ( parameter instanceof PercentageT )
		{
			return ( (PercentageT) parameter ).getConstValue();
		}
		else if ( parameter instanceof PriceOffsetT )
		{
			return ( (PriceOffsetT) parameter ).getConstValue();
		}
		else if ( parameter instanceof PriceT )
		{
			return ( (PriceT) parameter ).getConstValue();
		}
		else if ( parameter instanceof QtyT )
		{
			return ( (QtyT) parameter ).getConstValue();
		}
		else
		{
			return null;
		}
	}	
}