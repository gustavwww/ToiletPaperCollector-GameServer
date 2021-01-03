package me.gustavwww.services.protocol;

public class Command {

    private final String cmd;
    private final String[] args;

    public Command(String cmd, String... args) {
        this.cmd = cmd;
        this.args = args;
    }

    public String getCmd() {
        return cmd;
    }

    public String[] getArgs() {
        return args;
    }

}
