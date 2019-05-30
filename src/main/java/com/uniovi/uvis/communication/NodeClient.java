package com.uniovi.uvis.communication;

import java.net.*;

import com.uniovi.uvis.entities.dto.TransactionDto;

import java.io.*;

public class NodeClient {
	
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    private OutputStream os;
    private ObjectOutputStream oos;

	public NodeClient() {
	}
	
	public void startConnection(String ip, int port) throws UnknownHostException, IOException {
        this.clientSocket = new Socket(ip, port);
        this.os = clientSocket.getOutputStream();
        this.oos = new ObjectOutputStream(this.os);
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
	
	public String sendMessage(TransactionDto transactionDto) throws IOException {
		this.oos.writeObject(transactionDto);
        String resp = in.readLine();
        return resp;
    }
	
	public void stopConnection() throws IOException {
		this.oos.close();
		this.os.close();
		this.clientSocket.close();
    }

}
