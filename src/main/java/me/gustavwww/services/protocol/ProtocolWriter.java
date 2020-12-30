package me.gustavwww.services.protocol;

class ProtocolWriter {

    String writeMessage(Command cmd) {
        StringBuilder sb = new StringBuilder();

        sb.append(cmd.getCmd()).append(":");
        for (String arg : cmd.getArgs()) {
            sb.append(arg).append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length()-1);
        }

        return sb.toString();
    }

}
