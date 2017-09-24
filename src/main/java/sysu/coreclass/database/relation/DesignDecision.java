package sysu.coreclass.database.relation;

import java.util.ArrayList;
import java.util.List;

//���ڼ�¼��ƾ���
/**
 * A boundary class for design decisions. It communicates with the classes
 * {@link ClassRevisionHistory}, and ClassChange}, and consists mostly of
 * mutators to the design decision's state.
 * <p>
 * It allows managing:
 * <ul>
 * <li>involving class list.</li>
 * </ul>
 * <p>
 * It also allows:
 * <ul>
 * <li>adding involving class; and</li>
 * <li>adding involving classes.</li>
 * </ul>
 */
public class DesignDecision {

	// ��ص��ඨ��
	ArrayList<ClassRevisionHistory> involvingClassList;

	public ArrayList<ClassRevisionHistory> getInvolvingClassList() {
		if (involvingClassList == null)
			involvingClassList = new ArrayList<ClassRevisionHistory>();
		return involvingClassList;
	}

	public void setInvolvingClassList(ArrayList<ClassRevisionHistory> involvingClassList) {
		this.involvingClassList = involvingClassList;
	}

	public void addInvolvingClass(ClassChange classa, int version) {
		ClassRevisionHistory newClass = new ClassRevisionHistory();
		newClass.setClassa(classa);
		newClass.addRevisionVersion(version);
		this.getInvolvingClassList().add(newClass);
	}

	public void addInvolvingClasses(List<ClassChange> classList, int version) {
		for (int i = 0; i < classList.size(); i++) {

			ClassRevisionHistory newClass = new ClassRevisionHistory();

			newClass.setClassa(classList.get(i));

			newClass.addRevisionVersion(version);
			this.getInvolvingClassList().add(newClass);
		}
	}

}
