package org.atdl4j.ui.swt.impl;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * 
 * Helper class used to wrap call to SWTStrategyPanelHelper.revalidateLayout() with Display.getCurrent().asyncExec(new Runnable()...)
 * 
 * Creation date: (Mar 3, 2010 7:56:21 AM)
 * @author Scott Atwell
 * @version 1.0, Mar 3, 2010
 */
public class SWTRevalidateLayoutThread
		extends Thread
{
	private Control control;
	
	public SWTRevalidateLayoutThread( Control aControl )
	{
		control = aControl;
		
		Display.getCurrent().asyncExec(new Runnable()
		{
         public void run()
         {
         	SWTStrategyPanelHelper.revalidateLayout( control );
         }
     });
	}
}
