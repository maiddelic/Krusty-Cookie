package dbtLab3;

import java.util.ArrayList;
import java.sql.*;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * An SQL statement object.
	 */
	private Statement stmt;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	public boolean openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + "db11", "db11",
					"Dinfass4");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public ArrayList<String> getCookies(){
		ArrayList<String> cookies = new ArrayList<String>();
		try{
		String sql ="select cookieName from Cookies";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			cookies.add(rs.getString("cookieName"));
		}
		return cookies;
		}
		catch (SQLException e){
			return null;
		}
	}

	public ArrayList<String> getProductionDatesIds(String cookieName){
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<String> palletIds = new ArrayList<String>();
		palletIds = getPalletIds(cookieName);
		try{
		String sql ="select date " + "from pallets " + "where cookieName = " + "'" + cookieName + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		int i = 0;
		while(rs.next()){
			dates.add( palletIds.get(i) + "/" + rs.getString("date"));
			i++;
		}
		return dates;
		}
		catch (SQLException e){
			return null;
		}
	}
	
	public ArrayList<String> getBlockedPallets(){
		ArrayList<String> palletIds = new ArrayList<String>();
		try{
		String sql ="select palletId " + "from pallets " + "where isBlocked = true";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			palletIds.add(rs.getString("palletId"));
		}
		return palletIds;
		}
		catch (SQLException e){
			return null;
		}
	}
	
	
	public ArrayList<String> getPalletIds(String cookieName){
		ArrayList<String> palletIds = new ArrayList<String>();
		try{
		String sql ="select palletId " + "from pallets " + "where cookieName = " + "'" + cookieName + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			palletIds.add(rs.getString("palletId"));
		}
		return palletIds;
		}
		catch (SQLException e){
			return null;
		}
	}
	
	public boolean block(String palletId){
		try{
			String sql1 ="select orderNbr " + "from pallets " + "where palletId= " + "'" + palletId + "'";	
			PreparedStatement ps = conn.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				if(rs.getString("orderNbr") != null){
					System.out.println("LQWKLE");
					return false;
				}
			}	
			String sql2 ="update pallets set isBlocked = true where palletId = " + palletId;
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.executeUpdate();
			return true;
		}
		catch (SQLException e){
			System.out.println("hej");
			return false;
		}
	}
	
	public Pallet getPalletInfo(String palletId){
		Pallet pallet = null;
		try{
			String sql ="select* " + "from pallets where palletId = '"  + palletId + "'";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				pallet = new Pallet(rs.getString("palletId"), rs.getString("date"), rs.getBoolean("isBlocked"),
						rs.getString("cookieName"), rs.getString("orderNbr"));
			}
			return pallet;
			}
		catch (SQLException e){
			return null;
		}
	}
	

	
	public boolean produce(String cookieName){
		try{
			String sql = "insert into pallets values(?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
			ps.setBoolean(3, false);
			ps.setString(4, cookieName);
			ps.setNull(5, java.sql.Types.INTEGER);
			ps.executeUpdate();
			try{
				String sql2 ="select ingName, grams from consists where cookieName = '" + cookieName + "'";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ResultSet rs = ps2.executeQuery();
				ArrayList<String> ingredients = new ArrayList<String>();
				ArrayList<Integer> grams = new ArrayList<Integer>();
				while(rs.next()){
					ingredients.add(rs.getString("ingName"));
					grams.add(rs.getInt("grams"));
				}
				for(int i = 0; i<ingredients.size(); i++){
					String sql3 ="update ingredients set amount = amount - ? where ingName = ?";
					PreparedStatement ps3 = conn.prepareStatement(sql3);
					ps3.setInt(1, (grams.get(i)));
					ps3.setString(2, ingredients.get(i));
					ps3.executeUpdate();
				}

			}catch(SQLException e){
			}
			return true;
		}
		catch (SQLException e){
			return false;
		}
	}
}
