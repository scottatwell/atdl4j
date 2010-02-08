/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;

import java.util.Map;

import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;

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
	private String classNameStrategiesUIFactory;
	private StrategiesUIFactory strategiesUIFactory;
	
	private InputAndFilterData inputAndFilterData;
	
	private boolean showStrategyDescription = true;
	private boolean showTimezoneSelector = false;

	private StrategiesT strategies;
	private Map<StrategyT, StrategyUI> strategyUIMap;
	private StrategyT selectedStrategy;
	

	
	/**
	 * 
	 */
	public AbstractAtdl4jConfig()
	{
		setClassNameStrategiesUIFactory( getDefaultClassNameStrategiesUIFactory() );
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
	
	abstract protected String getDefaultClassNameStrategiesUIFactory();

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
	
}
