package sysu.coreclass.database.relation;

import java.util.ArrayList;
import java.util.List;


//
/**
 * A trivial entity class for history comparators. It includes accessors and
 * mutators to the history comparator's state.
 * <p>
 * It provides access to:
 * <ul>
 * <li>class revision history array list;</li>
 * <li>history array list; and</li>
 * <li>paths.</li>
 * </ul>
 * <p>
 * It allows managing:
 * <ul>
 * <li>history;</li>
 * <li>paths; and</li>
 * <li>class revision history.</li>
 * </ul>
 */
public class HistoryComparator {

	// ��ʷ�仯��Ϣ
	public ArrayList<VersionChange> history;
	public String[] paths;
	public ArrayList<ClassRevisionHistory> classRevisionHistory;

	public ArrayList<ClassRevisionHistory> getClassRevisionHistory() {
		return classRevisionHistory;
	}

	public void setClassRevisionHistory(ArrayList<ClassRevisionHistory> classRevisionHistory) {
		this.classRevisionHistory = classRevisionHistory;
	}

	public void generateHistory(String[] paths) {
		history = new ArrayList<VersionChange>();
		VersionComparator1 comparator = new VersionComparator1();
		FileFilter filter = new FileFilter();
		if (paths.length > 1) {
			filter.filteringAnnotation4folder(paths[0]);
			for (int i = 1; i < paths.length; i++) {
				filter.filteringAnnotation4folder(paths[i]);
				comparator.compareProject(paths[i - 1], paths[i]);
				// comparator.getChange().writeLog("D:/a/log"+i);
				history.add(comparator.getChange());
			}
		}
		setHistory(history);

	}

	public void compareHistory() {
		classRevisionHistory = new ArrayList<ClassRevisionHistory>();
		boolean tag;
		for (int i = 0; i < history.size(); i++) {
			List<ClassChange> changeList = history.get(i).getChangedClassList();

			for (int j = 0; j < changeList.size(); j++) {
				tag = false;
				for (int p = 0; p < classRevisionHistory.size(); p++) {
					if (classRevisionHistory.get(p).getClassa().getNewClass().getName().getFullyQualifiedName()
							.equals(changeList.get(j).getOldClass().getName().getFullyQualifiedName())) {
						classRevisionHistory.get(p).addRevisionTime();
						tag = true;
					}
				}
				if (tag == false) {
					ClassRevisionHistory classRevision = new ClassRevisionHistory();
					classRevision.setClassa(changeList.get(j));
					classRevision.addRevisionTime();
					classRevisionHistory.add(classRevision);
				}
			}
		}

		for (int i = 0; i < classRevisionHistory.size(); i++) {
			System.out.println("Class " + classRevisionHistory.get(i).getClassa().getNewClass().getName()
					+ " Revisioin Time :  " + classRevisionHistory.get(i).getRevisionTime());
			TemporarySpace.setConsoleString("Class " + classRevisionHistory.get(i).getClassa().getNewClass().getName()
					+ " Revisioin Time :  " + classRevisionHistory.get(i).getRevisionTime());
		}
	}

	public ArrayList<VersionChange> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<VersionChange> history) {
		this.history = history;
	}

	public String[] getPaths() {
		return paths;
	}

	public void setPaths(String[] paths) {
		this.paths = paths;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] pathList = {

				"D:/workspace/SOURCE/jdom source code/jdom-b4/src/java/org/jdom",
				"D:/workspace/SOURCE/jdom source code/jdom-b5/src/java/org/jdom",
				"D:/workspace/SOURCE/jdom source code/jdom-b6/src/java/org/jdom",
				"D:/workspace/SOURCE/jdom source code/jdom-b7/src/java/org/jdom",
				"D:/workspace/SOURCE/jdom source code/jdom-b8/src/java/org/jdom",

		};

		HistoryComparator c = new HistoryComparator();
		c.setPaths(pathList);
		c.generateHistory(c.paths);
		// System.out.println("test---------");
		for (int i = 0; i < c.history.size(); i++) {
			c.history.get(i).generateLDAInput(
					"D:/a/change" + i + "vs" + (i + 1) + "/" + "version(" + i + "vs" + (i + 1) + ")" + ".txt");
		}
		c.compareHistory();

	}

}
