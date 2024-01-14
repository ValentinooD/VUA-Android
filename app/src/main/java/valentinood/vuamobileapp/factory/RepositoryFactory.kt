package valentinood.vuamobileapp.factory

import android.content.Context
import valentinood.vuamobileapp.dao.DatabaseHelper

fun getDatabase(context: Context?) = DatabaseHelper(context)

