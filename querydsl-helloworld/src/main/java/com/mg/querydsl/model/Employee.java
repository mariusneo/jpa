package com.mg.querydsl.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    
    private int weeklyhours;
    
    @ManyToOne
    private Department department;

    public Employee() {}

    public Employee(String name, int weeklyhours, Department department) {
        this.name = name;
        this.weeklyhours = weeklyhours;
        this.department = department;
    }
    

    public Employee(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    
    public int getWeeklyhours() {
		return weeklyhours;
	}

	public void setWeeklyhours(int weeklyhours) {
		this.weeklyhours = weeklyhours;
	}

	@Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + 
        		", weeklyhours=" + weeklyhours  +
        		", department="
                + department.getName() + "]";
    }

}
