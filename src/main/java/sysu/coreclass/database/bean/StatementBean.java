package sysu.coreclass.database.bean;

import org.eclipse.jdt.core.dom.Statement;

public class StatementBean {

	private int statementID;
	private String statementeType;
	private Statement statement;
	public int getStatementID() {
		return statementID;
	}
	public void setStatementID(int statementID) {
		this.statementID = statementID;
	}
	public String getStatementeType() {
		return statementeType;
	}
	public void setStatementeType(String statementeType) {
		this.statementeType = statementeType;
	}
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	
	
	
}
