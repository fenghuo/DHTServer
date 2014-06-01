import java.util.Vector;


public class FingerTable {

	private Vector<Entry> table;
	private final int id;
	private final int max;
	private String ip;
	
	public FingerTable(int i,int m)
	{
		table=new Vector<Entry>();
		id=i;
		max=m;
	}
	
	public void Init()
	{
		int next=id+1;
		for(int i=0;next<max;i++)
		{
			table.add(new Entry(next,id,ip));
			next=id+(int)Math.pow(2, i);
		}
	}
	
	public Entry get(int index)
	{
		Entry entry=null;
		for(int i=0;i<table.size();i++)
		{
			Entry e=table.get(i);
			if(entry==null || (e.id<=index && e.id>entry.id))
				entry=e;
		}
		return entry;
	}
	
	public class Entry
	{
		public int id;
		public int succ;
		public String succIP;
		
		public Entry(int id,int succ,String succIP)
		{
			this.id=id;
			this.succ=succ;
			this.succIP=succIP;
		}
	}
}
