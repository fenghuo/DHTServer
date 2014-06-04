import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class Node implements DHT {

	public String ip;
	public Vector<BigInteger> map;
	public FingerTable fingerTable;
	private BigInteger id;

	public Node(String ip, String file) {
		this.ip = ip;
		map = Util.loadMap(file);
		id = getID(ip);
		fingerTable = new FingerTable(id, ip);
	}

	public void init() {
		fingerTable.Init();
	}
	
	public void init(String jip) {
		fingerTable.Init(jip);
	}

	public static BigInteger getID(String iip) {

		BigInteger r = BigInteger.ZERO;

		String[] s = iip.split("[.]");
		for (int i = s.length - 1; i >= 0; i--)
			r = r.multiply(BigInteger.valueOf(256)).add(
					BigInteger.valueOf(Long.parseLong(s[i])));

		r=r.multiply(BigInteger.valueOf(2).pow(Util.SIZE-32));

		return r;
	}
	
	

	@Override
	public String put(String key) {

		BigInteger bkey = toInt(key);

		String res = null;

		String nip = fingerTable.getNext(bkey).succIP;

		if (nip.equals(ip)) {
			if (Collections.binarySearch(map, bkey) < 0) {
				map.add(toInt(key));
				Collections.sort(map);
				Util.saveMap(map);
			}
		} else 
			res=nip;
		
		return res;
	}

	@Override
	public String get(String key) {

		BigInteger bkey = toInt(key);

		String nip = fingerTable.getNext(bkey).succIP;

		if (nip.equals(ip))
			return findCoin(bkey).toString();
		else
			return ip;
	}
	
	public String join(String jip){
		
		BigInteger bkey=getID(jip);
		
		String res=null;
		
		if(fingerTable.table[0].id.compareTo(bkey)>=0 || fingerTable.table[0].succIP.equals(ip))
			fingerTable.Join(bkey,jip);
		else
			res=fingerTable.getNext(bkey).succIP;
		
		return res;
	}

	private BigInteger findCoin(BigInteger bkey) {
		// TODO Auto-generated method stub

		BigInteger r = BigInteger.valueOf(-1);

		if (map.isEmpty() || map.firstElement().compareTo(bkey) > 0)
			return BigInteger.valueOf(-1);

		for (int i = 0; i < map.size(); i++) {
			if (map.get(i).compareTo(bkey) <= 0)
				r = map.get(i);
			else
				break;
		}

		return r;
	}

	public static BigInteger toInt(String key) {
		BigInteger r = BigInteger.ZERO;

		for (int i = 0; i < key.length(); i++) {
			r = r.multiply(BigInteger.valueOf(2)).add(
					BigInteger.valueOf(Long.parseLong(key.charAt(i) + "")));
		}

		return r;
	}
	public static BigInteger toInt10(String key) {
		BigInteger r = BigInteger.ZERO;

		for (int i = 0; i < key.length(); i++) {
			r = r.multiply(BigInteger.valueOf(10)).add(
					BigInteger.valueOf(Long.parseLong(key.charAt(i) + "")));
		}

		return r;
	}

}
