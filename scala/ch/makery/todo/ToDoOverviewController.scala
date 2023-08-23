package ch.makery.todo
import ch.makery.todo.model.Task
import scalafx.scene.control.{Alert, TableView, TableColumn, Label}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty} 
import scalafx.Includes._
import ch.makery.todo.util.DateUtil._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scala.util.{Failure, Success}

@sfxml
class ToDoOverviewController(
    private val taskTable : TableView[Task],
    private val catagoryColumn : TableColumn[Task, String],
    private val titleColumn : TableColumn[Task, String],
    //private val dueColumn: TableColumn[Task, String],
    private val catagoryLabel : Label,
    private val titleLabel : Label,
    private val streetLabel :  Label,
    private val dueLabel : Label,
    private val statusLabel: Label
  ) {
  
  //Initialize Table View display contents model
  taskTable.items = Task.taskData 

  //Initialize columns's cell values
  catagoryColumn.cellValueFactory = {x => x.value.catagory}
  titleColumn.cellValueFactory  = {_.value.title} 
 
  private def showTaskDetails (task : Option[Task]) = {
    task match {
      case Some(task) =>
      // Fill the labels with info from the task object
      catagoryLabel.text <== task.catagory
      titleLabel.text  <== task.title
      dueLabel.text = task.due.value.asString
      streetLabel.text    <== task.street
      statusLabel.text <== task.status
    
      
      case None =>
        //Task is null, remove all the text.
      catagoryLabel.text.unbind()      
      titleLabel.text.unbind()   
      statusLabel.text.unbind()
      dueLabel.text.unbind()

      catagoryLabel.text = ""
      titleLabel.text    = ""
      streetLabel.text   = ""
      dueLabel.text      = ""
      statusLabel.text   = ""
    }    
  }
  
  showTaskDetails(None);
  
  taskTable.selectionModel().selectedItem.onChange(
     (_, _, newValue) => showTaskDetails(Option(newValue))
  )
//delete button
  def handleDeletePerson(action : ActionEvent) = {
    val selectedIndex = taskTable.selectionModel().selectedIndex.value
       if (selectedIndex >= 0) {
        taskTable.items().remove(selectedIndex).delete()
    }
    else {
        // Nothing selected.
        val alert = new Alert(AlertType.Error){
        initOwner(MainApp.stage)
        title       = "No Selection"
        headerText  = "No task Selected"
        contentText = "Please select a task in the table."
        }.showAndWait()
    }
  } 
//when clicked new button
    def handleNewPerson(action : ActionEvent) = {
    val task = new Task("", "")
    val okClicked = MainApp.showToDoEditDialog(task);
        if (okClicked) {
          Task.taskData += task
          task.save() 
      
  }
 }

   //edit button 
    def handleEditPerson(action : ActionEvent) = {
      val selectedTask = taskTable.selectionModel().selectedItem.value
      if (selectedTask != null) {
          val okClicked = MainApp.showToDoEditDialog(selectedTask)
          if (okClicked) {
          showTaskDetails(Some(selectedTask))
           selectedTask.save()
    
          }
      } 
      else {
          // Nothing selected.
          val alert = new Alert(Alert.AlertType.Warning){
            initOwner(MainApp.stage)
            title       = "No Selection"
            headerText  = "No task Selected"
            contentText = "Please select a task in the table."
          }.showAndWait()
      }
  } 

} 

