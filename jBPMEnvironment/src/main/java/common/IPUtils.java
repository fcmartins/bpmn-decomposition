package common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {
    public static String getOwnIpAddress() throws UnknownHostException {
        return "127.0.0.1";
    }

    public static int getOpenPort() {
        // TODO detect port
        return 8080;
    }
}
