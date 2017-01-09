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
import com.storageservice.model.Bmi;
//service definition
@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) 
public interface ExternalApiModel {
    @WebMethod(operationName="getbmi")
    @WebResult(name="bmi") 
    public Bmi getBmi(@WebParam(name="personId") long id);
 
    @WebMethod(operationName="getPersonInformation")
    @WebResult(name="bmi") 
    public Person getPersonInformation(@WebParam(name="personId") long id);
    
    @WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person readPerson(@WebParam(name="personId") long id);
 
    @WebMethod(operationName="readPersonList")
    @WebResult(name="people") 
    public List<Person> readPersonList();

    @WebMethod(operationName="getWeatherByLatLng")
    @WebResult(name="weather") 
    public String getWeatherByLatLng(@WebParam(name="lat") String lat,@WebParam(name="lng") String lng);



}