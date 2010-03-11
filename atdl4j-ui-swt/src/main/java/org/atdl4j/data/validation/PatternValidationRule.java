package org.atdl4j.data.validation;

import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.data.exception.ValidationException;

/**
 * Validator that validates input against a regular expression.
 * 
 * @author renato.gallart
 */
public class PatternValidationRule
		implements ValidationRule
{

	private static final Logger logger = Logger.getLogger( PatternValidationRule.class );

	private String field;

	private String pattern;

	public PatternValidationRule(String field, String pattern)
	{
		this.field = field;
		this.pattern = pattern;

		String tempMsg = "PatternValidationRule constructor: field: " + field + " pattern: " + pattern;
		logger.debug( tempMsg );
		logger.trace( tempMsg, new Exception( "Stack trace" ) );
	}

	public void validate(Map<String, ValidationRule> refRules, Map<String, ControlUI<?>> targets) throws ValidationException, JAXBException
	{

		// get the widget from context using field name
		ControlUI<?> target = targets.get( field );
		if ( target == null )
		{
			String tempMsg = "No parameter defined for field \"" + field + "\" in this context (PatternValidationRule) field: " + field + " pattern: "
					+ pattern;
			logger.debug( tempMsg );
			logger.trace( tempMsg, new Exception( "Stack trace" ) );

			throw new JAXBException( tempMsg );
		}

		// PatternRules always validate against a parameter,
		// so no need to fetch control value
		String value = target.getParameterValueAsString();
		if ( value != null && !Pattern.matches( this.pattern, value ) )
		{
			throw new ValidationException( target, "Rule tested: [" + value + " pattern match: " + this.pattern + "]" );
		}
	}
}
