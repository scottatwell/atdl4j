/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.List;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategyT;


/**
 * Represents the FIXatdl strategy panels (StrategiesUI) GUI component. 
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 * @author Scott Atwell
 * @version 1.0, Feb 26, 2010
 */
public interface StrategiesPanel
{
	public Object buildStrategiesPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig);

	public void createStrategyPanels(List<StrategyT> aFilteredStrategyList);

	public void removeAllStrategyPanels();
	
	public void adjustLayoutForSelectedStrategy(int aIndex);
	
	public Atdl4jConfig getAtdl4jConfig();
	
	public void addListener(StrategiesPanelListener strategiesUIListener);
	
	public void removeListener(StrategiesPanelListener strategiesUIListener);

}
