package com.uf.chatapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserInputProcessor implements Runnable {

	private String userInput;
	private Socket clientSocket;
	private ObjectOutputStream objectOutputStream;

	public UserInputProcessor(final Socket clientSocket, final ObjectOutputStream outputStream) {
		this.clientSocket = clientSocket;
		this.objectOutputStream = outputStream;
	}

	@Override
	public void run() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				userInput = bufferedReader.readLine();
				sendMessage(userInput);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private void sendMessage(final String userInput) {

		try {
			if (userInput != null) {
				this.objectOutputStream.writeObject(userInput);
				this.objectOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
