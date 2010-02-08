/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.swt.config;

import org.atdl4j.config.AbstractAtdl4jConfig;

/**
 * 
 *  =====================================================================*
 *  Copyright Notice 
 *  This file contains proprietary information of American Century.
 *  Copying or reproduction without prior written approval is prohibited.
 *  Copyright (c) 2010
 *  =====================================================================*
 * 
 * 
 * This class contains the data associated with the <code>SWTAtdl4jConfig</code>.
 * 
 * Creation date: (Feb 7, 2010 6:39:07 PM)
 * @author SWL
 * @version 1.0, Feb 7, 2010
 */
public class SWTAtdl4jConfig
		extends AbstractAtdl4jConfig
{
	private static String PACKAGE_PATH_ORG_ATDL4J_UI_SWT = "org.atdl4j.ui.swt.";
	
	public SWTAtdl4jConfig()
	{
		super();
	}
	
	protected String getDefaultClassNameStrategiesUIFactory()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "impl.SWTStrategiesUIFactory";
	}
	
	protected String getDefaultClassNameStrategySelectionUI()
	{ 
		return PACKAGE_PATH_ORG_ATDL4J_UI_SWT + "app.SWTStrategySelectionUI";
	}
	
}
