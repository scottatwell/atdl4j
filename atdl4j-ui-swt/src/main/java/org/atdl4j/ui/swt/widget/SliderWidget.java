package org.atdl4j.ui.swt.widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.ui.swt.util.ParameterListenerWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Widget;

import org.fixprotocol.atdl_1_1.core.EnumPairT;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.layout.SliderT;

public class SliderWidget extends AbstractSWTWidget<BigDecimal> {
	
	private Scale slider;
	private Label label;

	public SliderWidget(SliderT control, ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style) {

		// label
		Label l = new Label(parent, SWT.NONE);
// 1/20/2010 Scott Atwell avoid NPE as label is not required on Control		l.setText(control.getLabel());
		if ( control.getLabel() != null )
		{
			l.setText(control.getLabel());
		}
		this.label = l;

		Composite c = new Composite(parent, SWT.NONE);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		int numColumns = (parameter != null && 
				parameter.getEnumPair() != null && 
				parameter.getEnumPair().size() > 0) ?
				parameter.getEnumPair().size() : 1;
		c.setLayout(new GridLayout(numColumns, true));

		// slider
		Scale slider = new Scale(c, style | SWT.HORIZONTAL);
		this.slider = slider;
		slider.setIncrement(1);
		slider.setPageIncrement(1);
		GridData sliderData = new GridData(SWT.FILL, SWT.FILL, true, false);
		sliderData.horizontalSpan = numColumns;
		slider.setLayoutData(sliderData);
		slider.setMaximum(numColumns > 1 ? numColumns - 1 : 1);
		
		// labels based on parameter enumPair
		// TODO: this should be changed in the FIXatdl spec so that sliders can have
		// list items, with labels based on the UiRep.
		if (parameter != null)
		{
			for (EnumPairT enumPair : parameter.getEnumPair()) {
				Label label = new Label(c, SWT.NONE);
				label.setText(enumPair.getEnumID());
				label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
								false));
			}
		}
		
		// tooltip
		String tooltip = control.getTooltip();
		slider.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return parent;
	}

	public BigDecimal getControlValue() {
//TODO 1/24/2010 Scott Atwell added
		if ( ( slider.isVisible() == false ) || ( slider.isEnabled() == false ) )
		{
			return null;
		}
		
		try {
			return new BigDecimal(slider.getSelection());
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public BigDecimal getParameterValue() {
		return getControlValue();
	}

	public void setValue(BigDecimal value) {
		slider.setSelection(value.intValue());
	}

	public void generateStateRuleListener(Listener listener) {
		slider.addListener(SWT.Selection, listener);
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(slider);
		return widgets;
	}

	public void addListener(Listener listener) {
		slider.addListener(SWT.Selection, new ParameterListenerWrapper(this,
				listener));
	}

	public void removeListener(Listener listener) {
		slider.removeListener(SWT.Selection, listener);
	}

}
