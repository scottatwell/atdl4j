package org.atdl4j.ui.swt.widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.atdl4j.atdl.layout.DoubleSpinnerT;
import org.atdl4j.atdl.layout.SingleSpinnerT;
import org.atdl4j.data.converter.NumberConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

public class SpinnerWidget
		extends AbstractSWTWidget<BigDecimal>
{

	private Spinner spinner;
	private Label label;
	private Button buttonUp;
	private Button buttonDown;

	/**
	 * SelectionListener that implements the dual spinner behavior.
	 * 
	 */
	public class DoubleSpinnerSelection
			implements SelectionListener
	{
		private Spinner spinner;

		private int increment;

		public DoubleSpinnerSelection(Spinner spinner, int increment)
		{
			this.spinner = spinner;
			this.increment = increment;
		}

		public void widgetDefaultSelected(SelectionEvent event)
		{
		}

		public void widgetSelected(SelectionEvent event)
		{
			spinner.setSelection( spinner.getSelection() + increment );
		}
	}

	/**
	 * 2/9/2010 Scott Atwell @see AbstractControlUI.init(ControlT aControl,
	 * ParameterT aParameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException
	 * public SpinnerWidget(DoubleSpinnerT control, ParameterT parameter) throws
	 * JAXBException { this.control = control; this.parameter = parameter;
	 * init(); }
	 * 
	 * public SpinnerWidget(SingleSpinnerT control, ParameterT parameter) throws
	 * JAXBException { this.control = control; this.parameter = parameter;
	 * init(); }
	 **/

	public Widget createWidget(Composite parent, int style)
	{

		// label
		label = new Label( parent, SWT.NONE );
		if ( control.getLabel() != null )
			label.setText( control.getLabel() );

		if ( control instanceof SingleSpinnerT )
		{
			// spinner
			spinner = new Spinner( parent, style | SWT.BORDER );
			spinner.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
		}
		else if ( control instanceof DoubleSpinnerT )
		{
			// doubleSpinnerGrid
			Composite c = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.horizontalSpacing = 0;
			gridLayout.verticalSpacing = 0;
			c.setLayout( gridLayout );
			c.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );

			// doubleSpinner
			spinner = new Spinner( c, style | SWT.BORDER );
			GridData spinnerData = new GridData( SWT.FILL, SWT.CENTER, false, false );
			spinnerData.verticalSpan = 2;
			spinner.setLayoutData( spinnerData );

			this.buttonUp = new Button( c, SWT.ARROW | SWT.UP );
			this.buttonDown = new Button( c, SWT.ARROW | SWT.DOWN );
		}

		// tooltip
		String tooltip = getTooltip();
		if ( tooltip != null )
		{
			spinner.setToolTipText( tooltip );
			label.setToolTipText( tooltip );
		}

		// Set min/max/precision if a parameter is attached
		// TODO this is a bit messy... should probably be done separately of
		// NumberConverter
		if ( parameterConverter != null && parameterConverter instanceof NumberConverter )
		{
			spinner.setDigits( ( (NumberConverter) parameterConverter ).getPrecision() );

			Integer minimum = ( (NumberConverter) parameterConverter ).getMinimum();
			if ( minimum != null )
			{
				spinner.setMinimum( minimum );
			}
			else
			{
				spinner.setMinimum( -Integer.MAX_VALUE );
			}

			Integer maximum = ( (NumberConverter) parameterConverter ).getMaximum();
			if ( maximum != null )
			{
				spinner.setMaximum( maximum );
			}
			else
			{
				spinner.setMaximum( Integer.MAX_VALUE );
			}
		}
		else
		{
			spinner.setDigits( 0 );
			spinner.setMinimum( -Integer.MAX_VALUE );
			spinner.setMaximum( Integer.MAX_VALUE );
		}

		if ( control instanceof DoubleSpinnerT )
		{
			if ( ( (DoubleSpinnerT) control ).getInnerIncrement() != null )
				spinner.setIncrement( ( (DoubleSpinnerT) control ).getInnerIncrement().intValue() );

			int outerStepSize = 1 * (int) Math.pow( 10, spinner.getDigits() );
			if ( ( (DoubleSpinnerT) control ).getOuterIncrement() != null )
				outerStepSize = ( ( (DoubleSpinnerT) control ).getOuterIncrement().intValue() );

			buttonUp.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize ) );
			buttonDown.addSelectionListener( new DoubleSpinnerSelection( spinner, -1 * outerStepSize ) );
		}

		Double initValue = control instanceof SingleSpinnerT ? ( (SingleSpinnerT) control ).getInitValue() : ( (DoubleSpinnerT) control )
				.getInitValue();
		if ( initValue != null )
			spinner.setSelection( initValue.intValue() * (int) Math.pow( 10, spinner.getDigits() ) );

		return parent;
	}

/** 2/10/2010 Scott Atwell	
	public BigDecimal getControlValue()
	{
		// 1/24/2010 Scott Atwell added
		if ( !spinner.isVisible() || !spinner.isEnabled() )
			return null;

		try
		{
			return BigDecimal.valueOf( spinner.getSelection(), spinner.getDigits() );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}
**/
	public BigDecimal getControlValueRaw()
	{
		try
		{
			return BigDecimal.valueOf( spinner.getSelection(), spinner.getDigits() );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	public BigDecimal getParameterValue()
	{
		return (BigDecimal) getControlValue();
	}

	public void setValue(BigDecimal value)
	{
		spinner.setSelection( value.unscaledValue().intValue() );
	}

	public List<Control> getControls()
	{
		List<Control> widgets = new ArrayList<Control>();
		widgets.add( label );
		widgets.add( spinner );
		if ( control instanceof DoubleSpinnerT )
		{
			widgets.add( buttonUp );
			widgets.add( buttonDown );
		}
		return widgets;
	}

	public void addListener(Listener listener)
	{
		spinner.addListener( SWT.Modify, listener );
		if ( control instanceof DoubleSpinnerT )
		{
			buttonUp.addListener( SWT.Selection, listener );
			buttonDown.addListener( SWT.Selection, listener );
		}
	}

	public void removeListener(Listener listener)
	{
		spinner.removeListener( SWT.Modify, listener );
		if ( control instanceof DoubleSpinnerT )
		{
			buttonUp.removeListener( SWT.Selection, listener );
			buttonDown.removeListener( SWT.Selection, listener );
		}
	}
}
