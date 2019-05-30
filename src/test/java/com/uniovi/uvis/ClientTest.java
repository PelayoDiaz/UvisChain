package com.uniovi.uvis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uniovi.uvis.communication.NodeClient;
import com.uniovi.uvis.entities.dto.TransactionDto;

public class ClientTest {
	
	private NodeClient client;
	
	@Before
	public void setup() {
	    client = new NodeClient();
	    try {
			client.startConnection("127.0.0.1", 6666);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() {
	    try {
			client.stopConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenClient1_whenServerResponds_thenCorrect() throws UnknownHostException, IOException {
//		NodeClient client1 = new NodeClient();
//	    client1.startConnection("localhost", 6666);
	    TransactionDto dto = new TransactionDto();
	    dto.setContent("prueba");
//	    TransactionDto terminate = new TransactionDto();
//	    dto.setContent("end");
	    
	    String devuelto = client.sendMessage(dto);
//	    String devueltoEnd = client.sendMessage(terminate);
	    assertEquals(devuelto, "prueba");
//	    assertEquals(devueltoEnd, "end");
	}
//
//	@Test
//	public void givenClient1_whenServerResponds_thenCorrect() throws UnknownHostException, IOException {
//		NodeClient client1 = new NodeClient();
//	    client1.startConnection("localhost", 6666);
//	    String msg1 = client1.sendMessage("hello");
//	    String msg2 = client1.sendMessage("world");
//	    String terminate = client1.sendMessage(".");
//	     
//	    assertEquals(msg1, "hello");
//	    assertEquals(msg2, "world");
//	    assertEquals(terminate, "bye");
//	}
//	 
//	@Test
//	public void givenClient2_whenServerResponds_thenCorrect() throws UnknownHostException, IOException {
//		NodeClient client2 = new NodeClient();
//	    client2.startConnection("127.0.0.1", 6666);
//	    String msg1 = client2.sendMessage("hello");
//	    String msg2 = client2.sendMessage("world");
//	    String terminate = client2.sendMessage(".");
//	     
//	    assertEquals(msg1, "hello");
//	    assertEquals(msg2, "world");
//	    assertEquals(terminate, "bye");
//	}

}
