package com.uf.chatapp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerMessageListener implements Runnable {

	private Socket clientSocket;
	private ObjectInputStream objectInputStream;

	private String serverMessage;

	public ServerMessageListener(final Socket socket, final ObjectInputStream inputStream) {
		this.clientSocket = socket;
		this.objectInputStream = inputStream;
	}

	@Override
	public void run() {
		while (true) {
			try {
				serverMessage = (String) this.objectInputStream.readObject();
				System.out.println("SERVER MESSAGE : " + serverMessage);

				/*if (serverMessage.startsWith("filesend")) {
					String[] messageArgs = serverMessage.split(" ");
					FileReceiver fileReceiver = new FileReceiver(clientSocket, objectInputStream, messageArgs[1]);
					fileReceiver.run();
				}*/

			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

		}
	}

}
