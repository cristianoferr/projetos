package cristiano.intel;

import java.util.HashMap;

public class QueryManager {

	HashMap<String,Query> queries;
	FactManager fm;

	public QueryManager(FactManager fm){
		queries=new HashMap<String,Query>();
		this.fm=fm;
	}

	public void addQuery(Query query){
		if (query==null)return;
		queries.put(query.getName(),query);
	}

	public Query createQuery(String name,String fact){
		if ((fact.equals("")) || (fact==null)) return null;
		Query query=null;
//		if (name.equals(fact_SelfPositionX))fact=new FactSelfPositionX(fact_SelfPositionX,0,item); 
//		if (name.equals(fact_SelfPositionY))fact=new FactSelfPositionY(fact_SelfPositionY,0,item);
//		if (name.equals(fact_SelfPositionZ))fact=new FactSelfPositionZ(fact_SelfPositionZ,0,item);
		if (query==null) query=new Query(name,fact);
		addQuery(query);
		return query;
	}

	public Query getQuery(String str){
		return queries.get(str);
	}

	
}
