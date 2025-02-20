package com.eraytasay.framework.mapper.test;

import com.eraytasay.framework.mapper.Mapper;
import com.eraytasay.framework.mapper.dto.Person1DTO;
import com.eraytasay.framework.mapper.dto.Person2DTO;
import com.eraytasay.framework.mapper.dto.Person3DTO;
import com.eraytasay.framework.mapper.dto.Person4DTO;
import com.eraytasay.framework.mapper.entity.Person1;
import com.eraytasay.framework.mapper.entity.Person2;
import com.eraytasay.framework.mapper.entity.Person3;
import com.eraytasay.framework.mapper.exception.MapperException;
import com.eraytasay.framework.mapper.mapper.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MapperTest {
    @Test
    public void givenNameAndClass_whenMap_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper1.class);
        var person = getPerson1();
        var res = mapper.map(person, "toPersonDTO", Person1DTO.class);

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.lastName, res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birthDate, res.birthDate);
    }

    @Test
    public void givenNameAndClass_whenMapWithDifferentFields_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper2.class);
        var person = getPerson2();
        var res = mapper.map(person,"toPersonDTO", Person2DTO.class);

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.familyName, res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birth, res.birthDate);
    }

    @Test
    public void givenNameAndClass_whenMapWithIgnore_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper3.class);
        var person = getPerson2();
        var res = mapper.map(person, "toPersonDTO", Person2DTO.class);

        assertEquals(person.firstName, res.firstName);
        assertNull(res.lastName);
        assertEquals(person.age, res.age);
        assertNull(res.birthDate);
    }

    @Test
    public void givenNameAndClass_whenMapWithRename_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper4.class);
        var person = getPerson1();
        var res = mapper.map(person, "toPersonDTORenamed", Person1DTO.class);

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.lastName, res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birthDate, res.birthDate);
    }

    @Test
    public void givenNameAndClass_whenMapWithFieldMapper_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper5.class);
        var person = getPerson1();
        var res = mapper.map(person, "toPersonDTO", Person1DTO.class);

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.lastName.toUpperCase(), res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birthDate, res.birthDate);
    }

    @Test
    public void givenNameAndClass_whenMapWithRenamedFieldMapper_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper6.class);
        var person = getPerson1();
        var res = mapper.map(person, "toPersonDTO", Person1DTO.class);

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.lastName.toUpperCase(), res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birthDate, res.birthDate);
    }

    @Test
    public void givenName_whenMap_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper1.class);
        var person = getPerson1();
        var res = (Person1DTO)mapper.map(person, "toPersonDTO");

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.lastName, res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birthDate, res.birthDate);
    }

    @Test
    public void givenNothing_whenMap_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper7.class);
        var person = getPerson1();
        var res = (Person1DTO)mapper.map(person);

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.lastName, res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birthDate, res.birthDate);
    }

    @Test
    public void givenClass_whenMap_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper7.class);
        var person = getPerson1();
        var res = mapper.map(person, Person1DTO.class);

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.lastName, res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birthDate, res.birthDate);
    }

    @Test
    public void givenNothing_whenTwoMapperExists_thenFails()
    {
        var mapper = new Mapper(IPersonMapper8.class);
        var person = getPerson1();

        assertThrows(MapperException.class, () -> mapper.map(person));
    }

    @Test
    public void givenNameAndClass_whenMap_thenGettersAreCalled()
    {
        var mapper = new Mapper(IPersonMapper1.class);
        var mock = Mockito.mock(Person1.class);

        mapper.map(mock, "toPersonDTO", Person1DTO.class);

        verify(mock, times(1)).getFirstName();
        verify(mock, times(1)).getLastName();
        verify(mock, times(1)).getBirthDate();
        verify(mock, times(1)).getAge();
    }

    @Test
    public void givenClass_whenMap_thenSettersAreCalled()
    {
        var mapper = new Mapper(IPersonMapper9.class);
        var person = getPerson1();
        var res = mapper.map(person, Person3DTO.class);

        assertEquals(person.firstName.toUpperCase(), res.firstName);
        assertEquals(person.lastName.toUpperCase(), res.lastName);
        assertEquals(person.age * 10, res.age);
        assertEquals(person.birthDate, res.birthDate);
    }

    @Test
    public void givenClass_whenMapWithoutAccessors_thenSuccess()
    {
        var mapper = new Mapper(IPersonMapper10.class);
        var person = getPerson3();
        var res = mapper.map(person, Person4DTO.class);

        assertEquals(person.firstName, res.firstName);
        assertEquals(person.lastName, res.lastName);
        assertEquals(person.age, res.age);
        assertSame(person.birthDate, res.birthDate);
    }

    @Test
    public void givenInvalidSourceObject_whenMap_thenFail()
    {
        var mapper = new Mapper(IPersonMapper1.class);
        var sourceObj = new Object();

        assertThrows(MapperException.class, () -> mapper.map(sourceObj));
    }

    private Person1 getPerson1()
    {
        var person = new Person1();

        person.firstName = "Ahmet";
        person.lastName = "Çelik";
        person.age = 100;
        person.birthDate = LocalDate.of(1980, 1, 1);

        return person;
    }

    private Person2 getPerson2()
    {
        var person = new Person2();

        person.firstName = "Ahmet";
        person.familyName = "Çelik";
        person.age = 100;
        person.birth = LocalDate.of(1980, 1, 1);

        return person;
    }

    private Person3 getPerson3()
    {
        var person = new Person3();

        person.firstName = "Ahmet";
        person.lastName = "Çelik";
        person.age = 100;
        person.birthDate = LocalDate.of(1980, 1, 1);

        return person;
    }
}
