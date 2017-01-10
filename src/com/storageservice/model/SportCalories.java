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
@Table(name = "SportCalories")
@NamedQuery(name = "SportCalories.findAll", query = "SELECT s FROM SportCalories s")
@XmlRootElement
public class SportCalories implements Serializable{
	@Id
	@GeneratedValue(generator = "sqlite_sportCalories")
	@TableGenerator(name = "sqlite_sportCalories", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "SportCalories")
	@Column(name = "idSportCalories")
	private int idSportCalories;
@Column(name = "sixtyKg")
private int sixtyKg;
@Column(name = "seventyKg")
private int seventyKg;
@Column(name = "eightyKg")
private int eightyKg;
@Column(name = "ninetyKg")
private int ninetyKg;




public int getIdSportCalories(){
	return idSportCalories;
}
public void setIdSportCalories(int idSportCalories){
	this.idSportCalories=idSportCalories;
}
public int getSixtyKg(){
	return sixtyKg;
}
public void setSixtyKg(int sixtyKg){
	this.sixtyKg=sixtyKg;
}
public int getSeventyKg(){
	return seventyKg;
}
public void setSeventyKg(int seventyKg){
	this.seventyKg=seventyKg;
}
public int getEightyKg(){
	return eightyKg;
}
public void setEightyKg(int eightyKg){
	this.eightyKg=eightyKg;
}public int getNinetyKg(){
	return ninetyKg;
}
public void setNinetyKg(int seventyyKg){
	this.ninetyKg=ninetyKg;
}

//Database operations
	// get the SportCalories which id correspond to the given id as parameter, return a
	// SportCalories
	public static SportCalories getSportCaloriesById(int sportCaloriesId) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		SportCalories s = em.find(SportCalories.class, (int) sportCaloriesId);
		StorageServiceDao.instance.closeConnections(em);
		return s;
	}

	// get all the SportCalories present in the db, return a list of Person
	public static List<SportCalories> getAll() {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		List<SportCalories> list = em.createNamedQuery("SportCalories.findAll", SportCalories.class).getResultList();
		StorageServiceDao.instance.closeConnections(em);
		return list;
	}

	// save a new SportCalories in the db
	public static SportCalories saveSport(SportCalories s) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(s);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
		return s;
	}



	// delete the SportCalories givean as input in the db
	public static void removeSportCalories(SportCalories s) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		s = em.merge(s);
		em.remove(s);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
	}
	
	



}
