package org.atdl4j.data.converter;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.atdl4j.atdl.core.LocalMktDateT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.UTCDateOnlyT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimestampT;
import org.atdl4j.atdl.timezones.Timezone;
import org.atdl4j.data.ParameterHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeConverter
		extends AbstractTypeConverter<DateTime>
{
// 2/15/2010 Scott Atwell added	
	Timezone timezone = null;
	
	public DateTimeConverter(ParameterT parameter)
	{
		this.parameter = parameter;
		
		setTimezone( ParameterHelper.getLocalMktTz( parameter ) );
	}

	private String getFormatString()
	{
		if ( parameter != null )
		{
			if ( parameter instanceof LocalMktDateT )
			{
				return "yyyyMMdd";
			}
			else if ( parameter instanceof MonthYearT )
			{
				return "yyyyMM";
			}
			else if ( parameter instanceof UTCDateOnlyT )
			{
				return "yyyyMMdd";
			}
			else if ( parameter instanceof UTCTimeOnlyT )
			{
				return "HH:mm:ss";
			}
			else if ( parameter instanceof UTCTimestampT )
			{
				return "yyyyMMdd-HH:mm:ss";
			}
			// TODO: Uncomment when TZTimestamp / TZTimeOnly becomes available
			/*
			 * else if (parameter instanceof TZTimeOnlyT) { return "HH:mm:ssZZ"; }
			 * else if (parameter instanceof TZTimestampT) { return
			 * "yyyyMMdd-HH:mm:ssZZ"; }
			 */
		}
		return "yyyyMMdd-HH:mm:ss";
	}

	public DateTime convertValueToParameterComparable(Object value) throws JAXBException
	{
		if ( value instanceof DateTime )
		{
			return (DateTime) value;
		}
// 2/16/2010 Scott Atwell Added (when handling dailyConstValue)		
// 2/23/2010 Scott Atwell Added (when handling 'daily' format for constValue)		
		else if ( value instanceof XMLGregorianCalendar )
		{
			return convertXMLGregorianCalendarToDateTime( (XMLGregorianCalendar) value, getTimezone() );
		}
		else if ( value instanceof String )
		{
			String str = (String) value;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormat.forPattern( format );

			try
			{  
				if ( parameter == null || parameter instanceof UTCTimeOnlyT || parameter instanceof UTCTimestampT )
				{
// 2/16/2010 Scott Atwell makes 06:30 CT come back 06:30 UTC					return fmt.withZone( DateTimeZone.UTC ).parseDateTime( str );
					DateTime tempDateTime = fmt.parseDateTime( str ); 
					return tempDateTime.withZone( DateTimeZone.UTC );
				}

				/*
				 * else if (parameter instanceof TZTimestamp || parameter instanceof
				 * TZTimeOnlyT) { return fmt.withOffsetParsed().parseDateTime(str);
				 * }
				 */
				else
				{
					return fmt.parseDateTime( str );
				}

			}
			catch (IllegalArgumentException e)
			{
				throw new JAXBException( "Unable to parse \"" + str + "\" with format \"" + format + "\"" );
			}
		}
		return null;
	}

	public DateTime convertValueToControlComparable(Object value) throws JAXBException
	{
		return convertValueToParameterComparable( value );
	}

	public String convertValueToParameterString(Object value) throws JAXBException
	{
		DateTime date = convertValueToParameterComparable( value ); 
		
		if ( date != null )
		{
			DateTimeFormatter fmt = DateTimeFormat.forPattern( getFormatString() );
// 2/15/2010 Scott Atwell			return fmt.print( date );
			return fmt.withZone( DateTimeZone.UTC ).print( date );
		}
		return null;
	}

	public String convertValueToControlString(Object value) throws JAXBException
	{
		DateTime date = convertValueToControlComparable( value ); 
		
		if ( date != null )
		{
			DateTimeFormatter fmt = DateTimeFormat.forPattern( getFormatString() );
// 2/15/2010 Scott Atwell			return fmt.print( date );
			return fmt.withZone( DateTimeZone.getDefault() ).print( date );
		}
		return null;
	}

	public static DateTimeZone convertTimezoneToDateTimeZone( Timezone aTimezone )
	{
		if ( aTimezone != null )
		{
			return DateTimeZone.forID( aTimezone.value() );
		}
		else
		{
			return null;
		}
	}
	
	public static DateTime convertXMLGregorianCalendarToDateTime( XMLGregorianCalendar aXMLGregorianCalendar, Timezone aTimezone )
	{
		// -- DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) --
		int tempSubsecond = 0;
		if ( aXMLGregorianCalendar.getFractionalSecond() != null )
		{
			tempSubsecond = aXMLGregorianCalendar.getFractionalSecond().intValue();
		}
		
		DateTimeZone tempDateTimeZone = convertTimezoneToDateTimeZone( aTimezone );
		if ( tempDateTimeZone == null )
		{
			tempDateTimeZone = DateTimeZone.getDefault();
		}
		
		return new DateTime( aXMLGregorianCalendar.getYear(), 
									aXMLGregorianCalendar.getMonth(),
									aXMLGregorianCalendar.getDay(),
									aXMLGregorianCalendar.getHour(),
									aXMLGregorianCalendar.getMinute(),
									aXMLGregorianCalendar.getSecond(),
									tempSubsecond, 
									tempDateTimeZone );
	}

	public static XMLGregorianCalendar convertDailyValueToValue( XMLGregorianCalendar aDailyValue, Timezone aTimezone )
	{
		// -- Note that the XMLGregorianCalendar does not default to current month, day, year --
		if ( aDailyValue != null )
		{
			// -- Init calendar date portion equal to "current date" local/default --
			DateTime tempDateTime = new DateTime();
			
			if ( aTimezone != null )
			{
				DateTimeZone tempDateTimeZone = DateTimeZone.forID( aTimezone.value() );
				if ( tempDateTimeZone != null )
				{
					int tempOffsetMillis = tempDateTimeZone.getOffset( System.currentTimeMillis() );
					// -- convert milliseconds to minutes --
					aDailyValue.setTimezone( tempOffsetMillis / 60000 );
					
					// -- Set calendar date portion equal to "current date" of the Timezone --
					// -- (eg Asian security trading in Japan during the morning of Feb 15 might be local of 9pm ET Feb 14.  
					// -- 	Want to ensure we use Feb 15, not Feb 14 if localMktTz is for Japan) --
					tempDateTime = new DateTime( tempDateTimeZone );
				}
			}
			
			aDailyValue.setMonth( tempDateTime.getMonthOfYear() );
			aDailyValue.setDay( tempDateTime.getDayOfMonth() );
			aDailyValue.setYear( tempDateTime.getYear() );
		}
		
		return aDailyValue;
	}


	/**
	 * @return the timezone
	 */
	public Timezone getTimezone()
	{
		return this.timezone;
	}

	/**
	 * @param aTimezone the timezone to set
	 */
	public void setTimezone(Timezone aTimezone)
	{
		this.timezone = aTimezone;
	}
}
