package com.uf.chatapp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private static Map<String, ClientInfo> clientMap = new HashMap<String, ClientInfo>();

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8000, 10);
			System.out.println("Server has started...");
			ExecutorService executorService = Executors.newCachedThreadPool();
			executorService.execute(new ClientRegistrar(serverSocket, clientMap));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
