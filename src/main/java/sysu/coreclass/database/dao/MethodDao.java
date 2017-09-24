package sysu.coreclass.database.dao;


import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import sysu.coreclass.database.bean.MethodBean;
import sysu.coreclass.database.bean.StatementBean;
import sysu.coreclass.database.tool.Connection;


public class MethodDao {

	public static void insertOne(MethodBean method, DB db) {
		DBCollection methods = db.getCollection("method");

		DBObject DBMethod = methodToDBObject(method);
		methods.insert(DBMethod);
	}

	public static DBObject methodToDBObject(MethodBean method) {

		DBObject DBMethod = new BasicDBObject();
		DBMethod.put("methodID", method.getMethodID());
		DBMethod.put("classID", method.getClassID());

		DBMethod.put("commitID", method.getCommitID());
		DBMethod.put("project", method.getProject());
		DBMethod.put("methodName", method.getMethodName());
		DBMethod.put("returnType", method.getReturnType());
		DBMethod.put("parameters", method.getParameters());
		DBMethod.put("methodType", method.getMethodType());
		
		List<DBObject> DBStatements = new ArrayList<DBObject>();
		for(StatementBean sta:method.getStatementList()){
			DBStatements.add(StatementDao.statementToDBObject(sta));
		}
		DBMethod.put("statements", DBStatements);
		DBMethod.put("innerCount", method.getInnerCount());
		DBMethod.put("outerCount", method.getOutterCount());
		return DBMethod;
	}

	public static MethodBean DBObjectToMethod(DBObject DBMethod) {
		MethodBean methodBean = new MethodBean();
		methodBean.setMethodID((Integer) DBMethod.get("methodID"));
		methodBean.setClassID((Integer) DBMethod.get("classID"));
		methodBean.setCommitID((Integer) DBMethod.get("commitID"));
		methodBean.setProject((String) DBMethod.get("project"));
		methodBean.setMethodName((String) DBMethod.get("methodName"));
		methodBean.setReturnType((String) DBMethod.get("returnType"));

		List<String> parameters;
		parameters = (List<String>) DBMethod.get("parameters");

		methodBean.setParameters(parameters);

		methodBean.setMethodType((String) DBMethod.get("methodType"));

//		List<StatementBean> statements;
//		statements = (List<StatementBean>)DBMethod.get("statements");
//		methodBean.setStatementList(statements);
		
		methodBean.setInnerCount((Integer)DBMethod.get("innerCount"));
		methodBean.setOutterCount((Integer)DBMethod.get("outerCount"));
		return methodBean;
	}

	public static List<MethodBean> findByCommitID(int commitID, String project, DB db) {

		List<MethodBean> methodList = new ArrayList<MethodBean>();
		DBObject query = new BasicDBObject();
		query.put("commitID", commitID);
		query.put("project", project);
		DBCollection methods = db.getCollection("method");
		DBCursor cursor = methods.find(query);

		for (DBObject obj : cursor) {
			methodList.add(DBObjectToMethod(obj));
		}

		return methodList;
	}

	public static void update(DBObject query, MethodBean methodBean, DB db) {
		DBCollection methods = db.getCollection("method");
		methods.update(query, methodToDBObject(methodBean));
	}

	public static void main(String[] args) {
		List<MethodBean> methodList = MethodDao.findByCommitID(918, "jhotdraw", Connection.getConnection());
		for (MethodBean method : methodList) {
			System.out.println(method.getMethodName());
		}
	}

}
