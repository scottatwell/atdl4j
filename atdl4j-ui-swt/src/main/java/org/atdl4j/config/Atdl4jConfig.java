/*
 * Created on Feb 7, 2010
 *
 */
package org.atdl4j.config;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.atdl4j.atdl.core.ParameterT;
import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;
import org.atdl4j.atdl.layout.CheckBoxListT;
import org.atdl4j.atdl.layout.CheckBoxT;
import org.atdl4j.atdl.layout.ClockT;
import org.atdl4j.atdl.layout.DoubleSpinnerT;
import org.atdl4j.atdl.layout.DropDownListT;
import org.atdl4j.atdl.layout.EditableDropDownListT;
import org.atdl4j.atdl.layout.HiddenFieldT;
import org.atdl4j.atdl.layout.LabelT;
import org.atdl4j.atdl.layout.MultiSelectListT;
import org.atdl4j.atdl.layout.RadioButtonListT;
import org.atdl4j.atdl.layout.RadioButtonT;
import org.atdl4j.atdl.layout.SingleSelectListT;
import org.atdl4j.atdl.layout.SingleSpinnerT;
import org.atdl4j.atdl.layout.SliderT;
import org.atdl4j.atdl.layout.TextFieldT;
import org.atdl4j.data.TypeConverterFactory;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.ui.ControlUI;
import org.atdl4j.ui.ControlUIFactory;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategiesUIFactory;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.StrategySelectionUI;

/**
 * 
 * Creation date: (Feb 7, 2010 7:01:05 PM)
 * @author Scott Atwell
 * @version 1.0, Feb 7, 2010
 */
public interface Atdl4jConfig
{
	// -- UI Infrastructure --
	/**
	 * @return the strategiesUIFactory
	 */
	public StrategiesUIFactory getStrategiesUIFactory();

	/**
	 * @param strategies
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public StrategiesUI getStrategiesUI(StrategiesT strategies, Atdl4jConfig aAtdl4jConfig)
		throws JAXBException;


	/**
	 * @param strategy
	 * @param aAtdl4jConfig (contains getStrategies())
	 * @param strategiesRules
	 * @param parentContainer (for SWT: should be swt.Composite)
	 * @return
	 * @throws JAXBException
	 */
	public StrategyUI getStrategyUI(StrategyT strategy, Atdl4jConfig aAtdl4jConfig, Map<String, ValidationRule> strategiesRules, Object parentContainer)
		throws JAXBException;
	
	/**
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public ControlUIFactory getControlUIFactory(Atdl4jConfig aAtdl4jConfig)
		throws JAXBException;
	
	/**
	 * @param aAtdl4jConfig
	 * @return
	 * @throws JAXBException
	 */
	public TypeConverterFactory getTypeConverterFactory(Atdl4jConfig aAtdl4jConfig)
		throws JAXBException;
	
	
	// -- Controls/Widgets (first arg is of type ControlT -- 
	public ControlUI getControlUIForCheckBoxT(CheckBoxT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForDropDownListT(DropDownListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForEditableDropDownListT(EditableDropDownListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForRadioButtonListT(RadioButtonListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForTextFieldT(TextFieldT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForSliderT(SliderT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForCheckBoxListT(CheckBoxListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForClockT(ClockT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForSingleSpinnerT(SingleSpinnerT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForDoubleSpinnerT(DoubleSpinnerT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForSingleSelectListT(SingleSelectListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForMultiSelectListT(MultiSelectListT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForHiddenFieldT(HiddenFieldT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForLabelT(LabelT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;
	public ControlUI getControlUIForRadioButtonT(RadioButtonT control, ParameterT parameter, Atdl4jConfig aAtdl4jConfig) throws JAXBException;

	// -- App Components --
	public StrategySelectionUI getStrategySelectionUI();
	
	public InputAndFilterData getInputAndFilterData();
	public void setInputAndFilterData(InputAndFilterData inputAndFilterData);
	
	public StrategiesT getStrategies();
	public void setStrategies(StrategiesT strategies);

	public Map<StrategyT, StrategyUI> getStrategyUIMap();
	public void setStrategyUIMap(Map<StrategyT, StrategyUI> strategyUIMap);

	public StrategyT getSelectedStrategy();
	public void setSelectedStrategy(StrategyT selectedStrategy);

	public void setShowStrategyDescription(boolean showStrategyDescription);
	public boolean isShowStrategyDescription();

	public void setShowTimezoneSelector(boolean showTimezoneSelector);
	public boolean isShowTimezoneSelector();
	
	public List<StrategyT> getStrategiesFilteredStrategyList();

	public boolean isTreatControlVisibleFalseAsNull();
	public boolean isTreatControlEnabledFalseAsNull();
	public boolean isRestoreLastNonNullStateControlValueBehavior();
	
	public boolean isShowEnabledCheckboxOnOptionalClockControl();

	public Integer getDefaultDigitsForSpinnerControl();
}
