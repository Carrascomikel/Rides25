package testOperations;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Alert;
import domain.Booking;
import domain.Car;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("TestDataAccess created");

		//open();
		
	}

	
	public void open(){
		

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		System.out.println("TestDataAccess opened");

		
	}
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}
	public Driver createDriver(String name, String pass) {
		System.out.println(">> TestDataAccess: addDriver");
		Driver driver=null;
			db.getTransaction().begin();
			try {
			    driver=new Driver(name,pass);
				db.persist(driver);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return driver;
    }
	public boolean existDriver(String email) {
		 return  db.find(Driver.class, email)!=null;
		 

	}
	public double getActualMoney(String erab) {
		TypedQuery<Double> query = db.createQuery("SELECT u.money FROM User u WHERE u.username = :username",
				Double.class);
		query.setParameter("username", erab);
		Double money = query.getSingleResult();
		if (money != null) {
			return money;
		} else {
			return 0;
		}
	}
	public void deleteUser(User us) {
		try {
			db.getTransaction().begin();
			us = db.merge(us);
			db.remove(us);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public User getUser(String erab) {
		TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
		query.setParameter("username", erab);
		try {
		    return query.getSingleResult();
		} catch (NoResultException e) {
		    return null;
		}

	}
	public boolean addDriver(String username, String password) {
		try {
			db.getTransaction().begin();
			Driver existingDriver = getDriver(username);
			Traveler existingTraveler = getTraveler(username);
			if (existingDriver != null || existingTraveler != null) {
				return false;
			}

			Driver driver = new Driver(username, password);
			driver.setMoney(0);
			db.persist(driver);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}
	public boolean addTraveler(String username, String password) {
		try {
			db.getTransaction().begin();
			Driver existingDriver = getDriver(username);
			Traveler existingTraveler = getTraveler(username);
			if (existingDriver != null || existingTraveler != null) {
				return false;
			}

			Traveler traveler = new Traveler(username, password);
			db.persist(traveler);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}
	
	public Driver getDriver(String erab) {
		TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class);
		query.setParameter("username", erab);
		List<Driver> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}
	public boolean removeDriver(String name) {
		System.out.println(">> TestDataAccess: removeDriver");
		Driver d = db.find(Driver.class, name);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }

	public Traveler getTraveler(String erab) {
		TypedQuery<Traveler> query = db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username",
				Traveler.class);
		query.setParameter("username", erab);
		List<Traveler> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}
		
		public Driver addDriverWithRide(String name, String from, String to,  Date date, int nPlaces, float price) {
			System.out.println(">> TestDataAccess: addDriverWithRide");
				Driver driver=null;
				db.getTransaction().begin();
				try {
					 driver = db.find(Driver.class, name);
					if (driver==null) {
						System.out.println("Entra en null");
						driver=new Driver(name,null);
				    	db.persist(driver);
					}
				    driver.addRide(from, to, date, nPlaces, price);
					db.getTransaction().commit();
					System.out.println("Driver created "+driver);
					
					return driver;
					
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return null;
	    }
		
		
		public boolean existRide(String name, String from, String to, Date date) {
			System.out.println(">> TestDataAccess: existRide");
			Driver d = db.find(Driver.class, name);
			if (d!=null) {
				return d.doesRideExists(from, to, date);
			} else 
			return false;
		}
		public Ride removeRide(String name, String from, String to, Date date ) {
			System.out.println(">> TestDataAccess: removeRide");
			Driver d = db.find(Driver.class, name);
			if (d!=null) {
				db.getTransaction().begin();
				Ride r= d.removeRide(from, to, date);
				db.getTransaction().commit();
				System.out.println("created rides" +d.getCreatedRides());
				return r;

			} else 
			return null;

		}


		
}