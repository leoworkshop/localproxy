package jp.co.leonbase.tools;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * LocalProxy
 *
 * @author leon
 */
public class LocalProxy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("-- localproxy sample --");
        String remoteProxy;
        int remotePort;
        int localPort;
        String keyword;

        try {
            ResourceBundle properties = ResourceBundle.getBundle("localproxy");
            remoteProxy = properties.getString("remoteProxy");
            remotePort = Integer.parseInt(properties.getString("remoteProxyPort"));
            localPort = Integer.parseInt(properties.getString("localProxyPort"));
            keyword = properties.getString("keyword");
            TcpListener tcpListener = new TcpListener(remoteProxy, remotePort, localPort, keyword);
            tcpListener.start();
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
