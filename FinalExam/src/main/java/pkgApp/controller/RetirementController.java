package pkgApp.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import pkgApp.RetirementApp;
import pkgCore.Retirement;

@SuppressWarnings("restriction")
public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decmial
		// hmTextFieldRegEx.put(txtYearsToWork, "\\d*?");
		// hmTextFieldRegEx.put(txtAnnualReturnWorking, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtYearsToWork, "[0-9]|[1-3][0-9]|40");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtYearsRetired, "[0-9]|1[0-9]|20");
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtRequiredIncome, "[3-9][0-9][0-9][0-9]|2[7-9][0-9][0-9]|26[5-9][0-9]|264[2-9]|10000");
		hmTextFieldRegEx.put(txtMonthlySSI, "[0-9]|[1-9][0-9]|[1-9][0-9][0-9]|1[0-9][0-9][0-9]|2[0-5][0-9][0-9]|26[0-3][0-9]|264[0-2]");

		// Check out these pages (how to validate controls):
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format
		
		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx) || "".equals(txtField.getText())) {
							txtField.setText("");
							txtField.requestFocus();
						}else{
							if("txtAnnualReturnWorking".equals(txtField.getId()) || "txtAnnualReturnRetired".equals(txtField.getId())){
								float val = Float.valueOf(txtField.getText());
								if(val < 0 || val > 10){
									txtField.setText("");
									txtField.requestFocus();
								}
							}
						}
					}
					
				}
			});
		}

	}

	@FXML
	public void btnClear(ActionEvent event) {
		// System.out.println("Clear pressed");

		// disable read-only controls
		txtSaveEachMonth.clear();
		txtWhatYouNeedToSave.clear();
		txtSaveEachMonth.setDisable(true);
		txtWhatYouNeedToSave.setDisable(true);

		// Clear, enable txtYearsToWork
		txtYearsToWork.clear();
		txtYearsToWork.setDisable(false);

		// TODO: Clear, enable the rest of the input controls. Hint! You already have a
		// HashMap of all the input controls....!!!!
		txtYearsToWork.clear();
		txtYearsToWork.setDisable(false);
		
		txtAnnualReturnWorking.clear();
		txtAnnualReturnWorking.setDisable(false);
		
		txtYearsRetired.clear();
		txtYearsRetired.setDisable(false);
		
		txtAnnualReturnRetired.clear();
		txtAnnualReturnRetired.setDisable(false);
		
		txtRequiredIncome.clear();
		txtRequiredIncome.setDisable(false);
		
		txtMonthlySSI.clear();
		txtMonthlySSI.setDisable(false);
	}

	@FXML
	public void btnCalculate() {

		//System.out.println("calculating");

		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);

		// as input
		int iYearsToWork = Integer.valueOf(txtYearsToWork.getText());
		double dAnnualReturnWorking = Double.valueOf(txtAnnualReturnWorking.getText())/100;
		int iYearsRetired = Integer.valueOf(txtYearsRetired.getText());;
		double dAnnualReturnRetired = Double.valueOf(txtAnnualReturnRetired.getText())/100;
		int dRequiredIncome = Integer.valueOf(txtRequiredIncome.getText());;
		int dMonthlySSI = Integer.valueOf(txtMonthlySSI.getText());;
		
		Retirement r = new Retirement(iYearsToWork, dAnnualReturnWorking, iYearsRetired, dAnnualReturnRetired, dRequiredIncome, dMonthlySSI);
		
		txtSaveEachMonth.setText(String.valueOf(Math.abs(r.MonthlySavings())));
		txtWhatYouNeedToSave.setText(String.valueOf(Math.abs(r.TotalAmountToSave())));
	}
}
