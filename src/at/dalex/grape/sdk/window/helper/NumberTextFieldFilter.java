package at.dalex.grape.sdk.window.helper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * This class is used to apply a filter to TextFields which should only accept numbers.
 */
public class NumberTextFieldFilter implements ChangeListener<String> {

    //The textfield instance
    private TextField textField;

    /**
     * Applies a filter, so that the given textfield can only accept numbers.
     * @param textField The textfield to apply this filter to
     */
    public NumberTextFieldFilter(TextField textField) {
        this.textField = textField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
    }
}
