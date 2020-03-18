package xclients;

/**
 * A wrapper class around the components needed for a TCP connection.
 */
class HostPortType {

    private String host;
    private int port;

    HostPortType(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
