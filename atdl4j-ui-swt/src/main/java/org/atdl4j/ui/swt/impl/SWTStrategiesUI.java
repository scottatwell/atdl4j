package org.atdl4j.ui.swt.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.ui.StrategiesUI;
import org.eclipse.swt.widgets.Composite;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.validation.EditT;

public class SWTStrategiesUI implements StrategiesUI<Composite> {

	private Map<String, ValidationRule> strategiesRules;
//TODO 1/16/2010 Scott Atwell added
	private StrategiesT strategies;

	public SWTStrategiesUI(StrategiesT strategies)
			throws JAXBException {
//TODO 1/16/2010 Scott Atwell added
		this.strategies = strategies;

		strategiesRules = new HashMap<String, ValidationRule>();

		for (EditT edit : strategies.getEdit()) {
			String id = edit.getId();
			if (id != null) {
				ValidationRule rule = ValidationRuleFactory.createRule(edit,
						strategiesRules, strategies);
				strategiesRules.put(id, rule);
			} else {
				throw new JAXBException("Strategies-scoped edit without id");
			}
		}
	}

//TODO 1/17/2010 Scott Atwell	public SWTStrategyUI createUI(StrategyT strategy, Composite parent)
public SWTStrategyUI createUI(StrategyT strategy, Composite parent, Map<String, String> inputHiddenFieldNameValueMap)
			throws JAXBException {
		//try {
//TODO 1/16/2010 Scott Atwell			return new SWTStrategyUI(strategy, strategiesRules, parent);
//TODO 1/17/2010 Scott Atwell			return new SWTStrategyUI(strategy, strategiesRules, parent, strategies);
			return new SWTStrategyUI(strategy, strategiesRules, parent, strategies, inputHiddenFieldNameValueMap);
						
		//} catch (JAXBException e) {
		//	throw new JAXBException("Error in Strategy \"" + strategy.getName() + "\": " + e.getMessage());
		//}	
	}

public SWTStrategyUI createUI(StrategyT strategy, Object parent, Map<String, String> inputHiddenFieldNameValueMap)
			throws JAXBException {
	return createUI( strategy, (Composite) parent, inputHiddenFieldNameValueMap);
}
}
