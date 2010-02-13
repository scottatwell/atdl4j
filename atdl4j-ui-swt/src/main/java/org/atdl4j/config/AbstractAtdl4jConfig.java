/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.atdl.layout.CheckBoxListT;
import org.atdl4j.atdl.layout.CheckBoxT;
import org.atdl4j.atdl.layout.ClockT;
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
import org.atdl4j.data.TypeConverterFactory;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.ControlUIFactory;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.StrategySelectionUI;

/**
 * Typical setup (for class named XXX):
 * 	private String classNameXXX;
 * 	public void setClassNameXXX(String);
 * 	public String getClassNameXXX();
 *    abstract protected String getDefaultClassNameXXX();
 *    public XXX getXXX() throws ;
 * 	add to constructor:  setClassNameXXX( getDefaultClassNameXXX() );
 * 	NOTE:  add public XXX getXXX() to Atdl4jConfig
 * 	NOTE:  implement protected String getDefaultClassNameXXX(); within derived classes
 * 
 * Note that Class.forName()'s InstantiationException, IllegalAccessException, ClassNotFoundException will be caught and 
 * handled as a run-time error (IllegalStateException)
 * 
 * This class contains the data associated with the <code>AbstractAtdl4jConfig</code>.
 * 
 * Creation date: (Feb 7, 2010 6:12:35 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public abstract class AbstractAtdl4jConfig
	implements Atdl4jConfig
{
	private final Logger logger = Logger.getLogger(AbstractAtdl4jConfig.class);
	
	public static String DEFAULT_CLASS_NAME_STRATEGIES_UI_FACTORY = "org.atdl4j.ui.impl.BaseStrategiesUIFactory";
	public static String DEFAULT_CLASS_NAME_CONTROL_UI_FACTORY = "org.atdl4j.ui.impl.BaseControlUIFactory";
	public static String DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY = "org.atdl4j.data.TypeConverterFactory";
	

	// -- UI Infrastructure --
	private String classNameStrategiesUIFactory;
	private StrategiesUIFactory strategiesUIFactory;
	private TypeConverterFactory typeConverterFactory;
	
	private String classNameStrategiesUI;
	private String classNameStrategyUI;
	private String classNameControlUIFactory;
	private String classNameTypeConverterFactory;

	
	// -- Controls/Widgets -- 
	private String classNameControlUIForCheckBoxT;
	private String classNameControlUIForDropDownListT;
	private String classNameControlUIForEditableDropDownListT;
	private String classNameControlUIForRadioButtonListT;
	private String classNameControlUIForTextFieldT;
	private String classNameControlUIForSliderT;
	private String classNameControlUIForCheckBoxListT;
	private String classNameControlUIForClockT;
	private String classNameControlUIForSingleSpinnerT;
	private String classNameControlUIForDoubleSpinnerT;
	private String classNameControlUIForSingleSelectListT;
	private String classNameControlUIForMultiSelectListT;
	private String classNameControlUIForHiddenFieldT;
	private String classNameControlUIForLabelT;
	private String classNameControlUIForRadioButtonT;
	
	// -- App Components --
	private String classNameStrategySelectionUI;
	private StrategySelectionUI strategySelectionUI;
	
	
	private InputAndFilterData inputAndFilterData;
	
	private boolean showStrategyDescription = true;
	private boolean showTimezoneSelector = false;
	
	private boolean treatControlVisibleFalseAsNull = false;
	private boolean treatControlEnabledFalseAsNull = false;	
	private boolean restoreLastNonNullStateControlValueBehavior = true;	
	
	private boolean showEnabledCheckboxOnOptionalClockControl = false;
	private Integer defaultDigitsForSpinnerControl = new Integer( 2 );

	private StrategiesT strategies;
	private Map<StrategyT, StrategyUI> strategyUIMap;
	private StrategyT selectedStrategy;
	

// 2/9/2010 Scott Atwell StrategiesUIFactory no longer needs to be GUI library-centric  	abstract protected String getDefaultClassNameStrategiesUIFactory();
	protected String getDefaultClassNameStrategiesUIFactory()
	{
		return DEFAULT_CLASS_NAME_STRATEGIES_UI_FACTORY;
	}
	
// 2/9/2010 Scott Atwell StrategiesUIFactory no longer needs to be GUI library-centric  	abstract protected String getDefaultClassNameStrategiesUIFactory();
	protected String getDefaultClassNameControlUIFactory()
	{ 
// 2/9/2010 Scott Atwell		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "impl.SWTControlUIFactory";
		return DEFAULT_CLASS_NAME_CONTROL_UI_FACTORY;
	}
	
	protected String getDefaultClassNameTypeConverterFactory()
	{ 
		return DEFAULT_CLASS_NAME_TYPE_CONVERTER_FACTORY;
	}
	
	
	// -- UI Infrastructure --
	abstract protected String getDefaultClassNameStrategiesUI();
	abstract protected String getDefaultClassNameStrategyUI();

	// -- Controls/Widgets -- 
	abstract protected String getDefaultClassNameControlUIForCheckBoxT();
	abstract protected String getDefaultClassNameControlUIForDropDownListT();
	abstract protected String getDefaultClassNameControlUIForEditableDropDownListT();
	abstract protected String getDefaultClassNameControlUIForRadioButtonListT();
	abstract protected String getDefaultClassNameControlUIForTextFieldT();
	abstract protected String getDefaultClassNameControlUIForSliderT();
	abstract protected String getDefaultClassNameControlUIForCheckBoxListT();
	abstract protected String getDefaultClassNameControlUIForClockT();
	abstract protected String getDefaultClassNameControlUIForSingleSpinnerT();
	abstract protected String getDefaultClassNameControlUIForDoubleSpinnerT();
	abstract protected String getDefaultClassNameControlUIForSingleSelectListT();
	abstract protected String getDefaultClassNameControlUIForMultiSelectListT();
	abstract protected String getDefaultClassNameControlUIForHiddenFieldT();
	abstract protected String getDefaultClassNameControlUIForLabelT();
	abstract protected String getDefaultClassNameControlUIForRadioButtonT();

	// -- App Components --
	abstract protected String getDefaultClassNameStrategySelectionUI();
	
	/**
	 * 
	 */
	public AbstractAtdl4jConfig()
	{
		// -- UI Infrastructure
		setClassNameStrategiesUIFactory( getDefaultClassNameStrategiesUIFactory() );
		setClassNameStrategiesUI( getDefaultClassNameStrategiesUI() );
		setClassNameStrategyUI( getDefaultClassNameStrategyUI() );
		setClassNameControlUIFactory( getDefaultClassNameControlUIFactory() );
		setClassNameTypeConverterFactory( getDefaultClassNameTypeConverterFactory() );

		// -- Controls/Widgets -- 
		setClassNameControlUIForCheckBoxT( getDefaultClassNameControlUIForCheckBoxT() );
		setClassNameControlUIForDropDownListT( getDefaultClassNameControlUIForDropDownListT() );
		setClassNameControlUIForEditableDropDownListT( getDefaultClassNameControlUIForEditableDropDownListT() );
		setClassNameControlUIForRadioButtonListT( getDefaultClassNameControlUIForRadioButtonListT() );
		setClassNameControlUIForTextFieldT( getDefaultClassNameControlUIForTextFieldT() );
		setClassNameControlUIForSliderT( getDefaultClassNameControlUIForSliderT() );
		setClassNameControlUIForCheckBoxListT( getDefaultClassNameControlUIForCheckBoxListT() );
		setClassNameControlUIForClockT( getDefaultClassNameControlUIForClockT() );
		setClassNameControlUIForSingleSpinnerT( getDefaultClassNameControlUIForSingleSpinnerT() );
		setClassNameControlUIForDoubleSpinnerT( getDefaultClassNameControlUIForDoubleSpinnerT() );
		setClassNameControlUIForSingleSelectListT( getDefaultClassNameControlUIForSingleSelectListT() );
		setClassNameControlUIForMultiSelectListT( getDefaultClassNameControlUIForMultiSelectListT() );
		setClassNameControlUIForHiddenFieldT( getDefaultClassNameControlUIForHiddenFieldT() );
		setClassNameControlUIForLabelT( getDefaultClassNameControlUIForLabelT() );
		setClassNameControlUIForRadioButtonT( getDefaultClassNameControlUIForRadioButtonT() );

		// -- App Components --
		setClassNameStrategySelectionUI( getDefaultClassNameStrategySelectionUI() );
	}
	
	
	/**
	 * @param classNameStrategiesUIFactory the classNameStrategiesUIFactory to set
	 */
	public void setClassNameStrategiesUIFactory(String classNameStrategiesUIFactory)
	{
		this.classNameStrategiesUIFactory = classNameStrategiesUIFactory;
		setStrategiesUIFactory( null );
	}

	/**
	 * @return the classNameStrategiesUIFactory
	 */
	public String getClassNameStrategiesUIFactory()
	{
		return classNameStrategiesUIFactory;
	}
	
	/**
	 * @param strategiesUIFactory the strategiesUIFactory to set
	 */
	public void setStrategiesUIFactory(StrategiesUIFactory strategiesUIFactory)
	{
		this.strategiesUIFactory = strategiesUIFactory;
	}

	/**
	 * @return the strategiesUIFactory
	 */
	public StrategiesUIFactory getStrategiesUIFactory() 
	{
		if ( ( strategiesUIFactory == null ) && ( getClassNameStrategiesUIFactory() != null ) )
		{
			String tempClassName = getClassNameStrategiesUIFactory();
			logger.debug( "getStrategiesUIFactory() loading class named: " + tempClassName );
			try
			{
				strategiesUIFactory = ((Class<StrategiesUIFactory>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return strategiesUIFactory;
	}

	/**
	 * @param classNameStrategiesUI the classNameStrategiesUI to set
	 */
	public void setClassNameStrategiesUI(String classNameStrategiesUI)
	{
		this.classNameStrategiesUI = classNameStrategiesUI;
	}

	/**
	 * @return the classNameStrategiesUI
	 */
	public String getClassNameStrategiesUI()
	{
		return classNameStrategiesUI;
	}
	
	/**
	 * Constructs a new instance every call.
	 * 
	 * @param strategies
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
//	public StrategiesUI getStrategiesUI() 
	public StrategiesUI getStrategiesUI(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// Constructs a new instance every call.
//		if ( ( strategiesUI == null ) && ( getClassNameStrategiesUI() != null ) )
//		{
			String tempClassName = getClassNameStrategiesUI();
			logger.debug( "getStrategiesUI() loading class named: " + tempClassName );
			StrategiesUI strategiesUI;
			try
			{
				strategiesUI = ((Class<StrategiesUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( strategiesUI != null )
			{
				strategiesUI.init( strategies, aAtdl4jConfig );
			}
//		}
		
		return strategiesUI;
	}

	/**
	 * @param classNameStrategyUI the classNameStrategyUI to set
	 */
	public void setClassNameStrategyUI(String classNameStrategyUI)
	{
		this.classNameStrategyUI = classNameStrategyUI;
	}

	/**
	 * @return the classNameStrategyUI
	 */
	public String getClassNameStrategyUI()
	{
		return classNameStrategyUI;
	}
	
	/**
	 * Constructs a new instance every call.
	 * 
	 * @param strategy
	 * @param aAtdl4jConfig (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (for SWT: should be swt.Composite)
	 * @return
	 * @throws JAXBException
	 */
//	public StrategyUI getStrategyUI() 
	public StrategyUI getStrategyUI(StrategyT strategy, Atdl4jConfig aAtdl4jConfig, Map<String, ValidationRule> strategiesRules, Object parentContainer)
		throws JAXBException 
	{
// Constructs a new instance every call.
//		if ( ( strategyUI == null ) && ( getClassNameStrategyUI() != null ) )
//		{
			String tempClassName = getClassNameStrategyUI();
			logger.debug( "getStrategyUI() loading class named: " + tempClassName );
			StrategyUI strategyUI;
			try
			{
				strategyUI = ((Class<StrategyUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( strategyUI != null )
			{
				strategyUI.init( strategy, aAtdl4jConfig, strategiesRules, parentContainer );
			}
//		}
		
		return strategyUI;
	}

	/**
	 * @param classNameControlUIFactory the classNameControlUIFactory to set
	 */
	public void setClassNameControlUIFactory(String classNameControlUIFactory)
	{
		this.classNameControlUIFactory = classNameControlUIFactory;
	}

	/**
	 * @return the classNameControlUIFactory
	 */
	public String getClassNameControlUIFactory()
	{
		return classNameControlUIFactory;
	}
	
	/**
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
//	public ControlUIFactory getControlUIFactory() 
	public ControlUIFactory getControlUIFactory(Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// construct new each time		if ( ( controlUIFactory == null ) && ( getClassNameControlUIFactory() != null ) )
//		{
			String tempClassName = getClassNameControlUIFactory();
			logger.debug( "getControlUIFactory() loading class named: " + tempClassName );
			ControlUIFactory controlUIFactory;
			try
			{
				controlUIFactory = ((Class<ControlUIFactory>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIFactory != null )
			{
				controlUIFactory.init( aAtdl4jConfig );
			}
//		}
		
		return controlUIFactory;
	}

		
	/**
	 * @param classNameTypeConverterFactory the classNameTypeConverterFactory to set
	 */
	public void setClassNameTypeConverterFactory(String classNameTypeConverterFactory)
	{
		this.classNameTypeConverterFactory = classNameTypeConverterFactory;
	}

	/**
	 * @return the classNameTypeConverterFactory
	 */
	public String getClassNameTypeConverterFactory()
	{
		return classNameTypeConverterFactory;
	}
	
	/**
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
//	public TypeConverterFactory getTypeConverterFactory() 
	public TypeConverterFactory getTypeConverterFactory(Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
		if ( ( typeConverterFactory == null ) && ( getClassNameTypeConverterFactory() != null ) )
		{
			String tempClassName = getClassNameTypeConverterFactory();
			logger.debug( "getTypeConverterFactory() loading class named: " + tempClassName );
			TypeConverterFactory typeConverterFactory;
			try
			{
				typeConverterFactory = ((Class<TypeConverterFactory>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
//			if ( typeConverterFactory != null )
//			{
//				typeConverterFactory.init( aAtdl4jConfig );
//			}

		}
		
		return typeConverterFactory;
	}
	
	/**
	 * @param classNameControlUIForCheckBoxT the classNameControlUIForCheckBoxT to set
	 */
	public void setClassNameControlUIForCheckBoxT(String classNameControlUIForCheckBoxT)
	{
		this.classNameControlUIForCheckBoxT = classNameControlUIForCheckBoxT;
	}

	/**
	 * @return the classNameControlUIForCheckBoxT
	 */
	public String getClassNameControlUIForCheckBoxT()
	{
		return classNameControlUIForCheckBoxT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForCheckBoxT(CheckBoxT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForCheckBoxT == null ) && ( getClassNameControlUIForCheckBoxT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForCheckBoxT();
			logger.debug( "getControlUIForCheckBoxT() loading class named: " + tempClassName );
			ControlUI controlUIForCheckBoxT;
			try
			{
				controlUIForCheckBoxT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForCheckBoxT != null )
			{
				controlUIForCheckBoxT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForCheckBoxT;
	}

	
	
	/**
	 * @param classNameControlUIForDropDownListT the classNameControlUIForDropDownListT to set
	 */
	public void setClassNameControlUIForDropDownListT(String classNameControlUIForDropDownListT)
	{
		this.classNameControlUIForDropDownListT = classNameControlUIForDropDownListT;
	}

	/**
	 * @return the classNameControlUIForDropDownListT
	 */
	public String getClassNameControlUIForDropDownListT()
	{
		return classNameControlUIForDropDownListT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForDropDownListT(DropDownListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForDropDownListT == null ) && ( getClassNameControlUIForDropDownListT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForDropDownListT();
			logger.debug( "getControlUIForDropDownListT() loading class named: " + tempClassName );
			ControlUI controlUIForDropDownListT;
			try
			{
				controlUIForDropDownListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForDropDownListT != null )
			{
				controlUIForDropDownListT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForDropDownListT;
	}

	
	
	/**
	 * @param classNameControlUIForEditableDropDownListT the classNameControlUIForEditableDropDownListT to set
	 */
	public void setClassNameControlUIForEditableDropDownListT(String classNameControlUIForEditableDropDownListT)
	{
		this.classNameControlUIForEditableDropDownListT = classNameControlUIForEditableDropDownListT;
	}

	/**
	 * @return the classNameControlUIForEditableDropDownListT
	 */
	public String getClassNameControlUIForEditableDropDownListT()
	{
		return classNameControlUIForEditableDropDownListT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForEditableDropDownListT(EditableDropDownListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForEditableDropDownListT == null ) && ( getClassNameControlUIForEditableDropDownListT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForEditableDropDownListT();
			logger.debug( "getControlUIForEditableDropDownListT() loading class named: " + tempClassName );
			ControlUI controlUIForEditableDropDownListT;
			try
			{
				controlUIForEditableDropDownListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForEditableDropDownListT != null )
			{
				controlUIForEditableDropDownListT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForEditableDropDownListT;
	}

	
	
	/**
	 * @param classNameControlUIForRadioButtonListT the classNameControlUIForRadioButtonListT to set
	 */
	public void setClassNameControlUIForRadioButtonListT(String classNameControlUIForRadioButtonListT)
	{
		this.classNameControlUIForRadioButtonListT = classNameControlUIForRadioButtonListT;
	}

	/**
	 * @return the classNameControlUIForRadioButtonListT
	 */
	public String getClassNameControlUIForRadioButtonListT()
	{
		return classNameControlUIForRadioButtonListT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForRadioButtonListT(RadioButtonListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForRadioButtonListT == null ) && ( getClassNameControlUIForRadioButtonListT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForRadioButtonListT();
			logger.debug( "getControlUIForRadioButtonListT() loading class named: " + tempClassName );
			ControlUI controlUIForRadioButtonListT;
			try
			{
				controlUIForRadioButtonListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForRadioButtonListT != null )
			{
				controlUIForRadioButtonListT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForRadioButtonListT;
	}

	
	
	/**
	 * @param classNameControlUIForTextFieldT the classNameControlUIForTextFieldT to set
	 */
	public void setClassNameControlUIForTextFieldT(String classNameControlUIForTextFieldT)
	{
		this.classNameControlUIForTextFieldT = classNameControlUIForTextFieldT;
	}

	/**
	 * @return the classNameControlUIForTextFieldT
	 */
	public String getClassNameControlUIForTextFieldT()
	{
		return classNameControlUIForTextFieldT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForTextFieldT(TextFieldT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForTextFieldT == null ) && ( getClassNameControlUIForTextFieldT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForTextFieldT();
			logger.debug( "getControlUIForTextFieldT() loading class named: " + tempClassName );
			ControlUI controlUIForTextFieldT;
			try
			{
				controlUIForTextFieldT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForTextFieldT != null )
			{
				controlUIForTextFieldT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForTextFieldT;
	}

	
	
	/**
	 * @param classNameControlUIForSliderT the classNameControlUIForSliderT to set
	 */
	public void setClassNameControlUIForSliderT(String classNameControlUIForSliderT)
	{
		this.classNameControlUIForSliderT = classNameControlUIForSliderT;
	}

	/**
	 * @return the classNameControlUIForSliderT
	 */
	public String getClassNameControlUIForSliderT()
	{
		return classNameControlUIForSliderT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForSliderT(SliderT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForSliderT == null ) && ( getClassNameControlUIForSliderT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForSliderT();
			logger.debug( "getControlUIForSliderT() loading class named: " + tempClassName );
			ControlUI controlUIForSliderT;
			try
			{
				controlUIForSliderT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForSliderT != null )
			{
				controlUIForSliderT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForSliderT;
	}

	
	
	/**
	 * @param classNameControlUIForCheckBoxListT the classNameControlUIForCheckBoxListT to set
	 */
	public void setClassNameControlUIForCheckBoxListT(String classNameControlUIForCheckBoxListT)
	{
		this.classNameControlUIForCheckBoxListT = classNameControlUIForCheckBoxListT;
	}

	/**
	 * @return the classNameControlUIForCheckBoxListT
	 */
	public String getClassNameControlUIForCheckBoxListT()
	{
		return classNameControlUIForCheckBoxListT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForCheckBoxListT(CheckBoxListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForCheckBoxListT == null ) && ( getClassNameControlUIForCheckBoxListT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForCheckBoxListT();
			logger.debug( "getControlUIForCheckBoxListT() loading class named: " + tempClassName );
			ControlUI controlUIForCheckBoxListT;
			try
			{
				controlUIForCheckBoxListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForCheckBoxListT != null )
			{
				controlUIForCheckBoxListT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForCheckBoxListT;
	}

	
	
	/**
	 * @param classNameControlUIForClockT the classNameControlUIForClockT to set
	 */
	public void setClassNameControlUIForClockT(String classNameControlUIForClockT)
	{
		this.classNameControlUIForClockT = classNameControlUIForClockT;
	}

	/**
	 * @return the classNameControlUIForClockT
	 */
	public String getClassNameControlUIForClockT()
	{
		return classNameControlUIForClockT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForClockT(ClockT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForClockT == null ) && ( getClassNameControlUIForClockT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForClockT();
			logger.debug( "getControlUIForClockT() loading class named: " + tempClassName );
			ControlUI controlUIForClockT;
			try
			{
				controlUIForClockT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForClockT != null )
			{
				controlUIForClockT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForClockT;
	}

	
	
	/**
	 * @param classNameControlUIForSingleSpinnerT the classNameControlUIForSingleSpinnerT to set
	 */
	public void setClassNameControlUIForSingleSpinnerT(String classNameControlUIForSingleSpinnerT)
	{
		this.classNameControlUIForSingleSpinnerT = classNameControlUIForSingleSpinnerT;
	}

	/**
	 * @return the classNameControlUIForSingleSpinnerT
	 */
	public String getClassNameControlUIForSingleSpinnerT()
	{
		return classNameControlUIForSingleSpinnerT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForSingleSpinnerT(SingleSpinnerT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForSingleSpinnerT == null ) && ( getClassNameControlUIForSingleSpinnerT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForSingleSpinnerT();
			logger.debug( "getControlUIForSingleSpinnerT() loading class named: " + tempClassName );
			ControlUI controlUIForSingleSpinnerT;
			try
			{
				controlUIForSingleSpinnerT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForSingleSpinnerT != null )
			{
				controlUIForSingleSpinnerT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForSingleSpinnerT;
	}

	
	
	/**
	 * @param classNameControlUIForDoubleSpinnerT the classNameControlUIForDoubleSpinnerT to set
	 */
	public void setClassNameControlUIForDoubleSpinnerT(String classNameControlUIForDoubleSpinnerT)
	{
		this.classNameControlUIForDoubleSpinnerT = classNameControlUIForDoubleSpinnerT;
	}

	/**
	 * @return the classNameControlUIForDoubleSpinnerT
	 */
	public String getClassNameControlUIForDoubleSpinnerT()
	{
		return classNameControlUIForDoubleSpinnerT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForDoubleSpinnerT(DoubleSpinnerT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForDoubleSpinnerT == null ) && ( getClassNameControlUIForDoubleSpinnerT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForDoubleSpinnerT();
			logger.debug( "getControlUIForDoubleSpinnerT() loading class named: " + tempClassName );
			ControlUI controlUIForDoubleSpinnerT;
			try
			{
				controlUIForDoubleSpinnerT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForDoubleSpinnerT != null )
			{
				controlUIForDoubleSpinnerT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForDoubleSpinnerT;
	}

	
	
	/**
	 * @param classNameControlUIForSingleSelectListT the classNameControlUIForSingleSelectListT to set
	 */
	public void setClassNameControlUIForSingleSelectListT(String classNameControlUIForSingleSelectListT)
	{
		this.classNameControlUIForSingleSelectListT = classNameControlUIForSingleSelectListT;
	}

	/**
	 * @return the classNameControlUIForSingleSelectListT
	 */
	public String getClassNameControlUIForSingleSelectListT()
	{
		return classNameControlUIForSingleSelectListT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForSingleSelectListT(SingleSelectListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForSingleSelectListT == null ) && ( getClassNameControlUIForSingleSelectListT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForSingleSelectListT();
			logger.debug( "getControlUIForSingleSelectListT() loading class named: " + tempClassName );
			ControlUI controlUIForSingleSelectListT;
			try
			{
				controlUIForSingleSelectListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForSingleSelectListT != null )
			{
				controlUIForSingleSelectListT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForSingleSelectListT;
	}

	
	
	/**
	 * @param classNameControlUIForMultiSelectListT the classNameControlUIForMultiSelectListT to set
	 */
	public void setClassNameControlUIForMultiSelectListT(String classNameControlUIForMultiSelectListT)
	{
		this.classNameControlUIForMultiSelectListT = classNameControlUIForMultiSelectListT;
	}

	/**
	 * @return the classNameControlUIForMultiSelectListT
	 */
	public String getClassNameControlUIForMultiSelectListT()
	{
		return classNameControlUIForMultiSelectListT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForMultiSelectListT(MultiSelectListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForMultiSelectListT == null ) && ( getClassNameControlUIForMultiSelectListT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForMultiSelectListT();
			logger.debug( "getControlUIForMultiSelectListT() loading class named: " + tempClassName );
			ControlUI controlUIForMultiSelectListT;
			try
			{
				controlUIForMultiSelectListT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForMultiSelectListT != null )
			{
				controlUIForMultiSelectListT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForMultiSelectListT;
	}

	
	
	/**
	 * @param classNameControlUIForHiddenFieldT the classNameControlUIForHiddenFieldT to set
	 */
	public void setClassNameControlUIForHiddenFieldT(String classNameControlUIForHiddenFieldT)
	{
		this.classNameControlUIForHiddenFieldT = classNameControlUIForHiddenFieldT;
	}

	/**
	 * @return the classNameControlUIForHiddenFieldT
	 */
	public String getClassNameControlUIForHiddenFieldT()
	{
		return classNameControlUIForHiddenFieldT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForHiddenFieldT(HiddenFieldT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForHiddenFieldT == null ) && ( getClassNameControlUIForHiddenFieldT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForHiddenFieldT();
			logger.debug( "getControlUIForHiddenFieldT() loading class named: " + tempClassName );
			ControlUI controlUIForHiddenFieldT;
			try
			{
				controlUIForHiddenFieldT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForHiddenFieldT != null )
			{
				controlUIForHiddenFieldT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForHiddenFieldT;
	}

	
	
	/**
	 * @param classNameControlUIForLabelT the classNameControlUIForLabelT to set
	 */
	public void setClassNameControlUIForLabelT(String classNameControlUIForLabelT)
	{
		this.classNameControlUIForLabelT = classNameControlUIForLabelT;
	}

	/**
	 * @return the classNameControlUIForLabelT
	 */
	public String getClassNameControlUIForLabelT()
	{
		return classNameControlUIForLabelT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForLabelT(LabelT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForLabelT == null ) && ( getClassNameControlUIForLabelT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForLabelT();
			logger.debug( "getControlUIForLabelT() loading class named: " + tempClassName );
			ControlUI controlUIForLabelT;
			try
			{
				controlUIForLabelT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForLabelT != null )
			{
				controlUIForLabelT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForLabelT;
	}

	
	
	/**
	 * @param classNameControlUIForRadioButtonT the classNameControlUIForRadioButtonT to set
	 */
	public void setClassNameControlUIForRadioButtonT(String classNameControlUIForRadioButtonT)
	{
		this.classNameControlUIForRadioButtonT = classNameControlUIForRadioButtonT;
	}

	/**
	 * @return the classNameControlUIForRadioButtonT
	 */
	public String getClassNameControlUIForRadioButtonT()
	{
		return classNameControlUIForRadioButtonT;
	}
	
	/**
	 * @param control
	 * @param parameter
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUI getControlUIForRadioButtonT(RadioButtonT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException 
	{
// create new each time		if ( ( controlUIForRadioButtonT == null ) && ( getClassNameControlUIForRadioButtonT() != null ) )
//		{
			String tempClassName = getClassNameControlUIForRadioButtonT();
			logger.debug( "getControlUIForRadioButtonT() loading class named: " + tempClassName );
			ControlUI controlUIForRadioButtonT;
			try
			{
				controlUIForRadioButtonT = ((Class<ControlUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
			
			if ( controlUIForRadioButtonT != null )
			{
				controlUIForRadioButtonT.init( control, parameter, aAtdl4jConfig );
			}
//		}
		
		return controlUIForRadioButtonT;
	}

	
	/**
	 * @param inputAndFilterData the inputAndFilterData to set
	 */
	public void setInputAndFilterData(InputAndFilterData inputAndFilterData)
	{
		this.inputAndFilterData = inputAndFilterData;
	}

	/**
	 * @return the inputAndFilterData
	 */
	public InputAndFilterData getInputAndFilterData()
	{
		return inputAndFilterData;
	}

	/**
	 * @param strategies the strategies to set
	 */
	public void setStrategies(StrategiesT strategies)
	{
		this.strategies = strategies;
	}

	/**
	 * @return the strategies
	 */
	public StrategiesT getStrategies()
	{
		return strategies;
	}

	/**
	 * @param strategyUIMap the strategyUIMap to set
	 */
	public void setStrategyUIMap(Map<StrategyT, StrategyUI> strategyUIMap)
	{
		this.strategyUIMap = strategyUIMap;
	}

	/**
	 * @return the strategyUIMap
	 */
	public Map<StrategyT, StrategyUI> getStrategyUIMap()
	{
		return strategyUIMap;
	}

	/**
	 * @param selectedStrategy the selectedStrategy to set
	 */
	public void setSelectedStrategy(StrategyT selectedStrategy)
	{
		this.selectedStrategy = selectedStrategy;
	}

	/**
	 * @return the selectedStrategy
	 */
	public StrategyT getSelectedStrategy()
	{
		return selectedStrategy;
	}

	/**
	 * @param showStrategyDescription the showStrategyDescription to set
	 */
	public void setShowStrategyDescription(boolean showStrategyDescription)
	{
		this.showStrategyDescription = showStrategyDescription;
	}

	/**
	 * @return the showStrategyDescription
	 */
	public boolean isShowStrategyDescription()
	{
		return showStrategyDescription;
	}

	/**
	 * @param showTimezoneSelector the showTimezoneSelector to set
	 */
	public void setShowTimezoneSelector(boolean showTimezoneSelector)
	{
		this.showTimezoneSelector = showTimezoneSelector;
	}

	/**
	 * @return the showTimezoneSelector
	 */
	public boolean isShowTimezoneSelector()
	{
		return showTimezoneSelector;
	}

	public List<StrategyT> getStrategiesFilteredStrategyList()
	{
		if ( ( getStrategies() == null ) && ( getStrategies().getStrategy() != null ) )
		{
			return null;
		}
		
		if ( getInputAndFilterData() == null )
		{
			return getStrategies().getStrategy();
		}
		
		List<StrategyT> tempFilteredList = new ArrayList<StrategyT>();
		
		for ( StrategyT strategy : getStrategies().getStrategy() ) 
		{
			if ( !getInputAndFilterData().isStrategySupported( strategy ) )
			{
				logger.info("Excluding strategy: " + strategy.getName() + " as inputAndFilterData.isStrategySupported() returned false." );
				continue; // skip it 
			}
			
			tempFilteredList.add( strategy );
		}
		
		return tempFilteredList;
	}

	/**
	 * @param classNameStrategySelectionUI the classNameStrategySelectionUI to set
	 */
	public void setClassNameStrategySelectionUI(String classNameStrategySelectionUI)
	{
		this.classNameStrategySelectionUI = classNameStrategySelectionUI;
	}

	/**
	 * @return the classNameStrategySelectionUI
	 */
	public String getClassNameStrategySelectionUI()
	{
		return classNameStrategySelectionUI;
	}

	/**
	 * @param strategySelectionUI the strategySelectionUI to set
	 */
	public void setStrategySelectionUI(StrategySelectionUI strategySelectionUI)
	{
		this.strategySelectionUI = strategySelectionUI;
	}

	/**
	 * @return the StrategySelectionUI
	 */
	public StrategySelectionUI getStrategySelectionUI() 
	{
		if ( ( strategySelectionUI == null ) && ( getClassNameStrategySelectionUI() != null ) )
		{
			String tempClassName = getClassNameStrategySelectionUI();
			logger.debug( "getStrategySelectionUI() loading class named: " + tempClassName );
			try
			{
				strategySelectionUI = ((Class<StrategySelectionUI>) Class.forName( tempClassName ) ).newInstance();
			}
			catch ( Exception e )
			{
				logger.warn( "Exception attempting to load Class.forName( " + tempClassName + " ).  Catching/Re-throwing as IllegalStateException", e );
				throw new IllegalStateException( "Exception attempting to load Class.forName( " + tempClassName + " )", e );
			}
		}
		
		return strategySelectionUI;
	}

	/**
	 * @return the treatControlVisibleFalseAsNull
	 */
	public boolean isTreatControlVisibleFalseAsNull()
	{
		return this.treatControlVisibleFalseAsNull;
	}

	/**
	 * @param aTreatControlVisibleFalseAsNull the treatControlVisibleFalseAsNull to set
	 */
	public void setTreatControlVisibleFalseAsNull(boolean aTreatControlVisibleFalseAsNull)
	{
		this.treatControlVisibleFalseAsNull = aTreatControlVisibleFalseAsNull;
	}

	/**
	 * @return the treatControlEnabledFalseAsNull
	 */
	public boolean isTreatControlEnabledFalseAsNull()
	{
		return this.treatControlEnabledFalseAsNull;
	}

	/**
	 * @param aTreatControlEnabledFalseAsNull the treatControlEnabledFalseAsNull to set
	 */
	public void setTreatControlEnabledFalseAsNull(boolean aTreatControlEnabledFalseAsNull)
	{
		this.treatControlEnabledFalseAsNull = aTreatControlEnabledFalseAsNull;
	}

	/**
	 * @return the showEnabledCheckboxOnOptionalClockControl
	 */
	public boolean isShowEnabledCheckboxOnOptionalClockControl()
	{
		return this.showEnabledCheckboxOnOptionalClockControl;
	}

	/**
	 * @param aShowEnabledCheckboxOnOptionalClockControl the showEnabledCheckboxOnOptionalClockControl to set
	 */
	public void setShowEnabledCheckboxOnOptionalClockControl(boolean aShowEnabledCheckboxOnOptionalClockControl)
	{
		this.showEnabledCheckboxOnOptionalClockControl = aShowEnabledCheckboxOnOptionalClockControl;
	}

	/**
	 * @return the restoreLastNonNullStateControlValueBehavior
	 */
	public boolean isRestoreLastNonNullStateControlValueBehavior()
	{
		return this.restoreLastNonNullStateControlValueBehavior;
	}

	/**
	 * @param aRestoreLastNonNullStateControlValueBehavior the restoreLastNonNullStateControlValueBehavior to set
	 */
	public void setRestoreLastNonNullStateControlValueBehavior(boolean aRestoreLastNonNullStateControlValueBehavior)
	{
		this.restoreLastNonNullStateControlValueBehavior = aRestoreLastNonNullStateControlValueBehavior;
	}

	/**
	 * @return the defaultDigitsForSpinnerControl
	 */
	public Integer getDefaultDigitsForSpinnerControl()
	{
		return this.defaultDigitsForSpinnerControl;
	}

	/**
	 * @param aDefaultDigitsForSpinnerControl the defaultDigitsForSpinnerControl to set
	 */
	public void setDefaultDigitsForSpinnerControl(Integer aDefaultDigitsForSpinnerControl)
	{
		this.defaultDigitsForSpinnerControl = aDefaultDigitsForSpinnerControl;
	}

}
