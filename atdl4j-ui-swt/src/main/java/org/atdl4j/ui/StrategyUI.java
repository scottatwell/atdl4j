package org.atdl4j.ui;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.FIXMessageBuilder;
import org.atdl4j.data.StrategyRuleset;
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
	public Map<String, ControlUI<?>> getControlUIMap();

	// -- Note invoking this method may result in object construction as a result of down-casting its own map of a specific templatized instance of ControlUI<?> --
	public Map<String, ControlUI<?>> getControlUIWithParameterMap();

   public void setCxlReplaceMode(boolean cxlReplaceMode);

   
// 2/10/2010 Scott Atwell exposed these (public within AbstractStrategyUI) -- may not be required/used   
   public Atdl4jConfig getAtdl4jConfig();
	public Map<String, ParameterT> getParameterMap();
	public StrategyRuleset getStrategyRuleset();
	public Map<String, ValidationRule> getCompleteValidationRuleMap();

}