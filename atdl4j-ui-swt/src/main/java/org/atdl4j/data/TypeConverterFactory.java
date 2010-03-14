package org.atdl4j.data;

import org.atdl4j.data.converter.BooleanConverter;
import org.atdl4j.data.converter.DatatypeConverter;
import org.atdl4j.data.converter.DateTimeConverter;
import org.atdl4j.data.converter.DecimalConverter;
import org.atdl4j.data.converter.IntegerConverter;
import org.atdl4j.data.converter.StringConverter;
import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.core.CharT;
import org.atdl4j.fixatdl.core.CurrencyT;
import org.atdl4j.fixatdl.core.DataT;
import org.atdl4j.fixatdl.core.ExchangeT;
import org.atdl4j.fixatdl.core.IntT;
import org.atdl4j.fixatdl.core.LengthT;
import org.atdl4j.fixatdl.core.LocalMktDateT;
import org.atdl4j.fixatdl.core.MonthYearT;
import org.atdl4j.fixatdl.core.MultipleCharValueT;
import org.atdl4j.fixatdl.core.MultipleStringValueT;
import org.atdl4j.fixatdl.core.NumInGroupT;
import org.atdl4j.fixatdl.core.NumericT;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.SeqNumT;
import org.atdl4j.fixatdl.core.StringT;
import org.atdl4j.fixatdl.core.TagNumT;
import org.atdl4j.fixatdl.core.UTCDateOnlyT;
import org.atdl4j.fixatdl.core.UTCTimeOnlyT;
import org.atdl4j.fixatdl.core.UTCTimestampT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.ClockT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.HiddenFieldT;
import org.atdl4j.fixatdl.layout.LabelT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.fixatdl.layout.SliderT;
import org.atdl4j.fixatdl.layout.TextFieldT;

/*
 * NumericT subclasses
 import org.atdl4j.fixatdl.core.AmtT;
 import org.atdl4j.fixatdl.core.FloatT;
 import org.atdl4j.fixatdl.core.PercentageT;
 import org.atdl4j.fixatdl.core.PriceT;
 import org.atdl4j.fixatdl.core.PriceOffsetT;
 import org.atdl4j.fixatdl.core.QtyT;
 */

/**
 * Factory that creates the appropriate ParameterTypeConveter depending on the parameter
 * or creates the appropriate ControlTypeConveter depending upon the control type.
 * 
 * Note that all UI widgets in ATDL are strongly typed.
 * 
 */
// 2/12/2010 (no one was extending this, simply contains static methods)  public abstract class TypeConverterFactory
public class TypeConverterFactory
{

	/*
	 * Create adapter based on ParameterT
	 */
// 3/10/2010 Scott Atwell	public static TypeConverter<?> create(ParameterT parameter) throws JAXBException
	public static ParameterTypeConverter<?> createParameterTypeConverter(ParameterT parameter)
	{
		if ( parameter instanceof StringT || parameter instanceof CharT || parameter instanceof MultipleCharValueT
				|| parameter instanceof MultipleStringValueT || parameter instanceof CurrencyT || parameter instanceof ExchangeT
				|| parameter instanceof DataT )
		{
			return new StringConverter( parameter );
		}
		else if ( parameter instanceof NumericT )
		{
// 2/12/2010 Scott Atwell			return new NumberConverter( parameter ); // Float field
			return new DecimalConverter( parameter ); // Float field
		}
		else if ( parameter instanceof IntT || parameter instanceof NumInGroupT || parameter instanceof SeqNumT || parameter instanceof TagNumT
				|| parameter instanceof LengthT )
		{
// 2/12/2010 Scott Atwell			return new NumberConverter( parameter ); // Integer field
			return new IntegerConverter( parameter ); // Integer field
		}
		else if ( parameter instanceof BooleanT )
		{
			return new BooleanConverter( (BooleanT) parameter );
		}
		else if ( parameter instanceof MonthYearT || parameter instanceof UTCTimestampT || parameter instanceof UTCTimeOnlyT
				|| parameter instanceof LocalMktDateT || parameter instanceof UTCDateOnlyT )
		{
			return new DateTimeConverter( parameter );
		}
		else
		{
// 3/10/2010 Scott Atwell			throw new JAXBException( "Unsupported ParameterT type: " + parameter.getClass().getName() );
			throw new IllegalArgumentException( "Unsupported ParameterT type: " + parameter.getClass().getName() );
		}
	}

	/*
	 * Create adapter based on ControlT (native type for each control)
	 */
// 3/10/2010 Scott Atwell	public static TypeConverter<?> create(ControlT control, ParameterT parameter) throws JAXBException
// 3/11/2010 Scott Atwell	public static ControlTypeConverter<?> createControlTypeConverter(ControlT control, ParameterT parameter)
	public static ControlTypeConverter<?> createControlTypeConverter(ControlT control, ParameterTypeConverter<?> aParameterTypeConverter)
	{
//TODO 2/21/2010 Scott Atwell Added
//		TypeConverter<?> tempParameterConverter = create( parameter );
		
		if ( control instanceof TextFieldT || control instanceof SingleSelectListT || control instanceof MultiSelectListT
				|| control instanceof CheckBoxListT || control instanceof DropDownListT || control instanceof EditableDropDownListT
				|| control instanceof RadioButtonListT || control instanceof HiddenFieldT || control instanceof LabelT )
		{
// 2/12/2010 Scott Atwell			return new StringConverter();
//			if ( ( tempParameterConverter instanceof DecimalConverter ) ||
//				  ( tempParameterConverter instanceof IntegerConverter ) )
//			{
//				return tempParameterConverter;
//			}
//TODO 2/12/2010 -- TODO Need to handle adjusting a PercentageT of "9999=.1234" from loadFixMessage() to put "12.34" in TextFieldT if PercentageT@multiplyBy100 is true. 
			
// 3/10/2010 Scott Atwell			return new StringConverter();
// 3/11/2010 Scott Atwell			return new StringConverter( parameter );
			return new StringConverter( aParameterTypeConverter );
		}
/*** 3/14/2010 Scott Atwell SliderWidget takes a "String" for enumID
		else if ( control instanceof SliderT || control instanceof SingleSpinnerT || control instanceof DoubleSpinnerT )
		{
// 2/12/2010 Scott Atwell			return new NumberConverter();
// ??? 3/11/2010 Scott Atwell			if ( parameter instanceof NumericT )  // all of the Decimal types presently extend NumericT
			if ( ( aParameterTypeConverter != null ) &&
					( aParameterTypeConverter.getParameter() instanceof NumericT ) )  // all of the Decimal types presently extend NumericT
			{
// 3/10/2010 Scott Atwell				return new DecimalConverter();
// 3/11/2010 Scott Atwell				return new DecimalConverter( parameter );
				return new DecimalConverter( aParameterTypeConverter );
			}
			else
			{
// 3/10/2010 Scott Atwell				return new IntegerConverter();
// 3/11/2010 Scott Atwell				return new IntegerConverter( parameter );
				return new IntegerConverter( aParameterTypeConverter );
			}
***/			
		else if ( control instanceof SliderT )
		{
			return new StringConverter( aParameterTypeConverter );
		}
		else if ( control instanceof SingleSpinnerT || control instanceof DoubleSpinnerT )
		{
// 2/12/2010 Scott Atwell			return new NumberConverter();
// ??? 3/11/2010 Scott Atwell			if ( parameter instanceof NumericT )  // all of the Decimal types presently extend NumericT
			if ( ( aParameterTypeConverter != null ) &&
					( aParameterTypeConverter.getParameter() instanceof NumericT ) )  // all of the Decimal types presently extend NumericT
			{
// 3/10/2010 Scott Atwell				return new DecimalConverter();
// 3/11/2010 Scott Atwell				return new DecimalConverter( parameter );
				return new DecimalConverter( aParameterTypeConverter );
			}
			else
			{
// 3/10/2010 Scott Atwell				return new IntegerConverter();
// 3/11/2010 Scott Atwell				return new IntegerConverter( parameter );
				return new IntegerConverter( aParameterTypeConverter );
			}
		}
		else if ( control instanceof CheckBoxT || control instanceof RadioButtonT )
		{
// 3/10/2010 Scott Atwell			return new BooleanConverter();
// 3/11/2010 Scott Atwell			return new BooleanConverter( (BooleanT) parameter );
			return new BooleanConverter( (BooleanT) aParameterTypeConverter );
		}
		else if ( control instanceof ClockT )
		{
// 3/11/2010 Scott Atwell			return new DateTimeConverter( parameter );
			return new DateTimeConverter( aParameterTypeConverter );
		}
		else
		{
// 3/10/2010 Scott Atwell			throw new JAXBException( "Unsupported ControlT type: " + control.getClass().getName() );
			throw new IllegalArgumentException( "Unsupported ControlT type: " + control.getClass().getName() );
		}
	}
	


	/*
	 * Returns an Object that is an instanceof the Parameter's base data type (eg String, BigDecimal, DateTime, etc)
	 */
// 3/12/2010 Scott Atwell added	
	public static Object getParameterDatatype(ParameterT aParameter)
	{
		if ( aParameter instanceof StringT || aParameter instanceof CharT || aParameter instanceof MultipleCharValueT
				|| aParameter instanceof MultipleStringValueT || aParameter instanceof CurrencyT || aParameter instanceof ExchangeT
				|| aParameter instanceof DataT )
		{
			return DatatypeConverter.DATATYPE_STRING;
		}
		else if ( aParameter instanceof NumericT )
		{
			return DatatypeConverter.DATATYPE_BIG_DECIMAL; // Float field
		}
		else if ( aParameter instanceof IntT || aParameter instanceof NumInGroupT || aParameter instanceof SeqNumT || aParameter instanceof TagNumT
				|| aParameter instanceof LengthT )
		{
			return DatatypeConverter.DATATYPE_BIG_INTEGER; // Integer field
		}
		else if ( aParameter instanceof BooleanT )
		{
			return DatatypeConverter.DATATYPE_BOOLEAN; 
		}
		else if ( aParameter instanceof MonthYearT )
		{
			return DatatypeConverter.DATATYPE_STRING;
		}
		else if ( aParameter instanceof UTCTimestampT || aParameter instanceof UTCTimeOnlyT
				|| aParameter instanceof LocalMktDateT || aParameter instanceof UTCDateOnlyT )
		{
			return DatatypeConverter.DATATYPE_XML_GREGORIAN_CALENDAR;
		}
		else
		{
// 3/10/2010 Scott Atwell			throw new JAXBException( "Unsupported ParameterT type: " + aParameter.getClass().getName() );
			throw new IllegalArgumentException( "Unsupported ParameterT type: " + aParameter.getClass().getName() );
		}
	}

}
