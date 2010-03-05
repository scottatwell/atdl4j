package org.atdl4j.ui.swt.app;

import java.util.HashMap;
import java.util.List;

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
		// implements StrategiesPanel
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
	}

/**** ???? ****
	private void initStrategiesUI()
	{
		StrategiesUIFactory factory = getAtdl4jConfig().getStrategiesUIFactory();
		StrategiesUI<?> strategiesUI = factory.create( getAtdl4jConfig().getStrategies(), getAtdl4jConfig() );
		getAtdl4jConfig().setStrategyUIMap( new HashMap<StrategyT, StrategyUI>() );

		List<StrategyT> tempFilteredStrategyList = getAtdl4jConfig().getStrategiesFilteredStrategyList();

		for ( StrategyT strategy : tempFilteredStrategyList )
		{
			// create composite
			Composite strategyParent = new Composite( strategiesPanel, SWT.NONE );
			strategyParent.setLayout( new FillLayout() );
			// 2/7/2010 Scott Atwell SWTStrategyUI ui;
			StrategyUI ui;

			// build strategy and catch strategy-specific errors
			try
			{
				// TODO 1/17/2010 Scott Atwell ui = strategiesUI.createUI(strategy,
				// strategyParent);
				// 2/8/2010 Scott Atwell StrategiesUI now already has Atdl4jConfig
				// ui = strategiesUI.createUI(strategy, strategyParent,
				// getAtdl4jConfig().getInputAndFilterData().getInputHiddenFieldNameValueMap());
				ui = strategiesUI.createUI( strategy, strategyParent );
			}
			catch (JAXBException e1)
			{
				MessageBox messageBox = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
				// e1.getMessage() is null if there is a JAXB parse error
				String msg = "";
				if ( e1.getMessage() != null )
				{
					messageBox.setText( "Strategy Load Error" );
					msg = e1.getMessage();
				}
				else if ( e1.getLinkedException() != null && e1.getLinkedException().getMessage() != null )
				{
					messageBox.setText( e1.getLinkedException().getClass().getSimpleName() );
					msg = e1.getLinkedException().getMessage();
				}
				messageBox.setMessage( "Error in Strategy \"" + Atdl4jHelper.getStrategyUiRepOrName( strategy ) + "\":\n\n" + msg );
				messageBox.open();

				// rollback changes
				strategyParent.dispose();

				// skip to next strategy
				continue;
			}

			// create dropdown item for strategy
			// 2/7/2010 Scott Atwell (this has been incorporated within
			// StrategySelectionPanel.loadStrategyList())
			// strategiesDropDown.add(getStrategyName(strategy));
			getAtdl4jConfig().getStrategyUIMap().put( strategy, ui );

			// TODO Scott Atwell 1/17/2010 Added BEGIN
			ui.setCxlReplaceMode( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );
			// TODO Scott Atwell 1/17/2010 Added END
		}

		// 2/7/2010 Scott Atwell (this has been incorporated within
		// StrategySelectionPanel.loadStrategyList()) if
		// (strategiesDropDown.getItem(0) != null) strategiesDropDown.select(0);

		// 2/7/2010 Scott Atwell added
		getStrategySelectionPanel().loadStrategyList( tempFilteredStrategyList );

		// TODO: This flashes all parameters on the screen when we first load
		// There's got to be a better way...
		shell.pack();
		for ( int i = 0; i < strategiesPanel.getChildren().length; i++ )
		{
			( (GridData) strategiesPanel.getChildren()[ i ].getLayoutData() ).heightHint = ( i != 0 ) ? 0 : -1;
			( (GridData) strategiesPanel.getChildren()[ i ].getLayoutData() ).widthHint = ( i != 0 ) ? 0 : -1;
		}
		strategiesPanel.layout();
		if ( getAtdl4jConfig().getStrategies() != null )
		{
			getAtdl4jConfig().setSelectedStrategy( getAtdl4jConfig().getStrategies().getStrategy().get( 0 ) );
		}
	}
**** ???? ****/	
	
	
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
}
