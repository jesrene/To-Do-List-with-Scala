package ch.makery.todo
import ch.makery.todo.MainApp
import ch.makery.todo.model.Task
import scalafx.scene.control.{TextField, TableColumn, Label, Alert, CheckBox, RadioButton, ToggleGroup, DatePicker}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import ch.makery.todo.util.DateUtil._
import scalafx.event.ActionEvent

@sfxml
class ToDoEditDialogController (
    private val    catagoryField : TextField, 
    private val       titleField : TextField,
    private val      streetField : TextField,
    private val       datePicker : DatePicker,
    private val       completeBox: RadioButton,
    private val     incompleteBox: RadioButton
){

  var       dialogStage : Stage  = null
  private var _task       : Task = null
  var         okClicked          = false
  val group = new ToggleGroup()
  completeBox.toggleGroup = group
  incompleteBox.toggleGroup = group

  def task = _task
  def task_=(x : Task) {
        _task = x
        catagoryField.text = _task.catagory.value
        titleField.text  = _task.title.value
        streetField.text    = _task.street.value
        datePicker.value  = _task.due.value
  }

//Done button
  def handleOk(action :ActionEvent){
     if (isInputValid()) {
        _task.catagory <== catagoryField.text
        _task.title  <== titleField.text
        _task.street    <== streetField.text
        _task.due      = datePicker.value
        okClicked = true;
        dialogStage.close()
    }
  }

//Cancel button
  def handleCancel(action :ActionEvent) {
        dialogStage.close();
  }

  def nullChecking (x : String) = x == null || x.length == 0
  def isInputValid() : Boolean = {
    var errorMessage = ""
    if (nullChecking(catagoryField.text.value))
      errorMessage += "No valid category!\n"
    if (nullChecking(titleField.text.value))
      errorMessage += "No valid title!\n"
    if (nullChecking(streetField.text.value))
      errorMessage += "No valid description!\n"
    if (errorMessage.length() == 0) {
        return true;
    } else {
        // Show the error message.
        val alert = new Alert(Alert.AlertType.Error){
          initOwner(dialogStage)
          title = "Invalid Fields"
          headerText = "Please correct invalid fields"
          contentText = errorMessage
        }.showAndWait()
        return false;
    }
   }
   
//Radiobutton status
 def complete (action :ActionEvent): Unit = {
   _task.status = new StringProperty("Complete")
 }

  def incomplete (action: ActionEvent): Unit = {
    _task.status = new StringProperty("Incomplete")
 }
}

