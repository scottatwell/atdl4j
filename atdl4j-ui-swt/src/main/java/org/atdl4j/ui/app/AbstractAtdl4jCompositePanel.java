package org.atdl4j.ui.app;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.FIXMessageParser;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategyUI;

/**
 * Represents the base, non-GUI system-specific CORE strategy selection and display GUI component.
 * 
 * @see org.atdl4j.ui.app.Atdl4jCompositePanel for AbstractAtdl4jTesterApp->AbstractAtdl4jTesterPanel->AbstractAtdl4jCompositePanel layering structure. *
 *
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public abstract class AbstractAtdl4jCompositePanel
	implements Atdl4jCompositePanel, 
	FixatdlFileSelectionPanelListener,
	StrategySelectionPanelListener
{
	public final Logger logger = Logger.getLogger(AbstractAtdl4jCompositePanel.class);

	Atdl4jConfig atdl4jConfig;
	Object parentOrShell;  // SWT: Shell, Swing: JFrame, etc

	private String lastFixatdlFilename;
	
	private FixatdlFileSelectionPanel fixatdlFileSelectionPanel;
	private StrategySelectionPanel strategySelectionPanel;
	private StrategyDescriptionPanel strategyDescriptionPanel;
	private StrategiesPanel strategiesPanel;

	abstract protected Object createValidateOutputSection();
	abstract protected void setValidateOutputText(String aText);
	abstract protected void packLayout();


	protected void init( Object aParentOrShell, Atdl4jConfig aAtdl4jConfig )
	{
		setAtdl4jConfig( aAtdl4jConfig );
		setParentOrShell( aParentOrShell );
		
		// -- Init InputAndFilterData if null --
		if ( getAtdl4jConfig().getInputAndFilterData() == null )
		{
			getAtdl4jConfig().setInputAndFilterData(  new InputAndFilterData() );
			getAtdl4jConfig().getInputAndFilterData().init();
		}

		// -- Init the Atdl4jUserMessageHandler --
		if ( ( getAtdl4jConfig() != null ) && 
			  ( getAtdl4jConfig().getAtdl4jUserMessageHandler() != null ) && 
			  ( getAtdl4jConfig().getAtdl4jUserMessageHandler().isInitReqd() ) )
		{
			getAtdl4jConfig().initAtdl4jUserMessageHandler( aParentOrShell );
		}

	
		// ----- Setup internal components (the GUI-specific versions will be instantiated, and add listeners, but defer to concrete classes: "build____Panel()" ----
		
		// -- FixatdlFileSelectionPanel (filename / file dialog) - build() method called via concrete class --
		setFixatdlFileSelectionPanel( getAtdl4jConfig().getFixatdlFileSelectionPanel() );
		getFixatdlFileSelectionPanel().addListener( this );

		// -- StrategySelectionPanel (drop down with list of strategies to choose from) - build() method called via concrete class --
		setStrategySelectionPanel( getAtdl4jConfig().getStrategySelectionPanel() );
		getStrategySelectionPanel().addListener( this );

		// -- StrategyDescriptionPanel (drop down with list of strategies to choose from) - build() method called via concrete class --
		setStrategyDescriptionPanel( getAtdl4jConfig().getStrategyDescriptionPanel() );

		// -- StrategiesPanel (GUI display of each strategy's parameters) - build() method called via concrete class --
		setStrategiesPanel( getAtdl4jConfig().getStrategiesPanel() );
// ????????????TODO TODO
///	getStrategiesPanel().addListener( this );
		
		
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
	 * @param strategySelectionPanel the strategySelectionPanel to set
	 */
	protected void setStrategySelectionPanel(StrategySelectionPanel strategySelectionPanel)
	{
		this.strategySelectionPanel = strategySelectionPanel;
	}

	/**
	 * @return the strategySelectionPanel
	 */
	public StrategySelectionPanel getStrategySelectionPanel()
	{
		return strategySelectionPanel;
	}

	/**
	 * @param strategyDescriptionPanel the strategyDescriptionPanel to set
	 */
	protected void setStrategyDescriptionPanel(StrategyDescriptionPanel strategyDescriptionPanel)
	{
		this.strategyDescriptionPanel = strategyDescriptionPanel;
	}

	/**
	 * @return the strategyDescriptionPanel
	 */
	public StrategyDescriptionPanel getStrategyDescriptionPanel()
	{
		return strategyDescriptionPanel;
	}

	/**
	 * @param fixatdlFileSelectionPanel the fixatdlFileSelectionPanel to set
	 */
	protected void setFixatdlFileSelectionPanel(FixatdlFileSelectionPanel fixatdlFileSelectionPanel)
	{
		this.fixatdlFileSelectionPanel = fixatdlFileSelectionPanel;
	}

	/**
	 * @return the fixatdlFileSelectionPanel
	 */
	public FixatdlFileSelectionPanel getFixatdlFileSelectionPanel()
	{
		return fixatdlFileSelectionPanel;
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.StrategySelectionPanelListener#strategySelected(org.atdl4j.fixatdl.core.StrategyT, int)
	 */
	@Override
	public void strategySelected(StrategyT aStrategy, int aIndex)
	{
		getAtdl4jConfig().setSelectedStrategyValidated( false );
		setValidateOutputText( "" );

		getStrategyDescriptionPanel().loadStrategyDescription( aStrategy );
		getStrategiesPanel().adjustLayoutForSelectedStrategy( aIndex );
/**		
//TODO -- These were the remnants from selectDropDownStrategy(int index) that did not become part of StrategySelectionPanel
		for (int i = 0; i < strategiesPanel.getChildren().length; i++) 
		{
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != index) ? 0 : -1;
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != index) ? 0 : -1;
		}
***/		

		
// TODO 2/28/2010 ????		
/***
		if (getAtdl4jConfig().isShowStrategyDescription())
		{
			strategyDescription.setText("");
		}
		
// 2/23/2010 Scott Atwell moved after StrategyDescription stuff		strategiesPanel.layout();
// 2/23/2010 Scott Atwell moved after StrategyDescription stuff		shell.pack();
//Strategy description must be updated after packing
// 2/7/2010 Scott Atwell - had to add the not null check	to avoid SWT.error of Argument cannot be null	
		if ( (getAtdl4jConfig().isShowStrategyDescription()) && 
			  ( getAtdl4jConfig().getSelectedStrategy().getDescription() != null ) )
		{
			strategyDescription.setText(getAtdl4jConfig().getSelectedStrategy().getDescription());
			strategyDescription.setVisible( true );
		}
		else if ( strategyDescription != null )
		{
//TODO 2/23/2010 -- the vertical height used by strategyDescription remains "taken" even after setVisible(false) and layout()/pack() below !!!!!!			
			strategyDescription.setVisible( false );
		}
***/
		
/***
		strategiesPanel.layout();
// 2/23/2010 Scott Atwell added shell.Layout()		
		shell.layout();
		shell.pack();
***/
		packLayout();
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.app.FixatdlFileSelectionPanelListener#fixatdlFileSelected(java.lang.String)
	 */
	@Override
	public void fixatdlFileSelected(String aFilename)
	{
		try
		{
			parseFixatdlFile( aFilename );
		}
		catch (Exception e)
		{
			logger.warn( "parseFixatdlFile exception", e );
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "FIXatdl File Parse Exception", "", e );
		}
	}

	/**
	 * @param strategiesPanel the strategiesPanel to set
	 */
	protected void setStrategiesPanel(StrategiesPanel strategiesPanel)
	{
		this.strategiesPanel = strategiesPanel;
	}

	/**
	 * @return the strategiesPanel
	 */
	public StrategiesPanel getStrategiesPanel()
	{
		return strategiesPanel;
	}
	
	/* 
	 * @return StrategyT (non-null only if passes all validation)
	 */
	public StrategyT validateStrategy() 
	{
		StrategyT tempSelectedStrategy = getAtdl4jConfig().getSelectedStrategy();
		
		if (tempSelectedStrategy == null)
		{
			setValidateOutputText("Please select a strategy");
			return null;
		}
		
		getAtdl4jConfig().setSelectedStrategyValidated( false );
		
		logger.info("Validating strategy " + tempSelectedStrategy.getName());
		
		try 
		{
			StrategyUI ui = getAtdl4jConfig().getStrategyUIMap().get(tempSelectedStrategy);
			ui.validate();
			String tempUiFixMsg = ui.getFIXMessage();
			setValidateOutputText( tempUiFixMsg );
			getAtdl4jConfig().setSelectedStrategyValidated( true );
			logger.info("Successfully Validated strategy " + tempSelectedStrategy.getName() + " FIXMessage: " + tempUiFixMsg );
			return tempSelectedStrategy;
		} 
		catch (ValidationException ex) 
		{
			setValidateOutputText( AbstractAtdl4jUserMessageHandler.extractExceptionMessage( ex ));
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Validation Exception", "", ex );
			logger.info( "Validation Exception:", ex );
			return null;
		} 
		catch (JAXBException ex) 
		{
			setValidateOutputText( AbstractAtdl4jUserMessageHandler.extractExceptionMessage( ex ));
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "XML Parse Exception", "", ex );
			logger.warn( "XML Parse Exception:", ex );
			return null;
		} 
		catch (Exception ex) 
		{
			setValidateOutputText( "" );
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Exception", "", ex );
			logger.warn( "Generic Exception", ex );
			return null;
		}
	}
	
	public void parseFixatdlFile( String aFilename ) 
		throws JAXBException,
				 IOException, 
				 NumberFormatException 
	{
		setLastFixatdlFilename( null );
		// remove all dropdown items
// 2/7/2010 Scott Atwell (this has been incorporated within StrategySelectionPanel.loadStrategyList())		strategiesDropDown.removeAll();
		
		// remove all strategy panels
// 2/26/2010		for (Control control : strategiesPanel.getChildren()) control.dispose();
		getStrategiesPanel().removeAllStrategyPanels();
		
		
		// parses the XML document and build an object model
		JAXBContext jc = JAXBContext.newInstance(StrategiesT.class.getPackage().getName());
		Unmarshaller um = jc.createUnmarshaller();

		try 
		{
			// try to parse as URL
			URL url = new URL( aFilename );
			JAXBElement<?> element = (JAXBElement<?>)um.unmarshal(url);
			getAtdl4jConfig().setStrategies( (StrategiesT)element.getValue() );
		} 
		catch (MalformedURLException e) 
		{
			// try to parse as file
			File file = new File( aFilename );		
			JAXBElement<?> element = (JAXBElement<?>)um.unmarshal(file);
			getAtdl4jConfig().setStrategies( (StrategiesT)element.getValue() );
		}

		List<StrategyT> tempFilteredStrategyList = getAtdl4jConfig().getStrategiesFilteredStrategyList();
		getStrategiesPanel().createStrategyPanels( tempFilteredStrategyList );
		getStrategySelectionPanel().loadStrategyList( tempFilteredStrategyList );
		
		if ( ( getAtdl4jConfig().getInputAndFilterData() != null ) && 
			  ( getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName() != null ) )
		{
			getStrategySelectionPanel().selectDropDownStrategy( getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName() );
		}
		else
		{
			getStrategySelectionPanel().selectFirstDropDownStrategy();
		}
/**** [moved to SWTStrategiesPanel] *****
// -- 2/7/2010 Eliminate SWT-specific Reference --		
// 2/7/2010 this worked		StrategiesUIFactory factory = new SWTStrategiesUIFactory();
		StrategiesUIFactory factory = getAtdl4jConfig().getStrategiesUIFactory();
// 2/8/2010 Scott Atwell		StrategiesUI<?> strategiesUI = factory.create(getAtdl4jConfig().getStrategies());
		StrategiesUI<?> strategiesUI = factory.create(getAtdl4jConfig().getStrategies(), getAtdl4jConfig());
		getAtdl4jConfig().setStrategyUIMap( new HashMap<StrategyT, StrategyUI>() );
		
		List<StrategyT> tempFilteredStrategyList = getAtdl4jConfig().getStrategiesFilteredStrategyList();
		
		for (StrategyT strategy : tempFilteredStrategyList) 
		{
			// create composite
			Composite strategyParent = new Composite(strategiesPanel, SWT.NONE);
			strategyParent.setLayout(new FillLayout());
// 2/7/2010 Scott Atwell			SWTStrategyUI ui;
			StrategyUI ui;

			
			// build strategy and catch strategy-specific errors
			try {
//TODO 1/17/2010 Scott Atwell				ui = strategiesUI.createUI(strategy, strategyParent);	
// 2/8/2010 Scott Atwell StrategiesUI now already has Atdl4jConfig				ui = strategiesUI.createUI(strategy, strategyParent, getAtdl4jConfig().getInputAndFilterData().getInputHiddenFieldNameValueMap());	
				ui = strategiesUI.createUI(strategy, strategyParent);	
			} catch (JAXBException e1) {
				MessageBox messageBox = new MessageBox(shell, SWT.OK
						| SWT.ICON_ERROR);
				// e1.getMessage() is null if there is a JAXB parse error 
				String msg = "";
				if (e1.getMessage() != null)
				{
					messageBox.setText("Strategy Load Error");
					msg = e1.getMessage();
				}
				else if (e1.getLinkedException() != null && 
					     e1.getLinkedException().getMessage() != null)
				{
					messageBox.setText(e1.getLinkedException().getClass().getSimpleName());
					msg = e1.getLinkedException().getMessage();
				}
				messageBox.setMessage("Error in Strategy \"" + Atdl4jHelper.getStrategyUiRepOrName(strategy) + "\":\n\n" +msg);
				messageBox.open();
				
				// rollback changes
				strategyParent.dispose();
				
				// skip to next strategy
				continue;
			}
			
			// create dropdown item for strategy
// 2/7/2010 Scott Atwell (this has been incorporated within StrategySelectionPanel.loadStrategyList())			strategiesDropDown.add(getStrategyName(strategy));
			getAtdl4jConfig().getStrategyUIMap().put(strategy, ui);
			
//TODO Scott Atwell 1/17/2010 Added BEGIN
			ui.setCxlReplaceMode( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );
//TODO Scott Atwell 1/17/2010 Added END
		}

// 2/7/2010 Scott Atwell (this has been incorporated within StrategySelectionPanel.loadStrategyList())		if (strategiesDropDown.getItem(0) != null) strategiesDropDown.select(0);

// 2/7/2010 Scott Atwell added
		getStrategySelectionPanel().loadStrategyList( tempFilteredStrategyList );
			
			
		// TODO: This flashes all parameters on the screen when we first load
		// There's got to be a better way...
		shell.pack();
		for (int i = 0; i < strategiesPanel.getChildren().length; i++) {
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != 0) ? 0 : -1;
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != 0) ? 0 : -1;
		}
		strategiesPanel.layout();
		if (getAtdl4jConfig().getStrategies() != null) {
			getAtdl4jConfig().setSelectedStrategy( getAtdl4jConfig().getStrategies().getStrategy().get(0) );
		}
**** [moved to SWTStrategiesPanel] *****/

//		shell.pack();
		packLayout();
		
		setLastFixatdlFilename( aFilename );
	}

	public boolean loadFixMessage( String aFixMessage ) 
	{
//		logger.info("Loading FIX string " + inputFixMessageText.getText());
		logger.info("Loading FIX string " + aFixMessage);
		try 
		{
			if ( ( getAtdl4jConfig().getInputAndFilterData() != null ) &&
				  ( getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName() != null ) )
			{
				logger.info("getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName(): " + getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName());			
				logger.info("Invoking selectDropDownStrategy: " + getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName() );							
				getStrategySelectionPanel().selectDropDownStrategy( getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName() );
			}
			else  // Match getWireValue() and then use getUiRep() if avail, otherwise getName()
			{
				if ( ( getAtdl4jConfig().getStrategies() != null ) && ( getAtdl4jConfig().getStrategies().getStrategyIdentifierTag() != null ) )
				{
					String strategyWireValue = FIXMessageParser.extractFieldValueFromFIXMessage( aFixMessage, getAtdl4jConfig().getStrategies().getStrategyIdentifierTag().intValue() );
					
					logger.info("strategyWireValue: " + strategyWireValue);			
					if ( strategyWireValue != null )
					{
						if ( getAtdl4jConfig().getStrategies().getStrategy() != null )
						{
							for ( StrategyT tempStrategy : getAtdl4jConfig().getStrategies().getStrategy() )
							{
								if ( strategyWireValue.equals( tempStrategy.getWireValue() ) )
								{
									if ( tempStrategy.getUiRep() != null )
									{
										logger.info("Invoking selectDropDownStrategy for tempStrategy.getUiRep(): " + tempStrategy.getUiRep() );							
										getStrategySelectionPanel().selectDropDownStrategy( tempStrategy.getUiRep() );
									}
									else
									{
										logger.info("Invoking selectDropDownStrategy for tempStrategy.getName(): " + tempStrategy.getName() );							
										getStrategySelectionPanel().selectDropDownStrategy( tempStrategy.getName() );
									}
									break;
								}
							}
						}
					}
				}
			}

			if (getAtdl4jConfig().getSelectedStrategy() == null)
			{
				setValidateOutputText("Please select a strategy");
				return false;
			}
			
			StrategyUI ui = getAtdl4jConfig().getStrategyUIMap().get(getAtdl4jConfig().getSelectedStrategy());
			// -- Note available getAtdl4jConfig().getStrategies() may be filtered due to SecurityTypes, Markets, or Region/Country rules --  
			if ( ui != null )
			{
				ui.setFIXMessage(aFixMessage);
				setValidateOutputText("FIX string loaded successfully!");
				return true;
			}
			else
			{
				setValidateOutputText( getAtdl4jConfig().getSelectedStrategy().getName() + " is not available.");
				return false;
			}
		} 
		catch (ValidationException ex) 
		{
			setValidateOutputText( AbstractAtdl4jUserMessageHandler.extractExceptionMessage( ex ));
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Validation Exception", "", ex );
			logger.info( "Validation Exception:", ex );
			return false;
		} 
		catch (JAXBException ex) 
		{
			setValidateOutputText( AbstractAtdl4jUserMessageHandler.extractExceptionMessage( ex ));
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "XML Parse Exception", "", ex );
			logger.warn( "XML Parse Exception:", ex );
			return false;
		} 
		catch (Exception ex) 
		{
			setValidateOutputText( "" );
			getAtdl4jConfig().getAtdl4jUserMessageHandler().displayException( "Exception", "", ex );
			logger.warn( "Generic Exception", ex );
			return false;
		}

	}
	/**
	 * @param lastFixatdlFilename the lastFixatdlFilename to set
	 */
	protected void setLastFixatdlFilename(String lastFixatdlFilename)
	{
		this.lastFixatdlFilename = lastFixatdlFilename;
	}
	
	/**
	 * @return the lastFixatdlFilename
	 */
	public String getLastFixatdlFilename()
	{
		return lastFixatdlFilename;
	}
	
	/* 
	 * Invokes fixatdlFileSelected() with getLastFixatdlFilename() if non-null.
	 */
	public void reloadFixatdlFile() 
	{
		if ( getLastFixatdlFilename() != null )
		{
			fixatdlFileSelected( getLastFixatdlFilename() );
		}
	}
	
}
