import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.ServerProtocolFactory;
import org.junit.Assert;
import org.junit.Test;

public class ProtocolTest {

    @Test
    public void TestMessageParser() {
        String msg1 = "login:6dsf6dsgf6s7df,bananen";
        String msg2 = "login:23d32d32e";
        String msg3 = "login";

        Command cmd1 = ServerProtocolFactory.getServerProtocol().parseMessage(msg1);
        Command cmd2 = ServerProtocolFactory.getServerProtocol().parseMessage(msg2);
        Command cmd3 = ServerProtocolFactory.getServerProtocol().parseMessage(msg3);

        Assert.assertEquals("login", cmd1.getCmd());
        String[] arr1 = {"6dsf6dsgf6s7df", "bananen"};
        Assert.assertArrayEquals(arr1, cmd1.getArgs());

        Assert.assertEquals("login", cmd2.getCmd());
        String[] arr2 = {"23d32d32e"};
        Assert.assertArrayEquals(arr2, cmd2.getArgs());

        Assert.assertEquals("login", cmd3.getCmd());
        String[] arr3 = new String[0];
        Assert.assertArrayEquals(arr3, cmd3.getArgs());
    }

    @Test
    public void TestMessageWriter() {
        String msg = ServerProtocolFactory.getServerProtocol().writeMessage(new Command("login", "68astbd7sad", "nickname"));
        Assert.assertEquals("login:68astbd7sad,nickname", msg);
    }

}
