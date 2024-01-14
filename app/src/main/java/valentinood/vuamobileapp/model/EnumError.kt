package valentinood.vuamobileapp.model

import valentinood.vuamobileapp.R

enum class EnumError(val resTitle: Int, val resDescription: Int) {
    NOT_ONLINE(R.string.not_online, R.string.not_online_description),
    API_ERROR(R.string.api_error, R.string.api_error_descrption),
    INTERNAL(R.string.internal_error, R.string.internal_error_description),

    EASTER_EGG(R.string.easter_egg, R.string.easter_egg_description)
}