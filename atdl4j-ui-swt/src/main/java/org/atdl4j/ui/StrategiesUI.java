package org.atdl4j.ui;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.config.Atdl4jConfig;

public interface StrategiesUI<T> {

// 2/8/2010 Scott Atwell added	
	// -- Call this after constructor --
	public void init(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
			throws JAXBException;

	public Atdl4jConfig getAtdl4jConfig();
	
//TODO 1/17/2010 Scott Atwell	public StrategyUI createUI(StrategyT strategy, T parent)
//TODO?????? 2/7/2010 Scott Atwell	public StrategyUI createUI(StrategyT strategy, T parent, Map<String, String> inputHiddenFieldNameValueMap)
// 2/8/2010 Scott Atwell (use getAtdl4jConfig() vs. inputHiddenFieldNameValueMap) 	public StrategyUI createUI(StrategyT strategy, Object parent, Map<String, String> inputHiddenFieldNameValueMap)
	public StrategyUI createUI(StrategyT strategy, Object parent)
		throws JAXBException;
}
