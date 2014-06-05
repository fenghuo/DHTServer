import java.util.Vector;
import java.math.BigInteger;

public class FingerTable {

	public Entry[] table;
	private BigInteger id;
	private String ip;

	public FingerTable(BigInteger i, String ip) {
		table = new Entry[Util.SIZE];
		id = i;
		this.ip = ip;
	}

	public void Init() {
		for (int i = 0; i < Util.SIZE; i++) {
			table[i] = new Entry(BigInteger.valueOf(2).pow(i), id, ip);
		}
		return;
	}

	public void Init(String jip) {

		for (int i = 0; i < Util.SIZE; i++) {
			table[i] = new Entry(BigInteger.valueOf(2).pow(i).add(id), id, ip);
		}

		System.out.println("Initialization Finished~");

		//new Sync(jip).start();

		return;

	}

	public void Join(BigInteger jid, String jip) {
		for (int i = 0; i < table.length; i++) {
			if (table[i].id.compareTo(jid) <= 0) {
				table[i].succ = jid;
				table[i].succIP = jip;
			}
		}
	}

	public Entry getNext(BigInteger index) {
		Entry entry = new Entry(id, id, ip);
		for (int i = 0; i < table.length; i++) {
			Entry e = table[i];
			if (e.succ.compareTo(index) < 0 && (e.succ.compareTo(entry.id) > 0))
				entry = e;
		}
		return entry;
	}

	public class Entry {
		public BigInteger id;
		public BigInteger succ;
		public String succIP;

		public Entry(BigInteger id, BigInteger succ, String succIP) {
			this.id = id;
			this.succ = succ;
			this.succIP = succIP;
		}
	}

	public class Sync extends Thread {

		String ip = "";

		public Sync(String ip) {
			this.ip = ip;
		}

		@Override
		public void run() {

			for (int i = 0; i < Util.SIZE; i++) {
				BigInteger tid = BigInteger.valueOf(2).pow(i).add(id);
				String sip = Util.getResponse(ip,
						new String[][] { { "getipd", tid.toString() } })
						.toString();
				BigInteger sid = BigInteger.ZERO;
				if (!sip.equals(Util.SIP))
					sid = Node.getID(sip);
				table[i] = new Entry(tid, id, sip);
				System.out.println("Initing: " + i);
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
