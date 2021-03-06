package sysu.coreclass.database.relation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class VersionComparator1 {

	private VersionChange change;

	public VersionChange getChange() {
		if (change == null) {
			change = new VersionChange();
		}
		return change;
	}

	public void setChange(VersionChange change) {
		this.change = change;
	}

	public void compareProject(String fileLocation1, String fileLocation2) {

		setChange(null);
		File file1 = new File(fileLocation1);
		File file2 = new File(fileLocation2);
		findProjectDCElement(file1, file2);
		findProjectNewElement(file1, file2);

	}

	public void findProjectDCElement(File file1, File file2) {
		String targetPath = null;
		if (file1.isDirectory()) {
			File[] files1 = file1.listFiles();
			for (int i = 0; i < files1.length; i++) {
				// ���files1[i]��java�ļ�����ʼ�������°汾������ͬ���ļ����Ƚ�
				if (files1[i].isFile() && files1[i].getName().endsWith("java")) {
					targetPath = findFile(files1[i], file2);

					// �����°���δ���ҵ���ɰ�ͬ�����ļ�������Ϊ���°�����ɾ���˸ù��ܡ�����Ӧ��Ԫ����deletedUnits
					if (targetPath == null) {
						deletedUnits(getCompilationUnit(files1[i].getAbsolutePath()));
					}
					// ����������Ԫ���бȽϣ�����compareUints����
					else {
						CompilationUnit unit1 = getCompilationUnit(files1[i].getAbsolutePath());
						CompilationUnit unit2 = getCompilationUnit(targetPath);

						compareUnits(unit1, unit2);
					}
				}
				// ���files1[i]���ļ��У����������findProjectDCElement���²���
				else {
					findProjectDCElement(files1[i], file2);
				}
			}
		}

	}

	public void findProjectNewElement(File file1, File file2) {
		String targetPath2 = null;
		if (file2.isDirectory()) {
			File[] files2 = file2.listFiles();

			for (int i = 0; i < files2.length; i++) {
				// ���files2[i]��java�ļ������ھɰ���Ѱ������ͬ�����ļ�
				if (files2[i].isFile() && files2[i].getName().endsWith(".java")) {
					targetPath2 = findFile(files2[i], file1);

					// ���ھɰ���û���ҵ�ͬ���ļ�������Ϊ�������Ĺ��ܣ����뵽�������ܵ�Ԫ��
					if (targetPath2 == null) {
						newUnits(getCompilationUnit(files2[i].getAbsolutePath()));
					}
				}
				// ���file2[i]���ļ��У����������findProjectNewElement�������²���
				else {
					findProjectNewElement(file1, files2[i]);
				}
			}
		}
	}

	// ���ļ���folder��Ѱ����ָ���ļ�file������ͬ���ֵ��ļ�
	public String findFile(File file, File folder) {
		String targetLocation = null;

		if (folder.isDirectory()) {
			File[] files = folder.listFiles();

			for (int i = 0; i < files.length; i++) {
				if ((files[i].isFile()) && files[i].getName().endsWith(".java")
						&& (file.getName().equals(files[i].getName()))) {

					// Ѱ�ҵ���ͬ���ļ������ڵİ���Ҳ��ͬ�Ļ�������Ϊ���ҳɹ�
					if (getCompilationUnit(files[i].getAbsolutePath()).getPackage() != null
							&& getCompilationUnit(file.getAbsolutePath()).getPackage() != null
							&& getCompilationUnit(files[i].getAbsolutePath()).getPackage().getName()
									.getFullyQualifiedName().equals(getCompilationUnit(file.getAbsolutePath())
											.getPackage().getName().getFullyQualifiedName())) {

						targetLocation = files[i].getAbsolutePath();

					}

					// Ѱ�ҵ���ͬ���ļ��������а��ṹ��Ҳ��Ϊ���ҳɹ�
					if (getCompilationUnit(files[i].getAbsolutePath()).getPackage() == null
							&& getCompilationUnit(file.getAbsolutePath()).getPackage() == null) {

						targetLocation = files[i].getAbsolutePath();
					}

				}
				// ���û�ҵ�ָ���ļ���files[i]��һ���ļ��У���files[i]����folder�м������²���
				if (targetLocation == null && files[i].isDirectory()) {
					targetLocation = findFile(file, files[i]);
				}
			}
		}

		return targetLocation;
	}

	public CompilationUnit getCompilationUnit(String fileLocation) {
		String content = "";

		try {
			// ��ȡ��Ҫ�������ļ������ַ�����ʽ�洢��content��
			File file = new File(fileLocation);
			BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(file));
			String tempString;

			while ((tempString = reader.readLine()) != null) {
				content = content + tempString + "\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// ���ɴ����ļ���صĳ����﷨�������������CompilationUnit���͵�result���󷵻�
		ASTParser parsert = ASTParser.newParser(AST.JLS8);

		parsert.setSource(content.toCharArray());

		CompilationUnit result = (CompilationUnit) parsert.createAST(null);

		return result;

	}

	public void newUnits(CompilationUnit u) {
		List types = u.types();

		for (int i = 0; i < types.size(); i++) {
			if (u.getPackage() != null) {
				this.getChange().addAddedClass((TypeDeclaration) types.get(i),
						u.getPackage().getName().getFullyQualifiedName(), u.imports());
			} else {
				this.getChange().addAddedClass((TypeDeclaration) types.get(i), "", u.imports());
			}
		}
	}

	public void deletedUnits(CompilationUnit u) {
		List types = u.types();
		for (int i = 0; i < types.size(); i++) {
			this.getChange().addDeletedClass((TypeDeclaration) types.get(i));
		}
	}

	// �Ƚ�������ͬ��Ԫ�ı仯���������������classChange�����С�
	public void compareUnits(CompilationUnit u1, CompilationUnit u2) {

		List types1 = u1.types();
		List types2 = u2.types();

		if (types1.size() != 0 && types2.size() != 0) {

			for (int i = 0; i < types1.size(); i++) {
				if (types1.get(i) instanceof TypeDeclaration) {
					TypeDeclaration def1 = (TypeDeclaration) types1.get(i);

					for (int j = 0; j < types2.size(); j++) {
						if (types2.get(j) instanceof TypeDeclaration) {
							TypeDeclaration def2 = (TypeDeclaration) types2.get(j);

							// ������Ϊ�գ����ļ�����ȫ��ͬ������compareClassDef�������Ժͷ����ıȽ�
							if ((u2.getPackage() == null && u1.getPackage() == null) && def1.getName()
									.getFullyQualifiedName().equals(def2.getName().getFullyQualifiedName())) {
								compareClassDef(def1, def2, "", "", u1.imports(), u2.imports());

								if (def1.getTypes() != null && def2.getTypes() != null && def1.getTypes().length > 0
										&& def2.getTypes().length > 0) {

									for (int k = 0; k < def1.getTypes().length; k++) {
										for (int l = 0; l < def2.getTypes().length; l++) {

											// �������ڲ�������
											if (def1.getTypes()[k].getName().getFullyQualifiedName()
													.equals(def2.getTypes()[l].getName().getFullyQualifiedName())) {
												compareClassDef(def1.getTypes()[k], def2.getTypes()[l], "", "",
														u1.imports(), u2.imports());
												def1.getTypes()[k].delete();
												def2.getTypes()[l].delete();
												k = k - 1;
												l = def2.getTypes().length + 1;
											}
										}
									}
								}
								if (def1.getTypes() != null && def1.getTypes().length > 0) {
									for (int k = 0; k < def1.getTypes().length; k++) {
										if (def1.getTypes()[k] != null) {
											this.getChange().addDeletedClass(def1.getTypes()[k]);
										}
									}

								}
								if (def2.getTypes() != null && def2.getTypes().length > 0) {
									for (int l = 0; l < def2.getTypes().length; l++) {
										if (def2.getTypes()[l] != null) {
											this.getChange().addAddedClass(def2.getTypes()[l], "", u2.imports());
										}
									}
								}
								types1.remove(i);// ���Ƚ�������Ƴ�
								types2.remove(j);// ���Ƚ�������Ƴ�
								i = i - 1;// �Ƚ���һ��
								j = types2.size() + 1;// ����ʣ�ಿ�֣�������һ��ѭ��
							}
							// ��������Ϊ�գ�������·����ȫ��ͬ�������compareClassDef�������Ժͷ����ıȽ�
							if (((u2.getPackage() != null && u1.getPackage() != null)
									&& u1.getPackage().getName().getFullyQualifiedName()
											.equals(u2.getPackage().getName().getFullyQualifiedName())
									&& def1.getName().getFullyQualifiedName()
											.equals(def2.getName().getFullyQualifiedName()))) {
								compareClassDef(def1, def2, u1.getPackage().getName().getFullyQualifiedName(),
										u2.getPackage().getName().getFullyQualifiedName(), u1.imports(), u2.imports());

								// ���ڲ�������
								if (def1.getTypes() != null && def2.getTypes() != null && def1.getTypes().length > 0
										&& def2.getTypes().length > 0) {

									for (int k = 0; k < def1.getTypes().length; k++) {
										for (int l = 0; l < def2.getTypes().length; l++) {

											if (def1.getTypes()[k].getName().getFullyQualifiedName()
													.equals(def2.getTypes()[l].getName().getFullyQualifiedName())) {
												compareClassDef(def1.getTypes()[k], def2.getTypes()[l],
														u1.getPackage().getName().getFullyQualifiedName(),
														u2.getPackage().getName().getFullyQualifiedName(), u1.imports(),
														u2.imports());
												def1.getTypes()[k].delete();
												def2.getTypes()[l].delete();

												k = k - 1;
												l = def2.getTypes().length + 1;
											}
										}
									}
								}

								if (def1.getTypes() != null && def1.getTypes().length > 0) {
									for (int k = 0; k < def1.getTypes().length; k++) {
										this.getChange().addDeletedClass(def1.getTypes()[k]);
									}
								}

								if (def2.getTypes() != null && def2.getTypes().length > 0) {
									for (int l = 0; l < def2.getTypes().length; l++) {
										this.getChange().addAddedClass(def2.getTypes()[l],
												u2.getPackage().getName().getFullyQualifiedName(), u2.imports());
									}
								}

								types1.remove(i);
								types2.remove(j);
								i = i - 1;
								j = types2.size() + 1;

							}
						}
					}
				}
			}
		}
		if (types1.size() > 0) {
			for (int i = 0; i < types1.size(); i++) {
				TemporarySpace.setConsoleString(
						"deleted class: " + ((TypeDeclaration) types1.get(i)).getName().getFullyQualifiedName()
								+ "   in  " + u1.getPackage());
				this.getChange().addDeletedClass((TypeDeclaration) types1.get(i));
			}

		}
		if (types2.size() > 0) {
			for (int i = 0; i < types2.size(); i++) {
				TemporarySpace.setConsoleString("testing " + i + " :" + ((TypeDeclaration) types2.get(i)).getName()
						+ "  in " + u2.getPackage());
				if (u2.getPackage() != null && u2.getPackage().getName() != null) {
					this.getChange().addAddedClass((TypeDeclaration) types2.get(i),
							u2.getPackage().getName().getFullyQualifiedName(), u2.imports());
				} else {
					this.getChange().addAddedClass((TypeDeclaration) types2.get(i), "", u2.imports());
				}
			}
		}
	}

	// �Ƚ�������Ԫ���ڲ����������Զ��壬���������classChange������
	public void compareClassDef(TypeDeclaration def1, TypeDeclaration def2, String oldPackageName,
			String newPackageName, List<ImportDeclaration> oldImportList, List<ImportDeclaration> newImportList) {

		MethodDeclaration[] methods1 = def1.getMethods();
		MethodDeclaration[] methods2 = def2.getMethods();

		FieldDeclaration[] fields1 = def1.getFields();
		FieldDeclaration[] fields2 = def2.getFields();

		ClassChange classChange = null;

		if (methods1.length > 0 && methods2.length > 0) {
			for (int i = 0; i < methods1.length; i++) {
				MethodDeclaration m1 = methods1[i];

				for (int j = 0; j < methods2.length; j++) {
					MethodDeclaration m2 = methods2[j];

					if ((m1 != null) && (m2 != null)
							&& (m1.getName().getFullyQualifiedName().equals(m2.getName().getFullyQualifiedName()))
							&& (m1.parameters().size() == m2.parameters().size())
							/*
							 * &&((m1.getReturnType2()==null&&m2.getReturnType2(
							 * )==null)||(m1.getReturnType2()!=null&&m2.
							 * getReturnType2()!=null)&&(m1.getReturnType2().
							 * toString().equals(m2.getReturnType2().toString())
							 * ))
							 */
							&& ifTheSameParameterList(m1, m2)) {

						methods1[i] = null;
						methods2[j] = null;

						// ���������Ԫ��������ͬ��ֻ����������ݲ���ͬ������Ϊ�ı�ķ��������뵽changeMethod�б���
						if ((m1.getBody() != null && m2.getBody() != null)) {
							if (!m1.getBody().toString().equals(m2.getBody().toString())) {
								if (classChange == null) {
									classChange = new ClassChange(def1, def2, oldPackageName, newPackageName,
											oldImportList, newImportList);

								}
								classChange.addChangedMethod(new MethodChange(m1, m2));
							}
						}
						// �����һ����Ԫ������Ϊ�գ���һ����Ϊ�գ���Ҳ��Ϊ�ı�ķ��������뵽changeMethod�б���
						else {
							if (!(m1.getBody() == null && m2.getBody() == null)) {
								if (classChange == null) {
									classChange = new ClassChange(def1, def2, oldPackageName, newPackageName,
											oldImportList, newImportList);
								}
								classChange.addChangedMethod(new MethodChange(m1, m2));
							}
						}
						m1 = null;
					}
				}
			}
		}
		// ����ھɰ����ж���ķ������򽫴˷�����Ϊ��ɾ���ķ��������뵽deleteMethod�б���
		for (int i = 0; i < methods1.length; i++) {
			if (methods1[i] != null) {
				if (classChange == null) {
					classChange = new ClassChange(def1, def2, oldPackageName, newPackageName, oldImportList,
							newImportList);
				}
				classChange.addDeletedMethod(methods1[i]);
			}
		}
		// ������°����ж���ķ������򽫴˷�����Ϊ�����ķ��������뵽addMethod�б���
		for (int i = 0; i < methods2.length; i++) {
			if (methods2[i] != null) {
				if (classChange == null) {
					classChange = new ClassChange(def1, def2, oldPackageName, newPackageName, oldImportList,
							newImportList);
				}
				classChange.addAddedMethod(methods2[i]);
			}
		}
		// �жϵ�Ԫ���ԣ������fields2���Ҳ�����fields1�е����ԣ�����Ϊ�������ѱ�ɾ����������deletedField�б������fields2���ж�������ԣ�
		// ����Ϊ������Ϊ���������ԣ������뵽addedField�б���
		if (fields1.length > 0 && fields2.length > 0) {
			for (int i = 0; i < fields1.length; i++) {
				FieldDeclaration f1 = fields1[i];
				boolean tag = false;

				for (int j = 0; j < fields2.length; j++) {
					FieldDeclaration f2 = fields2[j];

					if ((f2 != null) && (f1.getType().toString().equals(f2.getType().toString()))
							&& (f1.fragments().size() > 0) && (f2.fragments().size() > 0)) {

						for (int p = 0; p < f1.fragments().size(); p++) {
							VariableDeclarationFragment frag1 = (VariableDeclarationFragment) f1.fragments().get(p);

							for (int q = 0; q < f2.fragments().size(); q++) {
								VariableDeclarationFragment frag2 = (VariableDeclarationFragment) f2.fragments().get(q);
								if (frag1.getName().getFullyQualifiedName()
										.equals(frag2.getName().getFullyQualifiedName())) {
									tag = true;
									f2.fragments().remove(q);
								}
							}
						}
					}
				}
				if (tag == false) {
					if (classChange == null) {
						classChange = new ClassChange(def1, def2, oldPackageName, newPackageName, oldImportList,
								newImportList);

					}
					classChange.addDeletedField(f1);
				}
			}
			for (int j = 0; j < fields2.length; j++) {
				if (fields2[j].fragments().size() > 0) {
					if (classChange == null) {
						classChange = new ClassChange(def1, def2, oldPackageName, newPackageName, oldImportList,
								newImportList);
					}
					classChange.addAddedField(fields2[j]);
				}
			}

		}
		if (classChange != null) {
			getChange().addClassChange(classChange);
		}

	}

	private boolean ifTheSameParameterList(MethodDeclaration m1, MethodDeclaration m2) {

		boolean tag = true;
		List pList1 = m1.parameters();
		List pList2 = m2.parameters();

		if (pList1.size() == pList2.size() && pList1.size() > 0) {
			for (int i = 0; i < pList1.size(); i++) {
				if ((pList1.get(i) instanceof SingleVariableDeclaration)
						&& (pList2.get(i) instanceof SingleVariableDeclaration)) {
					if (!((SingleVariableDeclaration) (pList1.get(i))).getType().toString()
							.equals(((SingleVariableDeclaration) (pList2.get(i))).getType().toString())) {
						tag = false;
					}
				}
			}
		}

		return tag;
	}

	public static void main(String[] args) throws IOException {
		VersionComparator1 comparator = new VersionComparator1();
		String[] paths = { "D:/log/jhotdraw/21/old", "D:/log/jhotdraw/21/new" };

		if (paths.length > 1) {
			for (int i = 1; i < paths.length; i++) {
				comparator.compareProject(paths[i - 1], paths[i]);
				VersionChange versionChange;
				versionChange = comparator.getChange();
				System.out.println("acClassList size:" + versionChange.getAcClassList().size());
			}
		}
	}

}
