package com.mg.querydsl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.mg.querydsl.model.Department;
import com.mg.querydsl.model.Employee;
import com.mg.querydsl.model.QDepartment;
import com.mg.querydsl.model.QEmployee;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;

public class EmployeeQuerydslTest {
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("Querydsl");
		EntityManager manager = factory.createEntityManager();

		EntityTransaction tx = manager.getTransaction();
		tx.begin();

		Department departmentJava = new Department("java");
		manager.persist(departmentJava);

		manager.persist(new Employee("Jakab Gipsz", 50, departmentJava));
		manager.persist(new Employee("Captain Nemo", 30, departmentJava));
		manager.persist(new Employee("Arigo Sachi", 56, departmentJava));
		manager.persist(new Employee("Jules Verne", 45, departmentJava));

		Department departmentPython = new Department("Python");
		manager.persist(departmentPython);

		manager.persist(new Employee("Danny Boyle", 40, departmentPython));
		manager.persist(new Employee("Isaac Newton", 38, departmentPython));

		Department departmentGo = new Department("go");
		manager.persist(departmentGo);

		manager.persist(new Employee("Henry Ford", 60, departmentGo));
		manager.persist(new Employee("Nicky Lauda", 55, departmentGo));
		manager.persist(new Employee("Alberto Tomba", 46, departmentGo));
		manager.persist(new Employee("Gigi D'Agostino", 36, departmentGo));

		selectDepartmentEmployees(manager, "java");
		selectDepartmentEmployees(manager, "python");

		// selectDepartmentsWithMostEmployees(manager);
		
		selectHardWorkingEmployees(manager);
		tx.commit();

		manager.close();
		factory.close();
	}

	public static void selectDepartmentEmployees(EntityManager manager,
			String departmentName) {
		System.out.println("select employees from the department "
				+ departmentName);
		QEmployee employee = new QEmployee("departmentEmployees");
		QDepartment department = new QDepartment("department");
		JPAQuery query = new JPAQuery(manager);

		List<Employee> employees = query.from(employee)
				.innerJoin(employee.department, department)
				.where(department.name.equalsIgnoreCase(departmentName))
				.list(employee);

		for (Employee aEmployee : employees) {
			System.out.println(aEmployee);
		}

		System.out.println("done selecting employees from the department "
				+ departmentName);
	}

	public static void selectDepartmentsWithMostEmployees(EntityManager manager) {
		System.out.println("select the department with the most employees ");
		JPAQuery query = new JPAQuery(manager);
		QDepartment department = new QDepartment("department");
		QDepartment d = new QDepartment("d");

		List<Department> departments = query
				.from(department)
				.where(department.employees.size().eq(
						new JPASubQuery().from(d).unique(
								d.employees.size().max()))).list(department);

		for (Department aDepartment : departments) {
			System.out.println(aDepartment);
		}

		System.out
				.println("end select the department with the most employees ");
	}

	public static void selectHardWorkingEmployees(EntityManager manager) {
		System.out
				.println("select the employees who work more than the average in their department ");

		JPAQuery query = new JPAQuery(manager);
		QEmployee employee = new QEmployee("employee");
		QDepartment department = new QDepartment("department");
		QEmployee e = new QEmployee("e");

		List<Employee> employees = query
				.from(employee).join(employee.department, department).fetch()
				.where(employee.weeklyhours.gt(new JPASubQuery().from(e)
						.where(employee.department.id.eq(e.department.id))
						.unique(e.weeklyhours.avg())))
				.orderBy(department.name.asc(), employee.name.asc())
				.list(employee);

		for (Employee aEmployee : employees) {
			System.out.println(aEmployee);
		}
		System.out
				.println("end select the employees who work more than the average in their department ");
	}
}
