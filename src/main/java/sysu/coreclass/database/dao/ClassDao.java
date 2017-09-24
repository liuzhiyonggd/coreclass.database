package sysu.coreclass.database.dao;


import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import sysu.coreclass.database.bean.ClassBean;
import sysu.coreclass.database.bean.MethodInvoke;
import sysu.coreclass.database.tool.Connection;


public class ClassDao {

	public static void insertOne(ClassBean clazz, DB db) {
		DBCollection classes = db.getCollection("class");

		DBObject DBClass = classToDBObject(clazz);
		classes.insert(DBClass);
	}

	public static DBObject classToDBObject(ClassBean clazz) {

		DBObject DBClass = new BasicDBObject();
		DBClass.put("classID", clazz.getClassID());

		DBClass.put("commitID", clazz.getCommitID());
		DBClass.put("project", clazz.getProject());
		DBClass.put("className", clazz.getClassName());
		DBClass.put("classType", clazz.getClassType());

		DBClass.put("parent", clazz.getParentName());
		DBClass.put("interfaces", clazz.getInterfaceList().toArray());
		DBClass.put("innerCount", clazz.getInnerCount());
		DBClass.put("outterCount", clazz.getOutterCount());
		DBClass.put("isCore", clazz.isCore());
		DBClass.put("newMethodNum", clazz.getNewMethodNum());
		DBClass.put("changeMethodNum", clazz.getChangeMethodNum());
		DBClass.put("deleteMethodNum", clazz.getDeleteMethodNum());
		
		List<DBObject> DBMethodInvokes = new ArrayList<DBObject>();
		for(MethodInvoke mi:clazz.getMethodInvokeList()){
			DBMethodInvokes.add(MethodInvokeDao.methodInvokeToDBObject(mi));
		}
		DBClass.put("methodInvokes", DBMethodInvokes);
		DBClass.put("classIndex", clazz.getClassIndex());
		return DBClass;
	}

	public static ClassBean DBObjectToClass(DBObject DBClass) {
		ClassBean classBean = new ClassBean();
		classBean.setClassID((Integer) DBClass.get("classID"));
		classBean.setClassName((String) DBClass.get("className"));
		classBean.setClassType((String) DBClass.get("classType"));
		classBean.setCommitID((Integer) DBClass.get("commitID"));
		classBean.setParentName((String) DBClass.get("parent"));
		classBean.setProject((String) DBClass.get("project"));
		classBean.setInterfaceList((List<String>) DBClass.get("interfaces"));
		
		classBean.setInnerCount((Integer)DBClass.get("innerCount"));
		classBean.setOutterCount((Integer)DBClass.get("outterCount"));
		classBean.setCore((Boolean)DBClass.get("isCore"));

		classBean.setChangeMethodNum((Integer)DBClass.get("changeMethodNum"));
		classBean.setNewMethodNum((Integer)DBClass.get("newMethodNum"));
		classBean.setDeleteMethodNum((Integer)DBClass.get("deleteMethodNum"));
		
		List<DBObject> DBMethodInvokes = (List<DBObject>)DBClass.get("methodInvokes");
		List<MethodInvoke> methodInvokeList = new ArrayList<MethodInvoke>();
		for(DBObject obj:DBMethodInvokes){
			methodInvokeList.add(MethodInvokeDao.DBObjectToMethodInvoke(obj));
		}
		classBean.setMethodInvokeList(methodInvokeList);
		if(DBClass.get("classIndex")!=null){
			classBean.setClassIndex((Integer)DBClass.get("classIndex"));
		}
		return classBean;
	}

	public static List<ClassBean> findByCommit(int commitID, String project) {
		DB db = Connection.getConnection();
		List<ClassBean> classBeanList = new ArrayList<ClassBean>();
		DBCollection classes = db.getCollection("class");
		DBObject query = new BasicDBObject();
		query.put("commitID", commitID);
		query.put("project", project);
		DBCursor cursor = classes.find(query);
		for (DBObject obj : cursor) {
			ClassBean classBean = DBObjectToClass(obj);
			classBeanList.add(classBean);
		}

		return classBeanList;
	}

	public static void update(DBObject query, ClassBean classBean, DB db) {
		DBCollection classes = db.getCollection("class");

		classes.update(query, classToDBObject(classBean));
	}
	
	public static void update(ClassBean classBean){
		DB db = Connection.getConnection();
		DBCollection classes = db.getCollection("class");
		DBObject query = new BasicDBObject();
		query.put("commitID", classBean.getCommitID());
		query.put("project", classBean.getProject());
		query.put("classID", classBean.getClassID());
		
		classes.update(query, classToDBObject(classBean));
	}
}
