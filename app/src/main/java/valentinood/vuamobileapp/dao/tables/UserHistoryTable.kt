package valentinood.vuamobileapp.dao.tables

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import valentinood.vuamobileapp.dao.DatabaseHelper
import valentinood.vuamobileapp.dao.DatabaseTable
import valentinood.vuamobileapp.model.User

class UserHistoryTable(private val helper: DatabaseHelper) : DatabaseTable {

    override fun getTableName() = "history"

    override fun getQueryCreateTable() =
        "create table ${getTableName()}( " +
                "${User::_id.name} integer primary key autoincrement, " +
                "${User::fullName.name} text not null, " +
                "${User::email.name} text not null, " +
                "${User::nationality.name} text not null, " +
                "${User::country.name} text not null, " +
                "${User::latitude.name} real null, " +
                "${User::longitude.name} real null, " +
                "${User::picturePath.name} int not null" + ")"

    override fun delete(selection: String?, selectionArgs: Array<String>?)
            = helper.writableDatabase.delete(getTableName(), selection, selectionArgs)

    override fun insert(values: ContentValues?)
            = helper.writableDatabase.insert(getTableName(), null, values)

    override fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor = helper.readableDatabase.query(
        getTableName(),
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun update(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ) = helper.writableDatabase.update(
        getTableName(),
        values,
        selection,
        selectionArgs
    )
}