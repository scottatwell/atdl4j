/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.List;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategyT;

/**
 * 
 * This class contains the data associated with the <code>StrategySelectionUI</code>.
 * 
 * Creation date: (Feb 7, 2010 9:49:04 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public interface StrategySelectionUI
{
	public Object buildStrategySelectionPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig);
	
	public void loadStrategyList( List<StrategyT> aStrategyList );

	public void selectDropDownStrategy(String strategyName);

	public Atdl4jConfig getAtdl4jConfig();
	
	public void setAtdl4jConfig(Atdl4jConfig atdl4jConfig);
	
	public void addListener(StrategySelectionUIListener strategySelectionUIListener);
	
	public void removeListener(StrategySelectionUIListener strategySelectionUIListener);

}
