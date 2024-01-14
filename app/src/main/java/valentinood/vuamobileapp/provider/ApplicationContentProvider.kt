package valentinood.vuamobileapp.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import valentinood.vuamobileapp.dao.DatabaseHelper
import valentinood.vuamobileapp.dao.tables.CountryFlagsTable
import valentinood.vuamobileapp.dao.tables.UserHistoryTable
import valentinood.vuamobileapp.factory.getDatabase
import valentinood.vuamobileapp.model.CountryFlag
import valentinood.vuamobileapp.model.User


private const val AUTHORITY = "valentinood.vuamobileapp.provider"

private const val PATH_FLAGS = "flags"
private const val PATH_HISTORY = "history"

val FLAG_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_FLAGS")
val HISTORY_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_HISTORY")

private const val FLAGS = 1
private const val FLAG_NAT = 2
private const val USERS = 3
private const val USER_ID = 4

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH_FLAGS, FLAGS)
    addURI(AUTHORITY, "$PATH_FLAGS/#", FLAG_NAT)
    addURI(AUTHORITY, PATH_HISTORY, USERS)
    addURI(AUTHORITY, "$PATH_HISTORY/#", USER_ID)
    this
}

class ApplicationContentProvider : ContentProvider() {
    private lateinit var db: DatabaseHelper

    override fun onCreate(): Boolean {
        db = getDatabase(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        when(URI_MATCHER.match(uri)) {
            FLAGS -> return db.getTable(CountryFlagsTable::class).query(projection, selection, selectionArgs, sortOrder)
            USERS -> return db.getTable(UserHistoryTable::class).query(projection, selection, selectionArgs, sortOrder)

            FLAG_NAT -> {
                val id = uri.lastPathSegment

                if (id != null) {
                    return db.getTable(CountryFlagsTable::class).query(
                        projection, "${CountryFlag::_id.name}=?",
                        arrayOf(id), sortOrder
                    )
                }
            }

            USER_ID -> {
                val id = uri.lastPathSegment

                if (id != null) {
                    return db.getTable(UserHistoryTable::class).query(
                        projection, "${User::_id.name}=?",
                        arrayOf(id), sortOrder
                    )
                }
            }
        }
        throw IllegalArgumentException("Wrong URI")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when(URI_MATCHER.match(uri)) {
            FLAGS -> {
                val id = db.getTable(CountryFlagsTable::class).insert(values)
                return ContentUris.withAppendedId(FLAG_PROVIDER_CONTENT_URI, id)
            }
            USERS -> {
                val id =  db.getTable(UserHistoryTable::class).insert(values)
                return ContentUris.withAppendedId(HISTORY_PROVIDER_CONTENT_URI, id)
            }
        }

        throw IllegalArgumentException("Invalid URI")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            FLAGS -> return db.getTable(CountryFlagsTable::class).delete(selection, selectionArgs)
            USERS -> return db.getTable(UserHistoryTable::class).delete(selection, selectionArgs)

            FLAG_NAT -> {
                val id = uri.lastPathSegment

                if (id != null) {
                    return db.getTable(CountryFlagsTable::class).delete(
                        "${CountryFlag::_id.name}=?", arrayOf(id))
                }
            }

            USER_ID -> {
                val id = uri.lastPathSegment

                if (id != null) {
                    return db.getTable(UserHistoryTable::class).delete(
                        "${User::_id.name}=?", arrayOf(id))
                }
            }
        }
        throw IllegalArgumentException("Wrong URI")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)) {
            FLAGS -> return db.getTable(CountryFlagsTable::class).update(values, selection, selectionArgs)
            USERS -> return db.getTable(UserHistoryTable::class).update(values, selection, selectionArgs)

            FLAG_NAT -> {
                val id = uri.lastPathSegment

                if (id != null) {
                    return db.getTable(CountryFlagsTable::class).update(values,
                        "${CountryFlag::_id.name}=?", arrayOf(id))
                }
            }

            USER_ID -> {
                val id = uri.lastPathSegment

                if (id != null) {
                    return db.getTable(UserHistoryTable::class).update(values,
                        "${User::_id.name}=?", arrayOf(id))
                }
            }
        }

        throw IllegalArgumentException("Wrong URI")
    }
}