package org.atdl4j.ui.app;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategyT;

/**
 * Represents the core strategy selection and display GUI component.
 * 
 * 
 * 	[as of March 4, 2010 @author Scott Atwell] "SWTApplication" refactoring - rename, overhaul, and layered
 * 
 *		Interfaces:
 * 	-----------
 *		- org.atdl4j.ui.app.Atdl4jTesterPanel
 *		  - org.atdl4j.ui.app.Atdl4jInputAndFilterDataSelectionPanel
 *		    - org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel
 *		      - org.atdl4j.ui.app.FixMsgLoadPanel
 *		  - org.atdl4j.ui.app.Atdl4jCompositePanel
 *		    - org.atdl4j.ui.app.FixatdlFileSelectionPanel
 *		    - org.atdl4j.ui.app.StrategySelectionPanel
 *		    - org.atdl4j.ui.app.StrategyDescriptionPanel
 *		    - org.atdl4j.ui.app.StrategiesPanel
 *		
 *		Non-UI-specific abstract base implementation:
 *		---------------------------------------------
 *		org.atdl4j.ui.app.AbstractAtdl4jTesterApp
 *		- org.atdl4j.ui.app.AbstractAtdl4jTesterPanel
 *		  - org.atdl4j.ui.app.AbstractAtdl4jInputAndFilterDataSelectionPanel
 *		    - org.atdl4j.ui.app.AbstractAtdl4jInputAndFilterDataPanel
 *		      - org.atdl4j.ui.app.AbstractFixMsgLoadPanel
 *		  - org.atdl4j.ui.app.AbstractAtdl4jCompositePanel
 *		    - org.atdl4j.ui.app.AbstractFixatdlFileSelectionPanel
 *		    - org.atdl4j.ui.app.AbstractStrategySelectionPanel
 *		    - org.atdl4j.ui.app.AbstractStrategyDescriptionPanel
 *		    - org.atdl4j.ui.app.AbstractStrategiesPanel
 *		
 *		SWT-specific implementation:
 *		----------------------------  
 *		org.atdl4j.ui.swt.app.SWTAtdl4jTesterApp
 *		- org.atdl4j.ui.swt.app.SWTAtdl4jTesterPanel
 *		  - org.atdl4j.ui.swt.app.SWTAtdl4jInputAndFilterDataSelectionPanel
 *		    - org.atdl4j.ui.swt.app.SWTAtdl4jInputAndFilterDataPanel
 *		      - org.atdl4j.ui.swt.app.SWTFixMsgLoadPanel
 *		  - org.atdl4j.ui.swt.app.SWTAtdl4jCompositePanel
 *		    - org.atdl4j.ui.swt.app.SWTFixatdlFileSelectionPanel
 *		    - org.atdl4j.ui.swt.app.SWTStrategySelectionPanel
 *		    - org.atdl4j.ui.swt.app.SWTStrategyDescriptionPanel
 *		    - org.atdl4j.ui.swt.app.SWTStrategiesPanel
 *		
 *		Listeners:
 *		----------
 *		- org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanelListener
 *		- org.atdl4j.ui.app.FixMsgLoadPanelListener
 *		- org.atdl4j.ui.app.FixatdlFileSelectionPanelListener
 *		- org.atdl4j.ui.app.StrategySelectionPanelListener
 *		- org.atdl4j.ui.app.StrategiesPanelListener
 *		
 *		Pop-up message handler:
 *		-----------------------
 *		- org.atdl4j.ui.app.Atdl4jUserMessageHandler
 *		- org.atdl4j.ui.app.AbstractAtdl4jUserMessageHandler
 *		- org.atdl4j.ui.swt.app.SWTAtdl4jUserMessageHandler
 *		
 *		Config:
 *		-------
 *		- org.atdl4j.config.Atdl4jConfig
 *		- org.atdl4j.config.AbstractAtdl4jConfig
 *		- org.atdl4j.ui.swt.config.SWTAtdl4jConfig
 *		- org.atdl4j.config.InputAndFilterData 
 *
 *
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public interface Atdl4jCompositePanel
{
	public Object buildAtdl4jCompositePanel(Object aParentOrShell, Atdl4jConfig aAtdl4jConfig);

	public Atdl4jConfig getAtdl4jConfig();
	
	public void parseFixatdlFile( String aFilename ) 
		throws JAXBException, IOException, NumberFormatException; 

	public boolean loadFixMessage( String aFixMessage );

	/* 
	 * @return StrategyT (non-null only if passes all validation)
	 */
	public StrategyT validateStrategy();
	
	public void reloadFixatdlFile();
	
	public void setVisibleValidateOutputSection( boolean aVisible );
	
	public void setVisibleOkCancelButtonSection( boolean aVisible );
	
	public void addListener(Atdl4jCompositePanelListener aAtdl4jCompositePanelListener);
	
	public void removeListener(Atdl4jCompositePanelListener aAtdl4jCompositePanelListener);
}
