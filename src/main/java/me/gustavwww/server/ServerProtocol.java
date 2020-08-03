package me.gustavwww.server;

public class ServerProtocol {

    public synchronized static String parseId(String input) throws ProtocolException {

        if (input != null) {
            if (input.startsWith("id:")) {

                return input.substring(3).strip();
            }
        }

        throw new ProtocolException(ProtocolError.INVALID_ID);
    }

    public synchronized static String parseNickname(String input) throws ProtocolException {

        if (input != null) {
            if (input.startsWith("nickname:")) {
                String nick = input.substring(9).trim();
                if (nick.length() >= 3 && nick.length() <= 15) {
                    return nick;
                }
            }
        }

        throw new ProtocolException(ProtocolError.INVALID_NICKNAME);
    }

    public synchronized static boolean parseCount(String input) {
        return input.equalsIgnoreCase("count");
    }

    public synchronized static boolean parseCountRequest(String input) {
        return input.equalsIgnoreCase("want:count");
    }

    public synchronized static String writeError(ProtocolError error) {
        return "error:" + error.msg;
    }

    public synchronized static String writeWant(String input) {
        return "want:" + input;
    }

}
