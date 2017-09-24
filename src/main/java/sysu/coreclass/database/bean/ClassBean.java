package sysu.coreclass.database.bean;


import java.util.ArrayList;
import java.util.List;


public class ClassBean {
	private int classID;
	private int commitID;
	private String project;
	private String className;
	private String parentName;
	private List<String> interfaceList;
	private String classType;
	private String comment;
	private int innerCount=0;
	private int outterCount=0;
	private boolean isCore=false;
	private int changeMethodNum;
	private int newMethodNum;
	private int deleteMethodNum;
	private List<String> useClassList=new ArrayList<String>();
	private List<MethodInvoke> methodInvokeList = new ArrayList<MethodInvoke>();
	private int classIndex;
	
	
	public int getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

	public List<MethodInvoke> getMethodInvokeList() {
		return methodInvokeList;
	}

	public void setMethodInvokeList(List<MethodInvoke> methodInvokeList) {
		this.methodInvokeList = methodInvokeList;
	}

	public int getChangeMethodNum() {
		return changeMethodNum;
	}

	public void setChangeMethodNum(int changeMethodNum) {
		this.changeMethodNum = changeMethodNum;
	}

	public int getNewMethodNum() {
		return newMethodNum;
	}

	public void setNewMethodNum(int newMethodNum) {
		this.newMethodNum = newMethodNum;
	}

	public int getDeleteMethodNum() {
		return deleteMethodNum;
	}

	public void setDeleteMethodNum(int deleteMethodNum) {
		this.deleteMethodNum = deleteMethodNum;
	}

	public List<String> getUseClassList() {
		return useClassList;
	}

	public void setUseClassList(List<String> useClassList) {
		this.useClassList = useClassList;
	}

	public int getInnerCount() {
		return innerCount;
	}

	public void setInnerCount(int innerCount) {
		this.innerCount = innerCount;
	}

	public int getOutterCount() {
		return outterCount;
	}

	public void setOutterCount(int outterCount) {
		this.outterCount = outterCount;
	}

	public boolean isCore() {
		return isCore;
	}

	public void setCore(boolean isCore) {
		this.isCore = isCore;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public int getCommitID() {
		return commitID;
	}

	public void setCommitID(int commitID) {
		this.commitID = commitID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<String> getInterfaceList() {
		return interfaceList;
	}

	public void setInterfaceList(List<String> interfaceList) {
		this.interfaceList = interfaceList;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}
	
	

}
