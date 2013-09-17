package com.tandoan.app.bluetoothserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
		try {
			String filePath = "/Users/tdoan/Documents/workspace_eclipse/BluetoothServer/images/tree.jpg";

			FileInputStream fileStream = new FileInputStream(filePath);
			File file = new File(filePath);
			int size = (int) file.length();
			byte[] fileArray = new byte[size];

			fileStream.read(fileArray);
			fileStream.close();

			OutputStream outputStream = mConnection.openOutputStream();
			outputStream.write(fileArray);
			outputStream.close();

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