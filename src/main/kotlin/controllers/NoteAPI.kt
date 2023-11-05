package controllers

import models.Note
import persistence.Serializer
import utils.Utilities.isValidListIndex

class NoteAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType

    private var notes = ArrayList<Note>()

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            foundNote.noteContent = note.noteContent
            foundNote.reminderDays = note.reminderDays
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    // Archives the note at the given index if it exists and is not already archived.
    fun archiveNote(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) { // Check if the index is valid.
            val noteToArchive = notes[indexToArchive] // Get the note to archive.
            if (!noteToArchive.isNoteArchived) { // Check if the note is not already archived.
                noteToArchive.isNoteArchived = true // Archive the note.
                return true // Return true to indicate success.
            }
        }
        return false // Return false if the index is invalid or the note is already archived.
    }

    // Returns a string representation of all notes if any, otherwise a message stating none are stored.
    fun listAllNotes(): String =
        if (notes.isEmpty()) "No notes stored" // Check for empty notes list.
        else formatListString(notes) // Format the list of all notes.

    // Returns a string representation of all active (non-archived) notes if any, otherwise a message.
    fun listActiveNotes(): String =
        if (numberOfActiveNotes() == 0) "No active notes stored" // Check if there are no active notes.
        else formatListString(notes.filter { note -> !note.isNoteArchived }) // Format the list of active notes.

    // Returns a string representation of all archived notes if any, otherwise a message.
    fun listArchivedNotes(): String =
        if (numberOfArchivedNotes() == 0) "No archived notes stored" // Check if there are no archived notes.
        else formatListString(notes.filter { note -> note.isNoteArchived }) // Format the list of archived notes.

    // Lists notes by the selected priority, indicating the number of notes and the notes themselves.
    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty()) "No notes stored" // Check for empty notes list.
        else {
            val listOfNotes = formatListString(notes.filter { note -> note.notePriority == priority })
            if (listOfNotes.isEmpty()) "No notes with priority: $priority" // Check if no notes match the priority.
            else "${numberOfNotesByPriority(priority)} notes with priority $priority:\n$listOfNotes"
        }

    // Lists notes by the selected reminder day, indicating the number of notes and the notes themselves.
    fun listNotesBySelectedReminderDay(reminder: Int): String =
        if (notes.isEmpty()) "No notes stored" // Check for empty notes list.
        else {
            val listOfNotes = formatListString(notes.filter { note -> note.reminderDays == reminder })
            if (listOfNotes.isEmpty()) "No notes with reminder Days: $reminder" // Check if no notes match the reminder day.
            else "${numberOfNotesByReminderDay(reminder)} notes with reminder Days $reminder:\n$listOfNotes"
        }

    // Counts the total number of notes stored.
    fun numberOfNotes(): Int = notes.size

    // Counts the number of active notes (non-archived).
    fun numberOfActiveNotes(): Int = notes.count { note -> !note.isNoteArchived }

    // Counts the number of archived notes.
    fun numberOfArchivedNotes(): Int = notes.count { note -> note.isNoteArchived }

    // Counts the number of notes with the specified priority.
    fun numberOfNotesByPriority(priority: Int): Int = notes.count { note -> note.notePriority == priority }

    // Counts the number of notes with the specified number of reminder days.
    fun numberOfNotesByReminderDay(reminder: Int): Int = notes.count { note -> note.reminderDays == reminder }

    // Filters and formats a list of notes whose titles contain the search string, case-insensitive.
    fun searchByTitle(searchString: String) =
        formatListString(notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })

    // Filters and formats a list of notes whose categories contain the search string, case-insensitive.
    fun searchByCategory(searchString: String) =
        formatListString(notes.filter { note -> note.noteCategory.contains(searchString, ignoreCase = true) })

    // Filters and formats a list of notes whose content contains the search string, case-insensitive.
    fun searchByContent(searchString: String) =
        formatListString(notes.filter { note -> note.noteContent.contains(searchString, ignoreCase = true) })

    // Finds and returns a note at the specified index if the index is valid.
    fun findNote(index: Int): Note? {
        return if (isValidIndex(index)) {
            notes[index]
        } else null
    }

    // Validates if the given index is within the range of the notes list.
    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, notes)
    }

// Loads notes from a persistent storage using the serializer.

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

    private fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }

}