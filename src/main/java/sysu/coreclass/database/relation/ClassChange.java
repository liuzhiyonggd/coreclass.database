package sysu.coreclass.database.relation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;


//������¼��ͬ�汾֮�����ı仯
//�����еķ�����Ҫ����getter��setter������list���͵����ԣ�����adder��������������һ����Ԫ��

public class ClassChange {

	private int id;
	private ArrayList<Integer[]> relationship;
	private List<ChangeStatement> statementList = new ArrayList<ChangeStatement>();

	// �ɵ���
	private TypeDeclaration oldClass;
	// �µ���
	private TypeDeclaration newClass;
	// �ɵ����package����
	// ����AST������ʱ����һ���ļ��������һ��package�����������ǿ��ܰ�������࣬������������� û�а���package
	// ���ܿ��Դ��ඨ���parentԪ�ػ�ã�����Ŀǰû��ʹ��
	private String oldPackageName;
	// �µ����package
	private String newPackageName;
	// �ɵ����Ӧ��import�ļ����б����ɺ�package����
	// ���import��Ϣ�������ж�������֮���Ƿ����������ϵ�������жϿ��ܻ��д�����Ϊһ���ļ��п��ܰ�������࣬��import���������Ƕ�����Ӧ��import�ļ�
	// ����һ��һ����洢��һ���������ļ��У�����Ŀǰ��û�����ɴ���ɵ�һЩ����
	private List<ImportDeclaration> oldImportList;
	// �µ����Ӧ��import�ļ����б�
	private List<ImportDeclaration> newImportList;

	private List a;

	// �����ķ�������
	private List<MethodDeclaration> addedMethods;
	// �����޸ļ�¼
	private List<MethodChange> changedMethods;
	// ɾ�����ķ�������
	private List<MethodDeclaration> deletedMethods;
	// ���������Զ���
	private List<FieldDeclaration> addedFields;
	// ɾ���������Զ���

	private List<FieldDeclaration> deletedFields;

	public ClassChange() {

	}

	// ��ʼ������
	public ClassChange(TypeDeclaration oldClass, TypeDeclaration newClass, String oldPackageName, String newPackageName,
			List<ImportDeclaration> oldImportList, List<ImportDeclaration> newImportList) {
		this.oldPackageName = oldPackageName;
		this.newPackageName = newPackageName;
		this.oldImportList = oldImportList;
		this.newImportList = newImportList;

		this.oldClass = oldClass;
		this.newClass = newClass;

	}

	public List<ChangeStatement> getStatementList() {
		return statementList;
	}

	public void setStatementList(List<ChangeStatement> statementList) {
		this.statementList = statementList;
	}

	public void addStatement(ChangeStatement sta) {
		statementList.add(sta);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Integer[]> getRelationship() {

		return relationship;
	}

	public void setRelationship(ArrayList<Integer[]> relationship) {
		this.relationship = relationship;
	}

	public boolean addRevisionRelationship(int classId, int type) {
		boolean tag = false;
		// System.out.println("testing");
		if (relationship != null && relationship.size() > classId) {
			// System.out.println("testing added");
			tag = true;
			if (relationship.get(classId) == null) {
				relationship.set(classId, new Integer[100]);
			}
			if (relationship.get(classId)[type] == null) {
				relationship.get(classId)[type] = new Integer(0);
			}
			relationship.get(classId)[type]++;

		}

		return tag;
	}

	public boolean deleteRevisionRelationship(int classId, int type) {
		boolean tag = false;
		// System.out.println("testing");
		if (relationship != null && relationship.size() > classId) {
			// System.out.println("testing added");
			tag = true;
			if (relationship.get(classId) == null) {
				relationship.set(classId, new Integer[200]);
			}
			if (relationship.get(classId)[type] == null) {
				relationship.get(classId)[type] = new Integer(0);
			}
			relationship.get(classId)[type]--;

		}

		return tag;
	}

	public boolean ifRelated(int classId) {
		boolean tag = false;
		if (relationship != null && relationship.size() >= classId) {
			if (relationship.get(classId) != null) {
				for (int i = 0; i < 100; i++) {
					if (relationship.get(classId)[i] != null && relationship.get(classId)[i].intValue() != 0) {
						tag = true;
					}
				}
			}
		}
		return tag;
	}

	public void initializeRelationship(int classId) {

		if (relationship != null && relationship.size() >= classId) {
			if (classId < relationship.size() && relationship.get(classId) != null) {
				for (int i = 0; i < 100; i++) {
					relationship.get(classId)[i] = new Integer(0);

				}

			}
		}
	}

	public void initRelationship(int size) {
		if (relationship == null) {
			relationship = new ArrayList<Integer[]>();
			for (int i = 0; i < size; i++) {
				relationship.set(i, new Integer[100]);
			}
		}
	}

	public List<ImportDeclaration> getOldImportList() {
		return oldImportList;
	}

	public void setOldImportList(List<ImportDeclaration> oldImportList) {
		this.oldImportList = oldImportList;
	}

	public List<ImportDeclaration> getNewImportList() {
		return newImportList;
	}

	public void setNewImportList(List<ImportDeclaration> newImportList) {
		this.newImportList = newImportList;
	}

	public String getOldPackageName() {
		return oldPackageName;
	}

	public void setOldPackageName(String oldPackageName) {
		this.oldPackageName = oldPackageName;
	}

	public String getNewPackageName() {
		return newPackageName;
	}

	public void setNewPackageName(String newPackageName) {
		this.newPackageName = newPackageName;
	}

	public TypeDeclaration getOldClass() {
		return oldClass;
	}

	public void setOldClass(TypeDeclaration oldClass) {
		this.oldClass = oldClass;
	}

	public TypeDeclaration getNewClass() {
		return newClass;
	}

	public void setNewClass(TypeDeclaration newClass) {
		this.newClass = newClass;
	}

	public List<MethodDeclaration> getAddedMethods() {
		if (addedMethods == null) {
			addedMethods = new ArrayList<MethodDeclaration>();
		}
		return addedMethods;
	}

	public void setAddedMethods(List<MethodDeclaration> addedMethods) {

		this.addedMethods = addedMethods;
	}

	public void addAddedMethod(MethodDeclaration method) {

		this.getAddedMethods().add(method);
	}

	public List<MethodChange> getChangedMethods() {
		if (changedMethods == null) {
			changedMethods = new ArrayList<MethodChange>();
		}
		return changedMethods;
	}

	public void setChangedMethods(List<MethodChange> changedMethods) {
		this.changedMethods = changedMethods;
	}

	public void addChangedMethod(MethodChange method) {

		this.getChangedMethods().add(method);
	}

	public List<MethodDeclaration> getDeletedMethods() {
		if (deletedMethods == null) {
			deletedMethods = new ArrayList<MethodDeclaration>();
		}
		return deletedMethods;
	}

	public void setDeletedMethods(List<MethodDeclaration> deletedMethods) {

		this.deletedMethods = deletedMethods;
	}

	public void addDeletedMethod(MethodDeclaration method) {

		this.getDeletedMethods().add(method);
	}

	public List<FieldDeclaration> getAddedFields() {
		if (addedFields == null) {
			addedFields = new ArrayList<FieldDeclaration>();
		}
		return addedFields;
	}

	public void setAddedFields(List<FieldDeclaration> addedFields) {

		this.addedFields = addedFields;
	}

	public void addAddedField(FieldDeclaration filed) {

		this.getAddedFields().add(filed);
	}

	public List<FieldDeclaration> getDeletedFields() {
		if (deletedFields == null) {
			deletedFields = new ArrayList<FieldDeclaration>();
		}
		return deletedFields;
	}

	public void setDeletedFields(List<FieldDeclaration> deletedFields) {

		this.deletedFields = deletedFields;
	}

	public void addDeletedField(FieldDeclaration filed) {

		this.getDeletedFields().add(filed);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
