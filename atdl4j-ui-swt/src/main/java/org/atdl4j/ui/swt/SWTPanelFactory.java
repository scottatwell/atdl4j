package org.atdl4j.ui.swt;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.eclipse.swt.widgets.Composite;

public interface SWTPanelFactory {

//	3/5/2010 Scott Atwell renamed
//	public Map<String, SWTWidget<?>> create(Composite parent,
//			StrategyPanelT panel, Map<String, ParameterT> parameters, int style) throws JAXBException;

	public Map<String, SWTWidget<?>> createStrategyPanelAndWidgets(Composite parent,
			StrategyPanelT panel, Map<String, ParameterT> parameters, int style) throws JAXBException;

}