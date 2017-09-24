package sysu.coreclass.database.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import sysu.coreclass.database.bean.MethodInvoke;

public class MethodInvokeDao {
	
	public static DBObject methodInvokeToDBObject(MethodInvoke methodInvoke){
		DBObject DBMethodInvoke = new BasicDBObject();
		DBMethodInvoke.put("class1", methodInvoke.getClass1());
		DBMethodInvoke.put("class2", methodInvoke.getClass2());
		DBMethodInvoke.put("method1", methodInvoke.getMethod1());
		DBMethodInvoke.put("method2", methodInvoke.getMethod2());
		DBMethodInvoke.put("method1Type", methodInvoke.getMethod1_type());
		DBMethodInvoke.put("method2Type", methodInvoke.getMethod2_type());
		return DBMethodInvoke;
	}
	
	public static MethodInvoke DBObjectToMethodInvoke(DBObject DBMethodInvoke){
		MethodInvoke methodInvoke = new MethodInvoke();
		methodInvoke.setClass1((String)DBMethodInvoke.get("class1"));
		methodInvoke.setClass2((String)DBMethodInvoke.get("class2"));
		methodInvoke.setMethod1((String)DBMethodInvoke.get("method1"));
		methodInvoke.setMethod2((String)DBMethodInvoke.get("method2"));
		methodInvoke.setMethod1_type((String)DBMethodInvoke.get("method1Type"));
		methodInvoke.setMethod2_type((String)DBMethodInvoke.get("method2Type"));
		return methodInvoke;
	}

}
