package sysu.coreclass.database.download;



import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.SVNProperties;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class Download {

	static {
		DAVRepositoryFactory.setup();
	}

	private Download() {

	}

	/**
	 * 
	 * @param url
	 *            要下载的版本的url地址
	 * @param revisionNum
	 *            要下载的版本的版本号
	 * @param savedPath
	 *            下载文件的保存路径
	 * @throws SVNException
	 * @throws IOException
	 */
	public static void download(String url, int revisionNum, String savedPath) throws SVNException, IOException {
		SVNURL repositoryBaseUrl = SVNURL.parseURIEncoded(url);
		SVNRepository repository = SVNRepositoryFactory.create(repositoryBaseUrl);
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager();
		repository.setAuthenticationManager(authManager);

		Collection<SVNLogEntry> logEntries = new LinkedList<SVNLogEntry>();
		repository.log(new String[] { "/" }, logEntries, revisionNum, revisionNum, true, true);

		for (SVNLogEntry logEntry : logEntries) {

			File dir1 = new File(savedPath+"/"  + revisionNum + "/new/src");// 当前版本文件的存储路径
			File dir2 = new File(savedPath+"/"  + revisionNum + "/old/src");// 前一个版本文件的存储路径
			File dir3 = new File(savedPath+"/"  + revisionNum + "/files");//结果文件的存储路径
			if (!dir1.exists()) { 
				dir1.mkdirs();
			}
			if (!dir2.exists()) {
				dir2.mkdirs();
			}
			if(!dir3.exists()){
				dir3.mkdirs();
			}
			File logFile = new File(dir1.getPath() + "/" + logEntry.getRevision() + ".txt");
			BufferedWriter output = new BufferedWriter(new FileWriter(logFile));

			output.write("---------------------------------------------\r\n");

			// 获取修改版本号

			output.write("修订版本号: " + logEntry.getRevision() + "\r\n");

			// 获取提交者

			output.write("提交者: " + logEntry.getAuthor() + "\r\n");

			// 获取提交时间

			output.write("日期: " + logEntry.getDate() + "\r\n");

			// 获取注释信息

			output.write("注释信息: " + logEntry.getMessage() + "\r\n");

			if (logEntry.getChangedPaths().size() > 0) {

				output.write("受影响的文件、目录:\r\n");

				for (Object obj : logEntry.getChangedPaths().entrySet()) {
					Entry<String, SVNLogEntryPath> entry = (Entry<String, SVNLogEntryPath>)obj;
					output.write(entry.getValue() + "\r\n");

					// 此变量用来存放要查看的文件的属性名/属性值列表。
					SVNProperties fileProperties = new SVNProperties(logFile, savedPath);

					String temp = entry.getValue().toString();
					String filePath = "";// 文件的下载路径
					if (temp.indexOf("(") > 0) {
						filePath = temp.substring(temp.indexOf("/"), temp.indexOf("("));
					} else {
						filePath = temp.substring(temp.indexOf("/"));
					}

					String[] temps = filePath.split("/");
					String srcname = temps[temps.length - 1];// 获得文件名，不含路径

					// 只提取java文件
					if (srcname.endsWith(".java")) {
						if (repository.checkPath(filePath, logEntry.getRevision()) != SVNNodeKind.NONE) {
							// 此输出流用来存放要查看的文件的内容。
							ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();// 当前版本的文件输出流

							repository.getFile(filePath, logEntry.getRevision(), fileProperties.asMap(), outputStream1);
							BufferedWriter output1 = new BufferedWriter(new FileWriter(dir1 + "/" + srcname));
							output1.write(outputStream1.toString());
							outputStream1.close();

							output1.close();
						}

						if (logEntry.getRevision() > 0
								&& repository.checkPath(filePath, logEntry.getRevision() - 1) != SVNNodeKind.NONE) {
							ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();// 前一个版本的文件输出流

							repository.getFile(filePath, logEntry.getRevision() - 1, fileProperties.asMap(), outputStream2);
							BufferedWriter output2 = new BufferedWriter(new FileWriter(dir2 + "/" + srcname));
							output2.write(outputStream2.toString());
							outputStream2.close();
							output2.close();

						}
					}
				}

			}
			output.close();

		}

	}

	/**
	 * 输出当前项目的SVN记录信息
	 * 
	 * @param url
	 * @param infoFilePath
	 * @throws SVNException
	 * @throws IOException
	 */
	public static void writeInfo(String url, String infoFilePath) throws SVNException, IOException {
		SVNURL repositoryBaseUrl = SVNURL.parseURIEncoded(url);
		// "http://svn.code.sf.net/p/java-game-lib/code/trunk"
		SVNRepository repository = SVNRepositoryFactory.create(repositoryBaseUrl);
		System.out.println("create repository.");
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager();
		repository.setAuthenticationManager(authManager);

		Collection<SVNLogEntry> logEntries = new LinkedList<SVNLogEntry>();
		repository.log(new String[] { "/" }, logEntries, 0, -1, true, true);
		File logFile = new File(infoFilePath);

		BufferedWriter output = new BufferedWriter(new FileWriter(logFile));
		for (SVNLogEntry logEntry : logEntries) {

			System.out.println("---------------------------------------------");
			output.write("***\r\n");

			// 获取修改版本号

			System.out.println("修订版本号: " + logEntry.getRevision());
			output.write(logEntry.getRevision() + "\r\n");

			// 获取提交者

			System.out.println("提交者: " + logEntry.getAuthor());
			output.write(logEntry.getAuthor() + "\r\n");

			// 获取提交时间

			System.out.println("日期: " + logEntry.getDate());
			output.write(logEntry.getDate() + "\r\n");

			// 获取注释信息

			System.out.println("注释信息: " + logEntry.getMessage());
			output.write(logEntry.getMessage() + "\r\n");

			if (logEntry.getChangedPaths().size() > 0) {

				System.out.println();
				for (Object obj : logEntry.getChangedPaths().entrySet()) {
					Entry<String, SVNLogEntryPath> entry = (Entry<String, SVNLogEntryPath>)obj;
					if (entry.getValue().toString().indexOf(".java") > 0) {
						System.out.println(entry.getValue());
						output.write(entry.getValue() + "\r\n");
					}
				}
			}
		}
		output.close();
	}

	public static void main(String[] args) {

		try {
			Download.download("http://svn.code.sf.net/p/jhotdraw/svn/trunk", 21, "/home/angel/log");
			Download.writeInfo("svn://svn.code.sf.net/p/quickfixj/code/trunk", "/home/angel/log/quickfixj.txt");
		} catch (SVNException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}