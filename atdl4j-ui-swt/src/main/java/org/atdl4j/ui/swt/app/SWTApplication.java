package org.atdl4j.ui.swt.app;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.InputAndFilterData;
import org.atdl4j.data.FIXMessageParser;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.StrategySelectionUI;
import org.atdl4j.ui.app.StrategySelectionUIListener;
import org.atdl4j.ui.swt.config.SWTAtdl4jConfig;
import org.atdl4j.ui.swt.test.DebugMouseTrackListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class SWTApplication 
	implements StrategySelectionUIListener
{
//TODO 2/23/2010 -- the vertical height used by strategyDescription remains "taken" even after setVisible(false) and layout()/pack() below !!!!!!			
	

//	private static final Logger logger = Logger.getLogger(SWTApplication.class);
	public final Logger logger = Logger.getLogger(SWTApplication.class);
	
//	private static Combo strategiesDropDown;
//	private static Composite strategiesPanel;
//	private static Shell shell;
///	private Combo strategiesDropDown;
	private StrategySelectionUI strategySelectionUI;  // 2/7/2010 Scott Atwell replaces strategiesDropDown

	private Composite strategiesPanel;
	private Shell shell;

//	private static StrategiesT strategies;
//	private static Map<StrategyT, StrategyUI> strategyUI;
//	private static StrategyT selectedStrategy;
	
	// 2/2/2010 John Shields
	// TODO move this to a config file
//	private static boolean showStrategyDescription = true;
//	private static boolean showTimezoneSelector = false;
	
//	private static Composite descPanel;
//	private static Text strategyDescription;
//	private static Text outputFixMessageText;
//	private static Text inputFixMessageText;
//	private static Button cxlReplaceModeButton;
	private Composite descPanel;
	private Text strategyDescription;
	private Text outputFixMessageText;
	private Text inputFixMessageText;
	private Button cxlReplaceModeButton;
// 1/20/2010 Scott Atwell	
//	private static Button debugModeButton;
	private Button debugModeButton;

// 1/17/2010 Scott Atwell Added 
//	private static InputAndFilterData inputAndFilterData = new InputAndFilterData();	

//TODO 2/7/2010 This one controls SWT vs. Swing	
	private Atdl4jConfig atdl4jConfig = new SWTAtdl4jConfig();
	
	public static void main(String[] args) {
		SWTApplication tempSWTApplication = new SWTApplication();
		try
		{
			tempSWTApplication.mainLine(args);
		}
		catch ( Throwable e )
		{
			tempSWTApplication.logger.warn("Fatal Exception in mainLine", e);
		}
	}
	
	public void mainLine(String[] args) 
	{
		Display display = new Display();
		shell = new Shell(display);
		GridLayout shellLayout = new GridLayout(1, true);
		shell.setLayout(shellLayout);
	
		// header
		Composite headerComposite = new Composite(shell, SWT.NONE);
		GridLayout headerLayout = new GridLayout(3, false);
		headerComposite.setLayout(headerLayout);
		headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		final Text filepathText = new Text(headerComposite, SWT.BORDER);
		GridData filepathTextData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		filepathTextData.horizontalSpan = 2;
		filepathText.setLayoutData(filepathTextData);
		Button browseButton = new Button(headerComposite, SWT.NONE);
		browseButton.setText("...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				String filepath = dialog.open();
				if (filepath != null) {
					filepathText.setText(filepath);
					try {
						parse(filepath);
					} catch (JAXBException e1) {
						logger.warn("parse() Exception", e1);
						MessageBox messageBox = new MessageBox(shell, SWT.OK
								| SWT.ICON_ERROR);
						// e1.getMessage() is null if there is a JAXB parse error 
						String msg = "";
						if (e1.getMessage() != null) msg = e1.getMessage();
						else if (e1.getLinkedException() != null && 
								e1.getLinkedException().getMessage() != null)
						{
							messageBox.setText(e1.getLinkedException().getClass().getSimpleName());
							msg = e1.getLinkedException().getMessage();
						}
						messageBox.setMessage(msg);
						messageBox.open();
//					} catch (IOException e1) {
//						logger.warn("parse() Exception", e1);
//						MessageBox messageBox = new MessageBox(shell, SWT.OK
//								| SWT.ICON_ERROR);
//						messageBox.setMessage(e1.getMessage());
//						messageBox.open();
//					} catch (NumberFormatException e1) {
//						logger.warn("parse() Exception", e1);
//						MessageBox messageBox = new MessageBox(shell, SWT.OK
//								| SWT.ICON_ERROR);
//						messageBox.setMessage("NumberFormatExeception: " + e1.getMessage());
//						messageBox.open();
					} catch ( Exception e1) {
						logger.warn("parse() Exception", e1);
						MessageBox messageBox = new MessageBox(shell, SWT.OK
								| SWT.ICON_ERROR);
						messageBox.setMessage("Parse/UI Build Exception: " + e1.getMessage());
						messageBox.open();
					}
				}
			}
		});

	if (getAtdl4jConfig().isShowTimezoneSelector())
	{

	    Label tzLabel = new Label(headerComposite, SWT.NONE);
	    tzLabel.setText("Timezone:");
	    // dropDownList
	    Combo tzDropDown = new Combo(headerComposite, SWT.READ_ONLY | SWT.BORDER);
	    GridData tzData = new GridData(SWT.FILL, SWT.CENTER, true, true);
	    tzData.horizontalSpan = 2;
	    tzDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		    false));
	}
		
/****	
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
		strategiesDropDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int index = strategiesDropDown.getSelectionIndex();
//TODO 1/16/2010 Scott Atwell moved to selectDropDownStrategy()		
//				for (int i = 0; i < strategiesPanel.getChildren().length; i++) {
//					((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != index) ? 0 : -1;
//					((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != index) ? 0 : -1;
//				}
//				strategiesPanel.layout();
//				shell.pack();
//				if (strategies != null) {
//					selectedStrategy = strategies.getStrategy().get(index);
//				}
				selectDropDownStrategy( index );
			}
		});
****/
// 2/7/2010 Scott Atwell		setStrategySelectionUI( new SWTStrategySelectionUI() );
		setStrategySelectionUI( getAtdl4jConfig().getStrategySelectionUI() );
		getStrategySelectionUI().addListener( this );
		getStrategySelectionUI().buildStrategySelectionPanel( shell, getAtdl4jConfig() );
		
		
	
		if (getAtdl4jConfig().isShowStrategyDescription())
		{
        		//descPanel = new Composite(shell, SWT.NONE);
        		strategyDescription = new Text(shell, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
        	   strategyDescription.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        		strategyDescription.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        		
        		
        		GridData descData = new GridData(SWT.FILL, SWT.FILL, true, false);
        		descData.grabExcessHorizontalSpace = true;
       		descData.heightHint = 40;
        		strategyDescription.setLayoutData(descData);
        		
        		
        		
        		// strategyDescription.
        		/*
        		FillLayout descLayout = new FillLayout(SWT.VERTICAL);
        		descPanel.setLayout(descLayout);
        		
        		//descData.heightHint = 100;
        		descPanel.setLayoutData(descData);
        		//strategyDescription.setLayoutData(descData);*/
		}
		
		// Main strategies panel
		strategiesPanel = new Composite(shell, SWT.NONE);
		GridLayout strategiesLayout = new GridLayout(1, false);
		strategiesLayout.verticalSpacing = 0;
		strategiesPanel.setLayout(strategiesLayout);
		strategiesPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		if ( getAtdl4jConfig().getInputAndFilterData() == null )
		{
			getAtdl4jConfig().setInputAndFilterData(  new InputAndFilterData() );
			getAtdl4jConfig().getInputAndFilterData().init();
		}
		
		// Load in a file if passed into the app arguments
		if (args.length > 0) {
			try {
//TODO 1/18/2010 Scott Atwell added BELOW
//				getAtdl4jConfig().getInputAndFilterData().init();
				
				if (args.length >= 2)
				{
					// -- InputCxlReplaceMode = args[1] (eg "true" or "false")
					logger.info("args[1]: " + args[1] + " Boolean.parseBoolean() as inputCxlReplaceMode");
					getAtdl4jConfig().getInputAndFilterData().setInputCxlReplaceMode( Boolean.parseBoolean( args[1] ) );
				}
				
				if ( args.length >= 3)
				{
					// -- InputHiddenFieldNameValueMap = args[2] (eg "FIX_OrderQty=10000|FIX_Side=1|FIX_OrdType=1") 
					String tempStringToParse = args[2];
					logger.info("args[2]: " + tempStringToParse + " parse as InputHiddenFieldNameValueMap (eg \"FIX_OrderQty=10000|FIX_Side=1|FIX_OrdType=1\")");
					String[] tempFieldAndValuesArray = tempStringToParse.split( "\\|" );
					if ( tempFieldAndValuesArray != null )
					{
						Map<String, String> tempInputHiddenFieldNameValueMap = new HashMap<String, String>();
						for (String tempFieldAndValue : tempFieldAndValuesArray )
						{
							String[] tempCombo = tempFieldAndValue.split( "=" );
							if ( ( tempCombo != null ) && ( tempCombo.length == 2 ) )
							{
								tempInputHiddenFieldNameValueMap.put( tempCombo[0], tempCombo[1] );
							}
						}
						
						logger.info("InputHiddenFieldNameValueMap: " + tempInputHiddenFieldNameValueMap);
						getAtdl4jConfig().getInputAndFilterData().addMapToInputHiddenFieldNameValueMap( tempInputHiddenFieldNameValueMap );
					}
				}
//TODO 1/18/2010 Scott Atwell added ABOVE

				
				parse(args[0]);
			} catch (JAXBException e1) {
				logger.warn( "parse() Exception", e1);
				MessageBox messageBox = new MessageBox(shell, SWT.OK
						| SWT.ICON_ERROR);
				messageBox.setMessage(e1.getMessage());
				messageBox.open();
// 2/7/2010			} catch (IOException e1) {
			} catch (Exception e1) {
				logger.warn( "parse() Exception", e1);
				MessageBox messageBox = new MessageBox(shell, SWT.OK
						| SWT.ICON_ERROR);
				messageBox.setMessage(e1.getMessage());
				messageBox.open();
			}
		}

		// footer
		Group footer = new Group(shell, SWT.NONE);
		footer.setText("Debug");
		footer.setLayout(new GridLayout(2, false));
		footer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// validator button
		Button validateButton = new Button(footer, SWT.NONE);
		validateButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		validateButton.setText("Validate Output");
		validateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validateStrategy();
			}
		});
		outputFixMessageText = new Text(footer, SWT.BORDER);
		outputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		// loader button
		Button loadMessageButton = new Button(footer, SWT.NONE);
		loadMessageButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		loadMessageButton.setText("Load Message");
		loadMessageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadFixMessage();
			}
		});
		inputFixMessageText = new Text(footer, SWT.BORDER);
		inputFixMessageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// blank button used as spacer
		Button blankButton = new Button(footer, SWT.NONE);
		blankButton.setVisible(false);
		cxlReplaceModeButton = new Button(footer, SWT.CHECK);		
		cxlReplaceModeButton.setText("Cxl Replace Mode");
		
//TODO 1/18/2010 Scott Atwell added
		if ( getAtdl4jConfig().getInputAndFilterData() != null )
		{
			cxlReplaceModeButton.setSelection( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );
		}
		
		cxlReplaceModeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getAtdl4jConfig().getStrategyUIMap() != null)
				{
					for (StrategyUI strategy : getAtdl4jConfig().getStrategyUIMap().values())
					{
// 2/7/2010 Scott Atwell						((SWTStrategyUI)strategy).setCxlReplaceMode(cxlReplaceModeButton.getSelection());
						strategy.setCxlReplaceMode(cxlReplaceModeButton.getSelection());
					}
				}
			}
		});
		
//TODO 1/20/2010 Scott Atwell BELOW	
		//blank button used as spacer
		Button blankButton2 = new Button(footer, SWT.NONE);
		blankButton2.setVisible(false);
		debugModeButton = new Button(footer, SWT.CHECK);		
		debugModeButton.setText("Debug Mode");

		debugModeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applyLoggingLevel();
			}
		});
//TODO 1/20/2010 Scott Atwell ABOVE


		shell.pack();
		shell.open();

//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		display.dispose();
// 2/3/2010 Scott Atwell added pop-up to display the Exception before the app crashes		
		while (!shell.isDisposed()) 
		{
			try
			{
				if (!display.readAndDispatch())
				{
					display.sleep();
				}	
			}
			catch (Exception e)
			{
				logger.warn( "Fatal Exception encountered", e );
				MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
				if ( e.getMessage() != null )
				{
					messageBox.setMessage(e.getMessage());
				}
				else
				{
					messageBox.setMessage( e.toString() );
				}
				messageBox.open();
			}
		}
		
		display.dispose();
	}

//	protected static void validateStrategy() {
	protected void validateStrategy() {
		if (getAtdl4jConfig().getSelectedStrategy() == null)
		{
			outputFixMessageText.setText("Please select a strategy");
			return;
		}
		logger.info("Validating strategy " + getAtdl4jConfig().getSelectedStrategy().getName());
		try {
			StrategyUI ui = getAtdl4jConfig().getStrategyUIMap().get(getAtdl4jConfig().getSelectedStrategy());
			ui.validate();
			outputFixMessageText.setText(ui.getFIXMessage());

		} catch (ValidationException ex) {
			outputFixMessageText.setText(ex.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage(ex.getMessage());
			messageBox.open();
		} catch (JAXBException ex) {
			outputFixMessageText.setText(ex.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage(ex.getMessage());
			messageBox.open();
		} catch (Exception ex) {
			outputFixMessageText.setText("");
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage("Generic exception occurred: "
					+ ex.toString());
			messageBox.open();
			ex.printStackTrace();
		}
	}
	
//	protected static void loadFixMessage() {
	protected void loadFixMessage() {
		//TODO 1/16/2010 Scott Atwell replaced BELOW	
		logger.info("Loading FIX string " + inputFixMessageText.getText());
		try {
			if ( ( getAtdl4jConfig().getInputAndFilterData() != null ) &&
				  ( getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName() != null ) )
			{
				logger.info("getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName(): " + getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName());			
				logger.info("Invoking selectDropDownStrategy: " + getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName() );							
				getStrategySelectionUI().selectDropDownStrategy( getAtdl4jConfig().getInputAndFilterData().getInputSelectStrategyName() );
			}
			else  // Match getWireValue() and then use getUiRep() if avail, otherwise getName()
			{
				if ( ( getAtdl4jConfig().getStrategies() != null ) && ( getAtdl4jConfig().getStrategies().getStrategyIdentifierTag() != null ) )
				{
					String strategyWireValue = FIXMessageParser.extractFieldValueFromFIXMessage( inputFixMessageText.getText(), getAtdl4jConfig().getStrategies().getStrategyIdentifierTag().intValue() );
					
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
										getStrategySelectionUI().selectDropDownStrategy( tempStrategy.getUiRep() );
									}
									else
									{
										logger.info("Invoking selectDropDownStrategy for tempStrategy.getName(): " + tempStrategy.getName() );							
										getStrategySelectionUI().selectDropDownStrategy( tempStrategy.getName() );
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
				outputFixMessageText.setText("Please select a strategy");
				return;
			}
// 1/16/2010 Scott Atwell		logger.info("Loading FIX string " + inputFixMessageText.getText());
// 1/16/2010 Scott Atwell		try {
			StrategyUI ui = getAtdl4jConfig().getStrategyUIMap().get(getAtdl4jConfig().getSelectedStrategy());
//TODO 1/19/2010 Scott Atwell BEFORE			ui.setFIXMessage(inputFixMessageText.getText());
//TODO 1/19/2010 Scott Atwell BEFORE			outputFixMessageText.setText("FIX string loaded successfully!");
//TODO 1/19/2010 Scott Atwell AFTER - BELOW
			// -- Note available getAtdl4jConfig().getStrategies() may be filtered due to SecurityTypes, Markets, or Region/Country rules --  
			if ( ui != null )
			{
				ui.setFIXMessage(inputFixMessageText.getText());
				outputFixMessageText.setText("FIX string loaded successfully!");
			}
			else
			{
				outputFixMessageText.setText( getAtdl4jConfig().getSelectedStrategy().getName() + " is not available.");
			}
//TODO 1/19/2010 Scott Atwell AFTER - ABOVE			
		} catch (ValidationException ex) {
			outputFixMessageText.setText(ex.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage(ex.getMessage());
			messageBox.open();
		} catch (JAXBException ex) {
			outputFixMessageText.setText(ex.getMessage());
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage(ex.getMessage());
			messageBox.open();
		} catch (Exception ex) {
			outputFixMessageText.setText("");
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setMessage("Generic exception occurred: "
					+ ex.toString());
			messageBox.open();
			ex.printStackTrace();
		}
	}
	
//	protected static void parse(String filepath) throws JAXBException,
	protected void parse(String filepath) throws JAXBException,
// 2/7/2010 Scott Atwell			IOException, NumberFormatException {
		IOException, NumberFormatException 
	{
		
		// remove all dropdown items
// 2/7/2010 Scott Atwell (this has been incorporated within StrategySelectionUI.loadStrategyList())		strategiesDropDown.removeAll();
		
		// remove all strategy panels
		for (Control control : strategiesPanel.getChildren()) control.dispose();
	
		// parses the XML document and build an object model
		JAXBContext jc = JAXBContext.newInstance(StrategiesT.class.getPackage().getName());
		Unmarshaller um = jc.createUnmarshaller();
		try {
			// try to parse as URL
			URL url = new URL(filepath);
			JAXBElement<?> element = (JAXBElement<?>)um.unmarshal(url);
			getAtdl4jConfig().setStrategies( (StrategiesT)element.getValue() );
		} catch (MalformedURLException e) {
			// try to parse as file
			File file = new File(filepath);		
			JAXBElement<?> element = (JAXBElement<?>)um.unmarshal(file);
			getAtdl4jConfig().setStrategies( (StrategiesT)element.getValue() );
		}

// -- 2/7/2010 Eliminate SWT-specific Reference --		
// 2/7/2010 this worked		StrategiesUIFactory factory = new SWTStrategiesUIFactory();
		StrategiesUIFactory factory = getAtdl4jConfig().getStrategiesUIFactory();
// 2/8/2010 Scott Atwell		StrategiesUI<?> strategiesUI = factory.create(getAtdl4jConfig().getStrategies());
		StrategiesUI<?> strategiesUI = factory.create(getAtdl4jConfig().getStrategies(), getAtdl4jConfig());
		getAtdl4jConfig().setStrategyUIMap( new HashMap<StrategyT, StrategyUI>() );
		
/***	2/7/2010 Scot Atwell 	
		for (StrategyT strategy : getAtdl4jConfig().getStrategies().getStrategy()) {

//TODO 1/18/2010 Scott Atwell Added BELOW
			if ( getAtdl4jConfig().getInputAndFilterData().isStrategySupported( strategy ) == false)
			{
				logger.info("Excluding strategy: " + strategy.getName() + " as inputAndFilterData.isStrategySupported() returned false." );
				continue; // skip it 
			}
//TODO 1/18/2010 Scott Atwell Added ABOVE
***/
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
				messageBox.setMessage("Error in Strategy \"" + getStrategyUiRepOrName(strategy) + "\":\n\n" +msg);
				messageBox.open();
				
				// rollback changes
				strategyParent.dispose();
				
				// skip to next strategy
				continue;
			}
			
			// create dropdown item for strategy
// 2/7/2010 Scott Atwell (this has been incorporated within StrategySelectionUI.loadStrategyList())			strategiesDropDown.add(getStrategyName(strategy));
			getAtdl4jConfig().getStrategyUIMap().put(strategy, ui);
			
//TODO Scott Atwell 1/17/2010 Added BEGIN
			ui.setCxlReplaceMode( getAtdl4jConfig().getInputAndFilterData().getInputCxlReplaceMode() );
//TODO Scott Atwell 1/17/2010 Added END
		}

// 2/7/2010 Scott Atwell (this has been incorporated within StrategySelectionUI.loadStrategyList())		if (strategiesDropDown.getItem(0) != null) strategiesDropDown.select(0);

// 2/7/2010 Scott Atwell added
		getStrategySelectionUI().loadStrategyList( tempFilteredStrategyList );
			
			
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
		shell.pack();
	}

//	private static String getStrategyName(StrategyT strategy) {
	private String getStrategyUiRepOrName(StrategyT strategy) {
		if (strategy.getUiRep() != null) {
			return strategy.getUiRep();
		} else {
			return strategy.getName();
		}
	}

//	public static void addDebugMouseTrackListener(Control control) {
	public void addDebugMouseTrackListener(Control control) {
		if (!(control.getClass().equals(Composite.class) || 
		      control.getClass().equals(Group.class))) {
			control.addMouseTrackListener(new DebugMouseTrackListener(control));
		}
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			for (Control child : composite.getChildren()) {
				addDebugMouseTrackListener(child);
			}
		}
	}

	//TODO 1/20/2010 Scott Atwell added	
//	private static void applyLoggingLevel()
	private void applyLoggingLevel()
	{
		org.apache.log4j.Level tempLevel = org.apache.log4j.Level.INFO; 
		if ( debugModeButton.getSelection() )
		{
			tempLevel = org.apache.log4j.Level.DEBUG;
//			tempLevel = org.apache.log4j.Level.TRACE;
		}
		
/***		
		org.apache.log4j.Logger.getLogger( SWTApplication.class ).setLevel( tempLevel );
		
		org.apache.log4j.Logger.getLogger( AbstractOperatorValidationRule.class ).setLevel( tempLevel );
		org.apache.log4j.Logger.getLogger( InputAndFilterData.class ).setLevel( tempLevel );
		org.apache.log4j.Logger.getLogger( SWTFactory.class ).setLevel( tempLevel );
		org.apache.log4j.Logger.getLogger( AbstractStrategyUI.class ).setLevel( tempLevel );
		org.apache.log4j.Logger.getLogger( RadioButtonListWidget.class ).setLevel( tempLevel );
***/
		org.apache.log4j.Logger.getLogger( "org.atdl4j" ).setLevel( tempLevel );

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

	/**
	 * @return the strategySelectionUI
	 */
	public StrategySelectionUI getStrategySelectionUI()
	{
		return this.strategySelectionUI;
	}

	/**
	 * @param aStrategySelectionUI the strategySelectionUI to set
	 */
	public void setStrategySelectionUI(StrategySelectionUI aStrategySelectionUI)
	{
		this.strategySelectionUI = aStrategySelectionUI;
	}


	/**
	 * Invoked by StrategySelectionUI via StrategySelectionUIListener when a Strategy has been selected
	 */
	public void strategySelected(StrategyT aStrategy, int index)
	{
//TODO -- These were the remnants from selectDropDownStrategy(int index) that did not become part of StrategySelectionUI
		for (int i = 0; i < strategiesPanel.getChildren().length; i++) 
		{
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).heightHint = (i != index) ? 0 : -1;
			((GridData)strategiesPanel.getChildren()[i].getLayoutData()).widthHint = (i != index) ? 0 : -1;
		}
		

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
		

		strategiesPanel.layout();
// 2/23/2010 Scott Atwell added shell.Layout()		
		shell.layout();
		shell.pack();
		
	}

	
}
