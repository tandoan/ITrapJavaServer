package com.tandoan.app.bluetoothserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;

public class ProcessConnectionThread implements Runnable {

	private StreamConnection mConnection;

	private static final int EXIT_CMD = -1;
	private static final int KEY_RIGHT = 1;
	private static final int KEY_LEFT = 2;

	public ProcessConnectionThread(StreamConnection connection) {
		mConnection = connection;
	}

	public void run() {
		try {
			InputStream inputStream = mConnection.openInputStream();
			System.out.println("waiting for input");

			pushImage();
			while (true) {
				int command = inputStream.read();

				if (command == EXIT_CMD) {
					System.out.println("finished processing");
					break;
				}
				processCommand(command);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getBluetoothAddress() {
		return "0023125C3B07";
	}

	private void pushImage() {
		try {
			FileInputStream stream = new FileInputStream(
					"/Users/tdoan/Documents/workspace_eclipse/BluetoothServer/images/tree.jpg");
			File file = new File(
					"/Users/tdoan/Documents/workspace_eclipse/BluetoothServer/images/tree.jpg");
			int size = (int) file.length();
			byte[] fileArray = new byte[size];
			stream.read(fileArray);
			stream.close();

			String btConnectionURL = "btgoep://40:B0:FA:20:0B:F2" + ":9";
			Connection connection = Connector.open(btConnectionURL);
			ClientSession clientSession = (ClientSession) connection;
			HeaderSet headerSet = clientSession.createHeaderSet();

			clientSession.connect(headerSet);

			headerSet.setHeader(HeaderSet.NAME, "tree.jpg");
			headerSet.setHeader(HeaderSet.TYPE, "image/jpeg");

			// set file length
			headerSet.setHeader(HeaderSet.LENGTH, size);

			Operation putOperation = clientSession.put(headerSet);

			OutputStream outputStream = putOperation.openOutputStream();
			outputStream.write(fileArray);

			outputStream.close();
			putOperation.close();

			clientSession.disconnect(null);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void processCommand(int command) {
		try {
			System.out.println("command is: " + command);
			// Robot robot = new Robot();
			switch (command) {
			case KEY_RIGHT:
				System.out.println("RIGHT");
				break;
			case KEY_LEFT:
				System.out.println("left");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}