package jp.co.leonbase.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * クラス　ClientProcessor
 */
public class ClientProcessor implements Runnable {

    Socket client;
    Socket server;
    int id;
    String keyword;

    public ClientProcessor(final int id, final Socket client, Socket server, String keyword) {
        this.client = client;
        this.server = server;
        this.id = id;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        try {
            final byte[] request = new byte[1024];
            byte[] reply = new byte[4096];

            final InputStream from_client = client.getInputStream();
            final OutputStream to_client = client.getOutputStream();

            final InputStream from_server = server.getInputStream();
            final OutputStream to_server = server.getOutputStream();

            new Thread() {
                public void run() {
                    int bytes_read;
                    try {

                        while ((bytes_read = from_client.read(request)) != -1) {
                            to_server.write(request, 0, bytes_read);
                            if (isWarningKeyWord(new String(request, "UTF-8"))) {
                                System.out.println(">>>send msg to the user.");
                                java.awt.Toolkit.getDefaultToolkit().beep();
                            }
                            to_server.flush();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        to_server.close();
                    } catch (IOException e) {
                    }
                }
            }.start();
            int bytes_read;
            try {
                while ((bytes_read = from_server.read(reply)) != -1) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    to_client.write(reply, 0, bytes_read);
                    to_client.flush();
                }
            } catch (IOException e) {
            }
            to_client.close();
            disconnect();
        } catch (final IOException e) {
            System.err.println("Error in client processor: " + e);
        } finally {
            try {
                if (server != null) {
                    server.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * disconnecting
     *
     * @throws IOException
     */
    private void disconnect() throws IOException {
        System.out.println("Client " + this.id + " disconnected");
        this.client.close();
    }

    /**
     * check the key word before send request to the host
     */
    private boolean isWarningKeyWord(String toServer) {
        boolean rtn = false;
        if (toServer.startsWith("CONNECT")) {
            System.out.println("[" + id + "] " + toServer);
            if (toServer.indexOf(this.keyword) > 0) {
                System.out.println("######WARNNING MSG");
                rtn = true;
            }
        }
        return rtn;
    }
}
