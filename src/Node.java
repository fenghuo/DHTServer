import java.util.HashMap;

public class Node implements DHT {

	private String ip;
	private String port;
	private HashMap<Integer, String> map;
	private final int max;
	private FingerTable fingerTable;
	private int id;
	
	public Node(String ip, String port, String file, int m) {
		this.ip = ip;
		this.port = port;
		map = Util.loadMap(file);
		max = m;
		id=hash(ip);
		fingerTable=new FingerTable(id,m);
	}

	@Override
	public void put(String key, String vale) {

	}

	@Override
	public String get(String key) {
		FingerTable.Entry entry=fingerTable.get(hash(key));
		if(entry!=null)
		{
			if(entry.id==id)
				return map.get(key);
			else
			{
				//TODO call other ip
			}
		}
		
		return null;
	}

	private int hash(String key) {
		return key.hashCode() % max;
	}

}
