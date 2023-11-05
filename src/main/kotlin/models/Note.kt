package models


data class Note (var noteTitle: String,
                 var notePriority: Int,
                 var noteCategory: String,
                 var isNoteArchived: Boolean,
                 var noteContent: String, //Add content to the Notes
                 var reminderDays: Int )   // Days to remind before an event.
{
}