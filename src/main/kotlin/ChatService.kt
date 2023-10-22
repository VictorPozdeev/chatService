data class Chat(var messages: MutableList<Message> = mutableListOf())
data class Message(val id: Int, val text: String, var incoming: Boolean = false, var read: Boolean = false)

object ChatService {
    private val chats = mutableMapOf<Int, Chat>()

    class ChatNotFoundException(message: String) : RuntimeException(message)

    fun sendMessage(userId: Int, message: Message) {
        chats.getOrPut(userId) { Chat() }.messages += message
    }

    fun deleteMessage(userId: Int, messageIndex: Int): Message {
        val chat = chats[userId]?: throw ChatNotFoundException("Chats not found")
        return chat.messages.removeAt(messageIndex)
    }

    fun getLastMessages(): List<String> = chats.values.map { it.messages.lastOrNull()?.text?: "No messages" }

    fun getListMessages(userId: Int, startMessageId: Int, offsetMessage: Int): List<Message> {
        val chat = chats[userId]?: throw ChatNotFoundException("Chats not found")
        return chat.messages.asSequence().filter { it.id > startMessageId }.take(offsetMessage).onEach { if (it.incoming) it.read = true }.toList()
    }

    fun getUnreadChatsCount() = chats.values.count { chat -> chat.messages.any { !it.read } }

    fun getChats() = chats.values.toString()

    fun deleteChat(userId: Int) = chats.remove(userId)?: throw ChatNotFoundException("Chat with id: $userId has not deleted")

    fun getMessages(userId: Int, count: Int): List<Message> {
        val chat = chats[userId]?: throw ChatNotFoundException("Chats not found")
        return chat.messages.takeLast(count).onEach { it.read = true }
    }

    fun printChats() {
        println(chats)
    }

    fun clear() {
        chats.clear()
    }
}

fun main() {
    ChatService.sendMessage(1, Message(0, "Hello"))
    ChatService.sendMessage(2, Message(0, "Hi"))
    println(ChatService.getChats())
    ChatService.printChats()
}