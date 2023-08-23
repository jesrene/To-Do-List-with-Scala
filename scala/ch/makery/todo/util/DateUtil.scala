package ch.makery.todo.util
import java.time.LocalDate
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

object DateUtil {
  val DATE_PATTERN = "dd.MM.yyyy"
  val DATE_FORMATTER =  DateTimeFormatter.ofPattern(DATE_PATTERN)
  
  implicit class DateFormater (val due : LocalDate){
     def asString : String = {
        if (due == null) {
            return null;
        }
        return DATE_FORMATTER.format(due);
    }
  }

  implicit class StringFormater (val data : String) {
    def parseLocalDate : LocalDate = {
        try {
          LocalDate.parse(data, DATE_FORMATTER)
        } catch {
          case  e: DateTimeParseException => null
        }
    }

    //Validate input
    def isValid : Boolean = {
      data.parseLocalDate != null
    }
  }
}
