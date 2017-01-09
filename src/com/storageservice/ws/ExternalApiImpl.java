package com.storageservice.ws;



import java.io.BufferedReader;
import java.io.InputStreamReader;
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

	@Override
	public Bmi getBmi(long id) {
		Person p = Person.getPersonById(id);
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
		String url ="http://loclahost:5900/localdbservice/person/"+id;
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
		 p.setHeight(jobj.getDouble("height"));
		 p.setWeight(jobj.getDouble("weight"));
		Person.savePerson(p);
		 return p;	
}catch(Exception e){

System.out.println("error in getting person data on local db "+e);
	
	return null;
}

	}

	@Override
	public Person readPerson(long id) {
		Person p = Person.getPersonById(id);
		return p;
	}

	@Override
	public List<Person> readPersonList() {
		return Person.getAll();
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



}
