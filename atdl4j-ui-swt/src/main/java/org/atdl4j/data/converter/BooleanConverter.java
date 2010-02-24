package org.atdl4j.data.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.atdl4j.fixatdl.core.BooleanT;

public class BooleanConverter
		extends AbstractTypeConverter<Boolean>
{

	public static final String BOOLEAN_FALSE = "N";
	public static final String BOOLEAN_TRUE = "Y";

	public BooleanConverter()
	{
	}

	public BooleanConverter(BooleanT parameter)
	{
		this.parameter = parameter;
	}

	// TODO: improve handling of null values (null is a valid output)
	// 2/12/2010 public Boolean convertValueToComparable(Object value)
	public Boolean convertValueToParameterComparable(Object value)
	{
		if ( value == null )
			return null;

		if ( value instanceof Boolean )
		{
			return (Boolean) value;
		}
		if ( value instanceof String )
		{
			String str = (String) value;
			if ( str.equalsIgnoreCase( "true" ) || str.equals( "1" ) || str.equals( BooleanConverter.BOOLEAN_TRUE ) )
			{
				return new Boolean( true );
			}
			else if ( str.equalsIgnoreCase( "false" ) || str.equals( "0" ) || str.equals( BooleanConverter.BOOLEAN_FALSE ) )
			{
				return new Boolean( false );
			}
			else if ( str.equals( "" ) )
			{
				return null;
			}
			else
			{
				return new Boolean( false );
			}
		}
		if ( value instanceof BigDecimal || value instanceof BigInteger )
		{
			BigDecimal num = (BigDecimal) value;
			if ( num.intValue() == 1 )
			{
				return new Boolean( true );
			}
			else if ( num.intValue() == 0 )
			{
				return new Boolean( false );
			}
			else
			{
				return new Boolean( false );
			}
		}
		else
			return new Boolean( false );
		// TODO: is this an error??--I think it should be.
		// Dates are always false
	}

	public Boolean convertValueToControlComparable(Object value)
	{
		return convertValueToParameterComparable( value );
	}

	// 2/12/2010 public String convertValueToString(Object value)
	public String convertValueToParameterString(Object value)
	{
		// TODO: cleanup
		BooleanT booleanT = null;
		if ( parameter != null && parameter instanceof BooleanT )
		{
			booleanT = (BooleanT) parameter;
		}

		Boolean bool = convertValueToParameterComparable( value ); // TODO: this
																						// doesn't
																						// currently
																						// return null

		// 2/1/2010 John Shields added
		if ( bool != null )
		{
			return bool.booleanValue() ? BOOLEAN_TRUE : BOOLEAN_FALSE;
		}
		else
			return null;

		// 2/1/2010 John Shields deleted
		// trueWireValue and falseWireValue are deprecated
		/*
		 * if (bool != null) { if (bool.booleanValue()) { if (booleanT != null &&
		 * booleanT.getTrueWireValue() != null) return
		 * booleanT.getTrueWireValue(); else return BOOLEAN_TRUE; } else { if
		 * (booleanT != null && booleanT.getFalseWireValue() != null) return
		 * booleanT.getFalseWireValue(); else return BOOLEAN_FALSE; } } else {
		 * return null; }
		 */
	}

	public String convertValueToControlString(Object value)
	{
		// TODO: cleanup
		BooleanT booleanT = null;
		if ( parameter != null && parameter instanceof BooleanT )
		{
			booleanT = (BooleanT) parameter;
		}

		Boolean bool = convertValueToControlComparable( value ); // TODO: this
																					// doesn't
																					// currently
																					// return null

		// 2/1/2010 John Shields added
		if ( bool != null )
		{
			return bool.booleanValue() ? BOOLEAN_TRUE : BOOLEAN_FALSE;
		}
		else
			return null;

		// 2/1/2010 John Shields deleted
		// trueWireValue and falseWireValue are deprecated
		/*
		 * if (bool != null) { if (bool.booleanValue()) { if (booleanT != null &&
		 * booleanT.getTrueWireValue() != null) return
		 * booleanT.getTrueWireValue(); else return BOOLEAN_TRUE; } else { if
		 * (booleanT != null && booleanT.getFalseWireValue() != null) return
		 * booleanT.getFalseWireValue(); else return BOOLEAN_FALSE; } } else {
		 * return null; }
		 */
	}
}
