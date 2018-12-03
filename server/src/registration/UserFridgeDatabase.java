package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserFridgeDatabase {

	private Connection con= DataBaseConnector.connect();
	
	//Inserts into UserShopping table
	public String insertUserIngredient(JSONObject data) {
		try {	
			String sql = "insert ignore into userFridge (username, ingredient) value (?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("ingredient"));
			st2.executeUpdate();
			st2.close();
			return "User Ingredient insert success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	//Returns a ingredient array based recipeId
	public JSONObject getUserIngredient(String username) {
		String sql = "select * from userFridge where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			JSONArray ingredientInfo = new JSONArray();
			while (rs.next()) {
				ingredientInfo.put( ingredientListBuilder (rs.getString(2)) );	
			}
			JSONObject info = new JSONObject();
			info.put("username", username);
			info.put("fridge", ingredientInfo);
			st.close();
			return info;
		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}
	}
	
	public JSONObject ingredientListBuilder(String ingredient) {
		JSONObject ingredientO = new JSONObject();
		ingredientO.put("ingredient" , ingredient);
		   return ingredientO; 
	}
	
	//Deletes recipe from userHistory based on username and recipename
	public String deleteUserIngredient(String username, String ingredient) {
		String sql = "delete from userFridge WHERE username=? AND ingredient=?";
		try {	
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			st.setString(2, ingredient);
			int i = st.executeUpdate();
			if( i == 0 ) {
				return "Nothing to delete";
			}
			st.close();
			return "User Ingredient delete success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
}
