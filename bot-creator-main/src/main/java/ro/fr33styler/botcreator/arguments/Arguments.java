package ro.fr33styler.botcreator.arguments;

import ro.fr33styler.botcreator.bot.protocol.ProtocolVersion;

import java.util.StringJoiner;
import java.util.function.Consumer;

public class Arguments {

    private String host = "127.0.0.1";
    private int port = 25565;

    private int clients = 1;
    private ProtocolVersion version = ProtocolVersion.values()[0];
    private int joinDelay = 1000;
    private int retryDelay = 3000;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getClients() {
        return clients;
    }

    public ProtocolVersion getVersion() {
        return version;
    }

    public int getJoinDelay() {
        return joinDelay;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public static Arguments parse(String[] args) throws IllegalArgumentException {
        if (args.length % 2 != 0) throw new IllegalArgumentException("Invalid number of arguments!");

        Arguments arguments = new Arguments();

        Consumer<String> consumer = null;
        for (String currentArgument : args) {
            if (consumer == null) {
                consumer = handle(arguments, currentArgument);
            } else {
                consumer.accept(currentArgument);
                consumer = null;
            }
        }
        return arguments;
    }

    private static Consumer<String> handle(Arguments arguments, String currentArgument) {
        switch (currentArgument) {
            case "-host":
            case "-h":
                return argument -> arguments.host = argument;
            case "-port":
            case "-p":
                return argument -> {
                    try {
                        arguments.port = Integer.parseInt(argument);
                        if (arguments.port < 0)  throw new IllegalArgumentException("Invalid port number!");
                    } catch (NumberFormatException exception) {
                        throw new IllegalArgumentException("Invalid port number!");
                    }
                };
            case "-clients":
            case "-c":
                return argument -> {
                    try {
                        arguments.clients = Integer.parseInt(argument);
                        if (arguments.clients < 0) throw new IllegalArgumentException("Invalid clients!");
                    } catch (NumberFormatException exception) {
                        throw new IllegalArgumentException("Invalid clients number!");
                    }
                };
            case "-version":
            case "-v":
                return argument -> {
                    ProtocolVersion protocolVersion = ProtocolVersion.getByVersion(argument);
                    if (protocolVersion == null) {
                        StringJoiner joiner = new StringJoiner(", ", "", ".");
                        for (ProtocolVersion version : ProtocolVersion.values()) {
                            joiner.add(version.getVersion());
                        }
                        throw new IllegalArgumentException("Invalid version! Valid versions are: " + joiner);
                    }

                    arguments.version = protocolVersion;
                };
            case "-join-delay":
            case "-jd":
                return argument -> {
                    try {
                        arguments.joinDelay = Integer.parseInt(argument);
                        if (arguments.joinDelay < 0) throw new IllegalArgumentException("Join delay must be non-negative!");
                    } catch (NumberFormatException exception) {
                        throw new IllegalArgumentException("Invalid join delay number!");
                    }
                };
            case "-retry-delay":
            case "-rd":
                return argument -> {
                    try {
                        arguments.retryDelay = Integer.parseInt(argument);
                        if (arguments.retryDelay < 0) throw new IllegalArgumentException("Retry delay must be non-negative!");
                    } catch (NumberFormatException exception) {
                        throw new IllegalArgumentException("Invalid retry delay number!");
                    }
                };
            default: throw new IllegalArgumentException("The argument \"" + currentArgument + "\" is invalid!");
        }
    }

}
