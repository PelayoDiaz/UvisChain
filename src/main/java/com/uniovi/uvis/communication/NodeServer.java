package com.uniovi.uvis.communication;

import java.net.*;
import java.io.*;

public class NodeServer {

	private ServerSocket serverSocket;

	public NodeServer() {
	}

	public void start(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		while (true) {
			new NodeClientHandler(serverSocket.accept()).start();
		}
	}

	public void stop() throws IOException {
		this.serverSocket.close();
	}
	
	public static void main(String[] args) {
		NodeServer server = new NodeServer();
		try {
			server.start(6666);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
