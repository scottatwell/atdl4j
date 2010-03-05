package org.atdl4j.ui.swt.app;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.ui.app.AbstractAtdl4jCompositePanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Represents the SWT-specific strategy selection and display GUI component.
 * 
 * Creation date: (Feb 28, 2010 6:26:02 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 28, 2010
 */
public class SWTAtdl4jCompositePanel
		extends AbstractAtdl4jCompositePanel
{
	public final Logger logger = Logger.getLogger(SWTAtdl4jCompositePanel.class);
	private Composite parentComposite;
	
	private Composite validateOutputSection;
	private Text outputFixMessageText;
	
	
	public Object buildAtdl4jCompositePanel(Object aParentOrShell, Atdl4jConfig aAtdl4jConfig)
	{
		return buildAtdl4jCompositePanel( (Composite) aParentOrShell, aAtdl4jConfig );
	}
	
	public Composite buildAtdl4jCompositePanel(Composite aParentComposite, Atdl4jConfig aAtdl4jConfig)
	{
		setParentComposite( aParentComposite );
		
// SWT doesn't use this, if we use it, then consumes vertical height at top		
//		Composite tempComposite = new Composite(aParentComposite, SWT.NONE);
//		GridLayout tempLayout = new GridLayout(1, false);
//		tempComposite.setLayout(tempLayout);
//		tempComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// -- Delegate back to AbstractAtdl4jCompositePanel -- 
		init( aParentComposite, aAtdl4jConfig );
		
		// -- Build the SWT.Composite from FixatdlFileSelectionPanel (filename / file dialog) --
		getFixatdlFileSelectionPanel().buildFixatdlFileSelectionPanel( getParentOrShell(), getAtdl4jConfig() );

		// -- Build the SWT.Composite from StrategySelectionPanel (drop down with list of strategies to choose from) --
		getStrategySelectionPanel().buildStrategySelectionPanel( getParentOrShell(), getAtdl4jConfig() );

		// -- Build the SWT.Composite from StrategyDescriptionPanel (text box with description for selected strategy) --
		getStrategyDescriptionPanel().buildStrategyDescriptionPanel( getParentOrShell(), getAtdl4jConfig() );
		getStrategyDescriptionPanel().setVisible( false );  // hide until there is data to populate it with
		
		// -- Build the SWT.Composite from StrategiesPanel (GUI display of each strategy's parameters) --
		getStrategiesPanel().buildStrategiesPanel( getParentOrShell(), getAtdl4jConfig() );

		// -- Build the SWT.Composite containing "Validate Output" button and outputFixMessageText --
		createValidateOutputSection();
		
		
//		return tempComposite;
		return aParentComposite;
	}

	protected Composite createValidateOutputSection()
	{
		validateOutputSection = new Group(getShell(), SWT.NONE);
		((Group) validateOutputSection).setText("Validation");
		validateOutputSection.setLayout(new GridLayout(2, false));
		validateOutputSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// validate button
		Button validateButton = new Button(validateOutputSection, SWT.NONE);
		validateButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		validateButton.setText("Validate Output");
		validateButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				validateStrategy();
			}
		});
		
		outputFixMessageText = new Text(validateOutputSection, SWT.BORDER);
		outputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		return validateOutputSection;
	}
	
	protected void setValidateOutputText(String aText)
	{
		outputFixMessageText.setText( aText );
	}

	protected void packLayout()
	{
		getShell().layout();
		getShell().pack();
	}
	
	/**
	 * Returns getParentComposite().getShell().
	 * @return the shell
	 */
	private Shell getShell()
	{
		if ( getParentComposite() != null )
		{
			return getParentComposite().getShell();
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return the parentComposite
	 */
	private Composite getParentComposite()
	{
		return this.parentComposite;
	}

	/**
	 * @param aParentComposite the parentComposite to set
	 */
	private void setParentComposite(Composite aParentComposite)
	{
		this.parentComposite = aParentComposite;
	}


}
