package me.gustavwww.services.protocol;

import java.util.ArrayList;
import java.util.List;

class ProtocolReader {

    Command parseMessage(String msg) {
        String trimmed = msg.trim().toLowerCase();
        StringBuilder sb = new StringBuilder();

        String command = null;
        List<String> argsList = new ArrayList<>();

        // Loops through every character. Sums them up add adds them either as a command or argument
        // depending on following characters, ":" or ",".
        for (char c : trimmed.toCharArray()) {
            if (c == ' ') {
                continue;
            }

            if (c == ':' && command == null) {
                command = sb.toString();
                sb.setLength(0);
                continue;
            }

            if (c == ',') {
                argsList.add(sb.toString());
                sb.setLength(0);
                continue;
            }

            sb.append(c);
        }

        // If command missing arguments and ":", add the remaining characters as command,
        // else add the remaining characters as an argument.
        if (command == null) {
            command = sb.toString();
        } else {
            argsList.add(sb.toString());
        }

        String[] args = new String[argsList.size()];
        for (int i = 0; i < argsList.size(); i++) {
            args[i] = argsList.get(i);
        }

        return new Command(command, args);
    }


}
