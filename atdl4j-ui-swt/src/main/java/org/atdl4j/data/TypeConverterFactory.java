package org.atdl4j.data;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.BooleanT;
import org.atdl4j.atdl.core.CharT;
import org.atdl4j.atdl.core.CurrencyT;
import org.atdl4j.atdl.core.DataT;
import org.atdl4j.atdl.core.ExchangeT;
import org.atdl4j.atdl.core.IntT;
import org.atdl4j.atdl.core.LengthT;
import org.atdl4j.atdl.core.LocalMktDateT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.MultipleCharValueT;
import org.atdl4j.atdl.core.MultipleStringValueT;
import org.atdl4j.atdl.core.NumInGroupT;
import org.atdl4j.atdl.core.NumericT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.SeqNumT;
import org.atdl4j.atdl.core.StringT;
import org.atdl4j.atdl.core.TagNumT;
import org.atdl4j.atdl.core.UTCDateOnlyT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimestampT;
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
import org.atdl4j.data.converter.BooleanConverter;
import org.atdl4j.data.converter.DateTimeConverter;
import org.atdl4j.data.converter.DecimalConverter;
import org.atdl4j.data.converter.IntegerConverter;
import org.atdl4j.data.converter.StringConverter;

/*
 * NumericT subclasses
 import org.atdl4j.atdl.core.AmtT;
 import org.atdl4j.atdl.core.FloatT;
 import org.atdl4j.atdl.core.PercentageT;
 import org.atdl4j.atdl.core.PriceT;
 import org.atdl4j.atdl.core.PriceOffsetT;
 import org.atdl4j.atdl.core.QtyT;
 */

/**
 * Factory that creates the appropriate ParameterUI depending on the parameter
 * control type and value type.
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
	public static TypeConverter<?> create(ParameterT parameter) throws JAXBException
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
			throw new JAXBException( "Unsupported ParameterT type: " + parameter.getClass().getName() );
	}

	/*
	 * Create adapter based on ControlT (native type for each control)
	 */
	public static TypeConverter<?> create(ControlT control, ParameterT parameter) throws JAXBException
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
			
			return new StringConverter();
		}
		else if ( control instanceof SliderT || control instanceof SingleSpinnerT || control instanceof DoubleSpinnerT )
		{
// 2/12/2010 Scott Atwell			return new NumberConverter();
			if ( parameter instanceof NumericT )  // all of the Decimal types presently extend NumericT
			{
				return new DecimalConverter();
			}
			else
			{
				return new IntegerConverter();
			}
		}
		else if ( control instanceof CheckBoxT || control instanceof RadioButtonT )
		{
			return new BooleanConverter();
		}
		else if ( control instanceof ClockT )
		{
			return new DateTimeConverter( parameter );
		}
		else
			throw new JAXBException( "Unsupported ControlT type: " + control.getClass().getName() );
	}
}
