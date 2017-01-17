package com.storageservice.ws;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.PortableInterceptor.USER_EXCEPTION;

import com.storageservice.model.Person;
import com.storageservice.model.Sport;
import com.storageservice.model.Weather;
import com.storageservice.model.WeightGoal;
import com.storageservice.model.Activity;
import com.storageservice.model.Bmi;
import com.storageservice.model.BmiHistory;
import com.storageservice.model.Goal;

//Service Implementation

@WebService(endpointInterface = "com.storageservice.ws.ExternalApiModel", serviceName = "storageService")
public class ExternalApiImpl implements ExternalApiModel {
	static String localurl = "https://localdbservice.herokuapp.com/localdbservice";

	@Override
	public Bmi CalculateAndSaveBmi(Person p) {
		/*Person pverification = getPersonInformation(p.getIdPerson());
		if (pverification == null) {
			int idPerson = registration(p);
		}*/
		System.out.println(p.getIdPerson());
		String url = "https://adapterservice.herokuapp.com/Bmi?weight=" + p.getWeight() + "&height=" + p.getHeight()
				+ "&sex=" + p.getGenre() + "&age=" + p.ageCalculator(p.getBirthdate());
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
			JSONObject jobj = new JSONObject(response.toString());
			Bmi bmi = new Bmi();
			bmi.setIdPerson(p.getIdPerson());
			bmi.setStatus(jobj.getString("status"));
			bmi.setValue(jobj.getDouble("value"));
			bmi.setRisk(jobj.getString("risk"));
			bmi.setPrime(jobj.getString("prime"));
			
			Bmi OldBmi=Bmi.getBmiByIdPerson(p.getIdPerson());
			System.out.println(OldBmi.getRisk());
			if (OldBmi==null){
			Bmi.saveBmi(bmi);}else{
				Bmi.removeBmi(OldBmi);
				BmiHistory bmiHistory= new BmiHistory();
				bmiHistory.setIdPerson(OldBmi.getIdPerson());
				bmiHistory.setPrime(OldBmi.getPrime());
				bmiHistory.setRisk(OldBmi.getRisk());
				bmiHistory.setStatus(OldBmi.getStatus());
				bmiHistory.setValue(OldBmi.getValue());
				Date date = new Date();
				String sdate= new SimpleDateFormat("yyyy-MM-dd").format(date);
				bmiHistory.setDate(sdate);
				BmiHistory.saveBmiHistory(bmiHistory);
				Bmi.updateBmi(bmi);
				
			}

			return bmi;
		} catch (Exception e) {
			System.out.println("error sending bmi request to AdpaterService " + e);
			return null;
		}
	}

	@Override // attenti al bmi come si comporta?
	public Person getPersonInformation(long id) {
		String url = localurl + "/person/" + id;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			Person p = new Person();
			System.out.println(response.toString());
			JSONObject jobj = new JSONObject(response.toString());

			p.setLastname(jobj.getString("lastname"));
			p.setfirstname(jobj.getString("firstname"));
			p.setBirthdate(jobj.getString("birthdate"));
			p.setGenre(jobj.getString("genre"));
			p.setEmail(jobj.getString("email"));
			p.setLevel(jobj.getJSONObject("level").getString("name"));
			p.setNTotalGoal((jobj.getInt("ntotalGoal")));
			p.setNGoalAchieved(jobj.getInt("ngoalAchieved"));
			p.setDescription(jobj.getJSONObject("level").getString("description"));
			p.setLifeStyle(jobj.getJSONObject("lifeStyle").getString("style"));

			return p;
		} catch (Exception e) {

			System.out.println("error in getting person data on local db " + e);

			return null;
		}

	}

	public Person getWeightHeight(String access_token,String user_id,String refresh_token){
		String url = "https://adapterservice.herokuapp.com/FitbitProfile?access_token="+access_token+"&user_id="+user_id+"&refresh_token="+refresh_token;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject jobj= new JSONObject(response.toString());
			Person p=new Person();
		p.setWeight(jobj.getDouble("weight"));
		p.setHeight(jobj.getDouble("height"));
		
			return p;		
		} catch (Exception e) {
			System.out.println("error in getting weight and height  request on FitBitAdapter " + e);
			return null;
		}
	
}
	@Override
	public List<Weather> getForecastByLatLng(String lat, String lng) {
		List <Weather>forecast= new ArrayList();
		String url = "https://adapterservice.herokuapp.com/WeatherForecast?lat=" + lat + "&lng=" + lng;
		StringBuffer response = null;
		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONArray jarr = new JSONArray(response.toString());
			for (int i=0; i<jarr.length();i++){
				Weather weather=new Weather();
				weather.setDate(jarr.getJSONObject(i).getString("date"));
				weather.setHigh(jarr.getJSONObject(i).getString("high"));
				weather.setLow(jarr.getJSONObject(i).getString("low"));
				weather.setText(jarr.getJSONObject(i).getString("text"));
				weather.setDay(jarr.getJSONObject(i).getString("day"));
				forecast.add(weather);
			}

		} catch (Exception e) {
			forecast=null;
			System.out.println("error in getting the weather " + e);
		}
		return forecast;
	}
	@Override
	public Weather getWeatherByLatLng(String lat, String lng) {
		String url = "https://adapterservice.herokuapp.com/Weather?lat=" + lat + "&lng=" + lng;
		StringBuffer response = null;
		URL obj;
		Weather weather=new Weather();
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject jobj = new JSONObject(response.toString());
		
				weather.setDate(jobj.getString("date"));
				weather.setTemp(jobj.getString("temp"));
				weather.setText(jobj.getString("text"));
			

		} catch (Exception e) {
			weather=null;
			System.out.println("error in getting the weather " + e);
		}
		return weather;
	}
	@Override
	public int registration(Person p) {
		try {
			String url = localurl + "/person";

			URL obj = new URL(url);
			HttpURLConnection con;

			con = (HttpURLConnection) obj.openConnection();

			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Type", "application/json");
			// request body

			String str ="{\"lastname\":\""+p.getLastname()+"\",\"firstname\":\""+p.getfirstname()+"\",\"birthdate\":\""+p.getBirthdate()+"\","
					+ "\"email\":\""+p.getEmail()+"\",\"genre\":\""+p.getGenre()+"\",\"password\":\""+p.getPassword()+"\",\"idLifeStyle\":\""+p.getIdLifeStyle()+"\","
							+ "\"idLevel\":\""+p.getIdLevel()+"\"}";
							

			byte[] outputInBytes = str.getBytes("UTF-8");
			OutputStream os = con.getOutputStream();
			os.write(outputInBytes);
			os.close();

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject jobj = new JSONObject(response.toString());
			int idregistered = jobj.getInt("idPerson");
			return idregistered;

		} catch (Exception e) {
			System.out.println("error during post registration call " + e);
			return 0;

		}

	}

	@Override
	public Person login(String email,String psw) {
		String url = localurl + "/login?email="+email+"&psw="+psw ;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			Person p = new Person();
			System.out.println(response.toString());
			JSONObject jobj = new JSONObject(response.toString());
			p.setBirthdate(jobj.getString("birthdate"));
			p.setfirstname(jobj.getString("firstname"));
			p.setLastname(jobj.getString("lastname"));
			p.setEmail(jobj.getString("email"));
			p.setGenre(jobj.getString("genre"));
			//p.setHeight(jobj.getDouble("height"));
			//p.setWeight(jobj.getDouble("weight"));
			return p;
		}catch(Exception e){
			return null;
		}
		
	}

	@Override
	public Bmi getBmi(int id) {
		Bmi bmi = Bmi.getBmiById(id);
		return bmi;
	}

	@Override
	public List<Sport> getSportsByWeather(String weather) {

		return Sport.getSportByWeather(weather);
	}

	@Override
	public Activity getActivityBySport(Sport sport, String access_token,String user_id,String refresh_token) {
		Activity activity = Activity.getActivityBySport(sport.getName());
		if (activity == null) {
			activity=new Activity();
			String url = "https://adapterservice.herokuapp.com/FitbitActivity?access_token="+access_token+"&user_id="+user_id+"&refresh_token="+refresh_token;
			try {
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				con.setRequestMethod("GET");
				con.setRequestProperty("Accept", "application/json");
				int responseCode = con.getResponseCode();

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				JSONArray jarr = new JSONArray(response.toString());
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject jobj = jarr.getJSONObject(i);
					if (jobj.getString("name").equals(sport.getName())) {
					
						activity.setName(jobj.getString("name"));
						activity.setAccessLevel(jobj.getString("accessLevel"));
						activity.setMets(jobj.getDouble("mets"));
						activity.setHasSpeed(jobj.getBoolean("hasSpeed"));
						activity.setIdActivity(jobj.getLong("id"));
						Activity.saveActivity(activity);
					}

				}

			} catch (Exception e) {
				System.out.println("error in getting activity request on FitBitAdapter " + e);
			}

		}
		return activity;
	}

	@Override
	public Goal getDailyGoal(String access_token,String user_id,String refresh_token) {
		//get daily activity summary
		Date date=new Date();
	String sdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
			String url = "https://adapterservice.herokuapp.com/FitbitDailyActivitySummary?access_token="+access_token+"&user_id="+user_id+"&refresh_token="+refresh_token+"&date="+sdate;
			try {
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				con.setRequestMethod("GET");
				con.setRequestProperty("Accept", "application/json");
				int responseCode = con.getResponseCode();

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				System.out.println("response "+response);
				Goal goal= new Goal();
				JSONObject jobj = new JSONObject(response.toString());
				goal.setCaloriesOut(jobj.getJSONObject("goals").getInt("caloriesOut"));
				try{goal.setDistance((jobj.getJSONObject("goals").getDouble("distance")));}catch(Exception e){goal.setDistance(0);}
				goal.setSteps(jobj.getJSONObject("goals").getInt("steps"));
				JSONArray activities=jobj.getJSONArray("activities");
				
				int StepsDone=0;
				int DistanceDone=0;
				int CaloriesOutDone=jobj.getJSONObject("summary").getInt("caloriesOut");
				for (int i = 0; i < activities.length(); i++) {
					JSONObject activity = activities.getJSONObject(i);
					StepsDone+=activity.getInt("steps");
					try{
					DistanceDone+=activity.getDouble("distance");}catch(Exception e){}
					}
				goal.setMissingSteps(goal.getSteps()-StepsDone);
				goal.setMissingDistance(goal.getDistance()-DistanceDone);
				goal.setMissingCalories(goal.getCaloriesOut()-CaloriesOutDone);
				return goal;		
			} catch (Exception e) {
				System.out.println("error in getting daily goal request on FitBitAdapter " + e);
				return null;
			}
	
	}
	
	@Override
	public List<Sport> getSportsList() {

		return Sport.getAll();
	}

	@Override
	public Sport getSport() {
		// TODO Auto-generated method stub
		return null;
	}

	public WeightGoal getWeightGoal( String access_token,String user_id,String refresh_token) {

		//get body goal
		String url = "https://adapterservice.herokuapp.com/FitbitBodyGoal?access_token="+access_token+"&user_id="+user_id+"&refresh_token="+refresh_token;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			WeightGoal wg= new WeightGoal();
			JSONObject jobj = new JSONObject(response.toString());
			wg.setStartDate(jobj.getString("startDate"));
			wg.setGoalWeight(jobj.getDouble("weight"));
			wg.setStartWeight(jobj.getDouble("startWeight"));
			return wg;		
		} catch (Exception e) {
			System.out.println("error in getting weight goal request on FitBitAdapter " + e);
			return null;
		}

}
@Override
	public List<Activity> getFavouriteActivity( String access_token,String user_id,String refresh_token){
		String url = "https://adapterservice.herokuapp.com/FitbitFavoriteActivities?access_token="+access_token+"&user_id="+user_id+"&refresh_token="+refresh_token;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			List <Activity>activities = new ArrayList();	
			JSONArray jarr = new JSONArray(response.toString());
			for (int i=0; i<jarr.length();i++){
				Activity activity= new Activity();
				JSONObject jobj = jarr.getJSONObject(i);
					activity.setIdActivity(jobj.getInt("activityId"));
					activity.setName(jobj.getString("name"));
					activity.setMets(jobj.getDouble("mets"));
					activities.add(activity);}
		
			return activities;		
		} catch (Exception e) {
			System.out.println("error in getting favourite activity request on FitBitAdapter " + e);
			return null;
		}
		
	}

	public double PeriodWeightDifference(String startDate, String endDate, String access_token,String user_id,String refresh_token){
		String url = "https://adapterservice.herokuapp.com/FitbitWeightLogs?access_token="+access_token+"&user_id="+user_id+"&refresh_token="+refresh_token+"&base_date="+startDate+"&end_date="+endDate;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			JSONArray jarr = new JSONArray(response.toString());
			
				if(response!=null){
					System.out.println(response);
				double startWeight = jarr.getJSONObject(0).getDouble("weight");
				double endWeight =jarr.getJSONObject(1).getDouble("weight");
				
				return (startWeight-endWeight);
				}
				else{
					return 0;
				}
				
		} catch (Exception e) {
			System.out.println("error in getting weight logs request on FitBitAdapter " + e);
			return 0;
		}
	
	}

	@Override
	public int updatePerson(Person person){
		try{
		URL obj = new URL(localurl + "/person"+person.getIdPerson());
		HttpURLConnection con;

		con = (HttpURLConnection) obj.openConnection();

		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Type", "application/json");
		// request body

		String str = "";// "
						// {\"lastname\":\""+Norris+"\",\"firstname\":\"Chuck\",\"birthdate\":\"1945-01-01\",\"measureType\":
						// [{\"value\": \"78.9\",\"measureDefinition\":
						// {\"idMeasureDef\": 1,\"measureName\":
						// \"weight\",\"measureType\":
						// \"double\"}},{\"value\":
						// \"172\",\"measureDefinition\": {\"idMeasureDef\":
						// 2,\"measureName\":
						// \"height\",\"measureType\":\"double\"}}]}";
		
		byte[] outputInBytes = str.getBytes("UTF-8");
		OutputStream os = con.getOutputStream();
		os.write(outputInBytes);
		os.close();

		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONObject jobj = new JSONObject(response.toString());
		int idPerson= jobj.getInt("idPerson");
		return idPerson;

	} catch (Exception e) {
		System.out.println("error during post registration call " + e);
		return 0;

	}
	
		
		
	}
	@Override
	public double PeriodBmiDifference(String startDate,Person p){
	BmiHistory history = BmiHistory.getBmiHistoryByDate(startDate);
		Bmi actual= Bmi.getBmiByIdPerson(p.getIdPerson());
		
		return history.getValue()-actual.getValue();
		
	}

	public String StatusBmiDifference(String startDate,Person p){
		BmiHistory history = BmiHistory.getBmiHistoryByDate(startDate);
	Bmi actual= Bmi.getBmiByIdPerson(p.getIdPerson());
	return "Your status change from "+history.getStatus() +" to "+actual.getStatus();
			
		}
	

	
		
	}

