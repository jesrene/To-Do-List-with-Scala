package ch.makery.todo
import ch.makery.todo.model.Task
import ch.makery.todo.ToDoEditDialogController
import ch.makery.todo.ToDoOverviewController
import ch.makery.todo.util.Database
import ch.makery.todo.RootLayoutController
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.stage.{Stage, Modality}
import scalafx.scene.image.Image
import scalafx.collections.ObservableBuffer

object MainApp extends JFXApp{
Database.setupDB()
 val taskData = new ObservableBuffer[Task]()
 taskData ++= Task.getAllTasks

  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  stage = new PrimaryStage{
    title = "To Do List"
    icons += new Image(getClass.getResourceAsStream("images/todolist-icon.png"))
    scene = new Scene {
      root = roots
      stylesheets = List(getClass.getResource("view/PinkTheme.css").toString())
    }
    }
  
//ToDoList overview controller
  var ctrl: Option[ToDoOverviewController#Controller] = None
  def showToDoOverview() = {
    val resource = getClass.getResource("view/ToDoOverview.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      ctrl = Option(loader.getController[ToDoOverviewController#Controller])
      ctrl = Option(loader.getController[ToDoOverviewController#Controller])
      this.roots.setCenter(roots)
  } 

//Display welcome window
  def showWelcome()={
    val resource = getClass.getResource("view/Welcome.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.center_=(roots)
  }

showWelcome()

//ToDoList edit dialog
def showToDoEditDialog(task: Task): Boolean = {
    val resource = getClass.getResourceAsStream("view/ToDoEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[ToDoEditDialogController#Controller]
    val dialog = new Stage() {
      title = "Edit"
      icons += new Image(getClass.getResourceAsStream("images/todolist-icon.png"))
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        stylesheets = List(getClass.getResource("view/PinkTheme.css").toString())
      }
    }
    control.dialogStage = dialog
    control.task = task
    dialog.showAndWait()
    control.okClicked
  } 
}

