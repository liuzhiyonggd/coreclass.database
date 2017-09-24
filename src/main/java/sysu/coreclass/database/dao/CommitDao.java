package sysu.coreclass.database.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import sysu.coreclass.database.bean.CommitBean;
import sysu.coreclass.database.tool.Connection;


public class CommitDao {

	public static void insertOne(CommitBean commit) {
		DB db = Connection.getConnection();
		DBCollection commits = db.getCollection("commit");

		DBObject DBCommit = commitToDBObject(commit);
		commits.insert(DBCommit);
	}

	public static DBObject commitToDBObject(CommitBean commit) {

		DBObject DBCommit = new BasicDBObject();
		DBCommit.put("commitID", commit.getCommitID());
		DBCommit.put("author", commit.getAuthor());
		DBCommit.put("date", commit.getDate());
		DBCommit.put("message", commit.getMessage());
		DBCommit.put("project", commit.getProject());
		DBCommit.put("hasCoreClass", commit.isHasCoreClass());
		DBCommit.put("hasCoreMethod", commit.isHasCoreMethod());
		DBCommit.put("fileList",commit.getFileList());
		return DBCommit;
	}

	public static CommitBean DBObjectToCommit(DBObject DBCommit) {
		CommitBean commitBean = new CommitBean();
		commitBean.setCommitID((Integer) DBCommit.get("commitID"));
		commitBean.setProject((String) DBCommit.get("project"));
		commitBean.setAuthor((String) DBCommit.get("author"));
		commitBean.setDate((Date) DBCommit.get("date"));
		commitBean.setMessage((String) DBCommit.get("message"));
		commitBean.setHasCoreClass((Boolean)DBCommit.get("hasCoreClass"));
		commitBean.setHasCoreMethod((Boolean)DBCommit.get("hasCoreMethod"));
		commitBean.setFileList((List<String>)DBCommit.get("fileList"));
		return commitBean;
	}

	public static CommitBean findByID(int commitID, String project) {
		DB db = Connection.getConnection();
		DBObject query = new BasicDBObject();
		query.put("commitID", commitID);
		query.put("project", project);
		DBCollection commits = db.getCollection("commit");
		DBObject DBCommit = commits.findOne(query);
		if (DBCommit != null) {
			CommitBean commit = DBObjectToCommit(DBCommit);
			return commit;
		}
		return null;
	}
	
	public static List<CommitBean> findHasCore(){
		DB db = Connection.getConnection();
		DBObject query = new BasicDBObject();
		query.put("hasCoreClass", true);
		DBCollection commits = db.getCollection("commit");
		DBCursor cursor = commits.find(query);
		List<CommitBean> commitList = new ArrayList<CommitBean>();
		for(DBObject obj:cursor){
			commitList.add(DBObjectToCommit(obj));
		}
		return commitList;
	}
	
	public static List<CommitBean> findAll(){
		DB db = Connection.getConnection();
		DBCollection commits = db.getCollection("commit");
		DBCursor cursor = commits.find();
		List<CommitBean> commitList = new ArrayList<CommitBean>();
		for(DBObject obj:cursor){
			commitList.add(DBObjectToCommit(obj));
		}
		return commitList;
	}
	
	public static List<CommitBean> findByProject(String project){
		DB db =Connection.getConnection();
		DBCollection commits = db.getCollection("commit");
		DBObject query = new BasicDBObject();
		query.put("project", project);
		DBCursor cursor = commits.find(query);
		List<CommitBean> commitList = new ArrayList<CommitBean>();
		for(DBObject obj:cursor){
			commitList.add(DBObjectToCommit(obj));
		}
		return commitList;
	}
	
	
	public static void update(CommitBean commitBean){
		DB db = Connection.getConnection();
		DBObject query = new BasicDBObject();
		query.put("commitID", commitBean.getCommitID());
		query.put("project", commitBean.getProject());
		DBCollection commits = db.getCollection("commit");
		commits.update(query, commitToDBObject(commitBean));
	}

}
