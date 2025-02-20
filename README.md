This framework does the mapping between two classes. It uses reflection to do that. Assume you have two classes Employee and EmployeeDTO. You have an Employee instance. You want to map your Employee instance to an EmployeeDTO instance.
With the help of this framework you can do that without actually implementing the mapper method. Classes are given below:
```
public class Employee {
    public String firstName;
    public String familyName;
    public double wage;
    public LocalDate birthDate;
}

public class EmployeeDTO {
    public String firstName;
    public String lastName;
    public double salary;
    public LocalDate birthDate;
}
```
To use this framework, first you should create a mapper interface or class. //...
