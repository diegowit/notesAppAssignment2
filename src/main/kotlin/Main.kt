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



// Function to add a new note to the system.
fun addNote() {
    // Prompt the user to enter a title for the note and store it.
    val noteTitle = readNextLine("Enter a title for the note: ")
    // Prompt the user to enter a priority for the note, ensuring it's a valid priority.
    val notePriority = readValidPriority("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    // Prompt the user to enter a category for the note, showing available categories.
    val noteCategory = readValidCategory("Enter a category for the note from ${CategoryUtility.categories}: ")
    // Prompt the user to enter the content of the note.
    val noteContent = readNextLine("Enter The notes Content")
    // Prompt the user to enter the number of reminder days before the event.
    val reminderDays = readValidPriority("Enter the number of days to remind before the Event (Maximum 3 days)")
    // Try to add the new note to the system and store the result (success or failure).
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false, noteContent, reminderDays))

    // Notify the user of the result of the add operation.
    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

// Function to list notes based on a selected filtering option.
fun listNotes() {
    // Check if there are any notes stored in the system.
    if (noteAPI.numberOfNotes() > 0) {
        // Prompt the user to choose an option for listing notes.
        val option = readNextInt(
            """
                  > ----------------------------------
                  > |   1) View ALL notes             |
                  > |   2) View ACTIVE notes          |
                  > |   3) View ARCHIVED notes        |
                  > |   4) priority listing Menu      |
                  > |  5) reminder Days listing Menu  |
                  > ----------------------------------
                  > ==>> """.trimMargin(">"))

        // Execute different actions based on the user's choice.
        when (option) {
            1 -> listAllNotes()
            2 -> listActiveNotes()
            3 -> listArchivedNotes()
            4 -> runPriority()
            5 -> runReminder()
            // Handle unexpected option entries.
            else -> println("Invalid option entered: $option")
        }
    } else {
        // Notify the user that there are no notes to list.
        println("Option Invalid - No notes stored")
    }
}

// Function to display all notes in the system.
fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

// Function to display all active (not archived) notes.
fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}

// Function to display all archived notes.
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
║                   Priority Menu                   ║
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

// Function to list all notes with priority level 1.
fun listNoteByPriority1() {
    // Calls the API function that lists notes by the given priority and prints the result.
    println(noteAPI.listNotesBySelectedPriority(1))
}

// Function to list all notes with priority level 2.
fun listNoteByPriority2() {
    // Calls the API function that lists notes by the given priority and prints the result.
    println(noteAPI.listNotesBySelectedPriority(2))
}

// Function to list all notes with priority level 3.
fun listNoteByPriority3() {
    // Calls the API function that lists notes by the given priority and prints the result.
    println(noteAPI.listNotesBySelectedPriority(3))
}

// Function to list all notes with priority level 4.
fun listNoteByPriority4() {
    // Calls the API function that lists notes by the given priority and prints the result.
    println(noteAPI.listNotesBySelectedPriority(4))
}

// Function to list all notes with priority level 5.
fun listNoteByPriority5() {
    // Calls the API function that lists notes by the given priority and prints the result.
    println(noteAPI.listNotesBySelectedPriority(5))
}



/**
 *
 *  List Notes by Reminder days
 *
 */
fun runReminder() {

    do {
        when (val option = runReminderMenu()) {
            1 ->  listNoteByReminder1()
            2 ->  listNoteByReminder2()
            3 ->  listNoteByReminder3()


            0 -> return // Return to main menu
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun runReminderMenu(): Int {

    print(
        """
╔═══════════════════════════════════════════════════╗
║                   Reminder Menu                   ║
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

fun listNoteByReminder1() {
    println(noteAPI.listNotesBySelectedReminderDay(1))

}

fun listNoteByReminder2() {
    println(noteAPI.listNotesBySelectedReminderDay(2))

}

fun listNoteByReminder3() {
    println(noteAPI.listNotesBySelectedReminderDay(3))

}



/****************************************/



// Function to update an existing note.
fun updateNote() {
    // First, list all notes to the user.
    listNotes()
    // Check if there are any notes to update.
    if (noteAPI.numberOfNotes() > 0) {
        // Prompt the user for the index of the note they wish to update.
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        // Validate the index.
        if (noteAPI.isValidIndex(indexToUpdate)) {
            // Collect new note information from the user.
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readValidPriority("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readValidCategory("Enter a category for the note from ${CategoryUtility.categories}: ")
            val noteContent = readNextLine("Enter The notes Content")
            val reminderDays = readValidPriority("Enter the number of days to remind before the Event (Maximum 3 days)")
            // Attempt to update the note at the given index with the new information.
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

// Function to delete a note.
fun deleteNote() {
    // First, list all notes to the user.
    listNotes()
    // Check if there are any notes to delete.
    if (noteAPI.numberOfNotes() > 0) {
        // Prompt the user for the index of the note they wish to delete.
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        // Attempt to delete the note at the specified index.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

// Function to archive a note.
fun archiveNote() {
    // First, list all active (non-archived) notes to the user.
    listActiveNotes()
    // Check if there are any active notes to archive.
    if (noteAPI.numberOfActiveNotes() > 0) {
        // Prompt the user for the index of the note they wish to archive.
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        // Attempt to archive the note at the given index.
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
║                   Search   Menu                   ║
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

// Function to search for notes by their title.
fun searchNoteByTitle() {
    // Prompt the user to enter a title to search for.
    val searchTitle = readNextLine("Enter the title to search by: ")
    // Perform the search using the noteAPI's searchByTitle method.
    val searchResults = noteAPI.searchByTitle(searchTitle)
    // Check if the search results are empty.
    if (searchResults.isEmpty()) {
        // Notify the user that no notes were found.
        println("No notes found")
    } else {
        // Print out the search results.
        println(searchResults)
    }
}

// Function to search for notes by their category.
fun searchNoteByCategory() {
    // Prompt the user to enter a category to search for.
    val searchCategory = readNextLine("Enter the Category to search by: ")
    // Perform the search using the noteAPI's searchByCategory method.
    val searchResults = noteAPI.searchByCategory(searchCategory)
    // Check if the search results are empty.
    if (searchResults.isEmpty()) {
        // Notify the user that no notes were found.
        println("No notes found")
    } else {
        // Print out the search results.
        println(searchResults)
    }
}

// Function to search for notes by their content.
fun searchNoteByContent() {
    // Prompt the user to enter content keywords to search for.
    val searchContent = readNextLine("Enter the Content to search by: ")
    // Perform the search using the noteAPI's searchByContent method.
    val searchResults = noteAPI.searchByContent(searchContent)
    // Check if the search results are empty.
    if (searchResults.isEmpty()) {
        // Notify the user that no notes were found.
        println("No notes found")
    } else {
        // Print out the search results.
        println(searchResults)
    }
}

// Function to save notes to a file or database.
fun save() {
    try {
        // Attempt to save the notes using the noteAPI's store method.
        noteAPI.store()
    } catch (e: Exception) {
        // If an error occurs, print an error message with the exception details.
        System.err.println("Error writing to file: $e")
    }
}

// Function to load notes from a file or database.
fun load() {
    try {
        // Attempt to load the notes using the noteAPI's load method.
        noteAPI.load()
    } catch (e: Exception) {
        // If an error occurs, print an error message with the exception details.
        System.err.println("Error reading from file: $e")
    }
}

// Function to exit the application.
fun exitApp() {
    // Log the exit action (assumes a logging framework is being used).
    logger.info { "exitApp() function invoked" }
    // Terminate the program with status code 0.
    exit(0)
}