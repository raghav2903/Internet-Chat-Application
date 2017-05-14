package com.uf.chatapp.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class ClientMessageProcessor implements Runnable {

	private ClientInfo clientInfo;
	private Map<String, ClientInfo> clientMap;

	private static final String CHAR_SPACE = " ";
	private static final String EMPTY = "";

	private static final String CLIENT_ID = "clientid";
	private static final String UNICAST = "unicast";
	private static final String BROADCAST = "broadcast";
	private static final String BLOCKCAST = "blockcast";

	private static final String MESSAGE = "message";
	private static final String FILE = "file";

	public ClientMessageProcessor(final ClientInfo clientInfo, final Map<String, ClientInfo> clientMap) {
		this.clientInfo = clientInfo;
		this.clientMap = clientMap;

		System.out.println("Started Client Message Processor");
	}

	@Override
	public void run() {
		try {
			String clientCommand;
			String operation;
			String[] commandArgs;

			String receivingClientId;
			String clientMessage;
			String blockedClientId;
			String messageType;

			while (true) {
				clientCommand = (String) this.clientInfo.getInputStream().readObject();
				System.out.println("Recd message from client : " + clientCommand);
				if (clientCommand != null || EMPTY.equals(clientCommand)) {
					commandArgs = clientCommand.split(CHAR_SPACE);
					operation = commandArgs[0];
					if (operation != null || EMPTY.equals(operation)) {
						switch (operation) {
						case CLIENT_ID:
							System.out.println(clientCommand);
							this.clientInfo.setClientId(commandArgs[1]);
							clientMap.put(this.clientInfo.getClientId(), this.clientInfo);
							System.out.println("All clients regd are as follows");
							for (Map.Entry<String, ClientInfo> entry : clientMap.entrySet()) {
								System.out.println(entry.getValue());
							}

							break;

						case UNICAST:

							// file or message

							messageType = commandArgs[1];
							receivingClientId = commandArgs[2];
							clientMessage = commandArgs[3];

							sendMessage(receivingClientId, clientMessage);
							if (MESSAGE.equals(messageType)) {
								sendMessage(receivingClientId, clientMessage);
							} else if (FILE.equals(messageType)) {
								String filepath = commandArgs[3];
								sendFile(receivingClientId, filepath);
							}

							break;

						case BROADCAST:
							clientMessage = commandArgs[3];
							for (Map.Entry<String, ClientInfo> entry : clientMap.entrySet()) {
								if (entry.getValue().getClientId() != clientInfo.getClientId()) {
									sendMessage(entry.getValue().getClientId(), clientMessage);
								}

							}
							break;

						case BLOCKCAST:
							clientMessage = commandArgs[3];
							blockedClientId = commandArgs[2];
							System.out.println("clientMessage : " + clientMessage);
							System.out.println("Blocked clientId : " + blockedClientId);

							for (Map.Entry<String, ClientInfo> entry : clientMap.entrySet()) {
								if (!(entry.getValue().getClientId().equals(clientInfo.getClientId()))) {
									if (!(entry.getValue().getClientId().equals(blockedClientId))) {
										sendMessage(entry.getValue().getClientId(), clientMessage);
									}
								}
							}

							break;
						}
					}
				}

			}

		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendMessage(final String clientId, final String message) {
		try {
			ObjectOutputStream outputStream = clientMap.get(clientId).getOutputStream();
			outputStream.writeObject(message);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendFile(final String receivingClientId, final String filePath) {

		File file = new File(filePath);
		sendMessage(receivingClientId, "filesend " + file.getName());
		BufferedInputStream bis = null;
		FileInputStream fis = null;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] bytes = new byte[1024];
		ObjectOutputStream outputStream = clientMap.get(receivingClientId).getOutputStream();

		try {
			fis = new FileInputStream(new File(filePath));
			bis = new BufferedInputStream(fis);
			int count;
			while ((count = bis.read(bytes, 0, 1024)) > 0) {
				outputStream.write(bytes, 0, count);
				outputStream.flush();
				// Thread.sleep(50);
			}
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				fis.close();
				bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
