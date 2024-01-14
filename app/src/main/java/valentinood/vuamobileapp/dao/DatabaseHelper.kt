package valentinood.vuamobileapp.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import valentinood.vuamobileapp.dao.tables.CountryFlagsTable
import valentinood.vuamobileapp.dao.tables.UserHistoryTable
import java.lang.NullPointerException
import kotlin.reflect.KClass


const val DB_NAME = "random_user.db"
const val DB_VERSION = 9
class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private val tables = mutableMapOf<KClass<out DatabaseTable>, DatabaseTable>()

    init {
        add(CountryFlagsTable(this))
        add(UserHistoryTable(this))
    }

    fun <T : DatabaseTable> getTable(clazz: KClass<out T>): T {
        try {
            return (tables[clazz]!!) as T
        } catch (e: NullPointerException) {
            Log.e("DatabaseHelper", "${clazz.qualifiedName} is not registered in tables")
            throw e
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        for (table in tables.values) {
            db?.execSQL(table.getQueryCreateTable())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int) {
        for (table in tables.values) {
            db?.execSQL("DROP TABLE IF EXISTS " + table.getTableName())
        }

        onCreate(db)
    }

    private fun add(table: DatabaseTable) {
        tables[table::class] = table
    }
}