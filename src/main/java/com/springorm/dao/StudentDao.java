package com.springorm.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.orm.hibernate5.HibernateTemplate;

import com.springorm.entities.Student;
@Transactional
public class StudentDao {
	private HibernateTemplate hibernateTemplate;
	
	//CREATE
	public int insert(Student student)
	{
		Integer i=(Integer)this.hibernateTemplate.save(student);
		return i;
	}
	
	//REAL SINGLE DATA
	public Student getStudent(int studentId)
	{
		Student student=this.hibernateTemplate.get(Student.class,studentId);
		return student;
	}
	
	//READ MULTIPLE DATA
	public List<Student> getAllStudents()
	{
		List<Student> students=this.hibernateTemplate.loadAll(Student.class);
		return students;
	}
	
	//UPDATE
	public void update(Student student)
	{
		this.hibernateTemplate.update(student);
	}
	
	//DELETE
	public void delete(int studentId)
	{
		Student student=this.hibernateTemplate.get(Student.class,studentId);
		this.hibernateTemplate.delete(student);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	

	}
	
