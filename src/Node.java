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
		map = Util.loadMap();
		id = getID(ip);
		fingerTable = new FingerTable(id, ip);
	}

	public void init() {
		
		System.out.println("System Start 0: "+ip);
		
		fingerTable.Init();
	}
	
	public void init(String jip) {
		System.out.println("System Start 1: "+ip);
		
		fingerTable.Init(jip);
		
	}
	
	public void copy(String pip){
		new Sync(pip).start();
	}

	public static BigInteger getID(String iip) {

		BigInteger r = BigInteger.ZERO;

		if(iip.equals(Util.ROOTIP))
			return r;
		
		String[] s = iip.split("[.]");
		for (int i = s.length - 1; i >= 0; i--)
			r = r.multiply(BigInteger.valueOf(256)).add(
					BigInteger.valueOf(Long.parseLong(s[i])));

		r=r.multiply(BigInteger.valueOf(2).pow(Util.SIZE-32));

		return r;
	}
	
	

	@Override
	public String put(String key) {

		BigInteger bkey = BinaryToInt(key);

		return put(bkey);
		
	}
	
	public String put(BigInteger bkey){

		String res = null;

		FingerTable.Entry e=fingerTable.getNext(bkey);
		
		if(e==null || e.succIP.equals(ip)){
			if (Collections.binarySearch(map, bkey) < 0) {
				map.add(bkey);
				Collections.sort(map);
				Util.saveMap(map);
			}
		} else 
			res=e.succIP;
		
		System.out.println(res+":"+ip);
		
		return res;
	}

	@Override
	public String get(String key) {

		BigInteger bkey = BinaryToInt(key);

		return get(bkey);
		
	}
	
	public String get(BigInteger bkey){

		FingerTable.Entry e=fingerTable.getNext(bkey);

		if (e==null || e.succIP.equals(ip))
			return findCoin(bkey).toString();
		else
			return e.succIP;
	}
	
	public String join(String jip){
		
		BigInteger bkey=getID(jip);
		
		String res=null;

		FingerTable.Entry e=fingerTable.getNext(bkey);
		
		
		if(e==null || fingerTable.table[0].succ.compareTo(bkey)>=0 || fingerTable.table[0].succIP.equals(ip))
			fingerTable.Join(bkey,jip);
		else{
			res=e.succIP;
		}
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

	public static BigInteger BinaryToInt(String key) {
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
	
	public class Sync extends Thread{
		
		String sip="";
		
		public Sync(String ip){
			this.sip=ip;
		}
		
		@Override
		public void run(){
			BigInteger next=id;
			BigInteger b=getID(sip);
			
			while(true){
				String res=Util.getResponse(sip, new String[][]{{"getd",next.toString()}});
				next=new BigInteger(res.trim());
				if(next.compareTo(b)>=0){
					if (Collections.binarySearch(map, b) < 0) {
						map.add(b);
						Collections.sort(map);
					}
				}
				else
					break;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

}
