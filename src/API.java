import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Date;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class API {

	public static Node node = null;

	public API() {
		node = new Node("127.0.0.1", "test");
	}

	public static void main(String[] args) throws Exception {

		if (args.length > 1) {
			node = new Node(args[0], Util.File);
			node.init(args[1]);
			Util.getResponse(args[1], new String[][] { { "join", node.ip } });
		} 
		else if(args.length>0){
			node = new Node(args[0], Util.File);
			node.init();
		}
		else {
			node = new Node(Util.SIP, Util.File);
			node.init();
		}
		 test();

		HttpServer server = HttpServer.create(new InetSocketAddress(Util.PORT),
				0);
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
				if (type.equals("getd"))
					response += getd(key);
				else if (type.equals("put"))
					put(key);
				else if (type.equals("putd"))
					putd(key);
				if (type.equals("getip"))
					response += getip(key);
				if (type.equals("getipd"))
					response += getipd(key);
				if (type.equals("join"))
					response += join(key);
				else if (type.equals("dump"))
					response += dump();
				else if (type.equals("test"))
					response += "test";
			}

			return response;
		}

		private String get(String key) {

			String res = node.get(key);

			if (res.contains("."))
				// ip
				return Util.getResponse(res, new String[][] { { "get", key } });

			else
				// key
				return res;
		}

		private void put(String key) {
			String res = node.put(key);

			if (res != null)
				//ip
				Util.getResponse(res, new String[][] { { "put", key } });
			return;
		}
		
		private String getd(String key) {

			String res = node.get(new BigInteger(key.trim()));

			if (res.contains("."))
				// ip
				return Util.getResponse(res, new String[][] { { "getd", key } });

			else
				// key
				return res;
		}

		private void putd(String key) {
			
			String res = node.put(new BigInteger(key.trim()));

			if (res != null)
				Util.getResponse(res, new String[][] { { "putd", key } });
			return;
			
		}

		private String join(String key) {

			String res = node.join(key.trim());

			System.out.println("join : " + res + "\n");

			if (res == null)
				return node.ip;
			else
				res = Util.getResponse(res, new String[][] { { "join", key } });
			return res;
		}

		private String getip(String key) {

			String gip = node.fingerTable.getNext(Node.BinaryToInt(key)).succIP;

			if (!gip.equals(node.ip))
				//ip
				return Util.getResponse(gip,
						new String[][] { { "getip", key } });
			return gip;
		}
		

		private String getipd(String key) {

			String gip = node.fingerTable.getNext(new BigInteger(key)).succIP;

			if (!gip.equals(node.ip))
				//ip
				return Util.getResponse(gip,
						new String[][] { { "getip", key } });
			return gip;
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
		//res = node.join("127.0.0.2");
		
		res=Util.getResponse("127.0.0.1", new String[][]{{"getd","1111"}});
		
		System.out.println(res);
	}

}
