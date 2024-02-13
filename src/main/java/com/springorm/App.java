package com.springorm;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.springorm.dao.StudentDao;
import com.springorm.entities.Student;

public class App 
{
    public static void main( String[] args )
    {
       ApplicationContext context=new ClassPathXmlApplicationContext("config.xml");
       StudentDao studentDao=context.getBean("studentDao",StudentDao.class);
//        Student student=new Student(235,"mayank thakur","balrampur");
//       int r=studentDao.insert(student);
//       System.out.println("Inserted"+r);
//       
       Scanner in=new Scanner(System.in);
       
       while(true)
       {
    	   System.out.println("PRESS 1 for add new student");
    	   System.out.println("PRESS 2 for display all student");
    	   System.out.println("PRESS 3 to get detail of a single student");
    	   System.out.println("PRESS 4 for delete student");
    	   System.out.println("PRESS 5 for update student");
    	   System.out.println("PRESS 6 for exit");
    	   System.out.println("*****************************************************************");
    	  try
    	  {
    	int choice=in.nextInt();
    	
    	switch(choice)
    	{
    	case 1://add new student
    		//Taking input from user
    		System.out.println("Enter studentId");
    		int sid=in.nextInt();
    		System.out.println("Enter studentName");
    		String sname=in.next();
    		System.out.println("Enter studentCity");
    		String scity=in.next();
    		//creating student object and set value using constructor
    		Student student=new Student(sid,sname,scity);
    		//calling insert method
    		studentDao.insert(student);
    		System.out.println("INSERTED");
    		break;
    	case 2://display all student
    		List<Student> students=studentDao.getAllStudents();
    		for(Student s:students)
    		{
    		System.out.println("Id:"+s.getStudentId());
    		System.out.println("Name:"+s.getStudentName());
    		System.out.println("City:"+s.getStudentCity());
    		System.out.println("----------------------------------------------------------------------");
    		}
    		
    		break;
    	case 3://detail of single student
    		System.out.println("Enter studentId to get its details");
    		int stid=in.nextInt();
    		Student student1=studentDao.getStudent(stid);
    		System.out.println(student1.getStudentId()+":"+student1.getStudentName()+":"+student1.getStudentCity());
    		System.out.println("----------------------------------------------------------------------");
    		break;
    	case 4://delete student
    		System.out.println("Enter studentId to get deleted");
    		int sdid=in.nextInt();
    		studentDao.delete(sdid);
    		System.out.println("DELETED");
    		break;
    	case 5://update
    		System.out.println("Enter studentId to get updated its details");
    		int suid=in.nextInt();
    		System.out.println("Enter new studentName");
    		String suname=in.next();
    		System.out.println("Enter new studentCity");
    		String sucity=in.next();
    		Student news=new Student(suid,suname,sucity);
    		studentDao.update(news);
    		System.out.println("UPDATED");
    		break;
    	case 6:
    		System.out.println("Thanks for using this application");
    		return;
    
    	}
       }
    	  catch(Exception e)
    	  {
    		  System.out.println("Wrong Input");
    	  }
    	  
       
    }
}
}
