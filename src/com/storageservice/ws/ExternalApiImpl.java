package com.storageservice.ws;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.json.JSONObject;


import com.storageservice.model.Person;


import com.storageservice.model.Bmi;

//Service Implementation

@WebService(endpointInterface = "com.storageservice.ws.ExternalApiModel", serviceName = "storageService")
public class ExternalApiImpl implements ExternalApiModel {
	static String localurl="http://localhost:5900/localdbservice";

	@Override
	public Bmi CalculateAndSaveBmi(Person p) {
		Person pverification= getPersonInformation(p.getIdPerson());
		if (pverification==null){
			int idPerson=registration(p);	
		}
		System.out.println(p.getIdPerson());
		String url ="https://adapterservice.herokuapp.com/Bmi?weight="+p.getWeight()+"&height="+p.getHeight()+"&sex="+p.getGenre()+"&age="+p.ageCalculator(p.getBirthdate());
		try{
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
		JSONObject jobj  = new JSONObject(response.toString());
		Bmi bmi= new Bmi();
		bmi.setIdPerson(p.getIdPerson());
		bmi.setStatus(jobj.getJSONObject("bmi").getString("status"));
		bmi.setValue(jobj.getJSONObject("bmi").getDouble("value"));
		Bmi.saveBmi(bmi);
		p.setBmi(bmi);
		return bmi;
		}catch(Exception e){
			System.out.println("error sending bmi request to AdpaterService "+e);
				return null;
			}
	}

	@Override
	public Person getPersonInformation(long id) {	
		String url =localurl+"/person/"+id;
		try{
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
		Person p= new Person();
		System.out.println(response.toString());
		 JSONObject jobj  = new JSONObject(response.toString());
		 p.setLastname(jobj.getString("lastname"));
		 p.setfirstname(jobj.getString("firstname"));
		 p.setBirthdate(jobj.getString("birthdate"));
		 p.setGenre(jobj.getString("genre"));
		 p.setEmail(jobj.getString("email"));
		// p.setHeight(jobj.getJSONArray("measure"));
		 p.setWeight(jobj.getDouble("weight"));
		 return p;	
}catch(Exception e){

System.out.println("error in getting person data on local db "+e);
	
	return null;
}

	}

	@Override
	public String getWeatherByLatLng(String lat, String lng)  {
		
		String url ="https://adapterservice.herokuapp.com/Weather?lat="+lat+"&lng="+lng;
		StringBuffer response=null;
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
		} catch (Exception e) {
		
			System.out.println("error in getting the weather "+e);
		}
		
		return response.toString();
	}

	@Override
	public int registration(Person p) {
		try{
			String url = localurl+"/person";

			URL obj = new URL(url);
			HttpURLConnection con;

			con = (HttpURLConnection) obj.openConnection();

			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Type", "application/json");
			// request body
		
			String str="";//" {\"lastname\":\""+Norris+"\",\"firstname\":\"Chuck\",\"birthdate\":\"1945-01-01\",\"measureType\": [{\"value\": \"78.9\",\"measureDefinition\": {\"idMeasureDef\": 1,\"measureName\": \"weight\",\"measureType\": \"double\"}},{\"value\": \"172\",\"measureDefinition\": {\"idMeasureDef\": 2,\"measureName\": \"height\",\"measureType\":\"double\"}}]}";
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
			int idregistered=jobj.getInt("idPerson");
			return idregistered;
		
		}catch(Exception e){
				System.out.println("error during post registration call "+e);
				return 0;

				
			}
		
		
	}

	@Override
	public int login(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Bmi getBmi(int id) {
		Bmi bmi = Bmi.getBmiById(id);
		return bmi;
	}



}
