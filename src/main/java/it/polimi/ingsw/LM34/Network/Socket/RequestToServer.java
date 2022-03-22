package it.polimi.ingsw.LM34.Network.Socket;

import java.io.IOException;
import java.util.logging.Level;

import static it.polimi.ingsw.LM34.Utils.Utilities.LOGGER;

/**
 * inspired by https://stackoverflow.com/questions/12935709/call-a-specific-method-based-on-enum-type
 */
public enum RequestToServer {
    LOGIN {
        @Override
        void readAndHandle(SocketConnection connection) {
            try {
                String username = connection.getInputStream().readUTF();
                String password = connection.getInputStream().readUTF();

                Boolean result = connection.login(username, password);
                connection.getOutputStream().writeUTF(RequestToClient.LOGIN.name());
                connection.getOutputStream().writeBoolean(result);
                connection.getOutputStream().flush();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
    };

    abstract void readAndHandle(SocketConnection connection);
}
