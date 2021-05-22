import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientTest {

    private static final String HOST = "localhost";
    private static final int PORT = 26000;

    private static TCPClient client1;
    private static TCPClient client2;
    private static TCPClient client3;

    @BeforeAll
    public static void TestConnection() throws InterruptedException {
        client1 = new TCPClient(HOST, PORT);
        new Thread(client1).start();

        client2 = new TCPClient(HOST, PORT);
        new Thread(client2).start();

        client3 = new TCPClient(HOST, PORT);
        new Thread(client3).start();

        Thread.sleep(5000);

        client1.dequeueMessage();
        client2.dequeueMessage();
        client3.dequeueMessage();
    }

    @Test
    @Order(2)
    public void TestLogin() throws InterruptedException {
        client1.sendTCP("login");
        Thread.sleep(2000);
        Assertions.assertEquals("error:Invalid arguments.", client1.dequeueMessage());


        client1.sendTCP("login:qwefdsa32");
        Thread.sleep(2000);
        Assertions.assertEquals("error:User could not be found.", client1.dequeueMessage());


        client1.sendTCP("login:abcd,gustav");
        Thread.sleep(2000);
        Assertions.assertEquals("logged:gustav,0,0,0", client1.dequeueMessage());
    }

    @Test
    @Order(3)
    public void TestCount() throws InterruptedException {
        client2.sendTCP("login:bcda,oskar");
        Thread.sleep(2000);
        Assertions.assertEquals("logged:oskar,0,0,0", client2.dequeueMessage());

        for (int i = 0; i < 10; i++) {
            client2.sendTCP("count");
        }
    }

}
