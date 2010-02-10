package org.atdl4j.ui.impl;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.layout.StrategyPanelT;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.StrategyUI;

/**
 * Base class for ValidationRule.
 * 
 * @param <E>
 */
public abstract class AbstractStrategyUI implements StrategyUI {

	abstract	protected void buildControlMap( List<StrategyPanelT> aStrategyPanelList )
		throws JAXBException;
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
	abstract public Map<String, ControlUI<?>> getControlUIMap();
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
	abstract public Map<String, ControlUI<?>> getControlUIWithParameterMap();


}