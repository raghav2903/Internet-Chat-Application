package com.uf.chatapp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientRegistrar implements Runnable {

	private ServerSocket serverSocket;
	private Map<String, ClientInfo> clientMap;

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	public ClientRegistrar(final ServerSocket serverSocket, final Map<String, ClientInfo> clientMap) {
		this.serverSocket = serverSocket;
		this.clientMap = clientMap;

		System.out.println("Started client registrar");

	}

	@Override
	public void run() {

		try {
			while (true) {
				Socket newClientSocket = serverSocket.accept();
				if (newClientSocket != null) {
					ClientInfo clientInfo = new ClientInfo();
					clientInfo.setClientSocket(newClientSocket);
					clientInfo.setInputStream(new ObjectInputStream(newClientSocket.getInputStream()));
					clientInfo.setOutputStream(new ObjectOutputStream(newClientSocket.getOutputStream()));

					executorService.execute(new ClientMessageProcessor(clientInfo, clientMap));
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
