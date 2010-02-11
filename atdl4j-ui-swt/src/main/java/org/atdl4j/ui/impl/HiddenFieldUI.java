package org.atdl4j.ui.impl;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.layout.HiddenFieldT;
import org.atdl4j.data.ParameterHelper;

public abstract class HiddenFieldUI
		extends AbstractControlUI<String>
{

	protected String value;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	 * public HiddenFieldUI(HiddenFieldT control, ParameterT parameter) throws
	 * JAXBException { this.control = control; this.parameter = parameter;
	 * this.setValue(getConstInitValue()); init(); }
	 **/
	// -- Overriden --
	protected void initPreCheck() throws JAXBException
	{
		this.setValue( getConstInitValue() );
	}

	private String getConstInitValue()
	{
		return ( (HiddenFieldT) control ).getInitValue();
	}

/** 2/10/2010 Scott Atwell	
	public String getControlValue()
	{
		return value;
	}
**/
	public String getControlValueRaw()
	{
		return value;
	}

	public Object getParameterValue()
	{
		// TODO add this for all controls
		if ( ParameterHelper.getConstValue( parameter ) != null )
			return ParameterHelper.getConstValue( parameter );
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public void setEnabled(boolean enabled)
	{
		// Do nothing
	}

	public void setVisible(boolean visible)
	{
		// Do nothing
	}
	
	public boolean isEnabled()
	{
		return false;
	}
	
	public boolean isVisible()
	{
		return false;
	}
}
