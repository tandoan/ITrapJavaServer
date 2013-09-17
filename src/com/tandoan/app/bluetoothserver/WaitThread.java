package com.tandoan.app.bluetoothserver;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class WaitThread implements Runnable {

	public void run() {
		waitForConnection();
	}

	private void waitForConnection() {
		LocalDevice local = null;
		StreamConnectionNotifier notifier;
		StreamConnection connection = null;

		try {
			try {
				local = LocalDevice.getLocalDevice();
			} catch (BluetoothStateException e) {
				System.out.println(e.getMessage());
				return;
			}

			local.setDiscoverable(DiscoveryAgent.GIAC);
			System.out.println(local.getBluetoothAddress());
			// TODO: remove this is the current Bluetooth address of my mac
			// server: 0023125C3B07
			UUID uuid = new UUID("42F3C25DAA0B48E583EDA35AC3B636D5", false);
			String url = "btspp://localhost:" + uuid.toString()
					+ ";name=BluetoothServer";
			notifier = (StreamConnectionNotifier) Connector.open(url);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			try {
				System.out.println("waiting for connection...");
				connection = notifier.acceptAndOpen();

				Thread processThread = new Thread(new ProcessConnectionThread(
						connection));
				processThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
