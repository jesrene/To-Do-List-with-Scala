package ch.makery.todo.model
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.LocalDate
import ch.makery.todo.util.Database
import ch.makery.todo.util.DateUtil._
import scalikejdbc._
import scala.util.{ Try, Success, Failure }
import scalafx.collections.ObservableBuffer

class Task (val catagoryS : String, val titleS : String) extends Database {
    def this()     = this(null, null)
    var catagory   = new StringProperty(catagoryS)
    var title      = new StringProperty(titleS)
    var street     = new StringProperty("description.")
    var due        = ObjectProperty[LocalDate](LocalDate.now)
    var status     = new StringProperty("")
    
    def save() : Try[Int] = {
        if (!(isExist)) {
            Try(DB autoCommit { implicit session => 
                sql"""
                    insert into task (catagory, title,
                        street, postalCode, due,status) values 
                        (${catagory.value}, ${title.value}, ${street.value}, ${status.value}, 
                            ${due.value.asString})
                """.update.apply()
            })
        } else {
            Try(DB autoCommit { implicit session => 
                sql"""
                update task 
                set 
                catagory  = ${catagory.value} ,
                title   = ${title.value},
                street     = ${street.value},
                status     = ${status.value},
                due       = ${due.value.asString}
                 where catagory = ${catagory.value} and
                 title = ${title.value}
                """.update.apply()
            })
        }
            
    }

    def delete() : Try[Int] = {
        if (isExist) {
            Try(DB autoCommit { implicit session => 
            sql"""
                delete from task where  
                    catagory = ${catagory.value} and title = ${title.value}
                """.update.apply()
            })
        } else 
            throw new Exception("Task do not Exists in Database")        
    }
    def isExist : Boolean =  {
        DB readOnly { implicit session =>
            sql"""
                select * from task where 
                catagory = ${catagory.value} and title = ${title.value}
            """.map(rs => rs.string("catagory")).single.apply()
        } match {
            case Some(x) => true
            case None => false
        }
    }
}

//Set up database
object Task extends Database{
Database.setupDB()
 val taskData = new ObservableBuffer[Task]()
 taskData ++= Task.getAllTasks
    def apply (
        catagoryS : String, 
        titleS : String,
        streetS : String,
        dueS : String,
        statusS: String
        ) : Task = {
        new Task (catagoryS, titleS) {
            street.value     = streetS
            due.value       = dueS.parseLocalDate
            status.value = statusS
        }
        
    }

    def initializeTable() = {
        DB autoCommit { implicit session => 
            sql"""
            create table task (
              id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
              catagory varchar(64), 
              title varchar(64), 
              street varchar(200),
              due varchar(64),
              status varchar(50)
            )
            """.execute.apply()
        }
    }
    
def getAllTasks : List[Task] = {
        DB readOnly { implicit session =>
            sql"select * from task".map(rs => Task(rs.string("catagory"),
                rs.string("title"),rs.string("street"), 
                rs.string("due"),rs.string("status"))).list.apply()
        }
    }
}