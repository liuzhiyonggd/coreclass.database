package sysu.coreclass.database.relation;


import java.util.ArrayList;

/**
 * ��ʱ�ռ�,�������ִ�й����в�������ʾ��Ϣ��
 * 
 * @author Troy
 *
 */
public class TemporarySpace {

	public static ArrayList<String> consoleString = new ArrayList<String>();// ����̨��ʾ�ַ�
	public static ArrayList<String> designDecisionString = new ArrayList<String>();// �Ҳ�design
																					// decision������ʾ�ַ�
	public static ArrayList<String> LDASubjectString = new ArrayList<String>();// �Ҳ�LDA
																				// Subject������ʾ�ַ�

	public static ArrayList<String> getLDASubjectString() {
		return LDASubjectString;
	}

	public static void setLDASubjectString(String str) {
		LDASubjectString.add(str);
	}

	public static ArrayList<String> getDesignDecisionString() {
		return designDecisionString;
	}

	public static void setDesignDecisionString(String str) {
		TemporarySpace.designDecisionString.add(str);
	}

	public static ArrayList<String> getConsoleString() {
		return consoleString;
	}

	public static void setConsoleString(String str) {
		TemporarySpace.consoleString.add(str);
	}

}
