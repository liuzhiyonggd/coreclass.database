package sysu.coreclass.database.insert;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

import sysu.coreclass.database.bean.Revision;


public class RevisionTool {
	
	static {
        DAVRepositoryFactory.setup();  
    }  
  

	public static List<Revision> getRevisions(String url,String project) throws SVNException {
		SVNURL repositoryBaseUrl = SVNURL.parseURIEncoded(url);
        
        SVNRepository repository = SVNRepositoryFactory.create(repositoryBaseUrl);
        System.out.println("create repository.");
        
        Collection<SVNLogEntry> logEntries = new LinkedList<SVNLogEntry>();
        repository.log(new String[] { "/" }, logEntries, 0,-1, true, true);
        
        List<Revision> revisionList = new ArrayList<Revision>();
        for (SVNLogEntry logEntry : logEntries) {
        	
        	Revision revision = new Revision(); 
        	revision.setProject(project);
        	revision.setAuthor(logEntry.getAuthor());
        	revision.setComment(logEntry.getMessage());
        	revision.setDate(logEntry.getDate());
        	revision.setId((int)logEntry.getRevision());
        	List<String> pathList = new ArrayList<String>();
        	if (logEntry.getChangedPaths().size() > 0) {
        		for ( Object obj: logEntry.getChangedPaths().entrySet()) {
        			Entry<String, SVNLogEntryPath> entry = (Entry<String, SVNLogEntryPath>)obj;
        			if(entry.getValue().toString().indexOf(".java")>0){
        				pathList.add(entry.getValue().toString());
        			}
        		}
        		
        	}
        	revision.setFiles(pathList);
        	revisionList.add(revision);
        }

		return revisionList;
	}

}
