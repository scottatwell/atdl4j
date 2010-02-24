package org.atdl4j.data.converter;

import java.math.BigInteger;

import org.atdl4j.fixatdl.core.IntT;
import org.atdl4j.fixatdl.core.LengthT;
import org.atdl4j.fixatdl.core.NumInGroupT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.SeqNumT;
import org.atdl4j.fixatdl.core.TagNumT;

/*
 * Supports Integer or BigInteger-based Parameter types:
 * 		IntT
 * 		NumInGroupT
 * 		SeqNumT
 * 		TagNumT
 * 		LengthT
 *
 * @author Scott Atwell
 */
public class IntegerConverter
		extends AbstractTypeConverter<BigInteger>
{

	public IntegerConverter()
	{
	}

	public IntegerConverter(ParameterT parameter)
	{
		this.parameter = parameter;
	}

// 2/12/2010	public BigInteger convertValueToComparable(Object value)
	public BigInteger convertValueToParameterComparable(Object value)
	{
		if ( value instanceof BigInteger )
		{
			return (BigInteger) value;
		}
		else if ( value instanceof Integer )
		{
			return new BigInteger( ((Integer) value).toString() );
		}
		else if ( value instanceof String )
		{
			String str = (String) value;
// 2/12/2010			try
//			{
//				return new BigInteger( str );
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
					return new BigInteger( str );
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException( "Invalid Integer Number Format: [" + str + "] for Parameter: " + getParameter().getName() );
				}
			}

		}
		else if ( value instanceof Boolean )
		{
			Boolean bool = (Boolean) value;
			if ( bool != null )
			{
				if ( bool )
					return new BigInteger( "1" );
				else
					return new BigInteger( "0" );
			}
			else
				return null;
		}
		return null;
	}

	public BigInteger convertValueToControlComparable(Object value)
	{
		return convertValueToParameterComparable(value);
	}

	/* 
	 * Returns string value, formatted if so specified and applicable.
	 */
	public String convertValueToParameterString(Object value)
	{
		BigInteger num = convertValueToParameterComparable( value ); 
		if ( num == null )
			return null;
		else
			return num.toString();
	}
	
	/* 
	 * Returns string value, formatted so specified and applicable.
	 */
	public String convertValueToControlString(Object value)
	{
		BigInteger num = convertValueToControlComparable( value ); 
		if ( num == null )
			return null;
		else
			return num.toString();
	}

	/**
	 * Returns the value of Parameter.getMinValue() for the specific NumericT types for which this is
	 * applicable, assuming it has been set, otherwise returns null.
	 * 
	 * @return
	 */
	public BigInteger getMinValue()
	{
		if ( parameter instanceof IntT )
		{
//			return ( (IntT) parameter ).getMinValue();
			// -- upcast IntT from Integer to BigInteger --
			if ( ( (IntT) parameter ).getMinValue() != null )
			{
				return new BigInteger( ( (IntT) parameter ).getMinValue().toString() );
			}
			else
			{
				return null;
			}
		}
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
	public BigInteger getMaxValue()
	{
		if ( parameter instanceof IntT )
		{
//			return ( (IntT) parameter ).getMaxValue();
			// -- upcast IntT from Integer to BigInteger --
			if ( ( (IntT) parameter ).getMaxValue() != null )
			{
				return new BigInteger( ( (IntT) parameter ).getMaxValue().toString() );
			}
			else
			{
				return null;
			}
		}
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
	public BigInteger getConstValue()
	{
		if ( parameter instanceof IntT )
		{
			// -- upcast IntT from Integer to BigInteger --
			if ( ( (IntT) parameter ).getConstValue() != null )
			{
				return new BigInteger( ( (IntT) parameter ).getConstValue().toString() );
			}
			else
			{
				return null;
			}
		}
		else if ( parameter instanceof NumInGroupT )
		{
			return ( (NumInGroupT) parameter ).getConstValue();
		}
		else if ( parameter instanceof SeqNumT )
		{
			return ( (SeqNumT) parameter ).getConstValue();
		}
		else if ( parameter instanceof TagNumT )
		{
			return ( (TagNumT) parameter ).getConstValue();
		}
		else if ( parameter instanceof LengthT )
		{
			return ( (LengthT) parameter ).getConstValue();
		}
		else
		{
			return null;
		}
	}	
}