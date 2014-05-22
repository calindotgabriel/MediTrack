package com.google.code.gsonrmi.transport.tcp;

import com.google.code.gsonrmi.transport.*;
import com.google.code.gsonrmi.transport.Proxy;
import com.google.code.gsonrmi.transport.Transport.Shutdown;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class TcpProxy extends Proxy {
	
	private final List<TcpListener> listeners;
    protected KeyStore keyStore;

	public TcpProxy(List<InetSocketAddress> listeningAddresses, Transport transport, Gson serializer) throws IOException {
		super(transport, serializer);
		listeners = new LinkedList<TcpListener>();
		for (InetSocketAddress address : listeningAddresses) {
			TcpListener l = new TcpListener(address);
			l.start();
			listeners.add(l);
		}
	}

	@Override
	protected void handle(Shutdown m) {
		super.handle(m);
		for (TcpListener l : listeners) l.shutdown();
	}

	@Override
	public String getScheme() {
		return "tcp";
	}

	@Override
	public Connection createConnection(String remoteAuthority) {
		try {
			TcpConnection c = new TcpConnection(null, remoteAuthority);
			c.start();
			return c;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private class TcpListener extends Thread {
		private final ServerSocket ss;

		public TcpListener(InetSocketAddress address) throws IOException {

			ss =    buildServerSocket();
			ss.bind(address);
		}



        public void shutdown() {
			try {
				ss.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				while (true) {



					Socket s = ss.accept();
					InetSocketAddress addr = (InetSocketAddress) s.getRemoteSocketAddress();
					TcpConnection c = new TcpConnection(s, addr.getHostName() + ":" + addr.getPort());
					c.start();
					addConnection(c);
				}
			}
			catch (IOException e) {
				if (!ss.isClosed()) e.printStackTrace();
			}
			catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

	}
	
	private class TcpConnection extends Thread implements Connection {
		private Socket s;
		private volatile PrintWriter out;
		private final URI remoteAddr;
		private final List<Message> sendQueue;
		
		public TcpConnection(Socket socket, String remoteAuthority) throws URISyntaxException, IOException {
			s = socket;
			if (s != null) out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "utf-8"), true);
			remoteAddr = new URI(getScheme(), remoteAuthority, null, null, null);
			sendQueue = new LinkedList<Message>();
		}
		
		@Override
		public String getRemoteAuthority() {
			return remoteAddr.getAuthority();
		}
		
		@Override
		public void shutdown() {
			try {
				if (s != null) s.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void send(Message m) {
			if (out != null) send(m, out);
			else {
				synchronized (sendQueue) {
					sendQueue.add(m);
				}
				if (out != null) send(m, out);
			}
		}
		
		private void send(Message m, PrintWriter pw) {
			LinkedList<Route> dests = new LinkedList<Route>();
			for (Route dest : m.dests) dests.add(dest.removeFirst());
			pw.println(gson.toJson(new Message(m.src, dests, m.content, m.contentType)));
		}
		
		@Override
		public void run() {
			try {
				if (out == null) {
					String[] tokens = remoteAddr.getAuthority().split(":");
					s =    buildSocket(tokens[0], Integer.parseInt(tokens[1]));
					synchronized (sendQueue) {
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "utf-8"), true);
						for (Message m : sendQueue) send(m, pw);
						out = pw;

					}
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
				String line;
				while ((line = in.readLine()) != null) {
					Message m = gson.fromJson(line, Message.class);
					transport.send(new Message(m.src.addFirst(remoteAddr), m.dests, m.content, m.contentType));
				}
			}
			catch (IOException e) {
				if (s != null) {
					if (!s.isClosed()) e.printStackTrace();
				}
				else {
					System.err.println("Connect failed to " + remoteAddr.getAuthority());
					synchronized (sendQueue) {
						for (Message m : sendQueue) if (!m.contentOfType(DeliveryFailure.class))
							transport.send(new Message(null, Arrays.asList(m.src), new DeliveryFailure(m)));
					}
				}
			}catch (NullPointerException e1){
                if (s != null) {
                    if (!s.isClosed()) e1.printStackTrace();
                }
                else {
                    System.err.println("Connect failed to " + remoteAddr.getAuthority());
                    synchronized (sendQueue) {
                        for (Message m : sendQueue) if (!m.contentOfType(DeliveryFailure.class))
                            transport.send(new Message(null, Arrays.asList(m.src), new DeliveryFailure(m)));
                    }
            }
            }

			try {
				if (s != null) s.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}



	}
    protected Socket buildSocket(String host, int port) throws IOException {
        return  new Socket(host, port);
    }

    protected ServerSocket buildServerSocket() throws IOException {
        return new ServerSocket();

    }

    public void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }
}
