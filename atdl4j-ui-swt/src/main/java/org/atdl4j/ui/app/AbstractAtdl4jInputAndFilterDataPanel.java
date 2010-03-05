/*
 * Created on Feb 28, 2010
 *
 */
package org.atdl4j.ui.app;

import java.util.List;
import java.util.Vector;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;

/**
 * Represents the base, non-GUI system-specific Atdl4jConfig and InputAndFilterData GUI component.
 * 
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public abstract class AbstractAtdl4jInputAndFilterDataPanel
	implements Atdl4jInputAndFilterDataPanel,
		FixMsgLoadPanelListener
{

	Atdl4jConfig atdl4jConfig;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc
	
	private List<Atdl4jInputAndFilterDataPanelListener> listenerList = new Vector<Atdl4jInputAndFilterDataPanelListener>();
	private List<FixMsgLoadPanelListener> fixMsgLoadPaneListenerList = new Vector<FixMsgLoadPanelListener>();

	private FixMsgLoadPanel fixMsgLoadPanel;

	protected void init( Object aParentOrShell, Atdl4jConfig aAtdl4jConfig )
	{
		setAtdl4jConfig( aAtdl4jConfig );
		setParentOrShell( aParentOrShell );
		
		
		
		// -- FixMsgLoadPanel (Load Message button/text field) - build() method called via concrete class --
		setFixMsgLoadPanel( getAtdl4jConfig().getFixMsgLoadPanel() );
		getFixMsgLoadPanel().addListener( this );
	}

	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return this.atdl4jConfig;
	}

	/**
	 * @param aAtdl4jConfig the atdl4jConfig to set
	 */
	private void setAtdl4jConfig(Atdl4jConfig aAtdl4jConfig)
	{
		this.atdl4jConfig = aAtdl4jConfig;
	}

	/**
	 * @return the parentOrShell
	 */
	public Object getParentOrShell()
	{
		return this.parentOrShell;
	}

	/**
	 * @param aParentOrShell the parentOrShell to set
	 */
	private void setParentOrShell(Object aParentOrShell)
	{
		this.parentOrShell = aParentOrShell;
	}
	

	/**
	 * @return the fixMsgLoadPanel
	 */
	public FixMsgLoadPanel getFixMsgLoadPanel()
	{
		return this.fixMsgLoadPanel;
	}

	/**
	 * @param aFixMsgLoadPanel the fixMsgLoadPanel to set
	 */
	private void setFixMsgLoadPanel(FixMsgLoadPanel aFixMsgLoadPanel)
	{
		this.fixMsgLoadPanel = aFixMsgLoadPanel;
	}

	public void addFixMsgLoadPanelListener( FixMsgLoadPanelListener aFixMsgLoadPanelListener )
	{
		fixMsgLoadPaneListenerList.add( aFixMsgLoadPanelListener );
	}

	public void removeFixMsgLoadPanelListener( FixMsgLoadPanelListener aFixMsgLoadPanelListener )
	{
		fixMsgLoadPaneListenerList.remove( aFixMsgLoadPanelListener );
	}	

	protected void fireFixMsgLoadSelectedEvent( String aFixMsg )
	{
		for ( FixMsgLoadPanelListener tempListener : fixMsgLoadPaneListenerList )
		{
			tempListener.fixMsgLoadSelected( aFixMsg );
		}
	}
	
	public void addListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener )
	{
		listenerList.add( aAtdl4jInputAndFilterCriteriaPanelListener );
	}

	public void removeListener( Atdl4jInputAndFilterDataPanelListener aAtdl4jInputAndFilterCriteriaPanelListener )
	{
		listenerList.remove( aAtdl4jInputAndFilterCriteriaPanelListener );
	}	
	
	protected void fireInputAndFilterDataSpecifiedEvent( InputAndFilterData aInputAndFilterData )
	{
		for ( Atdl4jInputAndFilterDataPanelListener tempListener : listenerList )
		{
			tempListener.inputAndFilterDataSpecified( aInputAndFilterData );
		}
	}

	/* 
	 * Re-fire to listeners who have registered with us.
	 */
	public void fixMsgLoadSelected(String aFixMsg)
	{
		fireFixMsgLoadSelectedEvent( aFixMsg );
	}
}
