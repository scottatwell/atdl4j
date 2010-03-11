package org.atdl4j.data.validation;

import org.apache.log4j.Logger;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.ControlUI;

import org.atdl4j.fixatdl.validation.OperatorT;

/**
 * Base class for ValidationRule.
 * 
 * @param <E>
 * 
 * @author renato.gallart
 */
public abstract class AbstractOperatorValidationRule
		implements ValidationRule
{

	private static final Logger logger = Logger.getLogger( AbstractOperatorValidationRule.class );

	@SuppressWarnings("unchecked")
	protected void validateValues(ControlUI<?> target, Object value1, OperatorT operator, Object value2)
	{
		switch ( operator )
		{
			case NE :
				if ( value1 == value2 || ( value2 != null && value2.equals( value1 ) ) )
				{
					throw new ValidationException( target, "Rule tested: [" + value1 + " NE " + value2 + "]" );
				}
				break;

			case EX :
				if ( value1 == null || "".equals( value1.toString() ) )
				{
					throw new ValidationException( target, "Rule tested: [" + value1 + " EX]" );
				}
				break;

			case LT :
				if ( value1 instanceof Comparable )
				{
					Comparable c = (Comparable) value1;
					if ( c.compareTo( value2 ) >= 0 )
					{
						throw new ValidationException( target, "Rule tested: [" + value1 + " LT " + value2 + "]" );
					}
				}
				else
				{
					throw new ValidationException( target, "Value is not comparable [" + value1 + " class: " + (value1 != null ? value1.getClass() : "") + "]" );
				}
				break;

			case LE :
				if ( value1 instanceof Comparable )
				{
					Comparable c = (Comparable) value1;
					if ( c.compareTo( value2 ) > 0 )
					{
						throw new ValidationException(target, "Rule tested: [" + value1 + " LE " + value2 + "]" );
					}
				}
				else
				{
					throw new ValidationException( target, "Value is not comparable [" + value1 + " class: " + (value1 != null ? value1.getClass() : "") + "]" );
				}
				break;

			case GT :
				if ( value1 instanceof Comparable )
				{
					Comparable c = (Comparable) value1;
					if ( c.compareTo( value2 ) <= 0 )
					{
						throw new ValidationException(target, "Rule tested: [" + value1 + " GT " + value2 + "]" );
					}
				}
				else
				{
					throw new ValidationException( target, "Value is not comparable [" + value1 + " class: " + (value1 != null ? value1.getClass() : "") + "]" );
				}
				break;

			case GE :
				if ( value1 instanceof Comparable )
				{
					Comparable c = (Comparable) value1;
					if ( c.compareTo( value2 ) < 0 )
					{
						throw new ValidationException(target, "Rule tested: [" + value1 + " GE " + value2 + "]" );
					}
				}
				else
				{
					throw new ValidationException( target, "Value is not comparable [" + value1 + " class: " + (value1 != null ? value1.getClass() : "") + "]" );
				}
				break;

			case EQ :
				if ( !value2.equals( value1 ) )
				{
					throw new ValidationException(target, "Rule tested: [" + value1 + " EQ " + value2 + "]" );
				}
				break;

			case NX :
				if ( value1 != null && !"".equals( value1.toString() ) )
				{
					throw new ValidationException(target, "Rule tested: [" + value1 + " NX]" );
				}
				break;

			default:
				// Supposed to never happen, since the schema enforces an enumerated
				// base restriction.
				logger.error( "Invalid operator: " + operator );
				break;
		}
	}
}
