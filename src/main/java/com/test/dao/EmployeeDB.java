package com.test.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.test.representations.Employee;

public class EmployeeDB {

    public static HashMap<Integer, Employee> employees = new HashMap<>();
    static{
        employees.put(1, new Employee(1, "Lokesh", "Gupta", "India"));
        employees.put(2, new Employee(2, "John", "Gruber", "USA"));
        employees.put(3, new Employee(3, "Melcum", "Marshal", "AUS"));
        employees.put(4, new Employee(4, "Manasa", "V L", "IND"));
        employees.put(5, new Employee(5, "Varsha", "V N", "IND"));
    }


    public static List<Employee> getEmployees(){
        return new ArrayList<Employee>(employees.values());
    }

    public static Employee getEmployee(Integer id)
    {
        return employees.get(id);

    }

    public static Integer contains(Integer k)
    {
        if(employees.containsKey(k))
            return 1;

        return 0;

    }

    public static void insert(Integer id, Employee e)
    {
        //insert the new employee
        employees.put(id,e);
    }

    public static void updateEmployee(Integer id, Employee employee){
        employees.put(id, employee);
    }

    public static void removeEmployee(Integer id){
        employees.remove(id);
    }


}