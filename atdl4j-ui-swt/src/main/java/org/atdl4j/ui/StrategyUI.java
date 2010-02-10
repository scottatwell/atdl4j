package org.atdl4j.ui;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.ValidationException;

public interface StrategyUI {

	/**
	 * @param strategy
	 * @param aAtdl4jConfig (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (should be swt.Composite)
	 * @throws JAXBException
	 */
	public void init(StrategyT strategy, Atdl4jConfig aAtdl4jConfig, Map<String, ValidationRule> strategiesRules, Object parentContainer)
			 throws JAXBException;
   
	public void validate() throws ValidationException, JAXBException;

	public StrategyT getStrategy();

	public String getFIXMessage() throws JAXBException;

	public void getFIXMessage(FIXMessageBuilder builder) throws JAXBException;

	public void setFIXMessage(String text) throws JAXBException;
	
	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
//TODO Scott Atwell added 1/14/2010
// 2/9/2010 Scott Atwell	public Map<String, ControlUI<?>> getControls();
	public Map<String, ControlUI<?>> getControlUIMap();

	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
	public Map<String, ControlUI<?>> getControlUIWithParameterMap();

// Scott Atwell added 2/7/2010
   public void setCxlReplaceMode(boolean cxlReplaceMode);

}