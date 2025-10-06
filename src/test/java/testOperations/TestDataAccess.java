package testOperations;

import java.awt.print.Book;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();
	Logger logger = Logger.getLogger(getClass().getName());

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
	public boolean removeTraveler(String name) {
		System.out.println(">> TestDataAccess: removeTraveler");
		Traveler d = db.find(Traveler.class, name);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
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
			} else {
			return false;
			}
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
		public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverName)
				throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
			logger.info(
					">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverName + " date " + date);
			if (driverName==null) return null;
			try {
				if (new Date().compareTo(date) > 0) {
					logger.info("ppppp");
					throw new RideMustBeLaterThanTodayException(
							ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
				}

				db.getTransaction().begin();
				Driver driver = db.find(Driver.class, driverName);
				if (driver.doesRideExists(from, to, date)) {
					db.getTransaction().commit();
					throw new RideAlreadyExistException(
							ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
				}
				Ride ride = driver.addRide(from, to, date, nPlaces, price);
				// next instruction can be obviated
				db.persist(driver);
				db.getTransaction().commit();

				return ride;
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				return null;
			}
			
		}
		
		public boolean bookRide(String username, Ride ride, int seats, double desk) {
			try {
				db.getTransaction().begin();

				Traveler traveler = getTraveler(username);
				if (traveler == null) {
					return false;
				}

				if (ride.getnPlaces() < seats) {
					return false;
				}

				double ridePriceDesk = (ride.getPrice() - desk) * seats;
				double availableBalance = traveler.getMoney();
				if (availableBalance < ridePriceDesk) {
					return false;
				}

				Booking booking = new Booking(ride, traveler, seats);
				booking.setTraveler(traveler);
				booking.setDeskontua(desk);
				db.persist(booking);

				ride.setnPlaces(ride.getnPlaces() - seats);
				traveler.addBookedRide(booking);
				traveler.setMoney(availableBalance - ridePriceDesk);
				traveler.setIzoztatutakoDirua(traveler.getIzoztatutakoDirua() + ridePriceDesk);
				db.merge(ride);
				db.merge(traveler);
				db.getTransaction().commit();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				db.getTransaction().rollback();
				return false;
			}
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
		public boolean gauzatuEragiketa(String username, double amount, boolean deposit) {
			try {
				db.getTransaction().begin();
				User user = getUser(username);
				if (user != null) {
					double currentMoney = user.getMoney();
					if (deposit) {
						user.setMoney(currentMoney + amount);
					} else {
						if ((currentMoney - amount) < 0)
							user.setMoney(0);
						else
							user.setMoney(currentMoney - amount);
					}
					db.merge(user);
					db.getTransaction().commit();
					return true;
				}
				db.getTransaction().commit();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				db.getTransaction().rollback();
				return false;
			}
		}
		public User getUser(String erab) {
			TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
			query.setParameter("username", erab);
			return query.getSingleResult();
		}
	
	public void removeBook(String name, String from, String to, Date date ) {
		System.out.println(">> TestDataAccess: removeRide");
		Driver d = db.find(Driver.class, name);
			db.getTransaction().begin();
			Ride r= d.removeRide(from, to, date);
			
			List<Booking>b=r.getBookings();
			for(Booking bo: b) {
				r.getBookings().remove(bo);
			}
			db.getTransaction().commit();
			System.out.println("created rides" +d.getCreatedRides());
			
		
		
			
			
			
	}



		
}