import java.sql.*;


public class Connect {

	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "climinton";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connection ct;
	private Statement st;
	private static Connect connect;
	
	public ResultSet rs;
	public ResultSetMetaData rsmd;
	
	public static Connect getInstance() {
		if (connect == null) {
			return new Connect();
		}
		return connect;
	}
	
	private Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			ct = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = ct.createStatement();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public ResultSet executeQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsmd = rs.getMetaData();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public void executeUpdate(String query) {
		try {
			st.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public PreparedStatement prepareStatement(String query) {
		PreparedStatement ps = null;
		
		try {
			ps = connect.prepareStatement(query);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return ps;
	}
	
	
	
}
