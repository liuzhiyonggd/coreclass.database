package sysu.coreclass.database.tool;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class Connection {
//	private static final Mongo mongo = new Mongo("172.18.59.127", 27017);
	private static final Mongo mongo = new Mongo("localhost", 27018);
//	private static final Mongo mongo = new Mongo("localhost", 27017);
	// private static final Mongo mongo = new Mongo();
	public static DB getConnection() {

		return mongo.getDB("commitbase");
		// return mongo.getDB("MicroAgent");
	}

	public static void closeConnection() {
		mongo.close();
	}
}
