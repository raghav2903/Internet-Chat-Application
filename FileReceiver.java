package com.uf.chatapp.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class FileReceiver implements Runnable {

	private Socket clientSocket;
	private ObjectInputStream objectInputStream;
	private String fileName;

	private String serverMessage;

	public FileReceiver(final Socket socket, final ObjectInputStream inputStream, final String fileName) {
		this.clientSocket = socket;
		this.objectInputStream = inputStream;
		this.fileName = fileName;
	}

	@Override
	public void run() {

		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;

		try {
			byte[] bytes = new byte[1024];
			System.out.println("Filename : " + fileName);
			file = new File("D:/Dev/Eclipse_Neon_Workspace/CN_Project_v02/recd_test.txt");
			if (!file.exists()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int bytesRead;
			while ((bytesRead = this.objectInputStream.read(bytes, 0, bytes.length)) > 0) {
				System.out.println("writing bytes : " + bytesRead);
				bos.write(bytes, 0, bytesRead);
				bos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			System.out.println("File Recv complete");
			try {
				bos.flush();
				fos.close();
				bos.close();
				file = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
