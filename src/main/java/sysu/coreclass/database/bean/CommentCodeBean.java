package sysu.coreclass.database.bean;

import java.util.ArrayList;
import java.util.List;

public class CommentCodeBean {
	
	private String classCode;
	private List<IndexBean> blockCommentList = new ArrayList<IndexBean>();
	private List<IndexBean> javadocCommentList= new ArrayList<IndexBean>();
	private List<IndexBean> lineCommentList = new ArrayList<IndexBean>();
	private List<IndexBean> blockCodeList = new ArrayList<IndexBean>();
	private List<IndexBean> javadocCodeList = new ArrayList<IndexBean>();
	private List<IndexBean> lineCodeList = new ArrayList<IndexBean>();
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public List<IndexBean> getBlockCommentList() {
		return blockCommentList;
	}
	public void setBlockCommentList(List<IndexBean> blockCommentList) {
		this.blockCommentList = blockCommentList;
	}
	public List<IndexBean> getJavadocCommentList() {
		return javadocCommentList;
	}
	public void setJavadocCommentList(List<IndexBean> javadocCommentList) {
		this.javadocCommentList = javadocCommentList;
	}
	public List<IndexBean> getLineCommentList() {
		return lineCommentList;
	}
	public void setLineCommentList(List<IndexBean> lineCommentList) {
		this.lineCommentList = lineCommentList;
	}
	public List<IndexBean> getBlockCodeList() {
		return blockCodeList;
	}
	public void setBlockCodeList(List<IndexBean> blockCodeList) {
		this.blockCodeList = blockCodeList;
	}
	public List<IndexBean> getJavadocCodeList() {
		return javadocCodeList;
	}
	public void setJavadocCodeList(List<IndexBean> javadocCodeList) {
		this.javadocCodeList = javadocCodeList;
	}
	public List<IndexBean> getLineCodeList() {
		return lineCodeList;
	}
	public void setLineCodeList(List<IndexBean> lineCodeList) {
		this.lineCodeList = lineCodeList;
	}
	
	
	
}
