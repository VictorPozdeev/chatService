import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChatServiceTest {

    private val user1Message1 = Message(0, "Hello")
    private val user1Message2 = Message(1, "Hi!", read = true)

    private val user2Message1 = Message(0, "Buongiorno!")
    private val user2Message2 = Message(1, "no", true)
    private val user2Message3 = Message(2, "yes", true)

    private val user2Message2Incoming = Message(1, "no", true, true)
    private val user2Message3Incoming = Message(2, "yes", true, true)

    private val user3Message1 = Message(0, "Bonjour!", read = true)

    private val service = ChatService

    @Before
    fun clearBeforeTest() {
        service.clear()
    }

    @Test
    fun deleteMessage() {
        service.sendMessage(1, user1Message1)
        service.sendMessage(2, user2Message1)
        service.sendMessage(3, user3Message1)

        val result = service.deleteMessage(3, 0)
        assertEquals(user3Message1, result)
    }

    @Test(expected = ChatService.ChatNotFoundException::class)
    fun deleteMessage_ChatNotFoundException() {
        service.sendMessage(1, user1Message1)
        service.sendMessage(2, user2Message1)
        service.sendMessage(3, user3Message1)

        service.deleteMessage(100, 0)
    }

    @Test
    fun getLastMessages() {
        service.sendMessage(1, user1Message1)
        service.sendMessage(2, user2Message1)
        service.sendMessage(3, user3Message1)

        val result = service.getLastMessages()
        assertEquals(listOf("Hello", "Buongiorno!", "Bonjour!"), result)
    }

    @Test
    fun getListMessages() {
        service.sendMessage(2, user2Message1)
        service.sendMessage(2, user2Message2)
        service.sendMessage(2, user2Message3)

        val result = service.getListMessages(2, 0, 2)
        assertEquals(listOf(user2Message2Incoming, user2Message3Incoming), result)
    }

    @Test(expected = ChatService.ChatNotFoundException::class)
    fun getListMessages_ChatNotFoundException() {
        service.sendMessage(2, user2Message1)
        service.sendMessage(2, user2Message2)
        service.sendMessage(2, user2Message3)

        service.getListMessages(100, 0, 2)
    }

    @Test
    fun getUnreadChatsCount() {
        service.sendMessage(1, user1Message1)
        service.sendMessage(1, user1Message2)
        service.sendMessage(2, user2Message1)
        service.sendMessage(2, user2Message2)
        service.sendMessage(2, user2Message3)
        service.sendMessage(3, user3Message1)
        //service.sendMessage(3, user3Message2)

        val result = service.getUnreadChatsCount()
        assertEquals(2, result)
    }

    @Test
    fun getChats() {
        service.sendMessage(1, user1Message1)
        service.sendMessage(2, user2Message1)
        service.sendMessage(3, user3Message1)

        val list =
            "[Chat(messages=[Message(id=0, text=Hello, incoming=false, read=false)]), Chat(messages=[Message(id=0, text=Buongiorno!, incoming=false, read=false)]), Chat(messages=[Message(id=0, text=Bonjour!, incoming=false, read=true)])]"
        val result = service.getChats()
        assertEquals(list, result)
    }

    @Test
    fun deleteChat() {
        service.sendMessage(1, user1Message1)
        service.sendMessage(1, user1Message2)
        service.sendMessage(2, user2Message1)
        service.sendMessage(2, user2Message2)
        service.sendMessage(2, user2Message3)
        service.sendMessage(3, user3Message1)

        val removeChat = Chat(mutableListOf(user2Message1, user2Message2, user2Message3))

        val result = service.deleteChat(2)
        assertEquals(removeChat, result)
    }

    @Test(expected = ChatService.ChatNotFoundException::class)
    fun deleteChat_ChatNotFoundException() {
        service.sendMessage(1, user1Message1)
        service.sendMessage(1, user1Message2)
        service.sendMessage(2, user2Message1)
        service.sendMessage(2, user2Message2)
        service.sendMessage(2, user2Message3)
        service.sendMessage(3, user3Message1)

        service.deleteChat(100)
    }

    @Test
    fun getMessages() {
        service.sendMessage(2, user2Message1)
        service.sendMessage(2, user2Message2)
        service.sendMessage(2, user2Message3)
        val list = listOf(user2Message2, user2Message3)
        val result = service.getMessages(2, 2)
        assertEquals(list, result)
    }

    @Test(expected = ChatService.ChatNotFoundException::class)
    fun getMessages_ChatNotFoundException() {
        service.sendMessage(2, user2Message1)
        service.sendMessage(2, user2Message2)
        service.sendMessage(2, user2Message3)

        service.getMessages(100, 2)
    }
}