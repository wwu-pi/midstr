package de.wwu.pi.patecaru.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DBManager {
	
	private static DBManager instance;
	private Connection connection;
	private final String HISTORY_TABLE_NAME = "run_history";
	private final String dbPersistencePath = "./patecaru/db/pat.db.hsql";
	
	public void startDB() {
		createConnection();
		if(!doesDBFileExist()) {			
			createInitialDatabase();
		}
	}
	
	/**
	 * Creates the connection to the database.
	 */
	private void createConnection() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			this.connection = DriverManager.getConnection("jdbc:hsqldb:file:"+this.dbPersistencePath, "SA", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if the file for the DB exists.
	 */
	private boolean doesDBFileExist() {
		File f = new File(this.dbPersistencePath);
		if(f.exists() && !f.isDirectory()) { 
		    return true;
		}
		return false;
	}
	
	public Map<Integer, Long> loadTestResults()  {
		Map<Integer, Long> resultMap = new HashMap<>();
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = this.connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM " + HISTORY_TABLE_NAME);
			while(rs.next()) {
				Integer id = rs.getInt("test_ref_id");
				Long runtime = rs.getLong("run_time");
				resultMap.put(id, runtime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}
	
    public synchronized void update(String expression) {
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
			statement.executeUpdate(expression);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
    
    public static String fixedLengthString(String string) {
    	string += "                                                      ";
        return string.substring(0,20);
    }
    
	/**
	 * Create the initial file database -> <code>CREATE TABLE ...</code>
	 */
	private void createInitialDatabase() {
		System.out.println("## Create database ##");
		this.update("CREATE TABLE "+HISTORY_TABLE_NAME+" (test_ref_id INT, run_time BIGINT)");
	}
	
	public void storeTestResult(Integer testRefId, Long newRuntime) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = this.connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM " + HISTORY_TABLE_NAME + " WHERE test_ref_id=" + testRefId);
			if(rs.next()) {
				this.update("UPDATE " + HISTORY_TABLE_NAME + " SET run_time = " + newRuntime + " WHERE test_ref_id=" + testRefId);
			} else {
				this.update("INSERT INTO "+HISTORY_TABLE_NAME+" VALUES ("+testRefId+", "+newRuntime+")");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private DBManager() {
	}
	
	public static synchronized DBManager getInstance() {
		if(instance == null) {
			instance = new DBManager();
		}
		return instance;
	}
	
	
	public static void main(String[] args) {
		DBManager.getInstance().startDB();
		
		System.out.println("Initial DB State");
		DBManager.getInstance().printHistoryTable();
		
		System.out.println();
		DBManager.getInstance().storeTestResult(223123, 15L);
		DBManager.getInstance().printHistoryTable();
		
		System.out.println();
		DBManager.getInstance().storeTestResult(123, 10L);
		DBManager.getInstance().printHistoryTable();
		
		System.out.println();
		DBManager.getInstance().storeTestResult(223123, 10L);
		DBManager.getInstance().printHistoryTable();
	}

	public synchronized void printHistoryTable() {
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = this.connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM " + HISTORY_TABLE_NAME);
			
			System.out.println("---------------------- " + HISTORY_TABLE_NAME + " -----------------------------");
			System.out.println("    TEST_REF_ID               |     RUN_TIME   ");
			System.out.println("----------------------------------------------------------------");
			while(rs.next()) {
				String id = rs.getString("test_ref_id");
				Short runtime = rs.getShort("run_time");
				System.out.print(fixedLengthString(id));
				System.out.print(" | ");
				System.out.print(runtime + "ms\n");
			}
			
			System.out.println("----------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	
}