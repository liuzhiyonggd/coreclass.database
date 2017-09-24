package sysu.coreclass.database.insert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.mongodb.DB;

import sysu.coreclass.database.bean.ClassBean;
import sysu.coreclass.database.bean.CommitBean;
import sysu.coreclass.database.bean.MethodBean;
import sysu.coreclass.database.bean.MethodInvoke;
import sysu.coreclass.database.bean.StatementBean;
import sysu.coreclass.database.dao.ClassDao;
import sysu.coreclass.database.dao.CommitDao;
import sysu.coreclass.database.dao.MethodDao;
import sysu.coreclass.database.relation.ClassChange;
import sysu.coreclass.database.relation.InvokeAnalysis;
import sysu.coreclass.database.relation.VersionChange;
import sysu.coreclass.database.relation.VersionComparator1;
import sysu.coreclass.database.tool.Connection;


public class InsertClass {

	public static void insert(int commitID, String project) {

		
		DB db = Connection.getConnection();
		CommitBean commit = CommitDao.findByID(commitID, project);
		if(commit==null)return;
		String commitMessage = commit.getMessage();
		VersionComparator1 comp = new VersionComparator1();
		comp.compareProject("c:/zhiyong/log/" + project + "/" + commitID + "/old",
				"c:/zhiyong/log/" + project + "/" + commitID + "/new");
		VersionChange change = comp.getChange();
		if(change.getAcClassList().size()>200)return;
		change.compareChangType4Direct60();
		
		

		List<ClassChange> addClassList = change.getAddedClassList();

		List<ClassBean> classBeanList = new ArrayList<ClassBean>();
		List<MethodBean> methodBeanList = new ArrayList<MethodBean>();
		for (int i = 0; i < addClassList.size(); i++) {
			
			List<MethodInvoke> methodInvokeList = InvokeAnalysis.findInvoke(addClassList.get(i), change.getAcClassList(), "new", change.getAddedClassList().size());
			
			
			
			ClassBean classBean = new ClassBean();
			classBean.setClassID(i);
			classBean.setCommitID(commitID);
			classBean.setClassName(addClassList.get(i).getNewClass().getName().getFullyQualifiedName());

			classBean.setNewMethodNum(addClassList.get(i).getNewClass().getMethods().length);
			if (addClassList.get(i).getNewClass().getSuperclassType() != null) {
				classBean.setParentName(addClassList.get(i).getNewClass().getSuperclassType().toString());
			} else {
				classBean.setParentName("Object");
			}
			classBean.setProject(project);
			classBean.setClassType("new");
			List<String> interfaces = new ArrayList<String>();
			for (int j = 0; j < addClassList.get(i).getNewClass().superInterfaceTypes().size(); j++) {
				interfaces.add(addClassList.get(i).getNewClass().superInterfaceTypes().get(j).toString());
			}
			classBean.setInterfaceList(interfaces);
			
			
			classBean.setOutterCount(computeOuterCount(classBean, addClassList.get(i).getNewClass(), change.getAcClassList()));
			classBean.setMethodInvokeList(methodInvokeList);
			classBeanList.add(classBean);
			//ClassDao.insertOne(classBean, db);
			
			for (int j = 0; j < addClassList.get(i).getNewClass().getMethods().length; j++) {
				MethodDeclaration method = addClassList.get(i).getNewClass().getMethods()[j];
				MethodBean methodBean = new MethodBean();
				methodBean.setProject(project);
				methodBean.setClassID(classBean.getClassID());
				methodBean.setClassName(classBean.getClassName());
				methodBean.setMethodID(j);
				methodBean.setCommitID(commitID);
				methodBean.setMethodType("new");
				methodBean.setMethodName(method.getName().getFullyQualifiedName());

				if (method.getReturnType2() != null) {
					methodBean.setReturnType(method.getReturnType2().toString());
				} else {
					methodBean.setReturnType("null");
				}

				List<String> methodParameters = new ArrayList<String>();
				for (int k = 0; k < method.parameters().size(); k++) {
					methodParameters.add(method.parameters().get(k).toString());
				}
				methodBean.setParameters(methodParameters);
				
				methodBean.setOutterCount(computeMethodOuterCount(methodBean.getMethodName(), methodInvokeList));
				
				
				List<StatementBean> statementList = new ArrayList<StatementBean>();
				if(method.getBody()!=null){
					for(int l=0,n=method.getBody().statements().size();l<n;l++){
						StatementBean statementBean = new StatementBean();
						Statement sta = (Statement)method.getBody().statements().get(l);
						statementBean.setStatement(sta);
						statementBean.setStatementeType("new");
						statementBean.setStatementID(l);
						statementList.add(statementBean);
					}
				}
				methodBean.setStatementList(statementList);
				methodBeanList.add(methodBean);
//				MethodDao.insertOne(methodBean, db);
			}
		}

		List<ClassChange> changeClassList = change.getChangedClassList();
		for (int i = 0; i < changeClassList.size(); i++) {
			
			List<MethodInvoke> methodInvokeList = InvokeAnalysis.findInvoke(changeClassList.get(i), change.getAcClassList(), "new", change.getAddedClassList().size());
			
			ClassBean classBean = new ClassBean();
			classBean.setClassID(addClassList.size() + i);
			classBean.setCommitID(commitID);
			classBean.setClassName(changeClassList.get(i).getNewClass().getName().getFullyQualifiedName());

			classBean.setChangeMethodNum(changeClassList.get(i).getChangedMethods().size());
			classBean.setNewMethodNum(changeClassList.get(i).getAddedMethods().size());
			classBean.setDeleteMethodNum(changeClassList.get(i).getDeletedMethods().size());
			if (changeClassList.get(i).getNewClass().getSuperclassType() != null) {
				classBean.setParentName(changeClassList.get(i).getNewClass().getSuperclassType().toString());
			} else {
				classBean.setParentName("Object");
			}
			classBean.setProject(project);
			classBean.setClassType("change");
			List<String> interfaces = new ArrayList<String>();
			for (int j = 0; j < changeClassList.get(i).getNewClass().superInterfaceTypes().size(); j++) {
				interfaces.add(changeClassList.get(i).getNewClass().superInterfaceTypes().get(j).toString());
			}
			classBean.setInterfaceList(interfaces);
			
			classBean.setOutterCount(computeOuterCount(classBean, changeClassList.get(i).getNewClass(), change.getAcClassList()));
			classBeanList.add(classBean);
			//ClassDao.insertOne(classBean, db);

			for (int j = 0; j < changeClassList.get(i).getAddedMethods().size(); j++) {
				MethodDeclaration method = changeClassList.get(i).getAddedMethods().get(j);
				MethodBean methodBean = new MethodBean();
				methodBean.setProject(project);
				methodBean.setClassID(classBean.getClassID());
				methodBean.setClassName(classBean.getClassName());
				methodBean.setMethodID(j);
				methodBean.setCommitID(commitID);
				methodBean.setMethodType("new");
				methodBean.setMethodName(method.getName().getFullyQualifiedName());

				if (method.getReturnType2() != null) {
					methodBean.setReturnType(method.getReturnType2().toString());
				} else {
					methodBean.setReturnType("null");
				}

				List<String> methodParameters = new ArrayList<String>();
				for (int k = 0; k < method.parameters().size(); k++) {
					methodParameters.add(method.parameters().get(k).toString());
				}
				methodBean.setParameters(methodParameters);
				methodBean.setOutterCount(computeMethodOuterCount(methodBean.getMethodName(), methodInvokeList));
				
				List<StatementBean> statementList = new ArrayList<StatementBean>();
				if(method.getBody()!=null){
					for(int l=0,n=method.getBody().statements().size();l<n;l++){
						StatementBean statementBean = new StatementBean();
						Statement sta = (Statement)method.getBody().statements().get(l);
						statementBean.setStatement(sta);
						statementBean.setStatementeType("new");
						statementBean.setStatementID(l);
						statementList.add(statementBean);
					}
				}
				methodBean.setStatementList(statementList);
				
				methodBeanList.add(methodBean);
//				MethodDao.insertOne(methodBean, db);
			}
			for (int j = 0; j < changeClassList.get(i).getChangedMethods().size(); j++) {
				MethodDeclaration method = changeClassList.get(i).getChangedMethods().get(j).getNewMethod();
				MethodBean methodBean = new MethodBean();
				methodBean.setProject(project);
				methodBean.setClassID(classBean.getClassID());
				methodBean.setClassName(classBean.getClassName());
				methodBean.setMethodID(changeClassList.get(i).getAddedMethods().size() + j);
				methodBean.setCommitID(commitID);
				methodBean.setMethodType("change");
				methodBean.setMethodName(method.getName().getFullyQualifiedName());

				if (method.getReturnType2() != null) {
					methodBean.setReturnType(method.getReturnType2().toString());
				} else {
					methodBean.setReturnType("null");
				}

				List<String> methodParameters = new ArrayList<String>();
				for (int k = 0; k < method.parameters().size(); k++) {
					methodParameters.add(method.parameters().get(k).toString());
				}
				methodBean.setParameters(methodParameters);
				methodBean.setOutterCount(computeMethodOuterCount(methodBean.getMethodName(), methodInvokeList));
				
				methodBean.setStatementList(changeClassList.get(i).getChangedMethods().get(j).getStatementList());
				
				methodBeanList.add(methodBean);
//				MethodDao.insertOne(methodBean, db);
			}
		}
		computeInnerCount(classBeanList);
		computeMethodInnerCount(methodBeanList, classBeanList);
		for(ClassBean classBean:classBeanList){
			String splitToken = " .,;:/&|`~%+=-*<>$#@!^\\()[]{}''\"\r\n";
			StringTokenizer st = new StringTokenizer(commitMessage,splitToken,false);
			while(st.hasMoreTokens()){
				if(st.nextToken().equals(classBean.getClassName())){
					classBean.setCore(true);
					if(!commit.isHasCoreClass()){
						commit.setHasCoreClass(true);
					}
					break;
				}
			}
			ClassDao.insertOne(classBean, db);
		}
		for(MethodBean methodBean:methodBeanList){
			
			if(commitMessage.contains(methodBean.getMethodName())){
				methodBean.setCore(true);
				if(!commit.isHasCoreMethod()){
					commit.setHasCoreMethod(true);
				}
			}
			
			MethodDao.insertOne(methodBean, db);
		}
		if(commit.isHasCoreClass()||commit.isHasCoreMethod()){
			CommitDao.update(commit);
		}

	}
	
	private static int computeMethodOuterCount(String methodName,List<MethodInvoke> methodInvokeList){
		int result = 0;
		for(MethodInvoke methodInvoke:methodInvokeList){
			if(methodInvoke.getMethod1().equals(methodName)){
				result++;
			}
		}
		return result;
	}
	
	private static void computeMethodInnerCount(List<MethodBean> methodBeanList,List<ClassBean> classBeanList){
		for(MethodBean method:methodBeanList){
			int innerCount = 0;
			for(ClassBean clazz:classBeanList){
				for(MethodInvoke methodInvoke:clazz.getMethodInvokeList()){
					if(methodInvoke.getClass2().equals(method.getClassName())&&methodInvoke.getMethod2().equals(method.getMethodName())){
						innerCount++;
					}
				}
			}
			method.setInnerCount(innerCount);
		}
	}

	private static int computeOuterCount(ClassBean classBean,TypeDeclaration class1,List<ClassChange> class2List){
		
		int outerCount=0;
		for(ClassChange cc:class2List){
			for(MethodDeclaration md:class1.getMethods()){
				if(md.getBody()!=null&&md.getBody().toString().contains(cc.getNewClass().getName().getFullyQualifiedName())){
					classBean.getUseClassList().add(cc.getNewClass().getName().getFullyQualifiedName());
					outerCount++;
					break;
				}
			}
		}
		
		return outerCount;
	}
	
	private static void computeInnerCount(List<ClassBean> classBeanList){
		
		for(ClassBean classBean1:classBeanList){
			int innerCount = 0;
			String classBean1Name = classBean1.getClassName();
			for(ClassBean classBean2:classBeanList){
				if(classBean2.getUseClassList().contains(classBean1Name)){
					innerCount++;
				}
			}
			classBean1.setInnerCount(innerCount);
		}
	}
	
	public static void main(String[] args) {
		
		
		try {
			List<String> projectList = FileUtils.readLines(new File("C:/1.txt"));
			
			for(String project:projectList){
				String[] temps = project.split(",");
				int commitNum = Integer.parseInt(temps[2]);
				for(int i=0;i<=commitNum;i++){
					InsertClass.insert(i, temps[1]);
					if (i % 10 == 0) {
						System.out.println(temps[1]+":"+i + " commits were inserted");
					}
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
