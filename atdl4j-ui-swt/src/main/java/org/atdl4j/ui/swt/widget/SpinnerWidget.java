package org.atdl4j.ui.swt.widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.atdl4j.data.converter.DecimalConverter;
import org.atdl4j.data.converter.IntegerConverter;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.ui.ControlHelper;
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
		if ( parameterConverter != null && parameterConverter instanceof DecimalConverter )
		{
			DecimalConverter tempDecimalConverter = (DecimalConverter) parameterConverter;
			
			if ( tempDecimalConverter.getPrecision() != null )
			{
				spinner.setDigits( tempDecimalConverter.getPrecision().intValue() );
			}
			else
			{
				// -- not specified in FIXatdl file, use default if we have one within Atdl4jConfig --
				if ( ( getAtdl4jConfig() != null ) && ( getAtdl4jConfig().getDefaultDigitsForSpinnerControl() != null ) )
				{
					spinner.setDigits( getAtdl4jConfig().getDefaultDigitsForSpinnerControl().intValue() );
				}
			}

			if ( tempDecimalConverter.getMinValue() != null )
			{
				spinner.setMinimum( tempDecimalConverter.getMinValue().intValue() );
			}
			else
			{
				spinner.setMinimum( -Integer.MAX_VALUE );
			}
			
			if ( tempDecimalConverter.getMaxValue() != null )
			{
				spinner.setMaximum( tempDecimalConverter.getMaxValue().intValue() );
			}
			else
			{
				spinner.setMaximum( Integer.MAX_VALUE );
			}
		}
		else if ( parameterConverter != null && parameterConverter instanceof IntegerConverter )
		{
			IntegerConverter tempIntegerConverter = (IntegerConverter) parameterConverter;
			
			spinner.setDigits( 0 );

			if ( tempIntegerConverter.getMinValue() != null )
			{
				spinner.setMinimum( tempIntegerConverter.getMinValue().intValue() );
			}
			else
			{
				spinner.setMinimum( -Integer.MAX_VALUE );
			}
			
			if ( tempIntegerConverter.getMaxValue() != null )
			{
				spinner.setMaximum( tempIntegerConverter.getMaxValue().intValue() );
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
//			if ( ( (DoubleSpinnerT) control ).getInnerIncrement() != null )
//				spinner.setIncrement( ( (DoubleSpinnerT) control ).getInnerIncrement().intValue() );
// 2/21/2010 Scott Atwell -- this method supports innerIncrementPolicy (Static, LotSize, Tick) --			
			if ( ControlHelper.getInnerIncrementValue( control, getAtdl4jConfig() ) != null )
				spinner.setIncrement( ControlHelper.getInnerIncrementValue( control, getAtdl4jConfig() ).intValue() );

			int outerStepSize = 1 * (int) Math.pow( 10, spinner.getDigits() );
//			if ( ( (DoubleSpinnerT) control ).getOuterIncrement() != null )
//				outerStepSize = ( ( (DoubleSpinnerT) control ).getOuterIncrement().intValue() );
// 2/21/2010 Scott Atwell -- this method supports outerIncrementPolicy (Static, LotSize, Tick) --			
			if ( ControlHelper.getOuterIncrementValue( control, getAtdl4jConfig() ) != null )
				outerStepSize = ControlHelper.getOuterIncrementValue( control, getAtdl4jConfig() ).intValue();

			buttonUp.addSelectionListener( new DoubleSpinnerSelection( spinner, outerStepSize ) );
			buttonDown.addSelectionListener( new DoubleSpinnerSelection( spinner, -1 * outerStepSize ) );
		}
// 2/21/2010 Scott Atwell Added
		else if ( control instanceof SingleSpinnerT )
		{
//			if ( ( (SingleSpinnerT) control ).getIncrement() != null )
//				spinner.setIncrement( ( (SingleSpinnerT) control ).getIncrement().intValue() );
// 2/21/2010 Scott Atwell -- this method supports incrementPolicy (Static, LotSize, Tick) --			
			if ( ControlHelper.getIncrementValue( control, getAtdl4jConfig() ) != null )
				spinner.setIncrement( ControlHelper.getIncrementValue( control, getAtdl4jConfig() ).intValue() );
		}

//		Double initValue = control instanceof SingleSpinnerT ? ( (SingleSpinnerT) control ).getInitValue() : ( (DoubleSpinnerT) control )
//				.getInitValue();
		Double initValue = (Double) ControlHelper.getInitValue( control, getAtdl4jConfig() );
		if ( initValue != null )
			spinner.setSelection( initValue.intValue() * (int) Math.pow( 10, spinner.getDigits() ) );

		return parent;
	}


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

	public List<Control> getControlsExcludingLabel()
	{
		List<Control> widgets = new ArrayList<Control>();
//		widgets.add( label );
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

	/* (non-Javadoc)
	 * @see org.atdl4j.ui.ControlUI#reinit()
	 */
	@Override
	public void processReinit( Object aControlInitValue )
	{
		if ( ( spinner != null ) && ( ! spinner.isDisposed() ) )
		{
			if ( aControlInitValue != null )
			{
				// -- apply initValue if one has been specified --
				Double initValue = (Double) aControlInitValue;
				if ( initValue != null )
					spinner.setSelection( initValue.intValue() * (int) Math.pow( 10, spinner.getDigits() ) );
			}
			else
			{
				// -- set to minimum when no initValue exists --
				spinner.setSelection( spinner.getMinimum() );
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
