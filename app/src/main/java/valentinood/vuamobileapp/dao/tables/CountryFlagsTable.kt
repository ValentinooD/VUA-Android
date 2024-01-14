package valentinood.vuamobileapp.dao.tables

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import valentinood.vuamobileapp.dao.DatabaseHelper
import valentinood.vuamobileapp.dao.DatabaseTable
import valentinood.vuamobileapp.model.CountryFlag

class CountryFlagsTable(private val helper: DatabaseHelper) : DatabaseTable {

    override fun getTableName() = "flags"

    override fun getQueryCreateTable() =
        "create table ${getTableName()}( " +
                "${CountryFlag::_id.name} integer primary key autoincrement, " +
                "${CountryFlag::nat.name} text not null unique, " +
                "${CountryFlag::fileHash.name} text not null, " +
                "${CountryFlag::filePath.name} text not null " + ")"

    override fun delete(selection: String?, selectionArgs: Array<String>?)
            = helper.writableDatabase.delete(getTableName(), selection, selectionArgs)

    override fun insert(values: ContentValues?)
            = helper.writableDatabase.insertWithOnConflict(getTableName(), null, values, SQLiteDatabase.CONFLICT_REPLACE)

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
