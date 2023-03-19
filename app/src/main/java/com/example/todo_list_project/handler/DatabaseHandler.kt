package com.example.todo_list_project.handler

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import android.provider.BaseColumns
import android.util.Log
import com.example.todo_list_project.classes.Task
import java.util.*

object DatabaseHandler {

    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "Task"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_STARTING_DATE = "startingDate"
        const val COLUMN_NAME_ENDING_DATE = "endingDate"
        const val COLUMN_NAME_REMINDER = "reminder"
        const val COLUMN_NAME_IS_DONE = "isDone"
    }

    private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_NAME_TITLE} TEXT," +
                "${FeedEntry.COLUMN_NAME_DESCRIPTION} TEXT," +
                "${FeedEntry.COLUMN_NAME_STARTING_DATE} TEXT," +
                "${FeedEntry.COLUMN_NAME_ENDING_DATE} TEXT," +
                "${FeedEntry.COLUMN_NAME_REMINDER} TEXT," +
                "${FeedEntry.COLUMN_NAME_IS_DONE} INTEGER)"

    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"

    class DbReaderHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        private var format = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        private var formatComplete = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }

        fun addTask(task: Task) {
            // Gets the data repository in write mode
            val db = this.writableDatabase

            // Create a new map of values, where column names are the keys
            val startingDate : String = format.format(task.startingDate)
            val endingDate : String = format.format(task.endingDate)
            val reminder : String = formatComplete.format(task.reminder)

            val values = ContentValues().apply {
                put(BaseColumns._ID, task.id)
                put(FeedEntry.COLUMN_NAME_TITLE, task.title)
                put(FeedEntry.COLUMN_NAME_DESCRIPTION, task.description)
                put(FeedEntry.COLUMN_NAME_STARTING_DATE, startingDate)
                put(FeedEntry.COLUMN_NAME_ENDING_DATE, endingDate)
                put(FeedEntry.COLUMN_NAME_REMINDER, reminder)
                put(FeedEntry.COLUMN_NAME_IS_DONE, task.isDone)
            }

            // Insert the new row, returning the primary key value of the new row
            db?.insert(FeedEntry.TABLE_NAME, null, values)
        }

        fun getAllTasks() : MutableList<Task> {
            val db = this.readableDatabase

            val projection = arrayOf(BaseColumns._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_DESCRIPTION,
                FeedEntry.COLUMN_NAME_STARTING_DATE,
                FeedEntry.COLUMN_NAME_ENDING_DATE,
                FeedEntry.COLUMN_NAME_REMINDER,
                FeedEntry.COLUMN_NAME_IS_DONE
            )

            val sortOrder = "${BaseColumns._ID} DESC"

            val cursor = db.query(
                FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
            )

            val taskList = mutableListOf<Task>()
            with(cursor) {
                while (moveToNext()) {
                    val taskId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val taskTitle = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE))
                    val taskDescription = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_DESCRIPTION))
                    val taskStartingDate = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_STARTING_DATE))
                    val taskEndingDate = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENDING_DATE))
                    val taskReminder = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_REMINDER))
                    val taskIsDone = getInt(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_IS_DONE))

                    taskList.add(Task(taskId.toInt(), taskTitle, taskDescription, format.parse(taskStartingDate), format.parse(taskEndingDate), formatComplete.parse(taskReminder), taskIsDone ))
                }
            }
            cursor.close()
            return taskList
        }

        fun getAllTasksNotDone() : MutableList<Task> {
            val db = this.readableDatabase

            val projection = arrayOf(BaseColumns._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_DESCRIPTION,
                FeedEntry.COLUMN_NAME_STARTING_DATE,
                FeedEntry.COLUMN_NAME_ENDING_DATE,
                FeedEntry.COLUMN_NAME_REMINDER,
                FeedEntry.COLUMN_NAME_IS_DONE
            )

            val selection = "${FeedEntry.COLUMN_NAME_IS_DONE} = ?"
            val selectionArgs = arrayOf("-1")

            val sortOrder = "${BaseColumns._ID} DESC"

            val cursor = db.query(
                FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
            )

            val taskList = mutableListOf<Task>()
            with(cursor) {
                while (moveToNext()) {
                    val taskId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val taskTitle = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE))
                    val taskDescription = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_DESCRIPTION))
                    val taskStartingDate = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_STARTING_DATE))
                    val taskEndingDate = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENDING_DATE))
                    val taskReminder = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_REMINDER))
                    val taskIsDone = getInt(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_IS_DONE))

                    taskList.add(Task(taskId.toInt(), taskTitle, taskDescription, format.parse(taskStartingDate), format.parse(taskEndingDate), formatComplete.parse(taskReminder), taskIsDone ))
                }
            }
            cursor.close()
            return taskList
        }

        fun getAllTasksDone() : MutableList<Task> {
            val db = this.readableDatabase

            val projection = arrayOf(BaseColumns._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_DESCRIPTION,
                FeedEntry.COLUMN_NAME_STARTING_DATE,
                FeedEntry.COLUMN_NAME_ENDING_DATE,
                FeedEntry.COLUMN_NAME_REMINDER,
                FeedEntry.COLUMN_NAME_IS_DONE
            )

            val selection = "${FeedEntry.COLUMN_NAME_IS_DONE} = ?"
            val selectionArgs = arrayOf("1")

            val sortOrder = "${BaseColumns._ID} DESC"

            val cursor = db.query(
                FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
            )

            val taskList = mutableListOf<Task>()
            with(cursor) {
                while (moveToNext()) {
                    val taskId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val taskTitle = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE))
                    val taskDescription = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_DESCRIPTION))
                    val taskStartingDate = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_STARTING_DATE))
                    val taskEndingDate = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENDING_DATE))
                    val taskReminder = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_REMINDER))
                    val taskIsDone = getInt(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_IS_DONE))

                    taskList.add(Task(taskId.toInt(), taskTitle, taskDescription, format.parse(taskStartingDate), format.parse(taskEndingDate), formatComplete.parse(taskReminder), taskIsDone ))
                }
            }
            cursor.close()
            return taskList
        }

        fun getTaskNotDone(taskId: Int): Task {
            val db = this.readableDatabase

            val projection = arrayOf(
                BaseColumns._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_DESCRIPTION,
                FeedEntry.COLUMN_NAME_STARTING_DATE,
                FeedEntry.COLUMN_NAME_ENDING_DATE,
                FeedEntry.COLUMN_NAME_REMINDER,
                FeedEntry.COLUMN_NAME_IS_DONE
            )

            val selection = "${BaseColumns._ID} = ? AND ${FeedEntry.COLUMN_NAME_IS_DONE} = ?"
            val selectionArgs = arrayOf(taskId.toString(), "-1")

            val cursor = db.query(
                FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
            )

            var task = Task(0, "", "", Date(), Date(), Date(), 0)
            with(cursor) {
                if (moveToFirst()) {
                    val taskTitle = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE))
                    val taskDescription = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_DESCRIPTION))
                    val taskStartingDate = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_STARTING_DATE))
                    val taskEndingDate = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENDING_DATE))
                    val taskReminder = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_REMINDER))
                    val taskIsDone = getInt(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_IS_DONE))

                    task = Task(
                        taskId,
                        taskTitle,
                        taskDescription,
                        format.parse(taskStartingDate),
                        format.parse(taskEndingDate),
                        formatComplete.parse(taskReminder),
                        taskIsDone
                    )
                }
            }
            cursor.close()
            return task
        }

        fun getTaskDone(taskId: Int): Task {
            val db = this.readableDatabase

            val projection = arrayOf(
                BaseColumns._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_DESCRIPTION,
                FeedEntry.COLUMN_NAME_STARTING_DATE,
                FeedEntry.COLUMN_NAME_ENDING_DATE,
                FeedEntry.COLUMN_NAME_REMINDER,
                FeedEntry.COLUMN_NAME_IS_DONE
            )

            val selection = "${BaseColumns._ID} = ? AND ${FeedEntry.COLUMN_NAME_IS_DONE} = ?"
            val selectionArgs = arrayOf(taskId.toString(), "1")

            val cursor = db.query(
                FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
            )

            var task = Task(0, "", "", Date(), Date(), Date(), 0)
            with(cursor) {
                if (moveToFirst()) {
                    val taskTitle =
                        getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE))
                    val taskDescription =
                        getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_DESCRIPTION))
                    val taskStartingDate =
                        getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_STARTING_DATE))
                    val taskEndingDate =
                        getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENDING_DATE))
                    val taskReminder =
                        getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_REMINDER))
                    val taskIsDone =
                        getInt(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_IS_DONE))

                    task = Task(
                        taskId,
                        taskTitle,
                        taskDescription,
                        format.parse(taskStartingDate),
                        format.parse(taskEndingDate),
                        formatComplete.parse(taskReminder),
                        taskIsDone
                    )
                }
            }
            cursor.close()
            return task
        }

        fun deleteTask(id : Int) {
            val db = this.writableDatabase

            val selection = "${BaseColumns._ID} = ?"
            val selectionArgs = arrayOf(id.toString())
            // Issue SQL statement.
            db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs)
        }

        fun updateTaskToDone(id: Int) {
            val db = this.writableDatabase

            // New value for one column
            val values = ContentValues().apply {
                put(FeedEntry.COLUMN_NAME_IS_DONE, 1)
            }

            // Which row to update, based on the ID
            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArgs = arrayOf(id.toString())

            db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
            )
        }

        fun updateTaskToNotDone(id: Int) {
            val db = this.writableDatabase

            // New value for one column
            val values = ContentValues().apply {
                put(FeedEntry.COLUMN_NAME_IS_DONE, -1)
            }
            Log.d("update", values.toString())

            // Which row to update, based on the ID
            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArgs = arrayOf(id.toString())

            db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
            )
        }

        companion object {
            // If you change the database schema, you must increment the database version.
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "Task.db"
        }
    }
}