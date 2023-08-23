package ch.makery.todo
import scalafxml.core.macros.sfxml
@sfxml
//File button
class RootLayoutController(){
    def handleClose() {
       System.exit(0)
    }

//Controls button
    def handleBack(){
        MainApp.showWelcome()
        }

    def handleOverview(){
        MainApp.showToDoOverview()
        }        
    }

