/*
 * Created on Feb 10, 2010
 *
 */
package org.atdl4j.data;

/**
 * 
 * This class contains the data associated with the <code>Atdl4jConstants</code>.
 * 
 * @author Scott Atwell
 * @version 1.0, Feb 10, 2010
 */
public class Atdl4jConstants
{
	public static final String VALUE_NULL_INDICATOR = "{NULL}";
	
	// -- StrategyParametersGrp --
	public static final int TAG_NO_STRATEGY_PARAMETERS = 957;  // -- Repeating Group count --
	public static final int TAG_STRATEGY_PARAMETER_NAME = 958;
	public static final int TAG_STRATEGY_PARAMETER_TYPE = 959;
	public static final int TAG_STRATEGY_PARAMETER_VALUE = 960;

	public static int CLOCK_INIT_VALUE_MODE_USE_AS_IS = 0;  // default
	public static int CLOCK_INIT_VALUE_MODE_USE_CURRENT_TIME_IF_LATER = 1;  
	
	public static String INCREMENT_POLICY_STATIC = "Static";  // -- use value from increment attribute --
	public static String INCREMENT_POLICY_LOT_SIZE = "LotSize";  // -- use the round lot size of symbol --
	public static String INCREMENT_POLICY_TICK = "Tick";  // -- use symbol minimum tick size --
	
}
