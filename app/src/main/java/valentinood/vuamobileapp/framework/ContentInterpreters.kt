package valentinood.vuamobileapp.framework

import android.content.Context
import valentinood.vuamobileapp.model.CountryFlag
import valentinood.vuamobileapp.model.User
import valentinood.vuamobileapp.provider.FLAG_PROVIDER_CONTENT_URI
import valentinood.vuamobileapp.provider.HISTORY_PROVIDER_CONTENT_URI

fun Context.getCountryFlag(countryCode: String): CountryFlag? {
    val cursor = contentResolver.query(
        FLAG_PROVIDER_CONTENT_URI,
        arrayOf(CountryFlag::_id.name, CountryFlag::nat.name, CountryFlag::fileHash.name, CountryFlag::filePath.name),
        "${CountryFlag::nat.name}=?",
        arrayOf(countryCode),
        null
    )

    var value: CountryFlag? = null
    if (cursor != null && cursor.moveToFirst()) {
        value = CountryFlag(
            cursor.getLong(cursor.getColumnIndexOrThrow(CountryFlag::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(CountryFlag::nat.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(CountryFlag::fileHash.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(CountryFlag::filePath.name))
        )
        cursor.close()
    }

    return value
}

fun Context.getUser(id: Long): User? {
    val cursor = contentResolver.query(
        HISTORY_PROVIDER_CONTENT_URI,
        arrayOf(User::_id.name, User::fullName.name, User::email.name, User::country.name, User::nationality.name, User::picturePath.name, User::latitude.name, User::longitude.name),
        "${User::_id.name}=?",
        arrayOf(id.toString()),
        null)

    var value: User? = null
    if (cursor?.moveToFirst() == true) {
        value = User(
            cursor.getLong(cursor.getColumnIndexOrThrow(User::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::fullName.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::email.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::country.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::nationality.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::picturePath.name)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(User::latitude.name)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(User::longitude.name))
        )
        cursor.close()
    }
    return value
}

fun Context.getUsers(): MutableList<User> {
    val cursor = contentResolver.query(
        HISTORY_PROVIDER_CONTENT_URI,
        arrayOf(User::_id.name, User::fullName.name, User::email.name, User::country.name, User::nationality.name, User::picturePath.name, User::latitude.name, User::longitude.name),
        null,
        null,
        null)
    val list = mutableListOf<User>()

    if (cursor == null) return mutableListOf()

    while (cursor.moveToNext()) {
        list.add(User(
            cursor.getLong(cursor.getColumnIndexOrThrow(User::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::fullName.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::email.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::country.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::nationality.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(User::picturePath.name)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(User::latitude.name)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(User::longitude.name))
        ))
    }
    cursor.close()

    return list
}

