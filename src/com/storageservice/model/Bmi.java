package com.storageservice.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import com.storageservice.dao.StorageServiceDao;

@Entity
@Table(name = "Bmi")
@NamedQuery(name = "Bmi.findAll", query = "SELECT b FROM Bmi b")
@XmlRootElement
public class Bmi implements Serializable{
	@Id
	@GeneratedValue(generator = "sqlite_bmi")
	@TableGenerator(name = "sqlite_bmi", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Bmi")
	@Column(name = "idBmi")
	private int idBmi;
@Column(name = "value")
private double value;
@Column(name = "status")
private String status;

public double getValue(){
	return value;
}
public String getStatus(){
	return status;
}
public void setStatus(String status){
	this.status=status;
}
public void setValue(double value){
	this.value=value;
}


//Database operations
	// get the Bmi which id correspond to the given id as parameter, return a
	// Bmi
	public static Bmi getBmiById(long bmiId) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		Bmi b = em.find(Bmi.class, (int) bmiId);
		StorageServiceDao.instance.closeConnections(em);
		return b;
	}

	// get all the Bmi present in the db, return a list of Person
	public static List<Bmi> getAll() {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		List<Bmi> list = em.createNamedQuery("Bmi.findAll", Bmi.class).getResultList();
		StorageServiceDao.instance.closeConnections(em);
		return list;
	}

	// save a new Bmi in the db
	public static Bmi saveBmi(Bmi b) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(b);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
		return b;
	}



	// delete the Bmi givean as input in the db
	public static void removeBmi(Bmi b) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		b = em.merge(b);
		em.remove(b);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
	}

}
