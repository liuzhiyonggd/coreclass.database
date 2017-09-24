package sysu.coreclass.database.relation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A boundary class for file filters. It communicates with {@link File},
 * {@link BufferedReader}, {@link FileReader}, and other 6 classes.
 * <p>
 * It allows:
 * <ul>
 * <li>filteringing annotation 4 folder; and</li>
 * <li>filteringing annotation 4 file.</li>
 * </ul>
 */
public class FileFilter {


	public void filteringAnnotation4folder(String filePath) {
		File file = new File(filePath);
		if (file.isDirectory()) {

			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {
					filteringAnnotation4file(fileList[i]);
				} else {
					filteringAnnotation4folder(fileList[i].getAbsolutePath());
				}
			}
		}
	}

	// �����ļ����ÿһ�У�����//��ʼ��ע���й���
	public void filteringAnnotation4file(File file) {
		BufferedReader File_pwd;
		try {
			File_pwd = new BufferedReader(new FileReader(file.getAbsolutePath()));

			List<String> list = new ArrayList<String>();
			// �������ļ��е���ʱ����
			String temp;
			try {

				do {
					// ����ѭ����ȡ�ļ�

					temp = File_pwd.readLine();

					// System.out.println("��ȡ����ԭ�ļ�:"+temp);
					list.add(temp);
					// �Ѷ�ȡ�����д����������
				} while (temp != null);

				File_pwd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ������д�����ļ�
			try {
				BufferedWriter File_bak = new BufferedWriter(new FileWriter(new File(file.getAbsolutePath())));
				String s = new String();
				// Ϊע���еı�ʾ
				int commentFlag = 0;
				for (int j = 0; j < list.size() - 1; j++) {
					// ʹ��ѭ�������ַ���ȡ����,������replaceall����,���ַ����ݽ���������ʽ�滻
					s = list.get(j);
					if (s.indexOf("//") >= 0) {// || s.indexOf("*") >= 0) {
						s = s.substring(0, s.indexOf("//"));
						commentFlag = 1;
					} else {
						if (!"".equals(s.trim())) {
							commentFlag = 0;

						}
					}
					// ���ǰһ��Ϊע���У�����Ϊ������ɾ��

					if (commentFlag == 1) {

						if (!"".equals(s.trim())) {

							s.replace(" ", "    ");

							File_bak.write(s + "\n");

						}

					} else {

						s.replace(" ", "    ");

						File_bak.write(s + "\n");

					}

				}
				// ������ˢ��,������close�ر�
				File_bak.flush();

				File_bak.close();
				// System.out.println("file write success");
				TemporarySpace.setConsoleString("file write success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		FileFilter filter = new FileFilter();
		filter.filteringAnnotation4file(new File("D:/Users/test/test1/src/MainClass.java"));

	}
}
