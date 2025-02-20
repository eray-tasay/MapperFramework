package com.eraytasay.framework.mapper.mapper;

import com.eraytasay.framework.mapper.annotation.MapperMethod;
import com.eraytasay.framework.mapper.dto.Person4DTO;
import com.eraytasay.framework.mapper.entity.Person3;

public interface IPersonMapper10 {
    @MapperMethod
    Person4DTO toPersonDTO(Person3 person);
}
