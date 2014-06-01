import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Util {

	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private final static String DELIMIT = ",";

	public static HashMap<Integer, String> loadMap(String file) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		HashMap<Integer, Integer> index = new HashMap<Integer, Integer>();

		Path path = Paths.get(file);
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] lines = line.split(DELIMIT);
				int no = Integer.parseInt(lines[0]);
				if (index.containsKey(no))
					index.put(no, index.get(no) + 1);
				else
					index.put(no, 1);
				int idx = index.get(no) * 1000 + no;
				map.put(idx, lines[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void saveMap(HashMap<Integer,String> map,String file)
	{	
	    Path path = Paths.get(file);
	    try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), ENCODING),true);)
	    {
	    	for(int key:map.keySet())
	    		out.println(key+DELIMIT+map.get(out));
	    	out.close();
	    } catch (Exception e) {
			e.printStackTrace();
	    }
	}
}
