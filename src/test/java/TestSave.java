import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSave {

    private static final String HOST = "localhost";
    private static final int PORT = 26000;

    private static TCPClient client1;

    @BeforeAll
    public static void TestConnection() throws InterruptedException {
        client1 = new TCPClient(HOST, PORT);
        new Thread(client1).start();

        Thread.sleep(5000);

        client1.dequeueMessage();
    }

    @Test
    @Order(2)
    public void TestDisconnect() throws InterruptedException {

        client1.sendTCP("login:555,gurk");
        Thread.sleep(2000);

        Assertions.assertEquals("logged:gurk,0,0,0", client1.dequeueMessage());

        client1.closeConnection();
        Thread.sleep(5000);
    }

    @Test
    @Order(3)
    public void TestRelog() throws InterruptedException {

        TCPClient client1Relog = new TCPClient(HOST, PORT);
        new Thread(client1Relog).start();
        Thread.sleep(5000);
        client1Relog.dequeueMessage();

        client1Relog.sendTCP("login:555");
        Thread.sleep(2000);
        Assertions.assertEquals("logged:gurk,0,0,0", client1Relog.dequeueMessage());

    }

}
