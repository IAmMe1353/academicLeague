package checkComboBox;
import javafx.collections.ObservableList;

public interface CheckModel<T> {
    
    /**
     * Returns the count of items in the control.
     */
    public int getItemCount();

    /**
     * Returns a read-only list of the currently checked items in the control.
     */
    public ObservableList<T> getCheckedItems();

    /**
     * Checks all items in the control
     */
    public void checkAll();

    /**
     * Unchecks the given item in the control
     * @param item The item to uncheck.
     */
    public void clearCheck(T item);
    
    /**
     * Unchecks all items in the control
     */
    public void clearChecks();
    
    /**
     * Returns true if there are no checked items in the control.
     */
    public boolean isEmpty();

    /**
     * Returns true if the given item is checked in the control.
     * @param item Item whose check property is to be tested.
     */
    public boolean isChecked(T item);
    
    /**
     * Checks the given item in the control.
     * @param item The item to check.
     */
    public void check(T item);

    /**
     * Toggles the check state for the given item in the control.
     * @param item The item for which check state needs to be toggled.
     */
    public void toggleCheckState(T item);
}