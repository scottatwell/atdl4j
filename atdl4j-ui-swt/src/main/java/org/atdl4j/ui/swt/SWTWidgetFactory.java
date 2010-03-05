package org.atdl4j.ui.swt;

import javax.xml.bind.JAXBException;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.eclipse.swt.widgets.Composite;

public interface SWTWidgetFactory {

// 3/5/2010 Scott Atwell	public SWTWidget<?> create(Composite parent, ControlT control, ParameterT parameter, int style) throws JAXBException;
	public SWTWidget<?> createWidget(Composite parent, ControlT control, ParameterT parameter, int style) throws JAXBException;
}