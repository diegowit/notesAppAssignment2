import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.CategoryUtility
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.ValidateInput.readValidCategory
import utils.ValidateInput.readValidPriority
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
        ╔═════════════════════════════════════════════════╗
        ║                 NOTE KEEPER APP                 ║
        ╠═════════════════════════════════════════════════╣
        ║ MAIN MENU                                       ║
        ║   1) Add a note                                 ║
        ║   2) List notes                                 ║
        ║   3) Update a note                              ║
        ║   4) Delete a note                              ║
        ║   5) Archive a note                             ║
        ║   6) Search Menu                                ║
        ║                                                 ║
        ║ EXTRA OPTIONS                                   ║
        ║   20) Save notes                                ║
        ║   21) Load notes                                ║
        ║                                                 ║
        ╠═════════════════════════════════════════════════╣
        ║   0) Exit                                       ║
        ╚═════════════════════════════════════════════════╝
         > ==>> """.trimMargin(">"))
}


fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotes()
            3  -> updateNote()
            4  -> deleteNote()
            5 -> archiveNote()
            6 -> runSearch()
            20  -> save()
            21  -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}



fun addNote(){

    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readValidPriority("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readValidCategory("Enter a category for the note from ${CategoryUtility.categories}: ")
    val noteContent = readNextLine("Enter The notes Content")
    val reminderDays = readNextInt("Enter the number of days to remind before the Event")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false, noteContent, reminderDays))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listNotes(){
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes 
                  >     4) priority listing Menu   |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNotes();
            4 -> runPriority();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}


fun listArchivedNotes() {
    println(noteAPI.listArchivedNotes())


}

/**
 *
 *  List Notes Priority
 *
 */
fun runPriority() {

    do {
        when (val option = runPriorityMenu()) {
            1 ->  listNoteByPriority1()
            2 ->  listNoteByPriority2()
            3 ->  listNoteByPriority3()
            4 ->  listNoteByPriority4()
            5 ->  listNoteByPriority5()



            0 -> return // Return to main menu
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun runPriorityMenu(): Int {

    print(
        """
╔═══════════════════════════════════════════════════╗
║                   Employee Menu                   ║
╠═══════════════════════════════════════════════════╣
║   1) List Note By Priority of 1                   ║
║   2) List Note By Priority of 2                   ║
║   3) List Note By Priority of 3                   ║
║   4) List Note By Priority of 4                   ║
║   5) List Note By Priority of 5                   ║
║                                                   ║
║                                                   ║
║                                                   ║
╚═══════════════════════════════════════════════════╝
║   0) Return to Main Menu                          ║
╚═══════════════════════════════════════════════════╝
            
            
       ==>> """.trimMargin()
    )
    return readLine()!!.toInt()
    // Returning the user input as an integer.
}

fun listNoteByPriority1() {
    println(noteAPI.listNotesBySelectedPriority(1))

}

fun listNoteByPriority2() {
    println(noteAPI.listNotesBySelectedPriority(2))

}

fun listNoteByPriority3() {
    println(noteAPI.listNotesBySelectedPriority(3))

}

fun listNoteByPriority4() {
    println(noteAPI.listNotesBySelectedPriority(4))

}

fun listNoteByPriority5() {
    println(noteAPI.listNotesBySelectedPriority(5))

}






    fun updateNote() {

        listNotes()
        if (noteAPI.numberOfNotes() > 0) {

            val indexToUpdate = readNextInt("Enter the index of the note to update: ")
            if (noteAPI.isValidIndex(indexToUpdate)) {
                val noteTitle = readNextLine("Enter a title for the note: ")
                val notePriority = readValidPriority("Enter a priority (1-low, 2, 3, 4, 5-high): ")
                val noteCategory =
                    readValidCategory("Enter a category for the note from ${CategoryUtility.categories}: ")
                val noteContent = readNextLine("Enter The notes Content")
                val reminderDays = readNextInt("Enter the number of days to remind before the Event")

                if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false, noteContent, reminderDays))) {
                    println("Update Successful")
                } else {
                    println("Update Failed")
                }
            } else {
                println("There are no notes for this index number")
            }
        }
    }

    fun deleteNote() {

        listNotes()
        if (noteAPI.numberOfNotes() > 0) {
            val indexToDelete = readNextInt("Enter the index of the note to delete: ")
            val noteToDelete = noteAPI.deleteNote(indexToDelete)
            if (noteToDelete != null) {
                println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
            } else {
                println("Delete NOT Successful")
            }
        }
    }

    fun archiveNote() {
        listActiveNotes()
        if (noteAPI.numberOfActiveNotes() > 0) {
            val indexToArchive = readNextInt("Enter the index of the note to archive: ")
            if (noteAPI.archiveNote(indexToArchive)) {
                println("Archive Successful!")
            } else {
                println("Archive NOT Successful")
            }
        }
    }




/**
 *
 *  Search Notes Menu
 *
 */
fun runSearch () {

    do {
        when (val option = searchMenu()) {
            1 ->  searchNoteByTitle()
            2 ->  searchNoteByCategory()
            3 ->  searchNoteByContent()



            0 -> return // Return to main menu
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun searchMenu(): Int {

    print(
        """
╔═══════════════════════════════════════════════════╗
║                   Employee Menu                   ║
╠═══════════════════════════════════════════════════╣
║   1) Search Note By title                         ║
║   2) Search Note By Category                      ║
║   3) Search Note By  Content                      ║
║                                                   ║
║                                                   ║
║                                                   ║
╚═══════════════════════════════════════════════════╝
║   0) Return to Main Menu                          ║
╚═══════════════════════════════════════════════════╝
            
            
       ==>> """.trimMargin()
    )
    return readLine()!!.toInt()
    // Returning the user input as an integer.
}

    fun searchNoteByTitle() {
        val searchTitle = readNextLine("Enter the title to search by: ")
        val searchResults = noteAPI.searchByTitle(searchTitle)
        if (searchResults.isEmpty()) {
            println("No notes found")
        } else {
            println(searchResults)
        }
    }

  fun searchNoteByCategory()
  {
    val searchCategory = readNextLine("Enter the Category to search by: ")
    val searchResults = noteAPI.searchByCategory(searchCategory)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}
fun searchNoteByContent()
{
    val searchContent = readNextLine("Enter the Content to search by: ")
    val searchResults = noteAPI.searchByContent(searchContent)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}


    fun save() {
        try {
            noteAPI.store()
        } catch (e: Exception) {
            System.err.println("Error writing to file: $e")
        }
    }

    fun load() {
        try {
            noteAPI.load()
        } catch (e: Exception) {
            System.err.println("Error reading from file: $e")
        }
    }

    fun exitApp() {
        logger.info { "exitApp() function invoked" }
        exit(0)
    }
