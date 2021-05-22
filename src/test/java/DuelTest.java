import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DuelTest {

    private static final String HOST = "localhost";
    private static final int PORT = 26000;

    private static TCPClient client1;
    private static TCPClient client2;

    @BeforeAll
    public static void Connect() throws InterruptedException {

        client1 = new TCPClient(HOST, PORT);
        new Thread(client1).start();

        client2 = new TCPClient(HOST, PORT);
        new Thread(client2).start();

        Thread.sleep(5000);

        client1.dequeueMessage();
        client2.dequeueMessage();

        client1.sendTCP("login:123,gustavw");
        client2.sendTCP("login:321,oskarw");

        Thread.sleep(2000);

        client1.dequeueMessage();
        client2.dequeueMessage();
    }

    @Test
    @Order(2)
    public void TestDuelRequest() throws InterruptedException {

        client1.sendTCP("duel:request,oskarw");
        Thread.sleep(2000);

        Assertions.assertEquals("duel:request,gustavw", client2.dequeueMessage());
        client2.sendTCP("duel:response,accept");
        Thread.sleep(2000);

        Assertions.assertEquals("duel:response,finished", client1.dequeueMessage());
        Assertions.assertEquals("duel:response,finished", client2.dequeueMessage());

    }

    @Test
    @Order(3)
    public void TestDuelReady() throws InterruptedException {

        client1.sendTCP("duel:ready");
        Thread.sleep(2000);

        Assertions.assertEquals("duel:ready,gustavw", client2.dequeueMessage());
        Assertions.assertEquals("duel:ready,gustavw", client1.dequeueMessage());

        client2.sendTCP("duel:ready");
        Thread.sleep(2000);

        Assertions.assertEquals("duel:ready,oskarw", client1.dequeueMessage());
        Assertions.assertEquals("duel:ready,oskarw", client2.dequeueMessage());

    }

    @Test
    @Order(4)
    public void TestTimerCounters() throws InterruptedException {
        Thread.sleep(5000);

        Assertions.assertEquals("duel:starttimer,3", client1.dequeueMessage());
        Assertions.assertEquals("duel:starttimer,2", client1.dequeueMessage());
        Assertions.assertEquals("duel:starttimer,1", client1.dequeueMessage());

        Assertions.assertEquals("duel:starttimer,3", client2.dequeueMessage());
        Assertions.assertEquals("duel:starttimer,2", client2.dequeueMessage());
        Assertions.assertEquals("duel:starttimer,1", client2.dequeueMessage());

        Assertions.assertEquals("duel:started", client1.dequeueMessage());
        Assertions.assertEquals("duel:started", client2.dequeueMessage());

        Assertions.assertEquals("duel:gametimer,15", client1.dequeueMessage());
        Assertions.assertEquals("duel:gametimer,15", client2.dequeueMessage());
        Assertions.assertEquals("duel:gametimer,14", client1.dequeueMessage());
        Assertions.assertEquals("duel:gametimer,14", client2.dequeueMessage());
        Assertions.assertEquals("duel:gametimer,13", client1.dequeueMessage());
        Assertions.assertEquals("duel:gametimer,13", client2.dequeueMessage());
        Assertions.assertEquals("duel:gametimer,12", client1.dequeueMessage());
        Assertions.assertEquals("duel:gametimer,12", client2.dequeueMessage());
    }

    @Test
    @Order(5)
    public void TestCount() throws InterruptedException {
        Thread.sleep(500);

        client1.sendTCP("duel:count");
        Thread.sleep(1000);

        // Remove gametimer,11 message
        client1.dequeueMessage();
        client2.dequeueMessage();

        Assertions.assertEquals("duel:count,gustavw,1", client2.dequeueMessage());
        Assertions.assertEquals("duel:count,gustavw,1", client1.dequeueMessage());

    }

    @Test
    @Order(6)
    public void TestGameEnded() throws InterruptedException {
        Thread.sleep(15000);

        // Remove gametimer messages
        for (int i = 0; i < 10; i++) {
            client1.dequeueMessage();
            client2.dequeueMessage();
        }

        Assertions.assertEquals("duel:ended,gustavw", client1.dequeueMessage());
        Assertions.assertEquals("duel:ended,gustavw", client2.dequeueMessage());
    }

}
