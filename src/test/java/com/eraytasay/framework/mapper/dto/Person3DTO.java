package com.eraytasay.framework.mapper.dto;

import java.time.LocalDate;

public class Person3DTO {
    public String firstName;
    public String lastName;
    public int age;
    public LocalDate birthDate;

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName.toUpperCase();
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName.toUpperCase();
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age * 10;
    }

    public LocalDate getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }
}
