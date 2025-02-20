package com.eraytasay.framework.mapper.mapper;

import com.eraytasay.framework.mapper.annotation.MapperMethod;
import com.eraytasay.framework.mapper.dto.Person3DTO;
import com.eraytasay.framework.mapper.entity.Person1;

public interface IPersonMapper9 {
    @MapperMethod
    Person3DTO toPersonDTO(Person1 person);
}
