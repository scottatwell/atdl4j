package org.atdl4j.ui.impl;

import javax.xml.bind.JAXBException;

/* 
 * This class intentionally does not support parameterRef or initValue
 * which LabelT inherits from ControlT.
 * 
 * Use HiddenField if you'd like to have control associated with a parameter. 
 */

public abstract class LabelUI extends AbstractControlUI<String> {

/** 2/9/2010 Scott Atwell	@see AbstractControlUI.init(ControlT aControl, ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	public LabelUI(LabelT control) {
		this.control = control;
		this.parameter = null;
	}
**/
	// -- Overriden --
	protected void initPreCheck() throws JAXBException
	{
		this.parameter = null;
	}
	
	public void setValue(String value) {
		// do nothing
	}
	
	public String getControlValue() {
		return null; // Labels cannot store values
	}

	public Object getParameterValue() {
		return null; // Labels cannot store values
	}
}
