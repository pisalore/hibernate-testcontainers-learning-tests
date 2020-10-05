package com.unifi.attws.hibernate.learning.test;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.Test;


public class HibernteLearningTest {

	@Entity(name = "Museum")
	@Table(name = "museums")
	public static class Museum {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private long id;

		@Column(name = "Museum_Name")
		private String name;

		@Column(name = "Number_Of_Rooms")
		private int rooms;

		@Column(name = "Number_Of_Occupied_Rooms")
		private int occupiedRooms;

		public Museum(String name, int rooms) {
			super();
			this.name = name;
			this.rooms = rooms;
			this.occupiedRooms = 0;

		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getRooms() {
			return rooms;
		}

		public void setRooms(int rooms) {
			this.rooms = rooms;
		}

		public int getOccupiedRooms() {
			return occupiedRooms;
		}

		public void setOccupiedRooms(int availableRooms) {
			this.occupiedRooms = availableRooms;
		}

	}

	EntityManagerFactory sessionFactory;
	EntityManager entityManager;
	private static final Logger LOGGER = LogManager.getLogger(HibernteLearningTest.class);

	

	@Before
	public void setUp() throws Exception {
		sessionFactory = Persistence.createEntityManagerFactory("postgres.container");
		entityManager = sessionFactory.createEntityManager();

	}
	
	@Test
	public void testHibernateReturnsNothingWhenNOMuseumsArePersisted() {
		assertThat(entityManager.createQuery("FROM Museum", Museum.class).getResultList()).isEmpty();
	}
	

	@Test
	public void testHibernatePersistNewMuseum() {
		Museum museum = createMuseum("MoMa", 100);
		entityManager.getTransaction().begin();
		entityManager.persist(museum);
		entityManager.getTransaction().commit();
		Museum museum1 = entityManager.find(Museum.class, museum.getId());
		assertThat(museum).isEqualTo(museum1);
		entityManager.close();

	}

	@Test
	public void testHibernateReturnsAllPersistedMuseums() {
		Museum museum1 = createMuseum("Uffizi", 10);
		Museum museum2 = createMuseum("Prado", 10);
		entityManager.getTransaction().begin();
		entityManager.persist(museum1);
		entityManager.persist(museum2);
		entityManager.getTransaction().commit();
		List<Museum> museums = entityManager.createQuery("FROM Museum", Museum.class).getResultList();
		assertThat(museums).containsExactly(museum1, museum2);
	}


	public Museum createMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

}
