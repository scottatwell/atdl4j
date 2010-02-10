package org.atdl4j.ui.impl;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategiesUIFactory;

/*
 * Note that this class does not need a SWT or Swing-specific implementation as it solely uses aAtdl4jConfig.getStrategiesUI().
 * @author Scott Atwell 
 */
public class BaseStrategiesUIFactory
		implements StrategiesUIFactory
{

	public StrategiesUI<?> create(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
			throws JAXBException
	{
		return aAtdl4jConfig.getStrategiesUI( strategies, aAtdl4jConfig );
	}
}
