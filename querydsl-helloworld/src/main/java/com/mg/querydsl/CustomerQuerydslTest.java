package com.mg.querydsl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.mg.querydsl.model.Customer;
import com.mg.querydsl.model.QCustomer;
import com.mysema.query.jpa.impl.JPAQuery;

public class CustomerQuerydslTest {
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("Querydsl");
		EntityManager manager = factory.createEntityManager();

		EntityTransaction tx = manager.getTransaction();
		tx.begin();

		Customer aCustomer = new Customer();
		aCustomer.setFirstName("Bob");
		aCustomer.setLastName("Walters");
		manager.persist(aCustomer);

		Customer otherCustomer = new Customer();
		otherCustomer.setFirstName("Boy");
		otherCustomer.setLastName("George");
		manager.persist(otherCustomer);

		try {
			JPAQuery query = new JPAQuery(manager);
			QCustomer customer = QCustomer.customer;
			Customer bob = query.from(customer)
					.where(customer.firstName.eq("Bob")).uniqueResult(customer);

			System.out.println(bob);

			JPAQuery query2 = new JPAQuery(manager);
			QCustomer qcustomer = new QCustomer("qcustomer");

			List<Customer> boCustomers = query.from(qcustomer)
					.where(qcustomer.firstName.like("B%"))
					.orderBy(customer.firstName.asc(), customer.lastName.asc())
					.list(qcustomer);
			System.out.println("Retrieved customers count  : "
					+ boCustomers.size());
			for (Customer c : boCustomers) {
				System.out.println(c);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();

		manager.close();
		factory.close();
	}
}
