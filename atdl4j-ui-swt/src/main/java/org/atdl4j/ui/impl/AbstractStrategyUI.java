package org.atdl4j.ui.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.ParameterHelper;
import org.atdl4j.data.StrategyRuleset;
import org.atdl4j.data.TypeConverter;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.fix.PlainFIXMessageBuilder;
import org.atdl4j.data.validation.LengthValidationRule;
import org.atdl4j.data.validation.LogicalOperatorValidationRule;
import org.atdl4j.data.validation.PatternValidationRule;
import org.atdl4j.data.validation.ReferencedValidationRule;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.core.MultipleCharValueT;
import org.atdl4j.fixatdl.core.MultipleStringValueT;
import org.atdl4j.fixatdl.core.ObjectFactory;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.core.UseT;
import org.atdl4j.fixatdl.layout.HiddenFieldT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.fixatdl.validation.EditRefT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.fixatdl.validation.LogicOperatorT;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.fixatdl.validation.StrategyEditT;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.StrategyUI;

/**
 * Base class for ValidationRule.
 * 
 * @param <E>
 */
public abstract class AbstractStrategyUI implements StrategyUI 
{
	protected static final Logger logger = Logger.getLogger( AbstractStrategyUI.class );
	
	protected Map<String, ParameterT> parameterMap;
	
	private Atdl4jConfig atdl4jConfig;
	
	// of StateListeners to attach to controlWidgets
	private StrategyRuleset strategyRuleset;
	
	private Map<String, ValidationRule> completeValidationRuleMap;

	protected StrategyT strategy;
// 2/9/2010 Atdl4jConfig has the getStrategies()	protected StrategiesT strategies;
	
	
	abstract	protected void buildControlMap( List<StrategyPanelT> aStrategyPanelList )
		throws JAXBException;
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
	abstract public Map<String, ControlUI<?>> getControlUIMap();
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
	abstract public Map<String, ControlUI<?>> getControlUIWithParameterMap();

	// -- Used by init() --
	abstract protected void initBegin(Object parentContainer) throws JAXBException;
//	abstract protected Map<String, ValidationRule> buildGlobalAndLocalRuleMap(StrategyT strategy, Map<String, ValidationRule> strategiesRules) throws JAXBException;
	abstract protected void buildControlMap()	throws JAXBException;
	abstract protected void createRadioGroups()	throws JAXBException;
	abstract protected void buildControlWithParameterMap()	throws JAXBException;
	abstract protected void attachGlobalStateRulesToControls()	throws JAXBException;
	abstract protected void attachStateListenersToAllControls()	throws JAXBException;
	abstract protected void initEnd() throws JAXBException;

	abstract protected void addToControlMap( String aName, ControlUI aControlUI );
	abstract public void setCxlReplaceMode(boolean cxlReplaceMode);
	abstract protected void fireStateListeners();
	
	/**
	 * @param strategy
	 * @param aAtdl4jConfig (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (should be swt.Composite)
	 * @throws JAXBException
	 */
	public void init(StrategyT strategy, Atdl4jConfig aAtdl4jConfig, Map<String, ValidationRule> strategiesRules, Object parentContainer)
			 throws JAXBException
	{
		setStrategy( strategy );
		setAtdl4jConfig( aAtdl4jConfig );

		initBegin( parentContainer );
/** initBegin(Object parentContainer)		
		setParent( (Composite) parentContainer );
		setControlWithParameterMap( new HashMap<String, SWTWidget<?>>() );
//TODO !!! verify, as it does not appear that getWidgetStateListenerMap() is ever being populated (though, is being initialized)		
		setWidgetStateListenerMap( new HashMap<SWTWidget<?>, Set<SWTStateListener>>() );
**/
		// initialize rules collection with global rules
		setStrategyRuleset( new StrategyRuleset() );

		setParameterMap( buildParameters( getStrategy() ) );
		
		setCompleteValidationRuleMap( buildGlobalAndLocalRuleMap( getStrategy(), strategiesRules ) );
		
		buildControlMap();
/** buildControlMap()		
		buildControlMap( getStrategy().getStrategyLayout().getStrategyPanel() );
**/		
		
		checkForDuplicateControlIDs();
		createRadioGroups();

		addHiddenFieldsForInputAndFilterData( getAtdl4jConfig().getInputAndFilterData() );
		addHiddenFieldsForConstParameterWithoutControl( getParameterMap() );
		
		buildControlWithParameterMap();
		attachGlobalStateRulesToControls();
		
		attachStateListenersToAllControls();
	}


	
	/**
	 * @param atdl4jConfig
	 *           the atdl4jConfig to set
	 */
	protected void setAtdl4jConfig(Atdl4jConfig atdl4jConfig)
	{
		this.atdl4jConfig = atdl4jConfig;
	}

	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return atdl4jConfig;
	}


	/**
	 * @return the parameterMap
	 */
	public Map<String, ParameterT> getParameterMap()
	{
		return this.parameterMap;
	}

	/**
	 * @param aParameterMap the parameterMap to set
	 */
	protected void setParameterMap(Map<String, ParameterT> aParameterMap)
	{
		this.parameterMap = aParameterMap;
	}

	/**
	 * @return the strategyRuleset
	 */
	public StrategyRuleset getStrategyRuleset()
	{
		return this.strategyRuleset;
	}

	/**
	 * @param aStrategyRuleset the strategyRuleset to set
	 */
	protected void setStrategyRuleset(StrategyRuleset aStrategyRuleset)
	{
		this.strategyRuleset = aStrategyRuleset;
	}

	/**
	 * @param completeValidationRuleMap the completeValidationRuleMap to set
	 */
	protected void setCompleteValidationRuleMap(Map<String, ValidationRule> completeValidationRuleMap)
	{
		this.completeValidationRuleMap = completeValidationRuleMap;
	}

	/**
	 * @return the completeValidationRuleMap
	 */
	public Map<String, ValidationRule> getCompleteValidationRuleMap()
	{
		return completeValidationRuleMap;
	}

	/**
	 * @return the strategy
	 */
	public StrategyT getStrategy()
	{
		return this.strategy;
	}

	/**
	 * @param aStrategy the strategy to set
	 */
	protected void setStrategy(StrategyT aStrategy)
	{
		this.strategy = aStrategy;
	}
	
	// 2/9/2010 Scott Atwell - subdivided constructor/init() into discrete methods
	/**
	 * @param strategy
	 * @return
	 * @throws JAXBException
	 */
	protected Map<String, ParameterT> buildParameters(StrategyT strategy)
		throws JAXBException
	{
		Map<String, ParameterT> tempParameters = new HashMap<String, ParameterT>();
		
		// build parameters
		for ( ParameterT parameter : strategy.getParameter() )
		{
			// compile list of parameters (TODO: is this needed?)
			tempParameters.put( parameter.getName(), parameter );

// 2/12/2010 Scott Atwell
			boolean tempIsRequired = false;
			
			// required fields should be validated as well
			if ( parameter.getUse() != null )
			{
				if ( parameter.getUse().equals( UseT.REQUIRED ) )
				{
					tempIsRequired = true;
					ValidationRule requiredFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.EX, null, strategy );
					getStrategyRuleset().addRequiredFieldRule( requiredFieldRule );
				}
			}
			
// 2/12/2010 Scott Atwell added
			TypeConverter tempTypeConverter = getAtdl4jConfig().getTypeConverterFactory().create( parameter );
			
// Parameter/@const has been removed 			if ( ( parameter.isConst() ) && ( ParameterHelper.getConstValue( parameter ) != null ) )
			if ( ParameterHelper.getConstValue( parameter ) != null )
			{
// 2/12/2010 only difference is Parameter one adjusts for multiplyBy100 (use "ToControl" to avoid doing this twice)				String tempStringValue = tempTypeConverter.convertValueToParameterString( ParameterHelper.getConstValue( parameter ) ); 
				String tempStringValue = tempTypeConverter.convertValueToControlString( ParameterHelper.getConstValue( parameter ) ); 
				ValidationRule tempFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.EQ, tempStringValue, strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addConstFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addConstFieldRule( tempOptionalWrapperEdit );
				}
			}
			
// 2/12/2010 Scott Atwell added
			if ( ParameterHelper.getMinValue( parameter ) != null )
			{
// 2/12/2010 only difference is Parameter one adjusts for multiplyBy100 (use "ToControl" to avoid doing this twice)				String tempStringValue = tempTypeConverter.convertValueToParameterString( ParameterHelper.getConstValue( parameter ) ); 
				String tempStringValue = tempTypeConverter.convertValueToControlString( ParameterHelper.getMinValue( parameter ) ); 
				ValidationRule tempFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.GE, tempStringValue, strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addRangeFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addRangeFieldRule( tempOptionalWrapperEdit );
				}
			}
			
// 2/12/2010 Scott Atwell added
			if ( ParameterHelper.getMaxValue( parameter ) != null )
			{
// 2/12/2010 only difference is Parameter one adjusts for multiplyBy100 (use "ToControl" to avoid doing this twice)				String tempStringValue = tempTypeConverter.convertValueToParameterString( ParameterHelper.getMaxValue( parameter ) ); 
				String tempStringValue = tempTypeConverter.convertValueToControlString( ParameterHelper.getMaxValue( parameter ) ); 
				ValidationRule tempFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.LE, tempStringValue, strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addRangeFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addRangeFieldRule( tempOptionalWrapperEdit );
				}
			}
			
// 2/21/2010 Scott Atwell added
			if ( ParameterHelper.getMinLength( parameter ) != null )
			{
				ValidationRule tempFieldRule = new LengthValidationRule( parameter.getName(), OperatorT.GE, ParameterHelper.getMinLength( parameter ), strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addLengthFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addLengthFieldRule( tempOptionalWrapperEdit );
				}
			}
			
// 2/21/2010 Scott Atwell added
			if ( ParameterHelper.getMaxLength( parameter ) != null )
			{
				ValidationRule tempFieldRule = new LengthValidationRule( parameter.getName(), OperatorT.LE, ParameterHelper.getMaxLength( parameter ), strategy );
				
				if ( tempIsRequired )
				{
					getStrategyRuleset().addLengthFieldRule( tempFieldRule );
				}
				else // Parameter is optional
				{
					LogicalOperatorValidationRule tempOptionalWrapperEdit = new LogicalOperatorValidationRule( LogicOperatorT.OR, strategy );
					tempOptionalWrapperEdit.addRule( new ValueOperatorValidationRule( parameter.getName(), OperatorT.NX, null, strategy ) );
					tempOptionalWrapperEdit.addRule( tempFieldRule );
					getStrategyRuleset().addLengthFieldRule( tempOptionalWrapperEdit );
				}
			}

			
			// validate types based on patterns
			if ( parameter instanceof MultipleCharValueT )
			{
				MultipleCharValueT multipleCharValueT = (MultipleCharValueT) parameter;
				ValidationRule patternBasedRule = new PatternValidationRule( multipleCharValueT.getName(), "\\S?(\\s\\S?)*" );
				getStrategyRuleset().addPatternRule( patternBasedRule );

			}
			else if ( parameter instanceof MultipleStringValueT )
			{
				MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
				ValidationRule patternBasedRule = new PatternValidationRule( multipleStringValueT.getName(), "\\S+(\\s\\S+)*" );
				getStrategyRuleset().addPatternRule( patternBasedRule );
			}

			// 2/1/2010 John Shields added
			// Deprecate trueWireValue and falseWireValue attribute;
			if ( parameter instanceof BooleanT )
			{
				if ( ( (BooleanT) parameter ).getTrueWireValue() != null )
					throw new JAXBException( "Attribute \"trueWireValue\" on Boolean_t is deprecated."
							+ " Please use \"checkedEnumRef\" on CheckBox_t or RadioButton_t instead." );

				if ( ( (BooleanT) parameter ).getFalseWireValue() != null )
					throw new JAXBException( "Attribute \"falseWireValue\" on Boolean_t is deprecated."
							+ " Please use \"uncheckedEnumRef\" on CheckBox_t or RadioButton_t instead." );
			}
		}
		
		return tempParameters;
	}


	/**
	 * @param strategy
	 * @param strategiesRules
	 * @return
	 * @throws JAXBException
	 */
	protected Map<String, ValidationRule> buildGlobalAndLocalRuleMap(StrategyT strategy, Map<String, ValidationRule> strategiesRules)
		throws JAXBException
	{
		Map<String, ValidationRule> tempRuleMap = new HashMap<String, ValidationRule>( strategiesRules );

		// and add local rules
		for ( EditT edit : strategy.getEdit() )
		{
			String id = edit.getId();
			if ( id != null )
			{
				ValidationRule rule = ValidationRuleFactory.createRule( edit, tempRuleMap, strategy );
				tempRuleMap.put( id, rule );
			}
			else
			{
				throw new JAXBException( "Strategy-scoped edit without id" );
			}
		}

		// generate validation rules for StrategyEdit elements
		for ( StrategyEditT se : strategy.getStrategyEdit() )
		{
			if ( se.getEdit() != null )
			{
				EditT edit = se.getEdit();
				ValidationRule rule = ValidationRuleFactory.createRule( edit, tempRuleMap, se );
				String id = edit.getId();
				if ( id != null )
				{
					tempRuleMap.put( id, rule ); // TODO: this line should be moved
				}
				// to RuleFactory?
				getStrategyRuleset().putRefRule( se, rule ); // TODO: this line should be moved 
				// to RuleFactory?
			}

			// reference for a previously defined rule
			if ( se.getEditRef() != null )
			{
				EditRefT editRef = se.getEditRef();
				String id = editRef.getId();
				getStrategyRuleset().putRefRule( se, new ReferencedValidationRule( id ) ); // TODO:
				// this
				// line
				// should
				// be
				// moved
				// to
				// RuleFactory?
			}
		}

		return tempRuleMap;
	}
	
	protected void checkForDuplicateControlIDs()
		throws JAXBException
	{
// 2/10/2010 Scott Atwell added/amended
		// -- Note getControlUIMap() constructs a new Map --
		Collection<ControlUI<?>> tempControlMapValues = (Collection<ControlUI<?>>) getControlUIMap().values();
		
// 2/10/2010 Scott Atwell		for ( SWTWidget<?> widget : getControlMap().values() )
		for ( ControlUI<?> widget : tempControlMapValues )
		{
// 2/10/2010 Scott Atwell			for ( SWTWidget<?> widget2 : getControlMap().values() )
			for ( ControlUI<?> widget2 : tempControlMapValues )
			{
				if ( widget != widget2 && widget.getControl().getID().equals( widget2.getControl().getID() ) )
					throw new JAXBException( "Duplicate Control ID: \"" + widget.getControl().getID() + "\"" );
			}
		}
	}

	public ControlUI getControlForParameter( ParameterT aParameterRef )
	{
		if ( ( aParameterRef != null ) && ( getControlUIWithParameterMap() != null ) )
		{
			Collection<ControlUI<?>> tempControlWithParameterMapValues = (Collection<ControlUI<?>>) getControlUIWithParameterMap().values();
			
			for ( ControlUI<?> widget : tempControlWithParameterMapValues )
			{
				if ( aParameterRef.equals( widget.getParameter() ) )
				{
					return widget;
				}
			}
		}
		
		return null;
	}


	protected void addHiddenFieldsForInputAndFilterData( InputAndFilterData aInputAndFilterData )
		throws JAXBException
	{
		if ( ( aInputAndFilterData != null )
				&& ( aInputAndFilterData.getInputHiddenFieldNameValueMap() != null ) )
		{
			ObjectFactory tempObjectFactory = new ObjectFactory();
	
			for ( Map.Entry<String, String> tempMapEntry : aInputAndFilterData.getInputHiddenFieldNameValueMap().entrySet() )
			{
				String tempName = tempMapEntry.getKey();
				Object tempValue = tempMapEntry.getValue();
				ParameterT parameter = tempObjectFactory.createStringT();
				parameter.setName( tempName );
				parameter.setUse( UseT.OPTIONAL );
	
				// compile list of parameters (TODO: is this needed?)
				getParameterMap().put( parameter.getName(), parameter );
	
				HiddenFieldT hiddenField = new HiddenFieldT();
				hiddenField.setInitValue( tempValue.toString() );
				hiddenField.setParameterRef( tempName );
	
				ControlUI hiddenFieldWidget = getAtdl4jConfig().getControlUIForHiddenFieldT( hiddenField, parameter );
//			getControlMap().put( tempName, (SWTWidget<?>) hiddenFieldWidget );
				addToControlMap( tempName, hiddenFieldWidget );
			}
		}
		
	}

	protected void addHiddenFieldsForConstParameterWithoutControl( Map<String, ParameterT> aParameterMap )
		throws JAXBException
	{
		if ( aParameterMap != null )
		{
			ObjectFactory tempObjectFactory = new ObjectFactory();
	
			for ( Map.Entry<String, ParameterT> tempMapEntry : aParameterMap.entrySet() )
			{
				String tempName = tempMapEntry.getKey();
				ParameterT tempParameter = tempMapEntry.getValue();

				// -- If Parameter has const value and does not have a Control --
				if ( ( ParameterHelper.getConstValue( tempParameter ) != null ) &&
					  ( getControlForParameter( tempParameter ) == null ) )
				{
					// -- Add a HiddenField control for this parameter (to add to ControlWithParameters map used by StrategyEdit and FIX Message building) -- 
					HiddenFieldT tempHiddenField = new HiddenFieldT();
					tempHiddenField.setParameterRef( tempName );
		
					ControlUI hiddenFieldWidget = getAtdl4jConfig().getControlUIForHiddenFieldT( tempHiddenField, tempParameter );
					addToControlMap( tempName, hiddenFieldWidget );
				}
			}
		}
	}

	
	
	public void validate() throws ValidationException, JAXBException
	{
		if ( getStrategyRuleset() != null )
		{
			// delegate validation, passing all global and local rules as
			// context information, and all my parameters
// 2/10/2010			getStrategyRuleset().validate( getCompleteValidationRuleMap(), new HashMap<String, ControlUI<?>>( getControlWithParameterMap() ) );
			// -- Note that getControlUIWithParameterMap() constructs a new Map --
			getStrategyRuleset().validate( getCompleteValidationRuleMap(), getControlUIWithParameterMap() );
		}
		else
		{
			logger.info( "No validation rule defined for strategy " + getStrategy().getName() );
		}
	}

	// Scott Atwell added 1/16/2010
	protected StrategiesT getStrategies()
	{
		if ( getAtdl4jConfig() != null )
		{
			return getAtdl4jConfig().getStrategies();
		}
		else
		{
			return null;
		}
	}


	public String getFIXMessage() throws JAXBException
	{
		PlainFIXMessageBuilder builder = new PlainFIXMessageBuilder();
		getFIXMessage( builder );
		return builder.getMessage();
	}

	public void getFIXMessage(FIXMessageBuilder builder) throws JAXBException
	{
		builder.onStart();

		// Scott Atwell 1/16/2010 added
		if ( ( getStrategy() != null ) && ( getStrategies() != null ) )
		{
			// Set TargetStrategy
			String strategyIdentifier = getStrategy().getWireValue();
			if ( strategyIdentifier != null )
			{
				if ( getStrategies().getStrategyIdentifierTag() != null )
				{
					builder.onField( getStrategies().getStrategyIdentifierTag().intValue(), strategyIdentifier.toString() );
				}
				else
				{
					builder.onField( 847, strategyIdentifier );
				}
			}

			// Scott Atwell 1/16/2010 added
			// Set StrategyVersion
			String strategyVersion = getStrategy().getVersion();
			if ( strategyVersion != null )
			{
				if ( getStrategies().getVersionIdentifierTag() != null )
				{
					builder.onField( getStrategies().getVersionIdentifierTag().intValue(), strategyVersion.toString() );
				}
			}
		}

		/*
		 * 2/1/2010 John Shields added Beginning of Repeating Group
		 * implementation. Currently there is an error in ATDL I believe where
		 * StrategyT can only have one RepeatingGroupT HashMap<String,
		 * RepeatingGroupT> rgroups = new HashMap<String, RepeatingGroupT>(); for
		 * (RepeatingGroupT rg : strategy.getRepeatingGroup()) { for (ParameterT
		 * rg : strategy.getRepeatingGroup()) {
		 * 
		 * } }
		 */

// 2/10/2010 Scott Atwell		for ( ControlUI<?> control : getControlMap().values() )
		// -- Note that getControlUIMap() constructs a new Map --
		for ( ControlUI<?> control : getControlUIMap().values() )
		{
			if ( control.getParameter() != null )
				control.getFIXValue( builder );
		}
		builder.onEnd();
	}


	// TODO: this doesn't know how to load custom repeating groups
	// or standard repeating groups aside from Atdl4jConstants.TAG_NO_STRATEGY_PARAMETERS StrategyParameters
	// TODO: would like to integrate with QuickFIX engine
	public void setFIXMessage(String fixMessage) throws JAXBException
	{

		// TODO: need to reverse engineer state groups

		String[] fixParams = fixMessage.split( "\\001" );

		for ( int i = 0; i < fixParams.length; i++ )
		{
			String[] pair = fixParams[ i ].split( "=" );
			int tag = Integer.parseInt( pair[ 0 ] );
			String value = pair[ 1 ];

			// not repeating group
			if ( tag < Atdl4jConstants.TAG_NO_STRATEGY_PARAMETERS || tag > Atdl4jConstants.TAG_STRATEGY_PARAMETER_VALUE )
			{
// 2/10/2010 Scott Atwell				for ( SWTWidget<?> widget : getControlWithParameterMap().values() )
				// -- Note that getControlUIWithParameterMap() constructs a new Map --
				for ( ControlUI<?> widget : getControlUIWithParameterMap().values() )
				{
					if ( widget.getParameter().getFixTag() != null && widget.getParameter().getFixTag().equals( BigInteger.valueOf( tag ) ) )
					{
// 2/14/2010 Scott Atwell						widget.setValueAsString( value );
						widget.setFIXValue( value );
					}
				}
			}
			// StrategyParams repeating group
			else if ( tag == Atdl4jConstants.TAG_NO_STRATEGY_PARAMETERS )
			{
				i++;
				for ( int j = 0; j < Integer.parseInt( value ); j++ )
				{
					String name = fixParams[ i ].split( "=" )[ 1 ];
					String value2 = fixParams[ i + 2 ].split( "=" )[ 1 ];

// 2/10/2010 Scott Atwell					for ( SWTWidget<?> widget : getControlWithParameterMap().values() )
					// -- Note that getControlUIWithParameterMap() constructs a new Map --
					for ( ControlUI<?> widget : getControlUIWithParameterMap().values() )
					{
						if ( widget.getParameter().getName() != null && widget.getParameter().getName().equals( name ) )
						{
// 2/14/2010 Scott Atwell							widget.setValueAsString( value2 );
							widget.setFIXValue( value2 );
						}
					}
					i = i + 3;
				}
			}
		}

		fireStateListeners();
/** fireStateListeners()		
		// fire state listeners once for good measure
		for ( SWTStateListener stateListener : getStateListenerList() )
			stateListener.handleEvent( null );
**/			
	}
}