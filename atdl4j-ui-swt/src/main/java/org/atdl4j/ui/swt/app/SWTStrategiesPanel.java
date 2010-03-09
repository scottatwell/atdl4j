package org.atdl4j.ui.swt.app;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.AbstractStrategiesPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Represents the SWT-specific available strategy choices GUI component.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 */
public class SWTStrategiesPanel
		extends AbstractStrategiesPanel
{
	private final Logger logger = Logger.getLogger( SWTStrategiesPanel.class );

	private Composite strategiesPanel;

	public Object buildStrategiesPanel(Object parentOrShell, Atdl4jConfig atdl4jConfig)
	{
		return buildStrategiesPanel( (Composite) parentOrShell, atdl4jConfig );
	}

	public Composite buildStrategiesPanel(Composite aParentComposite, Atdl4jConfig atdl4jConfig)
	{
		setAtdl4jConfig( atdl4jConfig );

		if ( ( atdl4jConfig != null ) && ( atdl4jConfig.getAtdl4jUserMessageHandler() != null )
				&& ( atdl4jConfig.getAtdl4jUserMessageHandler().isInitReqd() ) )
		{
			atdl4jConfig.initAtdl4jUserMessageHandler( aParentComposite );
		}

		// Main strategies panel
		strategiesPanel = new Composite( aParentComposite, SWT.NONE );
		GridLayout strategiesLayout = new GridLayout( 1, false );
		strategiesLayout.verticalSpacing = 0;
		strategiesPanel.setLayout( strategiesLayout );
		strategiesPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

		return strategiesPanel;
	}

	public void removeAllStrategyPanels()
	{
		// remove all strategy panels
		for ( Control control : strategiesPanel.getChildren() )
			control.dispose();
	}

	public void createStrategyPanels(List<StrategyT> aFilteredStrategyList)
	{
		StrategiesUI<?> strategiesUI = null;
		setPreCached( false );
	
		try
		{
			StrategiesUIFactory factory = getAtdl4jConfig().getStrategiesUIFactory();
			strategiesUI = factory.create( getAtdl4jConfig().getStrategies(), getAtdl4jConfig() );
		}
		catch (Exception e)
		{
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Strategy Load Error",
					"Error creating StrategiesUIFactory and StrategisUI", e );
			return;
		}
		
		getAtdl4jConfig().setStrategyUIMap( new HashMap<StrategyT, StrategyUI>() );
		
		for ( StrategyT strategy : aFilteredStrategyList )
		{
			// create composite
			Composite strategyParent = new Composite( strategiesPanel, SWT.NONE );
			strategyParent.setLayout( new FillLayout() );
			StrategyUI ui;

			// build strategy and catch strategy-specific errors
			try
			{
				ui = strategiesUI.createUI( strategy, strategyParent );
			}
			catch (Throwable e)
			{
				getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Strategy Load Error",
						"Error in Strategy: " + Atdl4jHelper.getStrategyUiRepOrName( strategy ), e );

				// rollback changes
				strategyParent.dispose();

				// skip to next strategy
				continue;
			}

			getAtdl4jConfig().getStrategyUIMap().put( strategy, ui );

			ui.setCxlReplaceMode( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );
		}

		// -- Force the display to only show the Composite panels for the first strategy, otherwise the first screen is a jumbled mess of all strategy's parameters sequentially --
		if ( getAtdl4jConfig().getStrategyUIMap().size() > 0 )
		{
			adjustLayoutForSelectedStrategy( 0 );
		}
		
		setPreCached( true );
	}  

	public void adjustLayoutForSelectedStrategy(int aIndex)
	{
		if ( strategiesPanel != null )
		{
			// -- These were the remnants from selectDropDownStrategy(int index) that did not become part of StrategySelectionPanel
			for (int i = 0; i < strategiesPanel.getChildren().length; i++) 
			{
				if ( ( strategiesPanel.getChildren()[i] != null ) && 
					  ((GridData)strategiesPanel.getChildren()[i].getLayoutData() != null ) ) 
				{
					((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != aIndex) ? 0 : -1;
					((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != aIndex) ? 0 : -1;
				}
			}
			
			strategiesPanel.layout();
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategiesPanel#reinitStrategyPanels()
	 */
	@Override
	public void reinitStrategyPanels()
		throws JAXBException
	{
		for ( StrategyUI tempStrategyUI : getAtdl4jConfig().getStrategyUIMap().values() )
		{
			logger.info( "Invoking StrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName( tempStrategyUI.getStrategy() ) );

			tempStrategyUI.reinitStrategyPanel();
		}
		
		// -- Force the display to only show the Composite panels for the first strategy, otherwise the first screen is a jumbled mess of all strategy's parameters sequentially --
		if ( getAtdl4jConfig().getStrategyUIMap().size() > 0 )
		{
			adjustLayoutForSelectedStrategy( 0 );
		}
	}


}
