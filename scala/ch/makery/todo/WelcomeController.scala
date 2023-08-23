package ch.makery.todo

import scalafxml.core.macros.sfxml

@sfxml
//Start button
class WelcomeController{
    def handleStart() {
        MainApp.showToDoOverview()
    }
}