/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.StrategySelectionUI;

/**
 * Typical setup (for class named XXX):
 * 	private String classNameXXX;
 * 	public void setClassNameXXX(String);
 * 	public String getClassNameXXX();
 *    abstract protected String getDefaultClassNameXXX();
 *    public void setXXX(XXX);
 *    public XXX getXXX() throws InstantiationException, IllegalAccessException, ClassNotFoundException;
 * 	add to constructor:  setClassNameXXX( getDefaultClassNameXXX() );
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
	
	private String classNameStrategiesUIFactory;
	private StrategiesUIFactory strategiesUIFactory;
	
	private String classNameStrategySelectionUI;
	private StrategySelectionUI strategySelectionUI;
	
	private InputAndFilterData inputAndFilterData;
	
	private boolean showStrategyDescription = true;
	private boolean showTimezoneSelector = false;

	private StrategiesT strategies;
	private Map<StrategyT, StrategyUI> strategyUIMap;
	private StrategyT selectedStrategy;
	

	abstract protected String getDefaultClassNameStrategiesUIFactory();
	abstract protected String getDefaultClassNameStrategySelectionUI();
	
	/**
	 * 
	 */
	public AbstractAtdl4jConfig()
	{
		setClassNameStrategiesUIFactory( getDefaultClassNameStrategiesUIFactory() );
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
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public StrategiesUIFactory getStrategiesUIFactory() 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		if ( ( strategiesUIFactory == null ) && ( getClassNameStrategiesUIFactory() != null ) )
		{
			logger.debug( "getStrategiesUIFactory() loading class named: " + getClassNameStrategiesUIFactory() );
			return ((Class<StrategiesUIFactory>) Class.forName( getClassNameStrategiesUIFactory() ) ).newInstance();
		}
		
		return strategiesUIFactory;
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
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public StrategySelectionUI getStrategySelectionUI() 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		if ( ( strategySelectionUI == null ) && ( getClassNameStrategySelectionUI() != null ) )
		{
			logger.debug( "getStrategySelectionUI() loading class named: " + getClassNameStrategySelectionUI() );
			return ((Class<StrategySelectionUI>) Class.forName( getClassNameStrategySelectionUI() ) ).newInstance();
		}
		
		return strategySelectionUI;
	}

}
