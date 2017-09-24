package sysu.coreclass.database.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import sysu.coreclass.database.bean.StatementBean;


public class StatementDao {
	
	public static DBObject statementToDBObject(StatementBean statementBean){
		DBObject DBStatement = new BasicDBObject();
		DBStatement.put("statement", statementBean.getStatement().toString());
		DBStatement.put("type", statementBean.getStatementeType());
		DBStatement.put("statementID", statementBean.getStatementID());
		return DBStatement;
	}

}
