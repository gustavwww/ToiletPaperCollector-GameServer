import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SkinTest {

    private static final String HOST = "localhost";
    private static final int PORT = 26000;

    private static TCPClient client1;

    @BeforeAll
    public static void TestConnection() throws InterruptedException {
        client1 = new TCPClient(HOST, PORT);
        new Thread(client1).start();

        Thread.sleep(5000);

        client1.dequeueMessage();

        client1.sendTCP("login:gustavw,gustavw");
        Thread.sleep(2000);
        client1.dequeueMessage();
    }

    @Test
    @Order(2)
    public void TestBuySkin() throws InterruptedException {

        client1.sendTCP("skin:buy,chest");
        Thread.sleep(2000);

        Assertions.assertEquals("error:Insufficient funds.", client1.dequeueMessage());

    }

}
