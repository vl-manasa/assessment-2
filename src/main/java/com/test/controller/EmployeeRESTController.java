package com.test.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.test.dao.EmployeeDB;
import com.test.representations.Employee;
import org.eclipse.jetty.util.Fields;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeRESTController {

    private final Validator validator;

    public EmployeeRESTController(Validator validator) {
        this.validator = validator;
    }


    @RolesAllowed({ "USER" ,"ADMIN"})
    @GET
    public Response getEmployees() {
        return Response.ok(EmployeeDB.getEmployees()).build();
    }

    @RolesAllowed({ "USER" ,"ADMIN"})
    @GET
    @Path("/{id}")
    public Response getEmployeeById(@PathParam("id") Integer id)
    {

        Employee employee = EmployeeDB.getEmployee(id);
        if (employee != null)
            return Response.ok(employee).build();
        else
            throw new WebApplicationException(Status.NOT_FOUND);
    }


    @RolesAllowed({ "ADMIN" })
    @POST
    public Response createEmployee(Employee employee) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        //insert the employee into the database first
        EmployeeDB.insert(employee.getId(),employee);

        Employee e = EmployeeDB.getEmployee(employee.getId());

        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Employee> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (e != null) {
            EmployeeDB.updateEmployee(employee.getId(), employee);
            return Response.ok(employee).build();
        } else
            throw new WebApplicationException(Status.NOT_FOUND);
    }

    @RolesAllowed({ "USER" ,"ADMIN"})
    @PUT
    @Path("/{id}")
    public Response updateEmployeeById(@PathParam("id") Integer id, Employee employee)
    {
        // validation
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        Employee e = EmployeeDB.getEmployee(id);

        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Employee> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }

        if (e != null)
        {
            employee.setId(employee.getId());
            EmployeeDB.removeEmployee(id);
            EmployeeDB.updateEmployee(employee.getId(), employee);
            return Response.ok(employee).build();
        } else
            throw new WebApplicationException(Status.NOT_FOUND);
    }

    @RolesAllowed({ "ADMIN"})
    @DELETE
    @Path("/{id}")
    public Response removeEmployeeById(@PathParam("id") Integer id)
    {
        Employee employee = EmployeeDB.getEmployee(id);

        if (employee != null)
        {
            EmployeeDB.removeEmployee(id);
            return Response.ok().build();
        }
        else
            throw new WebApplicationException(Status.NOT_FOUND);

    }

    @RolesAllowed({ "USER","ADMIN"})
    @PATCH
    @Path("/{id}")
    public Response ModifyEmployeeById(@PathParam("id") Integer id,Employee employee)
    {
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        Employee old=EmployeeDB.getEmployee(id);

        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Employee> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }

        if(old==null)
        {
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        if(employee.getId()!=null)
        {
            old.setId(employee.getId());
        }

        if(employee.getEmail()!=null)
        {
            old.setEmail(employee.getEmail());
        }

        if(employee.getFirstName()!=null)
        {
            old.setFirstName(employee.getFirstName());
        }

        if(employee.getLastName()!=null)
        {
            old.setLastName(employee.getLastName());
        }

        EmployeeDB.removeEmployee(id);

        EmployeeDB.updateEmployee(old.getId(),old);

        return Response.ok(old).build();
    }

}