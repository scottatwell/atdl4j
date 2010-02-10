package org.atdl4j.ui.swt.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.atdl.core.BooleanT;
import org.atdl4j.atdl.core.MultipleCharValueT;
import org.atdl4j.atdl.core.MultipleStringValueT;
import org.atdl4j.atdl.core.ObjectFactory;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.atdl.core.UseT;
import org.atdl4j.atdl.flow.StateRuleT;
import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.atdl.layout.HiddenFieldT;
import org.atdl4j.atdl.layout.RadioButtonT;
import org.atdl4j.atdl.layout.StrategyPanelT;
import org.atdl4j.atdl.validation.EditRefT;
import org.atdl4j.atdl.validation.EditT;
import org.atdl4j.atdl.validation.OperatorT;
import org.atdl4j.atdl.validation.StrategyEditT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.StrategyRuleset;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.fix.PlainFIXMessageBuilder;
import org.atdl4j.data.validation.Field2OperatorValidationRule;
import org.atdl4j.data.validation.LogicalOperatorValidationRule;
import org.atdl4j.data.validation.PatternValidationRule;
import org.atdl4j.data.validation.ReferencedValidationRule;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.impl.AbstractStrategyUI;
import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.widget.ButtonWidget;
import org.atdl4j.ui.swt.widget.RadioButtonListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * UI representation for a Strategy object.
 * 
 */
public class SWTStrategyUI
		extends AbstractStrategyUI
{

// 2/9/2010 Scott Atwell -- force this to be used via getter/setter in conjunction with discrete methods  	protected Map<String, ParameterT> parameters;
	protected Map<String, ParameterT> parameterMap;
	
// 2/9/2010 Scott Atwell	protected Map<String, SWTWidget<?>> controls;
	protected Map<String, SWTWidget<?>> controlMap;
	
// 2/9/2010 Scott Atwell	protected Map<String, SWTWidget<?>> controlWithParameter;
	Map<String, SWTWidget<?>> controlWithParameterMap;
	
	// 2/1/2010
// 2/9/2010 Scott Atwell	protected Map<String, RadioButtonListener> radioGroups;
	protected Map<String, RadioButtonListener> radioGroupMap;
	
// 2/9/2010 Scott Atwell	protected List<SWTStateListener> stateListeners = new Vector<SWTStateListener>();
	protected List<SWTStateListener> stateListenerList;
	
// 2/9/2010 Scott Atwell	protected Map<SWTWidget<?>, Set<SWTStateListener>> widgetStateListeners; // map
	protected Map<SWTWidget<?>, Set<SWTStateListener>> widgetStateListenerMap; 
	
	// of
	// StateListeners
	// to
	// attach
	// to
	// control
	// Widgets
// 2/9/2010 Scott Atwell -- force this to be used via getter/setter in conjunction with discrete methods  	protected StrategyRuleset ruleset;
	private StrategyRuleset strategyRuleset;
	
// 2/9/2010 Scott Atwell -- force this to be used via getter/setter in conjunction with discrete methods  	protected Map<String, ValidationRule> refRules;
	private Map<String, ValidationRule> completeValidationRuleMap;
	
	protected static final Logger logger = Logger.getLogger( SWTStrategyUI.class );
	protected StrategyT strategy;
	// TODO 1/16/2010 Scott Atwell added
// 2/9/2010 Atdl4jConfig has the getStrategies()	protected StrategiesT strategies;
	// 2/8/2010 Scott Atwell added
	private Atdl4jConfig atdl4jConfig;
	// 2/9/2010 Scott Atwell added
	private Composite parent;
	private SWTFactory controlFactory;  // note this is lazy-init'd (adjust getControlFactory() if you wish to override/substitute concrete class)


	/*
	 * Call init() after invoking the no arg constructor
	 */
	public SWTStrategyUI()
	{
	}

//	public SWTStrategyUI(StrategyT strategy,
// 1/16/2010 Scott Atwell added strategies
		// 1/17/2010 Scott Atwell added inputHiddenFieldNameValueMap
//		Map<String, ValidationRule> strategiesRules, Composite parent, StrategiesT strategies, Atdl4jConfig aAtdl4jConfig) throws JAXBException
// 2/9/2010 Atdl4jConfig has the getStrategies()
	public SWTStrategyUI(StrategyT strategy, Atdl4jConfig aAtdl4jConfig, Map<String, ValidationRule> strategiesRules, Composite parent) 
	 	throws JAXBException	
	{
// 2/9/2010 Atdl4jConfig has the getStrategies()
//		init( strategy, strategiesRules, parent, strategies, aAtdl4jConfig );
		init( strategy, aAtdl4jConfig, strategiesRules, parent );
	}

	// 1/16/2010 Scott Atwell added strategies
	// 1/17/2010 Scott Atwell added inputHiddenFieldNameValueMap
	// 2/8/2010 Scott Atwell (use Atdl4jConfig vs. inputHiddenFieldNameValueMap)
// 2/9/2010 Scott Atwell	public void init(StrategyT strategy, Map<String, ValidationRule> strategiesRules, Composite parent, StrategiesT strategies,
	/**
	 * @param strategy
	 * @param aAtdl4jConfig (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (should be swt.Composite)
	 * @throws JAXBException
	 */
// 2/9/2010 Atdl4jConfig has the getStrategies()	public void init(StrategyT strategy, Map<String, ValidationRule> strategiesRules, Object parentContainer, StrategiesT strategies,
// 							Atdl4jConfig aAtdl4jConfig) throws JAXBException
	public void init(StrategyT strategy, Atdl4jConfig aAtdl4jConfig, Map<String, ValidationRule> strategiesRules, Object parentContainer)
			 throws JAXBException
	{
// 2/9/2010 Atdl4jConfig has the getStrategies()		this.strategy = strategy;

		// TODO 1/16/2010 Scott Atwell added
// 2/9/2010 Atdl4jConfig has the getStrategies()		this.strategies = strategies;
		setStrategy( strategy );
		setAtdl4jConfig( aAtdl4jConfig );

		// 2/9/2010 Scott Atwell added
		setParent( (Composite) parentContainer );

		// ParameterFieldFactory parameterFactory = new ParameterFieldFactory();
//		SWTFactory controlFactory = new SWTFactory();

// buildParameters()		parameters = new HashMap<String, ParameterT>();
// buildControlMap()		controls = new HashMap<String, SWTWidget<?>>();
		
//		controlWithParameter = new HashMap<String, SWTWidget<?>>();
		setControlWithParameterMap( new HashMap<String, SWTWidget<?>>() );
		
		
//createRadioGroups()		radioGroups = new HashMap<String, RadioButtonListener>();
		
//TODO !!! verify, as it does not appear that getWidgetStateListenerMap() is ever being populated (though, is being initialized)		
//		widgetStateListeners = new HashMap<SWTWidget<?>, Set<SWTStateListener>>();
		setWidgetStateListenerMap( new HashMap<SWTWidget<?>, Set<SWTStateListener>>() );

		// initialize rules collection with global rules
// buildGlobalAndLocalRuleMap()		refRules = new HashMap<String, ValidationRule>( strategiesRules );

// 2/9/2010		ruleset = new StrategyRuleset();
		setStrategyRuleset( new StrategyRuleset() );

		setParameterMap( buildParameters( getStrategy() ) );
/***	buildParameters()	***	
		// build parameters
		for ( ParameterT parameter : strategy.getParameter() )
		{

			// compile list of parameters (TODO: is this needed?)
			parameters.put( parameter.getName(), parameter );

			// required fields should be validated as well
			if ( parameter.getUse() != null )
			{
				if ( parameter.getUse().equals( UseT.REQUIRED ) )
				{
					ValidationRule requiredFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.EX, null, strategy );
					ruleset.addRequiredFieldRule( requiredFieldRule );
				}
			}

			// validate types based on patterns
			if ( parameter instanceof MultipleCharValueT )
			{
				MultipleCharValueT multipleCharValueT = (MultipleCharValueT) parameter;
				ValidationRule patternBasedRule = new PatternValidationRule( multipleCharValueT.getName(), "\\S?(\\s\\S?)*" );
				ruleset.addPatternRule( patternBasedRule );

			}
			else if ( parameter instanceof MultipleStringValueT )
			{
				MultipleStringValueT multipleStringValueT = (MultipleStringValueT) parameter;
				ValidationRule patternBasedRule = new PatternValidationRule( multipleStringValueT.getName(), "\\S+(\\s\\S+)*" );
				ruleset.addPatternRule( patternBasedRule );
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
***  buildParameters() ***/
				
		
		setCompleteValidationRuleMap( buildGlobalAndLocalRuleMap( getStrategy(), strategiesRules ) );
/*** buildGlobalAndLocalRuleMap() ***		
		// and add local rules
		for ( EditT edit : strategy.getEdit() )
		{
			String id = edit.getId();
			if ( id != null )
			{
				ValidationRule rule = ValidationRuleFactory.createRule( edit, refRules, strategy );
				refRules.put( id, rule );
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
				ValidationRule rule = ValidationRuleFactory.createRule( edit, refRules, se );
				String id = edit.getId();
				if ( id != null )
					refRules.put( id, rule ); // TODO: this line should be moved
				// to RuleFactory?
				ruleset.putRefRule( se, rule ); // TODO: this line should be moved
				// to RuleFactory?
			}

			// reference for a previously defined rule
			if ( se.getEditRef() != null )
			{
				EditRefT editRef = se.getEditRef();
				String id = editRef.getId();
				ruleset.putRefRule( se, new ReferencedValidationRule( id ) ); // TODO:
				// this
				// line
				// should
				// be
				// moved
				// to
				// RuleFactory?
			}
		}
***  buildGlobalAndLocalRuleMap() ***/
		
		buildControlMap( getStrategy().getStrategyLayout().getStrategyPanel() );
/*** buildControlMap() ***
		// build panels and widgets recursively
		for ( StrategyPanelT panel : strategy.getStrategyLayout().getStrategyPanel() )
		{
			controls.putAll( controlFactory.create( parent, panel, getParameterMap(), SWT.NONE ) );
		}
*** buildControlMap() ***/
		
		checkForDuplicateControlIDs();
		createRadioGroups();
/*** checkForDuplicateControlIDs() and createRadioGroups() ***		
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
			for ( SWTWidget<?> widget2 : getControlMap().values() )
			{
				if ( widget != widget2 && widget.getControl().getID().equals( widget2.getControl().getID() ) )
					throw new JAXBException( "Duplicate Control ID: \"" + widget.getControl().getID() + "\"" );
			}

			// 2/1/2010 John Shields added
			// create radioGroups
			if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
					&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != "" )
			{
				String rg = ( (RadioButtonT) widget.getControl() ).getRadioGroup();
				if ( !radioGroups.containsKey( rg ) )
					radioGroups.put( rg, new RadioButtonListener() );
				if ( widget instanceof ButtonWidget )
					radioGroups.get( rg ).addButton( (ButtonWidget) widget );
			}
		}
*** checkForDuplicateControlIDs() and createRadioGroups() ***/		

		addHiddenFieldsForInputAndFilterData( getAtdl4jConfig().getInputAndFilterData() );
/*** 	addHiddenFieldsForInputAndFilterData() ***	
		// 1/17/2010 Scott Atwell Added
		if ( ( getAtdl4jConfig() != null ) && ( getAtdl4jConfig().getInputAndFilterData() != null )
				&& ( getAtdl4jConfig().getInputAndFilterData().getInputHiddenFieldNameValueMap() != null ) )
		{
			ObjectFactory tempObjectFactory = new ObjectFactory();

			for ( Map.Entry<String, String> tempMapEntry : getAtdl4jConfig().getInputAndFilterData().getInputHiddenFieldNameValueMap().entrySet() )
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

				HiddenFieldWidget hiddenFieldWidget = new HiddenFieldWidget( hiddenField, parameter );
				getControlMap().put( tempName, hiddenFieldWidget );
			}
		}
*** 	addHiddenFieldsForInputAndFilterData() ***/
		
		buildControlWithParameterMap();
		attachGlobalStateRulesToControls();
/***	buildControlWithParameterMap()and attachGlobalStateRulesToControls() ***	
		// loop through all UI controls
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
			if ( widget.getParameter() != null )
			{
				// validate that a parameter is not being added twice
				for ( String parameterRef : controlWithParameter.keySet() )
				{
					if ( parameterRef.equals( widget.getParameter().getName() ) )
						throw new JAXBException( "Cannot add parameter \"" + parameterRef + "\" to two separate controls." );
				}
				controlWithParameter.put( widget.getParameter().getName(), widget );
			}
			

			// parameter state rules that have an id should be included in
			// the rules map
			ControlT control = widget.getControl();

			if ( control.getStateRule() != null )
			{
				for ( StateRuleT stateRule : control.getStateRule() )
				{

					SWTWidget<?> affectedWidget = getControlMap().get( control.getID() );
					SWTStateListener stateListener = new SWTStateListener( affectedWidget, stateRule, getControlMap(), getCompleteValidationRuleMap() );

					// attach the stateListener's rule to controls
					attachRuleToControls( stateListener.getRule(), stateListener );

					stateListeners.add( stateListener );

					// run the state rule to check if any parameter needs to be
					// enabled/disabled or hidden/unhidden before being rendered
					stateListener.handleEvent( null );
				}
			}
		}
*** buildControlWithParameterMap() and attachGlobalStateRulesToControls() ***/
		
		attachStateListenersToAllControls();
/*** attachStateListenersToAllControls() ***		
		// add all StateListeners to the controls
		for ( Entry<SWTWidget<?>, Set<SWTStateListener>> entry : widgetStateListeners.entrySet() )
		{
			for ( SWTStateListener listener : entry.getValue() )
				entry.getKey().addListener( listener );
		}
*** attachStateListenersToAllControls() ***/		
	}

	// Set screen how it should look when in CxlRpl
	public void setCxlReplaceMode(boolean cxlReplaceMode)
	{

		// enable/disable non-mutable parameters
		for ( SWTWidget<?> widget : getControlWithParameterMap().values() )
		{
			if ( !widget.getParameter().isMutableOnCxlRpl() )
				widget.setEnabled( !cxlReplaceMode );
		}

		// set all CxlRpl on all state listeners and fire
		// once for good measure
		for ( SWTStateListener stateListener : getStateListenerList() )
		{
			stateListener.setCxlReplaceMode( cxlReplaceMode );
			stateListener.handleEvent( null );
		}
	}

	// Create a map of StateListeners to be added to each widget. This is to
	// ensure
	// that each StateListener is only added once to a given widget.
	private void putStateListener(SWTWidget<?> widget, SWTStateListener stateListener)
	{
		if ( !getWidgetStateListenerMap().containsKey( widget ) )
			getWidgetStateListenerMap().put( widget, new HashSet<SWTStateListener>() );
		if ( !getWidgetStateListenerMap().get( widget ).contains( stateListener ) )
			getWidgetStateListenerMap().get( widget ).add( stateListener );
	}

	private void attachRuleToControls(ValidationRule rule, SWTStateListener stateRuleListener) throws JAXBException
	{
		if ( rule instanceof LogicalOperatorValidationRule )
		{
			for ( ValidationRule innerRule : ( (LogicalOperatorValidationRule) rule ).getRules() )
			{
				attachRuleToControls( innerRule, stateRuleListener );
			}
		}
		else if ( rule instanceof ValueOperatorValidationRule )
		{
			attachFieldToControls( ( (ValueOperatorValidationRule) rule ).getField(), stateRuleListener );
		}
		else if ( rule instanceof Field2OperatorValidationRule )
		{
			attachFieldToControls( ( (Field2OperatorValidationRule) rule ).getField(), stateRuleListener );
			attachFieldToControls( ( (Field2OperatorValidationRule) rule ).getField2(), stateRuleListener );
		}
	}

	private void attachFieldToControls(String field, SWTStateListener stateRuleListener) throws JAXBException
	{
		if ( field != null )
		{
			SWTWidget<?> targetParameterWidget = getControlMap().get( field );
			if ( targetParameterWidget == null )
				throw new JAXBException( "Error generating a State Rule => Control: " + field + " does not exist in Strategy: " + getStrategy().getName() );
			putStateListener( targetParameterWidget, stateRuleListener );

			// 2/1/2010 John Shields added
			// RadioButtonT requires adding all associated RadioButtonTs in the
			// ratioGroup
			if ( targetParameterWidget.getControl() instanceof RadioButtonT
					&& ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() != null
// 2/9/2010 Scott Atwell re-wrote (use .equals() vs. == or !=)					
//					&& ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() != "" )
					&& ( ! "".equals( ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup() ) ) )
			{
				String rg = ( (RadioButtonT) targetParameterWidget.getControl() ).getRadioGroup();
				for ( SWTWidget<?> widget : getControlMap().values() )
				{
					if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
							&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
// 2/9/2010 Scott Atwell re-wrote (use .equals() vs. == or !=)					
//							&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != ""
							&& ( ! "".equals( ( (RadioButtonT) widget.getControl() ).getRadioGroup() ) )
							&& ( (RadioButtonT) widget.getControl() ).getRadioGroup().equals( rg ) )
					{
						putStateListener( widget, stateRuleListener );
					}
				}
			}
		}
	}

	public SWTWidget<?> getControl(String name)
	{
		return getControlMap().get( name );
	}

	public void validate() throws ValidationException, JAXBException
	{
		if ( getStrategyRuleset() != null )
		{
			// delegate validation, passing all global and local rules as
			// context information, and all my parameters
			getStrategyRuleset().validate( getCompleteValidationRuleMap(), new HashMap<String, ControlUI<?>>( getControlWithParameterMap() ) );
		}
		else
		{
			logger.info( "No validation rule defined for strategy " + getStrategy().getName() );
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

		for ( ControlUI<?> control : getControlMap().values() )
		{
			if ( control.getParameter() != null )
				control.getFIXValue( builder );
		}
		builder.onEnd();
	}

	// TODO: this doesn't know how to load custom repeating groups
	// or standard repeating groups aside from 957 StrategyParameters
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
			if ( tag < 957 || tag > 960 )
			{
				for ( SWTWidget<?> widget : getControlWithParameterMap().values() )
				{
					if ( widget.getParameter().getFixTag() != null && widget.getParameter().getFixTag().equals( BigInteger.valueOf( tag ) ) )
					{
						widget.setValueAsString( value );
					}
				}
			}
			// StrategyParams repeating group
			else if ( tag == 957 )
			{
				i++;
				for ( int j = 0; j < Integer.parseInt( value ); j++ )
				{
					String name = fixParams[ i ].split( "=" )[ 1 ];
					String value2 = fixParams[ i + 2 ].split( "=" )[ 1 ];

					for ( SWTWidget<?> widget : getControlWithParameterMap().values() )
					{
						if ( widget.getParameter().getName() != null && widget.getParameter().getName().equals( name ) )
						{
							widget.setValueAsString( value2 );
						}
					}
					i = i + 3;
				}
			}
		}

		// fire state listeners once for good measure
		for ( SWTStateListener stateListener : getStateListenerList() )
			stateListener.handleEvent( null );
	}

	// Scott Atwell added 1/14/2010
// 2/9/2010 Scott Atwell	public Map<String, ControlUI<?>> getControls()
	/**
	 * Note invoking this method results in object construction as a result of down-casting 
	 * our own map of a specific templatized instance (SWTWidget<?>) of ControlUI<?> --
	 * @return
	 */
	public Map<String, ControlUI<?>> getControlUIWithParameterMap()
	{
		// return new HashMap<String,ControlUI<?>>(getControlMap());
		if ( getControlWithParameterMap() != null )
		{
			return new HashMap<String, ControlUI<?>>( getControlWithParameterMap()  );
		}
		else
		{
			return null;
		}
	}

	/**
	 * Note invoking this method results in object construction as a result of down-casting 
	 * our own map of a specific templatized instance (SWTWidget<?>) of ControlUI<?> --
	 * @return
	 */
	public Map<String, ControlUI<?>> getControlUIMap()
	{
		// return new HashMap<String,ControlUI<?>>(getControlMap());
		if ( getControlUIMap() != null )
		{
			return new HashMap<String, ControlUI<?>>( getControlMap()  );
		}
		else
		{
			return null;
		}
	}
	
	// Scott Atwell added 1/16/2010
	public StrategiesT getStrategies()
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
	 * @return the controlMap
	 */
	public Map<String, SWTWidget<?>> getControlMap()
	{
		return this.controlMap;
	}

	/**
	 * @param aControlMap the controlMap to set
	 */
	protected void setControlMap(Map<String, SWTWidget<?>> aControlMap)
	{
		this.controlMap = aControlMap;
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

			// required fields should be validated as well
			if ( parameter.getUse() != null )
			{
				if ( parameter.getUse().equals( UseT.REQUIRED ) )
				{
					ValidationRule requiredFieldRule = new ValueOperatorValidationRule( parameter.getName(), OperatorT.EX, null, strategy );
					getStrategyRuleset().addRequiredFieldRule( requiredFieldRule );
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
	
	/**
	 * @param aStrategyPanelList
	 * @return
	 * @throws JAXBException
	 */
	protected void buildControlMap( List<StrategyPanelT> aStrategyPanelList )
		throws JAXBException
	{
		Map<String, SWTWidget<?>> tempControlMap = new HashMap<String, SWTWidget<?>>();
		
		// build panels and widgets recursively
//		for ( StrategyPanelT panel : strategy.getStrategyLayout().getStrategyPanel() )
		for ( StrategyPanelT panel : aStrategyPanelList )
		{
//			tempControlMap.putAll( controlFactory.create( parent, panel, getParameterMap(), SWT.NONE ) );
			tempControlMap.putAll( getControlFactory().create( getParent(), panel, getParameterMap(), SWT.NONE ) );
		}
	
		setControlMap( tempControlMap );
	}


	protected void checkForDuplicateControlIDs()
		throws JAXBException
	{
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
			for ( SWTWidget<?> widget2 : getControlMap().values() )
			{
				if ( widget != widget2 && widget.getControl().getID().equals( widget2.getControl().getID() ) )
					throw new JAXBException( "Duplicate Control ID: \"" + widget.getControl().getID() + "\"" );
			}
/** createRadioGroups()
			// 2/1/2010 John Shields added
			// create radioGroups
			if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
					&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != "" )
			{
				String rg = ( (RadioButtonT) widget.getControl() ).getRadioGroup();
				if ( !radioGroups.containsKey( rg ) )
					radioGroups.put( rg, new RadioButtonListener() );
				if ( widget instanceof ButtonWidget )
					radioGroups.get( rg ).addButton( (ButtonWidget) widget );
			}
**/			
		}
	}
	
	protected void createRadioGroups()
	{
		Map<String, RadioButtonListener> tempRadioGroupMap = new HashMap<String, RadioButtonListener>();
		
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
/**  checkForDuplicateControlIDs()
  			for ( SWTWidget<?> widget2 : getControlMap().values() )

			{
				if ( widget != widget2 && widget.getControl().getID().equals( widget2.getControl().getID() ) )
					throw new JAXBException( "Duplicate Control ID: \"" + widget.getControl().getID() + "\"" );
			}
**/
			// 2/1/2010 John Shields added
			// create radioGroups
			if ( widget.getControl() instanceof RadioButtonT && ( (RadioButtonT) widget.getControl() ).getRadioGroup() != null
					&& ( (RadioButtonT) widget.getControl() ).getRadioGroup() != "" )
			{
				String rg = ( (RadioButtonT) widget.getControl() ).getRadioGroup();
				if ( !tempRadioGroupMap.containsKey( rg ) )
					tempRadioGroupMap.put( rg, new RadioButtonListener() );
				if ( widget instanceof ButtonWidget )
					tempRadioGroupMap.get( rg ).addButton( (ButtonWidget) widget );
			}
		}
		
		setRadioGroupMap( tempRadioGroupMap );
	}

	/**
	 * @return the radioGroupMap
	 */
	public Map<String, RadioButtonListener> getRadioGroupMap()
	{
		return this.radioGroupMap;
	}

	/**
	 * @param aRadioGroupMap the radioGroupMap to set
	 */
	protected void setRadioGroupMap(Map<String, RadioButtonListener> aRadioGroupMap)
	{
		this.radioGroupMap = aRadioGroupMap;
	}

	protected void addHiddenFieldsForInputAndFilterData( InputAndFilterData aInputAndFilterData )
		throws JAXBException
	{
/**		
		// 1/17/2010 Scott Atwell Added
		if ( ( getAtdl4jConfig() != null ) && ( getAtdl4jConfig().getInputAndFilterData() != null )
				&& ( getAtdl4jConfig().getInputAndFilterData().getInputHiddenFieldNameValueMap() != null ) )
**/				
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

// 2/9/2010 Scott Atwell				HiddenFieldWidget hiddenFieldWidget = new HiddenFieldWidget( hiddenField, parameter );
// 2/9/2010 Scott Atwell				getControlMap().put( tempName, hiddenFieldWidget );
				ControlUI hiddenFieldWidget = getAtdl4jConfig().getControlUIForHiddenFieldT( hiddenField, parameter, getAtdl4jConfig() );
				getControlMap().put( tempName, (SWTWidget<?>) hiddenFieldWidget );
			}
		}
		
	}

	/**
	 * @throws JAXBException
	 */
	protected void buildControlWithParameterMap()
		throws JAXBException
	{
		Map<String, SWTWidget<?>> tempControlWithParameterMap = new HashMap<String, SWTWidget<?>>();
		
		// loop through all UI controls
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
			if ( widget.getParameter() != null )
			{
				// validate that a parameter is not being added twice
// 2/9/2010 Scott Atwell re-wrote this to let the Collection handle the search 				
//				for ( String parameterRef : tempControlWithParameterMap.keySet() )
//				{
//					if ( parameterRef.equals( widget.getParameter().getName() ) )
//						throw new JAXBException( "Cannot add parameter \"" + parameterRef + "\" to two separate controls." );
//				}
				String tempParameterName = widget.getParameter().getName();
				if ( tempControlWithParameterMap.containsKey( tempParameterName ) )
				{
					throw new JAXBException( "Cannot add parameter \"" + tempParameterName + "\" to two separate controls." );
				}
				tempControlWithParameterMap.put( tempParameterName, widget );
			}
				
/**
		// parameter state rules that have an id should be included in
		// the rules map
		ControlT control = widget.getControl();

		if ( control.getStateRule() != null )
		{
			for ( StateRuleT stateRule : control.getStateRule() )
			{

				SWTWidget<?> affectedWidget = getControlMap().get( control.getID() );
				SWTStateListener stateListener = new SWTStateListener( affectedWidget, stateRule, getControlMap(), getCompleteValidationRuleMap() );

				// attach the stateListener's rule to controls
				attachRuleToControls( stateListener.getRule(), stateListener );

				stateListeners.add( stateListener );

				// run the state rule to check if any parameter needs to be
				// enabled/disabled or hidden/unhidden before being rendered
				stateListener.handleEvent( null );
			}
		}
**/				
		}
		
		setControlWithParameterMap( tempControlWithParameterMap );
	}

	/**
	 * @throws JAXBException
	 */
	protected void attachGlobalStateRulesToControls()
		throws JAXBException
	{
		List<SWTStateListener> tempStateListenerList = new Vector<SWTStateListener>();
		
		// loop through all UI controls
		for ( SWTWidget<?> widget : getControlMap().values() )
		{
/**			
			if ( widget.getParameter() != null )
			{
				// validate that a parameter is not being added twice
// 2/9/2010 Scott Atwell re-wrote this to let the Collection handle the search 				
//				for ( String parameterRef : tempControlWithParameterMap.keySet() )
//				{
//					if ( parameterRef.equals( widget.getParameter().getName() ) )
//						throw new JAXBException( "Cannot add parameter \"" + parameterRef + "\" to two separate controls." );
//				}
				String tempParameterName = widget.getParameter().getName();
				if ( tempControlWithParameterMap.containsKey( tempParameterName ) )
				{
					throw new JAXBException( "Cannot add parameter \"" + tempParameterName + "\" to two separate controls." );
				}
				tempControlWithParameterMap.put( tempParameterName, widget );
			}
**/				

			// parameter state rules that have an id should be included in
			// the rules map
			ControlT control = widget.getControl();
	
			if ( control.getStateRule() != null )
			{
				for ( StateRuleT stateRule : control.getStateRule() )
				{
	
					SWTWidget<?> affectedWidget = getControlMap().get( control.getID() );
					SWTStateListener stateListener = new SWTStateListener( affectedWidget, stateRule, getControlMap(), getCompleteValidationRuleMap() );
	
					// attach the stateListener's rule to controls
					attachRuleToControls( stateListener.getRule(), stateListener );
	
					tempStateListenerList.add( stateListener );
	
					// run the state rule to check if any parameter needs to be
					// enabled/disabled or hidden/unhidden before being rendered
					stateListener.handleEvent( null );
				}
			}
		}
		
		setStateListenerList( tempStateListenerList );
	}	
	
	/**
	 * @return the controlWithParameterMap
	 */
	public Map<String, SWTWidget<?>> getControlWithParameterMap()
	{
		return this.controlWithParameterMap;
	}

	/**
	 * @param aControlWithParameterMap the controlWithParameterMap to set
	 */
	protected void setControlWithParameterMap(Map<String, SWTWidget<?>> aControlWithParameterMap)
	{
		this.controlWithParameterMap = aControlWithParameterMap;
	}

	/**
	 * @return the stateListenerList
	 */
	protected List<SWTStateListener> getStateListenerList()
	{
		return this.stateListenerList;
	}

	/**
	 * @param aStateListenerList the stateListenerList to set
	 */
	protected void setStateListenerList(List<SWTStateListener> aStateListenerList)
	{
		this.stateListenerList = aStateListenerList;
	}

	/**
	 * @return the widgetStateListenerMap
	 */
	protected Map<SWTWidget<?>, Set<SWTStateListener>> getWidgetStateListenerMap()
	{
		return this.widgetStateListenerMap;
	}

	/**
	 * @param aWidgetStateListenerMap the widgetStateListenerMap to set
	 */
	protected void setWidgetStateListenerMap(Map<SWTWidget<?>, Set<SWTStateListener>> aWidgetStateListenerMap)
	{
		this.widgetStateListenerMap = aWidgetStateListenerMap;
	}

	
	/**
	 * 
	 */
	protected void	attachStateListenersToAllControls()
	{
//TODO !!! verify, as it does not appear that getWidgetStateListenerMap() is ever being populated (though, is being initialized)		
		// add all StateListeners to the controls
//		for ( Entry<SWTWidget<?>, Set<SWTStateListener>> entry : widgetStateListeners.entrySet() )
		for ( Entry<SWTWidget<?>, Set<SWTStateListener>> entry : getWidgetStateListenerMap().entrySet() )
		{
			for ( SWTStateListener listener : entry.getValue() )
				entry.getKey().addListener( listener );
		}
	}

	/**
	 * @return the parent
	 */
	public Composite getParent()
	{
		return this.parent;
	}

	/**
	 * @param aParent the parent to set
	 */
	protected void setParent(Composite aParent)
	{
		this.parent = aParent;
	}

	/**
	 * Lazy-init'd
	 * 
	 * @return the controlFactory
	 * @throws JAXBException 
	 */
	public SWTFactory getControlFactory() 
		throws JAXBException
	{
		if ( this.controlFactory == null )
		{
// 2/9/2010 Scott Atwell			this.controlFactory = new SWTFactory();
			this.controlFactory = new SWTFactory( getAtdl4jConfig() );
		}
		return this.controlFactory;
	}

	/**
	 * @param aControlFactory the controlFactory to set
	 */
	protected void setControlFactory(SWTFactory aControlFactory)
	{
		this.controlFactory = aControlFactory;
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
	public void setStrategy(StrategyT aStrategy)
	{
		this.strategy = aStrategy;
	}
}
