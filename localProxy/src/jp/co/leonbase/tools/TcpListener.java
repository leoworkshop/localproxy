package jp.co.leonbase.tools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * listener to the TCP Connections
 */
public class TcpListener {

    private final String remoteProxy;
    private final int remotePort;
    private final int localPort;
    private final String checkUrl;

    public TcpListener(String remoteProxy, int remotePort, int localPort, String checkUrl) {
        this.remoteProxy = remoteProxy;
        this.remotePort = remotePort;
        this.localPort = localPort;
        this.checkUrl = checkUrl;
    }

    public void start() throws IOException {
        int clientId = 0;

        final ServerSocket server = new ServerSocket(this.localPort);
        System.out.println("Listening on port: " + this.localPort);

        while (true) {
            final Socket clientSocket = server.accept();
            Socket remoteSocket = new Socket(this.remoteProxy, this.remotePort);
            clientId++;

            System.out.println("Client " + clientId + " connected from " + clientSocket.getInetAddress());

            final ClientProcessor processor = new ClientProcessor(clientId, clientSocket, remoteSocket, checkUrl);
            final Thread t = new Thread(processor);
            t.start();
        }
    }
}
