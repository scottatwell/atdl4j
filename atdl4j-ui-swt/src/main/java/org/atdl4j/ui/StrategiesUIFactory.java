package org.atdl4j.ui;

import javax.xml.bind.JAXBException;

import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.config.Atdl4jConfig;

// @see org.atdl4j.ui.impl.BaseStrategiesUIFactory
public interface StrategiesUIFactory
{
	// 2/8/2010 Scott Atwell public StrategiesUI<?> create(StrategiesT
	// strategies)
	// 2/8/2010 Scott Atwell throws JAXBException;
	public StrategiesUI<?> create(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
			throws JAXBException;
}
