package com.storageservice.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import com.storageservice.dao.StorageServiceDao;



@Entity
@Table(name = "Sport")
@NamedQuery(name = "Sport.findAll", query = "SELECT s FROM Sport s")
@XmlRootElement
public class Sport implements Serializable{
	@Id
	@GeneratedValue(generator = "sqlite_sport")
	@TableGenerator(name = "sqlite_sport", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Sport")
	@Column(name = "idSport")
	private int idSport;
@Column(name = "name")
private String name;
@Column(name = "perfectWeather")
private String perfectWeather;
@OneToOne
@JoinColumn(name = "idSportCalories", referencedColumnName = "idSportCalories", insertable = true, updatable = true)
private SportCalories sportCalories;

public int getIdSport(){
	return idSport;
}
public void setIdSport(int idSport){
	this.idSport=idSport;
}
public String getName(){
	return name;
}
public void setStatus(String name){
	this.name=name;
}
public String getPerfectWeather(){
	return perfectWeather;
}
public void setPerfectWeather(String perfectWeather){
	this.perfectWeather=perfectWeather;}

public SportCalories getSportCalories(){
	return sportCalories;
}
public void setSportCalories(SportCalories sportCalories){
	this.sportCalories=sportCalories;
}




//Database operations
	// get the Sport which id correspond to the given id as parameter, return a
	// Sport
	public static Sport getSportById(long sportId) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		Sport s = em.find(Sport.class, (int) sportId);
		StorageServiceDao.instance.closeConnections(em);
		return s;
	}

	// get all theSports present in the db, return a list of Person
	public static List<Sport> getAll() {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		List<Sport> list = em.createNamedQuery("Sport.findAll", Sport.class).getResultList();
		StorageServiceDao.instance.closeConnections(em);
		return list;
	}
	public static List<Sport> getSportByWeather(String weather) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		try {
			Query q = em.createQuery(
					"SELECT s FROM Sport s WHERE s.perfectWeather=:weather",
					Sport.class);
			q.setParameter("weather", weather);
			
			List<Sport>sports = q.getResultList();
			StorageServiceDao.instance.closeConnections(em);
			return sports;
		} catch (Exception e) {
			System.out.println(
					"The database doesn't contain a Sport with the perfect weather required");

			StorageServiceDao.instance.closeConnections(em);
			return null;
		}

	}

	// save a new Sport in the db
	public static Sport saveSport(Sport s) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(s);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
		return s;
	}



	// delete the Sport givean as input in the db
	public static void removeSport(Sport s) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		s = em.merge(s);
		em.remove(s);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
	}



}