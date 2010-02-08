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
 * 
 * Creation date: (Feb 7, 2010 7:01:05 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public interface Atdl4jConfig
{
	/**
	 * @return the strategiesUIFactory
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public StrategiesUIFactory getStrategiesUIFactory() 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException;

	public InputAndFilterData getInputAndFilterData();
	public void setInputAndFilterData(InputAndFilterData inputAndFilterData);
	
	public StrategiesT getStrategies();
	public void setStrategies(StrategiesT strategies);

	public Map<StrategyT, StrategyUI> getStrategyUIMap();
	public void setStrategyUIMap(Map<StrategyT, StrategyUI> strategyUIMap);

	public StrategyT getSelectedStrategy();
	public void setSelectedStrategy(StrategyT selectedStrategy);

	public void setShowStrategyDescription(boolean showStrategyDescription);
	public boolean isShowStrategyDescription();

	public void setShowTimezoneSelector(boolean showTimezoneSelector);
	public boolean isShowTimezoneSelector();

}
