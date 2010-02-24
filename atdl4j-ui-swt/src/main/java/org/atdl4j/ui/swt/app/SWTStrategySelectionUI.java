package org.atdl4j.ui.swt.app;


import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.ui.app.StrategySelectionUI;
import org.atdl4j.ui.app.StrategySelectionUIListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class SWTStrategySelectionUI 
	implements StrategySelectionUI
{
	private final Logger logger = Logger.getLogger(SWTStrategySelectionUI.class);
	
	private Combo strategiesDropDown;
	
	private List<StrategySelectionUIListener> listenerList = new Vector<StrategySelectionUIListener>();

	private Atdl4jConfig atdl4jConfig = null;

	public SWTStrategySelectionUI()
	{
	}

	public Object buildStrategySelectionPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig)
	{
		return buildStrategySelectionPanel( (Shell) parentOrShell, atdl4jConfig );
	}
	
	public Composite buildStrategySelectionPanel(Shell shell, Atdl4jConfig atdl4jConfig)
	{
		setAtdl4jConfig( atdl4jConfig );
		
		// Strategy selector dropdown
		Composite dropdownComposite = new Composite(shell, SWT.NONE);
		GridLayout dropdownLayout = new GridLayout(2, false);
		dropdownComposite.setLayout(dropdownLayout);
		dropdownComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// label
		Label strategiesDropDownLabel = new Label(dropdownComposite, SWT.NONE);
		strategiesDropDownLabel.setText("Strategy");
		// dropDownList
		strategiesDropDown = new Combo(dropdownComposite, SWT.READ_ONLY | SWT.BORDER);
		strategiesDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		// tooltip
		strategiesDropDown.setToolTipText("Select a Strategy");
		// action listener
		strategiesDropDown.addSelectionListener(new SelectionAdapter() 
			{
				@Override
				public void widgetSelected(SelectionEvent event) 
				{
					int index = strategiesDropDown.getSelectionIndex();
					selectDropDownStrategy( index );
				}
			} 
		);
	
		return dropdownComposite;
	}
	
	public void loadStrategyList( List<StrategyT> aStrategyList )
	{
		// remove all dropdown items
		strategiesDropDown.removeAll();

		if ( aStrategyList == null )
		{
			return;
		}
		
		for (StrategyT strategy : aStrategyList) 
		{
			// create dropdown item for strategy
			strategiesDropDown.add(getStrategyUiRepOrName(strategy));
		}

		if (strategiesDropDown.getItemCount() > 0)
		{
			strategiesDropDown.select( 0 );
		}
	}


//	private String getStrategyName(StrategyT strategy) 
	public static String getStrategyUiRepOrName(StrategyT strategy) 
	{
		if ( strategy == null )
		{
			return null;
		}
		
		if (strategy.getUiRep() != null) 
		{
			return strategy.getUiRep();
		} 
		else 
		{
			return strategy.getName();
		}
	}

	public void selectDropDownStrategy(int index) 
	{
		strategiesDropDown.select( index );
		
//TODO ??????		
//		// below moved from and called by strategiesDropDown.widgetSelected(SelectionEvent event)
//		for (int i = 0; i < strategiesPanel.getChildren().length; i++) {
//			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != index) ? 0 : -1;
//			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != index) ? 0 : -1;
//		}
		
		
		if ( (getAtdl4jConfig() != null ) && (getAtdl4jConfig().getStrategies() != null) )
		{
			String tempSelectedDropDownName = strategiesDropDown.getItem( index );
			getAtdl4jConfig().setSelectedStrategy( null ); 
			for ( StrategyT tempStrategy : getAtdl4jConfig().getStrategies().getStrategy() )
			{
				if ( ( ( tempStrategy.getUiRep() != null ) && ( tempStrategy.getUiRep().equals( tempSelectedDropDownName ) ) ) ||
					  ( ( tempStrategy.getUiRep() == null ) && ( tempStrategy.getName().equals( tempSelectedDropDownName ) ) ) )
				{
					getAtdl4jConfig().setSelectedStrategy( tempStrategy );
					fireStrategySelectedEvent( tempStrategy, index );
					break;
				}
			}
			
//TODO move outside to listener			if (getAtdl4jConfig().isShowStrategyDescription()) strategyDescription.setText("");
		}
//TODO move outside to listener		strategiesPanel.layout();
//TODO move outside to listener		shell.pack();
// Strategy description must be updated after packing
//TODO move outside to listener		if (getAtdl4jConfig().isShowStrategyDescription()) strategyDescription.setText(getAtdl4jConfig().getSelectedStrategy().getDescription());
	}

	
	public void selectDropDownStrategy(String strategyName) 
	{
		for (int i = 0; i < strategiesDropDown.getItemCount(); i++) 
		{
			if ( strategyName.equals( strategiesDropDown.getItem( i ) ) ) 
			{
				selectDropDownStrategy( i );
				return;
			}
		}
	}


	/**
	 * @param atdl4jConfig the atdl4jConfig to set
	 */
	public void setAtdl4jConfig(Atdl4jConfig atdl4jConfig)
	{
		this.atdl4jConfig = atdl4jConfig;
	}

	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return atdl4jConfig;
	}

	public void addListener( StrategySelectionUIListener aStrategySelectionPanelListener )
	{
		listenerList.add( aStrategySelectionPanelListener );
	}

	public void removeListener( StrategySelectionUIListener aStrategySelectionPanelListener )
	{
		listenerList.remove( aStrategySelectionPanelListener );
	}	
	
	private void fireStrategySelectedEvent( StrategyT aStrategy, int index )
	{
		for ( StrategySelectionUIListener tempListener : listenerList )
		{
			tempListener.strategySelected( aStrategy, index );
		}
	}
}
