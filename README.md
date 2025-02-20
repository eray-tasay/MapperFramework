This framework does the mapping between two classes. It uses reflection to do that. Assume you have two classes Student and StudentDTO. You have a Student instance. 
You want to map your Student instance to a StudentDTO instance. With the help of this framework, you can do that without actually implementing the mapper method. 
The classes are given below:
```
public class Student {
    public String firstName;
    public String lastName;
    public int age;
}

public class StudentDTO {
    public String firstName;
    public String lastName;
    public int age;
}
```
To use this framework, first you should create a mapper class or interface. Then declare a mapper method that is annotated with @MapperMethod annotation. 
You do not need to implement this method. The framework will do the mapping for you. You can get the mapper reference using Mappers.getMapper() method. 
This method takes the mapper class you declare. For example you could declare a mapper class that looks like this:
```
public interface IStudentMapper {
    Mapper MAPPER = Mappers.getMapper(IStudentMapper.class);

    @MapperMethod
    StudentDTO toStudentDTO(Student student);

    @MapperMethod
    Student toStudent(StudentDTO studentDTO);
}
```
```
Student student = StudentFactory.getStudent();
StudentDTO studentDTO = IStudentMapper.MAPPER.map(student, "toStudentDTO", StudentDTO.class);
```
Notice that the String argument that map method takes is identical to the method name in the former example. That does not have to be the case. 
You can give another name to a mapper method with 
@MapperMethod annotation like @MapperMethod("someOtherName"). 
```
public interface IStudentMapper {
    Mapper MAPPER = Mappers.getMapper(IStudentMapper.class);

    @MapperMethod
    StudentDTO toStudentDTO(Student student);

    @MapperMethod("someOtherName")
    Student toStudent(StudentDTO studentDTO);
}
```
```
StudentDTO studentDTO = StudentFactory.getStudentDTO();
Student student = IStudentMapper.MAPPER.map(studentDTO, "someOtherName", Student.class);
```
Now consider the case of mapping where there are some different named fields in both classes. You can overcome this situation by using @Mapping annotation. 
This annotation is used to associate a target field with another source field. Which is what we need in this case.
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
```
public interface IEmployeeMapper {
    Mapper MAPPER = Mappers.getMapper(IEmployeeMapper.class);

    @MapperMethod
    @Mapping(target = "lastName", source = "familyName")
    @Mapping(target = "salary", source = "wage")
    EmployeeDTO toEmployeeDTO(Employee employee);

    @MapperMethod
    @Mapping(target = "familyName", source = "lastName")
    @Mapping(target = "wage", source = "salary")
    Employee toEmployee(EmployeeDTO employeeDTO);
}
```
```
Employee employee = EmployeeFactory.getEmployee();
EmployeeDTO employeeDTO = IEmployeeMapper.MAPPER.map(employee, "toEmployeeDTO", EmployeeDTO.class);
```
Now consider the case where you want to perform some operations before assigning source field to target field. You can do that too. All you need to do is to declare a public static method that accepts 
a parameter whose type matches the type of source field and whose return type matches target field. In the run time your source field will be passed to this static method as parameter and 
this method will be invoked. The return value of this method is assigned to target field. Even source and target types may be different. You can specify field mapper methods in @Mapping annotation's
fieldMapper parameter. And do not forget to annotate all field mapper methods to annotate with @FieldMapperMethod. You can rename them to with @FieldMapperMethod("someOtherName"). If you do not 
specify any name the name of the method is used. Now let's give an example that illustrates this case. Assume you have Person and PersonDTO classes. Both classes have firstName, lastName, birthDate
fields. But you want to store birthDate as long in PersonDTO class and you want lastName to be upper case. The code is given below. Note that method name is not passed to map method. You can do
that when there is only one mapper method in your mapper class.
```
public class Person {
    public String firstName;
    public String lastName;
    public LocalDate birthDate;
}

public class PersonDTO {
    public String firstName;
    public String lastName;
    public long birthDate;
}

public interface IPersonMapper {
    Mapper MAPPER = Mappers.getMapper(IPersonMapper.class);

    @MapperMethod
    @Mapping(target = "lastName", source = "lastName", fieldMapper = "toUpperCase")
    @Mapping(target = "birthDate", source = "birthDate", fieldMapper = "toMillisRenamed")
    PersonDTO toPersonDTO(Person person);

    @FieldMapperMethod
    static String toUpperCase(String str)
    {
        return str.toUpperCase();
    }

    @FieldMapperMethod("toMillisRenamed")
    static long toMillis(LocalDate date)
    {
        //...
    }
}

Person person = PersonFactory.getPerson();
PersonDTO personDTO = IPersonMapper.MAPPER.map(person, PersonDTO.class);
```
If you have getters and setters in your source and target classes, they are fist take into account. 
They have privilege over basic field access and field assignment. If framework can not discover any getters it simply gets the value of the field instead of calling getter. 
Similarly, if framework can not discover any setters it simply sets the field instead of calling setter. Framework uses the java bean naming convention to discover getter and setters. 
For example if you have a field named value it looks for setValue() and getValue(). Lastly, the target class must have a public default constructor.
