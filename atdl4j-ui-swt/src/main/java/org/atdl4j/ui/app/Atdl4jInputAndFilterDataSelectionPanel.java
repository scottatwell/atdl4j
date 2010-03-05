package org.atdl4j.ui.app;

import javax.xml.bind.JAXBException;

import org.atdl4j.config.Atdl4jConfig;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;


/**
 * Represents the GUI component used to invoke Atdl4jInputAndFilterDataPanel pop-up.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public interface Atdl4jInputAndFilterDataSelectionPanel
{
	public Object buildAtdl4jInputAndFilterDataSelectionPanel(Object aParentOrShell, Atdl4jConfig aAtdl4jConfig);

	public Atdl4jConfig getAtdl4jConfig();
	
	public void addListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener );

	public void removeListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener );

	public void addFixMsgLoadPanelListener(FixMsgLoadPanelListener aFixMsgLoadPanelListener);
	
	public void removeFixMsgLoadPanelListener(FixMsgLoadPanelListener aFixMsgLoadPanelListener);

}
