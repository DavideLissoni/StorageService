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
import com.storageservice.model.SportCalories;
import com.storageservice.model.Bmi;
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
    

    @WebMethod(operationName="getWeatherByLatLng")
    @WebResult(name="weather") 
    public String getWeatherByLatLng(@WebParam(name="lat") String lat,@WebParam(name="lng") String lng);

    @WebMethod(operationName="registration")
    @WebResult(name="registration") 
    public int registration(@WebParam(name="person") Person p);
 
    @WebMethod(operationName="login")
    @WebResult(name="login") 
    public int login(@WebParam(name="id") int id);

    @WebMethod(operationName="getSporstByWeather")
    @WebResult(name="sportsbyWeather") 
    public List<Sport> getSportsByWeather(@WebParam(name="weather")String weather );
    
    @WebMethod(operationName="getCaloriesBySport")
    @WebResult(name="calories") 
    public SportCalories getCaloriesBySport(@WebParam(name="sport")Sport sport);
    
    @WebMethod(operationName="getSporsList")
    @WebResult(name="sports") 
    public List<Sport> getSportsList();
    
    
    //calorie-> tabella calorie attività davide
   
    
    //prendere le attività dispnobili su fitbit della
    //totale calorie bruciati o rimanenti dal goal
    //prendere il goal
    

}