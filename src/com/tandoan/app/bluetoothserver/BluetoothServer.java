package com.tandoan.app.bluetoothserver;

public class BluetoothServer {
	/**
     * @param args
     */
    public static void main(String[] args) {
            // TODO Auto-generated method stub
            Thread waitThread = new Thread(new WaitThread());
            waitThread.start();

    }
}
