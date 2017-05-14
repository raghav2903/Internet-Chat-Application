package com.uf.chatapp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

	public static void main(String[] args) {

		// Process arguments from the user
		// args[0] = Client ID
		// args[1] = Server socket port
		String clientID = args[0];
		int serverPort = Integer.parseInt(args[1]);

		Socket clientSocket;
		ObjectOutputStream objectOutputStream;
		ObjectInputStream objectInputStream;

		try {
			clientSocket = new Socket("localhost", serverPort);
			System.out.println("Client << " + clientID + " >> has started");
			if (clientSocket != null) {

				System.out.println("Sending clientId : " + clientID);

				objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				objectOutputStream.writeObject("clientid " + clientID);
				objectOutputStream.flush();

				objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

				ExecutorService executorService = Executors.newCachedThreadPool();
				executorService.execute(new UserInputProcessor(clientSocket, objectOutputStream));
				executorService.execute(new ServerMessageListener(clientSocket, objectInputStream));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
