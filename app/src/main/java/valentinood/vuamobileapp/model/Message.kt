package valentinood.vuamobileapp.model

data class Message(
    val to: String,
    val from: String,
    val subject: String,
    val html: String,
    val text: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (to != other.to) return false
        if (from != other.from) return false
        if (subject != other.subject) return false
        if (html != other.html) return false
        return text == other.text
    }

    override fun hashCode(): Int {
        var result = to.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + subject.hashCode()
        result = 31 * result + html.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}
