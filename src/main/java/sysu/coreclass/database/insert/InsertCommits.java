package sysu.coreclass.database.insert;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.tmatesoft.svn.core.SVNException;

import sysu.coreclass.database.bean.CommitBean;
import sysu.coreclass.database.bean.Revision;
import sysu.coreclass.database.dao.CommitDao;


public class InsertCommits {

	public static void insertCommits(String path, String project) {
		List<Revision> revisions = new ArrayList<Revision>();
		try {
			revisions = RevisionTool.getRevisions(path,project);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<CommitBean> commits = new ArrayList<CommitBean>();
		for (Revision revision : revisions) {
			CommitBean commit = new CommitBean();
			commit.setCommitID(revision.getId());
			commit.setAuthor(revision.getAuthor());
			commit.setDate(revision.getDate());
			commit.setMessage(revision.getComment());
			commit.setProject(project);
			commit.setFileList(revision.getFiles());
			commits.add(commit);
		}

		for (CommitBean commit : commits) {
			CommitDao.insertOne(commit);
		}
	}

	public static void main(String[] args) {
		
		InsertCommits.insertCommits("http://svn.code.sf.net/p/jhotdraw/svn/trunk", "jhotdraw");
		
		
	}

}
