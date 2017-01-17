package com.storageservice.ws;


import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.storageservice.model.Person;
import com.storageservice.model.Sport;
import com.storageservice.model.Weather;
import com.storageservice.model.WeightGoal;
import com.storageservice.model.Activity;
import com.storageservice.model.Bmi;
import com.storageservice.model.Goal;
//service definition
@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) 
public interface ExternalApiModel {
	
    @WebMethod(operationName="calculateandSavebmi")
    @WebResult(name="bmi") 
    public Bmi CalculateAndSaveBmi(@WebParam(name="person") Person p);
    
    @WebMethod(operationName="getbmi")
    @WebResult(name="bmi") 
    public Bmi getBmi(@WebParam(name="idBmi") int id);
    
    @WebMethod(operationName="getPersonInformation")
    @WebResult(name="bmi") 
    public Person getPersonInformation(@WebParam(name="personId") long id);
    
    @WebMethod(operationName="updatePerson")
    @WebResult(name="idPerson") 
    public int updatePerson(@WebParam(name="person") Person p);
   
    @WebMethod(operationName="getWeightHeight")
    @WebResult(name="Person") 
    public Person getWeightHeight(@WebParam(name="access_token")String access_token,@WebParam(name="user_id")String user_id,@WebParam(name="refresh_token")String refresh_token);
    
    @WebMethod(operationName="getForecastByLatLng")
    @WebResult(name="forecast") 
    public List<Weather> getForecastByLatLng(@WebParam(name="lat") String lat,@WebParam(name="lng") String lng);
    
    @WebMethod(operationName="getWeatherByLatLng")
    @WebResult(name="weather") 
    public Weather getWeatherByLatLng(@WebParam(name="lat") String lat,@WebParam(name="lng") String lng);
    
   
    @WebMethod(operationName="registration")
    @WebResult(name="registration") 
    public int registration(@WebParam(name="person") Person p);
 
    @WebMethod(operationName="login")
    @WebResult(name="login") 
    public Person login(@WebParam(name="email") String email,@WebParam(name="pwd") String psw);

    @WebMethod(operationName="getSportsByWeather")
    @WebResult(name="sportsbyWeather") 
    public List<Sport> getSportsByWeather(@WebParam(name="weather")String weather );
    
    @WebMethod(operationName="getActivityBySport")
    @WebResult(name="activity") 
    public Activity getActivityBySport(@WebParam(name="sport")Sport sport,@WebParam(name="access_token")String access_token,@WebParam(name="user_id")String user_id,@WebParam(name="refresh_token")String refresh_token);
    
    @WebMethod(operationName="getSportsList")
    @WebResult(name="sports") 
    public List<Sport> getSportsList();
    
    @WebMethod(operationName="getSport")
    @WebResult(name="sport") 
    public Sport getSport();
   
    @WebMethod(operationName="getDailyGoal")
    @WebResult(name="dailygoal") 
    public Goal getDailyGoal(@WebParam(name="access_token")String access_token,@WebParam(name="user_id")String user_id,@WebParam(name="refresh_token")String refresh_token);
    
    @WebMethod(operationName="getFavouriteActivity")
    @WebResult(name="favouriteActivity")  
    public List<Activity> getFavouriteActivity(@WebParam(name="access_token")String access_token,@WebParam(name="user_id")String user_id,@WebParam(name="refresh_token")String refresh_token);
    
    @WebMethod(operationName="getPeriodWeightDifference")
    @WebResult(name="weightDifference")  
    public double PeriodWeightDifference(@WebParam(name="startDate")String startDate,@WebParam(name="endDate")String endDate,@WebParam(name="access_token")String access_token,@WebParam(name="user_id")String user_id,@WebParam(name="refresh_token")String refresh_token);
   
    @WebMethod(operationName="getWeightGoal")
    @WebResult(name="weightGoal")  
    public WeightGoal getWeightGoal(@WebParam(name="access_token")String access_token,@WebParam(name="user_id")String user_id,@WebParam(name="refresh_token")String refresh_token);
    
    @WebMethod(operationName="getPeriodBmiDifference")
    @WebResult(name="bmiDifference")  
    public double PeriodBmiDifference(@WebParam(name="startDate")String startDate,@WebParam(name="person")Person p);
    

    @WebMethod(operationName="getStatusBmiDifference")
    @WebResult(name="statusBmiDifference")  
    public String StatusBmiDifference(@WebParam(name="startDate")String startDate,@WebParam(name="person")Person p);
    
    
}