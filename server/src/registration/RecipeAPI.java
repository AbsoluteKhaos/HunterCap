package registration;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

@Path("recipe")
public class RecipeAPI {
	RecipeDatabase dataBase = new RecipeDatabase();
	UserRecipeDatabase UserRecipeDatabase = new UserRecipeDatabase();
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
	
/* Call to insert json containing recipe info into SQL database
 * Requires JSON in this form
 * {
    "username": "ex",
    "label" : "aaaaaaaaaa",
    "description" : "howmayfirerice in the library",
    "image" : "hwihwieiqoejqwondijenfewinvmsov.com",
    "URL" : "adasdhweidewbfyuwiebvbyurv.com",
    "servings" : 33,
    "calories" : 2.3,
    "totalTime" : 5 ,
    "nutrients" : {
        "fat" : 2.1,
        "sugar" : 4.5,
        "protein" : 6.6,
        "fiber" : 4.4,
        "sodium" : 5.5,
        "cholesterol" : 7.7,
        "carbs" : 3.4
    },
    "ingredients" : [
       {"name" : "rice", "amount": 2 },
       {"name" : "egg", "amount": 2 }
    ]
}
 * 
 */
	@Path("insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertRecipe (String data){
		JSONObject temp = new JSONObject(data);
		String username= temp.getString("username");
		int recipeID= dataBase.insertrecipe(temp);
		if (recipeID == -1) {
			return "Recipe insert fail";
		}
		UserRecipeDatabase.insertUserRecipes(recipeID, username);
		return "recipe Insert success";
	}
	//end
	
	/* Returns json with recipe info, 
	 * Input form:
	 * {
			"recipeId" : 29
		}
	 */
	
	
	@Path("get")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecipe (String data) {
		JSONObject temp = new JSONObject(data);
		int recipe= temp.getInt("recipeId");
		return dataBase.getRecipe(recipe).toString();
	}
	
	/* Takes {id} as recipeId and a json with recipe info in order to update
	 * Same form as insert
	 */
	@Path("update/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateRecipe (@PathParam("id") int recipeid, String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.updateRecipe(recipeid, temp);
	}
	
	//Deletes recipe with recipeId == {id}
	@Path("delete/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteRecipe (@PathParam("id") int recipeid) {
		return dataBase.deleteRecipe(recipeid);
	}

	//for search API, when front end give the name, we need to return a list of recipe to them
	/* Input form:
		 * {
				"size" : 29,
				"search" : "chicken"
			}
	*/
	@Path("search")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchRecipe (String data) throws JSONException, IOException { 
		JSONObject temp = new JSONObject(data);
		int size = temp.getInt("size");
		String q = temp.getString("search");
		return dataBase.apiSearch(size, q).toString();
	}

}
