package org.atdl4j.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.ui.ControlHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class RadioButtonListWidget
		extends AbstractSWTWidget<String>
{
	private static final Logger logger = Logger.getLogger( RadioButtonListWidget.class );

	private List<Button> buttons = new ArrayList<Button>();
	private Label label;

	// 1/20/2010 Scott Atwell added
// 2/23/2010 support newly added RadioButtonList/@orientation="VERTICAL"	public static boolean disableVerticalLayoutHandling = false;

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	 * public RadioButtonListWidget(RadioButtonListT control, ParameterT
	 * parameter) throws JAXBException { this.control = control; this.parameter =
	 * parameter; init(); }
	 **/

	public Widget createWidget(Composite parent, int style)
	{
		String tooltip = getTooltip();
		GridData controlGD = new GridData( SWT.FILL, SWT.FILL, false, false );
		
		// label
		if ( control.getLabel() != null ) {
			label = new Label( parent, SWT.NONE );
			label.setText( control.getLabel() );
			if ( tooltip != null ) label.setToolTipText( tooltip );
			controlGD.horizontalSpan = 1;
		} else {
			controlGD.horizontalSpan = 2;
		}
		
		Composite c = new Composite( parent, SWT.NONE );
		c.setLayoutData(controlGD);

		// 2/23/2010 Scott Atwell RadioButtonList/@orientation has been added
		// 3/14/2010 John Shields implemented RowLayout for horizontal orientation
		if ( ((RadioButtonListT) control).getOrientation() != null &&
			 PanelOrientationT.VERTICAL.equals( ((RadioButtonListT) control).getOrientation() ) )
		{
			c.setLayout( new GridLayout( 1, false ) );
		} else {
			RowLayout rl = new RowLayout();
			rl.wrap = false;
			c.setLayout( rl );
		}

		// radioButton
		for ( ListItemT listItem : ( (RadioButtonListT) control ).getListItem() )
		{

			Button radioElement = new Button( c, style | SWT.RADIO );
			radioElement.setText( listItem.getUiRep() );
			if ( parameter != null )
			{
				for ( EnumPairT enumPair : parameter.getEnumPair() )
				{
					if ( enumPair.getEnumID() == listItem.getEnumID() )
					{
						radioElement.setToolTipText( enumPair.getDescription() );
						break;
					}
				}
			}
			else
				radioElement.setToolTipText( tooltip );
			buttons.add( radioElement );
		}

		// set initValue (Note that this has to be the enumID, not the
		// wireValue)
		// set initValue
//		if ( ( (RadioButtonListT) control ).getInitValue() != null )
//			setValue( ( (RadioButtonListT) control ).getInitValue(), true );
		if ( ControlHelper.getInitValue( control, getAtdl4jConfig() ) != null )
			setValue( (String) ControlHelper.getInitValue( control, getAtdl4jConfig() ), true );

		return c;
	}

/** 2/10/2010 Scott Atwell	
	public String getControlValue()
	{
		for ( int i = 0; i < this.buttons.size(); i++ )
		{
			Button b = buttons.get( i );
			// TODO 1/24/2010 Scott Atwell if (b.getSelection()) {
			if ( ( b.getSelection() ) && ( b.isVisible() ) && ( b.isEnabled() ) )
			{
				return ( (RadioButtonListT) control ).getListItem().get( i ).getEnumID();
			}
		}
		return null;
	}
**/
	public String getControlValueRaw()
	{
		for ( int i = 0; i < this.buttons.size(); i++ )
		{
			Button b = buttons.get( i );
			if ( b.getSelection() )
			{
				return ( (RadioButtonListT) control ).getListItem().get( i ).getEnumID();
			}
		}
		return null;
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
		for ( int i = 0; i < buttons.size(); i++ )
		{
			Button b = buttons.get( i );
			if ( setValueAsControl || parameter == null )
			{
				b.setSelection( value.equals( getListItems().get( i ).getEnumID() ) );
			}
			else
			{
				b.setSelection( value.equals( parameter.getEnumPair().get( i ).getWireValue() ) );
			}
		}
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		if (label != null) widgets.add( label );
		widgets.addAll( buttons );
		return widgets;
	}

	public List<Control> getControlsExcludingLabel()
	{
		List<Control> widgets = new ArrayList<Control>();
		widgets.addAll( buttons );
		return widgets;
	}

	public void addListener(Listener listener)
	{
		for ( Button b : buttons )
		{
			b.addListener( SWT.Selection, listener );
		}
	}

	public void removeListener(Listener listener)
	{
		for ( Button b : buttons )
		{
			b.removeListener( SWT.Selection, listener );
		}
	}

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( aControlInitValue != null )
		{
			// -- apply initValue if one has been specified --
			setValue( (String) aControlInitValue, true );
		}
		else
		{
			// -- reset each when no initValue exists --
			for ( Button tempButton : buttons )
			{
				if ( ( tempButton != null ) && ( ! tempButton.isDisposed() ) )
				{
					tempButton.setSelection( false );
				}
			}
		}
	}

	/* 
	 * 
	 */
	protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd)
	{
		// TODO ?? adjust the visual appearance of the control ??
	}
}
