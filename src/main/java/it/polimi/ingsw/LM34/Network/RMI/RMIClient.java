package it.polimi.ingsw.LM34.Network.RMI;

import it.polimi.ingsw.LM34.Network.Client.AbstractClient;
import it.polimi.ingsw.LM34.Network.Client.ClientNetworkController;
import it.polimi.ingsw.LM34.UI.UIInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

import static it.polimi.ingsw.LM34.Utils.Utilities.LOGGER;

public class RMIClient extends AbstractClient implements RMIClientInterface {
    private RMIServerInterface server;

    public RMIClient(String serverIP, Integer port, UIInterface ui) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, port);
            server = (RMIServerInterface) registry.lookup("RMIServer");
            UnicastRemoteObject.exportObject(this, 0);

            this.networkController = new ClientNetworkController(this);
            this.ui = ui;
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.FINEST, e.getMessage(), e);
        }
    }

    @Override
    public void login(String username, String password) {
        try {
            Boolean loginResult = server.login(username, password, this);
            this.networkController.loginResult(loginResult);
        } catch (RemoteException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
