package org.atdl4j.data.converter;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.LocalMktDateT;
import org.atdl4j.atdl.core.MonthYearT;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.UTCDateOnlyT;
import org.atdl4j.atdl.core.UTCTimeOnlyT;
import org.atdl4j.atdl.core.UTCTimestampT;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeConverter extends AbstractTypeConverter<DateTime> {

	// TODO: implement Tz handling
	// private String localMktTz;

	// public DateConverter() {
	// }

	public DateTimeConverter(ParameterT parameter) {
		this.parameter = parameter;
	}

	private String getFormatString() {
		if (parameter != null) {
			if (parameter instanceof LocalMktDateT) {
				return "yyyyMMdd";
			} else if (parameter instanceof MonthYearT) {
				return "yyyyMM";
			} else if (parameter instanceof UTCDateOnlyT) {
				return "yyyyMMdd";
			} else if (parameter instanceof UTCTimeOnlyT) {
				return "HH:mm:ss";
			} else if (parameter instanceof UTCTimestampT) {
				return "yyyyMMdd-HH:mm:ss";
			}
			// TODO: Uncomment when TZTimestamp / TZTimeOnly becomes available
			/*
			 * else if (parameter instanceof TZTimeOnlyT) { return "HH:mm:ssZZ";
			 * } else if (parameter instanceof TZTimestampT) { return
			 * "yyyyMMdd-HH:mm:ssZZ"; }
			 */
		}
		return "yyyyMMdd-HH:mm:ss";
	}

// 2/12/2010	public DateTime convertValueToComparable(Object value) throws JAXBException
	public DateTime convertValueToParameterComparable(Object value) throws JAXBException
	{
		if (value instanceof DateTime) {
			return (DateTime) value;
		}
		if (value instanceof String) {
			String str = (String) value;
			String format = getFormatString();
			DateTimeFormatter fmt = DateTimeFormat.forPattern(format);

			try {
				if (parameter == null || parameter instanceof UTCTimeOnlyT
						|| parameter instanceof UTCTimestampT) {
					return fmt.withZone(DateTimeZone.UTC).parseDateTime(str);
				}

				/*
				 * else if (parameter instanceof TZTimestamp || parameter
				 * instanceof TZTimeOnlyT) { return
				 * fmt.withOffsetParsed().parseDateTime(str); }
				 */
				else {
					return fmt.parseDateTime(str);
				}

			} catch (IllegalArgumentException e) {
				throw new JAXBException("Unable to parse \"" + str
						+ "\" with format \"" + format + "\"");
			}
		}
		return null;
	}

	public DateTime convertValueToControlComparable(Object value) throws JAXBException
	{
		return convertValueToParameterComparable(value);
	}

	public String convertValueToParameterString(Object value) throws JAXBException {
		DateTime date = convertValueToParameterComparable(value); // TODO: this doesn't
															// currently return
															// null
		if (date != null) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(getFormatString());
			return fmt.print(date);
		}
		return null;
	}
	
	public String convertValueToControlString(Object value) throws JAXBException {
		DateTime date = convertValueToControlComparable(value); // TODO: this doesn't
															// currently return
															// null
		if (date != null) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(getFormatString());
			return fmt.print(date);
		}
		return null;
	}

}
