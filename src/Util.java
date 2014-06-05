import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Vector;

public class Util {

	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private final static String DELIMIT = ",";
	public static int PORT = 1919;
	public static int maxSize = 101;
	public static int SIZE = maxSize * maxSize;
	public static BigInteger Max = BigInteger.valueOf(2).pow(SIZE);
	public static String File = "test";
	public static String FIP = "ip";
	public static String SIP = "127.0.0.1";

	public static Vector<BigInteger> loadMap() {
		Vector<BigInteger> map = new Vector<BigInteger>();

		Path path = Paths.get(File);
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				map.add(new BigInteger(line.trim()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void saveMap(Vector<BigInteger> map) {
		try (PrintWriter out = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(File), ENCODING), true);) {
			for (BigInteger key : map)
				out.println(key);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getResponse(String ip, String[][] params) {
		
		String req = "http://" + ip + ":" + PORT + "/?";
		for (String[] p : params)
			req += p[0] + ((p.length > 1) ? "=" + p[1] : "") + "&";
		return getResponse(req);
	}

	public static String getResponse(String url) {
		
		System.out.println(API.node.ip + "\tSending request: " + url);

		StringBuilder response = new StringBuilder();
		try {
			URL website = null;
			website = new URL(url);
			URLConnection connection = website.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);

			in.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response.toString();

	}

}
