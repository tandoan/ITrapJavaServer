package com.tandoan.app.bluetoothserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.microedition.io.StreamConnection;

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

	/**
	 * read in a file stream, then push it to the output stream cannot use OBEX
	 * because Android doesn't have a quick OBEX solution so we're going to use
	 * the raw stream writer instead
	 */
	private void pushImage() {
		System.out.println("pushing file");

		try {
			String filePath = "/Users/tdoan/Documents/workspace_eclipse/BluetoothServer/images/tree.jpg";

			FileInputStream fileStream = new FileInputStream(filePath);
			File file = new File(filePath);
			int size = (int) file.length();
			byte[] fileArray = new byte[size];
			fileStream.read(fileArray);
			fileStream.close();

			DataOutputStream outputStream = mConnection.openDataOutputStream();
			byte[] nameInBytes = "tree.jpg".getBytes("UTF-8");
			outputStream.writeInt(nameInBytes.length);
			outputStream.write(nameInBytes);
			outputStream.writeInt(size);
			outputStream.write(fileArray);
			outputStream.flush();
			outputStream.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("file pushed");

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