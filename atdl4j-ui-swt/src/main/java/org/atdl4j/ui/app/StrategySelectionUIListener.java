/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.ui.app;

import org.atdl4j.atdl.core.StrategyT;

/**
 * 
 * 
 * This class contains the data associated with the <code>StrategySelectionUIListener</code>.
 * 
 * Creation date: (Feb 7, 2010 9:52:59 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public interface StrategySelectionUIListener
{
	public void strategySelected(StrategyT strategy, int index);
}
