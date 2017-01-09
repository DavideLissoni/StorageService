package com.storageservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import com.storageservice.dao.StorageServiceDao;



@Entity
@Table(name = "Person")
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
@XmlRootElement
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	// fields
	@Id
	@GeneratedValue(generator = "sqlite_person")
	@TableGenerator(name = "sqlite_person", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Person")
	@Column(name = "idPerson")
	private int idPerson;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "birthdate")
	private String birthdate;
	
	@Column(name = "email")
	private String email;
	@Column(name = "genre")
	private String genre;
	
	@Column(name = "weight")
	private double weight;
	@Column(name = "height")
	private double height;
	
	// references
	@OneToOne
	@JoinColumn(name = "idBmi", referencedColumnName = "idBmi", insertable = true, updatable = true)
	private Bmi bmi;

	public Person() {
	}

	// getter annd setter methods

	public int getIdPerson() {
		return this.idPerson;
	}

	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public String getfirstname() {
		return this.firstname;
	}

	public void setfirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;

	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getHeight() {
		return this.height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	public String getGenre() {
		return this.genre;
	}

	public void setGenre(String genre) {
		this.birthdate = birthdate;
	}
	
	public Bmi getBmi(){
		return this.bmi;
	}
	public void setBmi(Bmi bmi){
		this.bmi=bmi;
	}
	
	
	public int ageCalculator(String birthdate){
		String[] parts = birthdate.split("-");
		int year = Integer.parseInt(parts[2]);
		int month = Integer.parseInt(parts[1]);
		int day=Integer.parseInt(parts[0]);
		System.out.println("anno"+year);
		System.out.println("mese"+month);
		System.out.println("day"+day);
		LocalDate dbirthdate = new LocalDate (year, month, day);
		LocalDate now = new LocalDate();
		String age = Years.yearsBetween(dbirthdate, now).toString();
		String sub= age.substring(1, age.length()-1);
		System.out.println(sub);
		return Integer.parseInt(sub.toString());
	}

	// Database operations
	// get the Person which id correspond to the given id as parameter, return a
	// Person
	public static Person getPersonById(long personId) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		Person p = em.find(Person.class, (int) personId);
		StorageServiceDao.instance.closeConnections(em);
		return p;
	}

	// get all the People present in the db, return a list of Person
	public static List<Person> getAll() {
		System.out.println("--> Initializing Entity manager...");
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		System.out.println("--> Querying the database for all the people...");
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
		System.out.println("--> Closing connections of entity manager...");
		StorageServiceDao.instance.closeConnections(em);
		return list;
	}

	// save a new Person in the db
	public static Person savePerson(Person p) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
		return p;
	}



	// delete the Person givean as input in the db
	public static void removePerson(Person p) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		em.remove(p);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
	}


}
