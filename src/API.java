import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class API {

	public static Node node = null;

	public API() {
		node = new Node("127.0.0.1", "test");
	}

	public static void main(String[] args) throws Exception {

		node = new Node("127.0.0.1", "test");
		if(args.length>0)
			node.init(args[0]);
		else
			node.init();

		//test();

		HttpServer server = HttpServer.create(new InetSocketAddress(Util.PORT), 0);
		server.createContext("/", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	static class MyHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {

			String response = getResponse(t);
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		private String getResponse(HttpExchange t) {

			String head = t.getRequestURI().getQuery();
			System.out.println(new Date() + " Request : " + head);

			String[] query = head.split("&");

			String response = "";

			for (String q : query) {
				String[] temp = q.split("=");
				String type = temp[0];
				String key = temp.length > 1 ? temp[1] : "";
				if (type.equals("get"))
					response += get(key);
				else if (type.equals("put"))
					put(key);
				if (type.equals("getip"))
					response += getip(key);
				if (type.equals("join"))
					response += join(key);
				else if (type.equals("dump"))
					response += dump();
				else if (type.equals("test"))
					response += "test";
			}

			return response;
		}

		private String join(String key) {

			String res = node.join(key.trim());
			if (res == null)
				System.out.println("Join before : " + node.ip);
			return res;
		}

		private String getip(String key) {
			
			String gip= node.fingerTable.getNext(Node.toInt(key)).succIP;
			
			if(!gip.equals(node.ip))
				return Util.getResponse(gip,new String[][]{{"getip",key}});
			return gip;
		}

		private String get(String key) {

			String res=node.get(key);
			
			if(res.contains("."))
				//ip
				return Util.getResponse(res, new String[][]{{"get",key}});
				
			else
				//key
				return res;
		}
		
		private void put(String key) {
			String res=node.put(key);
			
			if(res!=null)
				Util.getResponse(res,new String[][]{{"put",key}});
			return;
		}

		private String dump() {
			String res = "";

			res += "map:\n";

			for (int i = 0; i < node.map.size(); i++)
				res += node.map.get(i) + "\n";

			res += "Finger Table:\n";
			for (int i = 0; i < node.fingerTable.table.length; i++) {
				FingerTable.Entry e = node.fingerTable.table[i];
				res += i + "\t" + e.succIP + "\n";
			}
			return res;
		}

	}

	private static void test() {
		// TODO Auto-generated method stub

		String res = "";
		res = node.join("127.0.0.2");
		System.out.println(res);
	}

}
