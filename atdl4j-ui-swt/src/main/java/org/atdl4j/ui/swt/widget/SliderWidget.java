package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.atdl4j.atdl.layout.ListItemT;
import org.atdl4j.atdl.layout.SliderT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Widget;

public class SliderWidget
		extends AbstractSWTWidget<String>
{
	private Scale slider;
	private Label label;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	 * public SliderWidget(SliderT control, ParameterT parameter) throws
	 * JAXBException { this.control = control; this.parameter = parameter;
	 * init(); }
	 **/

	public Widget createWidget(Composite parent, int style)
	{
		// label
		label = new Label( parent, SWT.NONE );
		if ( control.getLabel() != null )
			label.setText( control.getLabel() );

		Composite c = new Composite( parent, SWT.NONE );
		c.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );

		int numColumns = ( ( (SliderT) control ).getListItem() != null && ( (SliderT) control ).getListItem().size() > 0 ) ? ( (SliderT) control )
				.getListItem().size() : 1;
		c.setLayout( new GridLayout( numColumns, true ) );

		// slider
		slider = new Scale( c, style | SWT.HORIZONTAL );
		slider.setIncrement( 1 );
		slider.setPageIncrement( 1 );
		GridData sliderData = new GridData( SWT.FILL, SWT.FILL, true, false );
		sliderData.horizontalSpan = numColumns;
		slider.setLayoutData( sliderData );
		slider.setMaximum( numColumns > 1 ? numColumns - 1 : 1 );

		// labels based on parameter ListItemTs
		if ( ( (SliderT) control ).getListItem() != null )
		{
			for ( ListItemT li : ( (SliderT) control ).getListItem() )
			{
				Label label = new Label( c, SWT.NONE );
				label.setText( li.getUiRep() );
				label.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, false, false ) );
			}
		}

		// tooltip
		String tooltip = getTooltip();
		if ( tooltip != null )
		{
			slider.setToolTipText( tooltip );
			label.setToolTipText( tooltip );
		}

		if ( ( (SliderT) control ).getInitValue() != null )
			setValue( ( (SliderT) control ).getInitValue(), true );

		return parent;
	}

/** 2/10/2010 Scott Atwell	
	public String getControlValue()
	{
		if ( !slider.isVisible() || !slider.isEnabled() )
			return null;
		return ( (SliderT) control ).getListItem().get( slider.getSelection() ).getEnumID();
	}
**/
	public String getControlValueRaw()
	{
		return ( (SliderT) control ).getListItem().get( slider.getSelection() ).getEnumID();
	}

	public String getParameterValue()
	{
		return getParameterValueAsEnumWireValue();
	}

	public void setValue(String value)
	{
		this.setValue( value, false );
	}

	public void setValue(String value, boolean setValueAsControl)
	{
		for ( int i = 0; i < getListItems().size(); i++ )
		{
			if ( setValueAsControl || parameter == null )
			{
				if ( getListItems().get( i ).getEnumID().equals( value ) )
				{
					slider.setSelection( i );
					break;
				}
			}
			else
			{
				if ( parameter.getEnumPair().get( i ).getWireValue().equals( value ) )
				{
					slider.setSelection( i );
					break;
				}
			}
		}
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		widgets.add( label );
		widgets.add( slider );
		return widgets;
	}

	public void addListener(Listener listener)
	{
		slider.addListener( SWT.Selection, listener );
	}

	public void removeListener(Listener listener)
	{
		slider.removeListener( SWT.Selection, listener );
	}


	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}
}
