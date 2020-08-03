package me.gustavwww.server;

public class ServerProtocol {

    public static String parseId(String input) throws ProtocolException {

        if (input.startsWith("id:")) {

            return input.substring(3).strip();
        }

        throw new ProtocolException(ProtocolError.INVALID_ID);
    }

    public static String parseNickname(String input) throws ProtocolException {


        if (input.startsWith("nickname:")) {

            String nick = input.substring(9).trim();
            if (nick.length() >= 3 && nick.length() <= 15) {
                return nick;
            }

        }

        throw new ProtocolException(ProtocolError.INVALID_NICKNAME);
    }

    public static boolean parseCount(String input) {
        return input.equalsIgnoreCase("count");
    }

    public static String writeError(ProtocolError error) {
        return "error:" + error.msg;
    }

}
