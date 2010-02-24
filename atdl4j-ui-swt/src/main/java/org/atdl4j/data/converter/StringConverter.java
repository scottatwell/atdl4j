package org.atdl4j.data.converter;

import org.atdl4j.fixatdl.core.MultipleCharValueT;
import org.atdl4j.fixatdl.core.MultipleStringValueT;
import org.atdl4j.fixatdl.core.ParameterT;

public class StringConverter extends AbstractTypeConverter<String> {
	
	public StringConverter(ParameterT parameter) {
		this.parameter = parameter;
	}
	
	public StringConverter() {
	}

// 2/12/2010	public String convertValueToComparable(Object value)
	public String convertValueToParameterComparable(Object value)
	{
		return (value == null || "".equals(value)) ? null : value.toString();
	}

	public String convertValueToControlComparable(Object value)
	{
		return convertValueToParameterComparable(value);
	}

	public String convertValueToParameterString(Object value)
	{
		String str = convertValueToParameterComparable(value);		
		if (str != null)
		{
			if (parameter instanceof MultipleCharValueT && 
					((MultipleCharValueT)parameter).isInvertOnWire())
				return invertOnWire(str);
			
			else if (parameter instanceof MultipleStringValueT && 
					((MultipleStringValueT)parameter).isInvertOnWire())
				return invertOnWire(str);
			
			return str;
		}
		return null;
	}

	public String convertValueToControlString(Object value)
	{
		String str = convertValueToControlComparable(value);		
		if (str != null)
		{
			if (parameter instanceof MultipleCharValueT && 
					((MultipleCharValueT)parameter).isInvertOnWire())
				return invertOnWire(str);
			
			else if (parameter instanceof MultipleStringValueT && 
					((MultipleStringValueT)parameter).isInvertOnWire())
				return invertOnWire(str);
			
			return str;
		}
		return null;
	}

	private static String invertOnWire(String text) {
		StringBuffer invertedString = new StringBuffer();

		int startIndex = text.lastIndexOf(" ");
		int endIndex = text.length();

		do {
			invertedString.append(text.substring(startIndex + 1, endIndex));
			if (startIndex == -1) {
				return invertedString.toString();
			} else {
				invertedString.append(" ");
			}
			endIndex = startIndex;
			startIndex = (text.substring(0, endIndex)).lastIndexOf(" ");
		} while (endIndex != -1);

		return invertedString.toString();
	}
}
