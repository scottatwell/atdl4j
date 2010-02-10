package org.atdl4j.ui.swt.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.layout.ControlT;
import org.atdl4j.atdl.layout.PanelOrientationT;
import org.atdl4j.atdl.layout.StrategyPanelT;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.ui.ControlUIFactory;
import org.atdl4j.ui.swt.SWTPanelFactory;
import org.atdl4j.ui.swt.SWTWidget;
import org.atdl4j.ui.swt.SWTWidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;

public class SWTFactory implements SWTWidgetFactory, SWTPanelFactory {
	private static final Logger logger = Logger.getLogger(SWTFactory.class);

// 2/9/2010 Scott Atwell	private SWTControlUIFactory controlWidgetFactory;
	private ControlUIFactory controlWidgetFactory;

// 2/9/2010 Scott Atwell	public SWTFactory() 
	public SWTFactory( Atdl4jConfig aAtdl4jConfig ) 
		throws JAXBException 
	{
// 2/9/2010 Scott Atwell		controlWidgetFactory = new SWTControlUIFactory();
		controlWidgetFactory = aAtdl4jConfig.getControlUIFactory( aAtdl4jConfig );
	}

	// Used to create a single parameter widget
	public SWTWidget<?> create(Composite parent, ControlT control, ParameterT parameter,
			int style) throws JAXBException {
		SWTWidget<?> parameterWidget = null;

		try {
//TODO 1/19/2010 Scott Atwell added for debug
			logger.debug( "create(Composite parent, ControlT control, ParameterT parameter, int style) invoked " +
					"with parms parent: " + parent + " control: " + control + " parameter: " + parameter + " style: " + style );
			
// 2/9/2010			parameterWidget = controlWidgetFactory.create(control, parameter);
			parameterWidget = (SWTWidget<?>) controlWidgetFactory.create(control, parameter);
			
//TODO 1/19/2010 Scott Atwell added for debug
			logger.debug( "controlWidgetFactory.create(control, parameter) returned parameterWidget: " + parameterWidget );
			
			parameterWidget.createWidget(parent, style);
//TODO 1/19/2010 Scott Atwell added for debug
			logger.debug( "create(Composite parent, ControlT control, ParameterT parameter, int style) completed.  parameterWidget: " + parameterWidget );

		} catch (JAXBException e) {
			throw e;
		}
		return parameterWidget;
	}

	// Given a panel, recursively populates a map of Panels and Parameter widgets
	// Can also process options for a group frame instead of a single panel
	public Map<String, SWTWidget<?>> create(Composite parent,
			StrategyPanelT panel, Map<String, ParameterT> parameters, int style) throws JAXBException {
//TODO 1/19/2010 Scott Atwell added for debug
		logger.debug( "create(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style)" +
				" invoked with parms parent: " + parent + " panel: " + panel + " parameters: " + parameters + " style: " + style );
		
		Map<String, SWTWidget<?>> controlWidgets = new HashMap<String, SWTWidget<?>>();

		Composite c;
		
		/*
		 * TODO: can't get SWT borders to work... help!

		int border = 0;
		if (panel.getBorder() == BorderT.LINE ||
		    panel.getBorder() == BorderT.LOWERED_BEVEL ||
	        panel.getBorder() == BorderT.LOWERED_ETCHED ||
		    panel.getBorder() == BorderT.RAISED_BEVEL ||
		    panel.getBorder() == BorderT.RAISED_ETCHED)
		{
			border = SWT.BORDER;
		} else if (panel.getBorder() == BorderT.NONE)
		{
			border = SWT.NONE;
		}
		*/

		if (panel.getTitle() == null || "".equals(panel.getTitle())) {
			// use simple composite
			c = new Composite(parent, style);

			// keep StrategyPanel in data object
			c.setData(panel);

			// create layout
			c.setLayout(createLayout(panel));
			c.setLayoutData(createLayoutData(c));

			// XXX tooltip debug
			// c.setToolTipText(panel.toString());

		} else {
			// use group
			c = new Group(parent, style);

			// keep StrategyPanel in data object
			c.setData(panel);

			((Group)c).setText(panel.getTitle());
			c.setLayout(createLayout(panel));
			c.setLayoutData(createLayoutData(c));
		}
		
		if (panel.getStrategyPanel().size() > 0 && panel.getControl().size() > 0)
			throw new JAXBException("StrategyPanel may not contain both StrategyPanel and Control elements.");
				
		// build panels widgets recursively
		for (StrategyPanelT p : panel.getStrategyPanel()) {
			
			Map<String, SWTWidget<?>> widgets = create(c, p, parameters, style);
			// check for duplicate IDs
			for (String newID : widgets.keySet())
			{			
				for (String existingID : controlWidgets.keySet())
				{
					if (newID.equals(existingID))
						throw new JAXBException("Duplicate Control ID: \"" + newID + "\"");
				}
			}			
			controlWidgets.putAll(widgets);
		}

		// build control widgets recursively
		for (ControlT control : panel.getControl()) {
			
			ParameterT parameter = null;
			
			if (control.getParameterRef() != null)
			{
				parameter = parameters.get(control.getParameterRef());
				if (parameter == null) throw new JAXBException("Cannot find Parameter \"" + control.getParameterRef()
						+ "\" for Control ID: \"" + control.getID() + "\"");
			}
			SWTWidget<?> widget = create(c, control, parameter, style);
			
			// check for duplicate Control IDs
			if (control.getID() != null) {
				// check for duplicate Control IDs
				for (SWTWidget<?> w : controlWidgets.values()) {
					if (w.getControl().getID().equals(control.getID()))
						throw new JAXBException("Duplicate Control ID: \""
								+ control.getID() + "\"");
				}
				controlWidgets.put(control.getID(), widget);
			} else {
				throw new JAXBException("Control Type: \"" + 
						control.getClass().getSimpleName() + "\" is missing ID");
			}
		}
		
		return controlWidgets;
	}

	private static Object createLayoutData(Composite c) {
		// if parent is a strategy panel
		if (c.getParent().getData() instanceof StrategyPanelT) {
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
			c.setLayoutData(layoutData);

			StrategyPanelT parentPanel = (StrategyPanelT) c.getParent().getData();
			// if parent orientation is vertical, make this panel span 2 columns
			if (parentPanel.getOrientation() == PanelOrientationT.VERTICAL) {
				layoutData.horizontalSpan = 2;
			}
			return layoutData;
		}
		return null;
	}

	private Layout createLayout(StrategyPanelT panel) {
		PanelOrientationT orientation = panel.getOrientation();
		if (orientation == PanelOrientationT.HORIZONTAL) {
			int parameterCount = panel.getControl() != null ?
					panel.getControl().size() : 0;
			int panelCount = panel.getStrategyPanel() != null ?
					panel.getStrategyPanel().size() : 0;
			
			// make 2 columns for each parameter and 1 column for each sub-panel
			GridLayout l = new GridLayout(parameterCount * 2 + panelCount,
					false);

			return l;
		} else if (orientation == PanelOrientationT.VERTICAL) {
			GridLayout l = new GridLayout(2, false);
			return l;
		}
		return null;
	}
}
