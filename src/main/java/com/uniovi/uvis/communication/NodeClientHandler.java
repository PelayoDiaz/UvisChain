package com.uniovi.uvis.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.uniovi.uvis.entities.dto.TransactionDto;

public class NodeClientHandler extends Thread {
	
	private Socket clientSocket;
	private PrintWriter out;
//	private BufferedReader in;
	
	private InputStream is;
	private ObjectInputStream ois;

	public NodeClientHandler(Socket socket) {
		this.clientSocket = socket;
	}
	
	@Override
	public void run() {
		try {
			
			out = new PrintWriter(clientSocket.getOutputStream(), true);

//			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

//			String inputLine;
			this.is = clientSocket.getInputStream();
			this.ois = new ObjectInputStream(is);
			TransactionDto transactionDto;
			
//			while ((transactionDto = (TransactionDto) ois.readObject()) != null) {
//				System.out.println("hola");				
//				out.println(transactionDto.getContent());
//			}
			if ((transactionDto = (TransactionDto) ois.readObject()) != null) {
				out.println(transactionDto.getContent());
			}
			
			ois.close();
			is.close();
			out.close();
			clientSocket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
