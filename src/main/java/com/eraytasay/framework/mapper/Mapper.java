package com.eraytasay.framework.mapper;

import com.eraytasay.framework.mapper.annotation.FieldMapperMethod;
import com.eraytasay.framework.mapper.annotation.MapperMethod;
import com.eraytasay.framework.mapper.annotation.Mapping;
import com.eraytasay.framework.mapper.exception.MapperException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static com.eraytasay.util.reflection.ReflectionUtil.*;

public class Mapper {
    private final Class<?> m_mapperCls;

    public Object map(Object srcObj, String mapperMethodName)
    {
        try {
            var mapperMethod = findMapperMethod(mapperMethodName);
            var destCls = mapperMethod.getReturnType();
            var srcCls = mapperMethod.getParameters()[0].getType();

            validateSourceObject(srcCls, srcObj);

            return map(destCls, srcCls, srcObj, mapperMethod);
        }
        catch (Throwable ex) {
            throw new MapperException(ex);
        }
    }

    public <T> T map(Object srcObj, String mapperMethodName, Class<T> returnTypeCls)
    {
        try {
            var mapperMethod = findMapperMethod(mapperMethodName);
            var srcCls = mapperMethod.getParameters()[0].getType();

            validateSourceObject(srcCls, srcObj);

            return map(returnTypeCls, srcCls, srcObj, mapperMethod);
        }
        catch (Throwable ex) {
            throw new MapperException(ex);
        }
    }

    public Object map(Object srcObj)
    {
        try {
            var mapperMethod = getJustOneMapperMethod();
            var destCls = mapperMethod.getReturnType();
            var srcCls = mapperMethod.getParameters()[0].getType();

            validateSourceObject(srcCls, srcObj);

            return map(destCls, srcCls, srcObj, mapperMethod);
        }
        catch (Throwable ex) {
            throw new MapperException(ex);
        }
    }

    public <T> T map(Object srcObj, Class<T> returnTypeCls)
    {
        try {
            var mapperMethod = getJustOneMapperMethod();
            var srcCls = mapperMethod.getParameters()[0].getType();

            validateSourceObject(srcCls, srcObj);

            return map(returnTypeCls, srcCls, srcObj, mapperMethod);
        }
        catch (Throwable ex) {
            throw new MapperException(ex);
        }
    }

    public Mapper(Class<?> mapperCls)
    {
        m_mapperCls = mapperCls;
    }

    private static <T> T createDestinationObject(Class<T> destCls) throws Throwable
    {
        var destDefaultCtor = destCls.getConstructor();

        return destDefaultCtor.newInstance();
    }

    private static boolean isMapperMethod(Method method, String mapperMethodName)
    {
        var mapperAnnotation = method.getAnnotation(MapperMethod.class);

        if (mapperAnnotation == null)
            return false;

        return mapperAnnotation.value().equals(mapperMethodName) || (mapperAnnotation.value().isEmpty() &&
                                                                        mapperMethodName.equals(method.getName()));
    }

    private static boolean isFieldMapperMethod(Method method, String mapperMethodName)
    {
        var fieldMapperAnnotation = method.getAnnotation(FieldMapperMethod.class);

        if (fieldMapperAnnotation == null)
            return false;

        return fieldMapperAnnotation.value().equals(mapperMethodName) || (fieldMapperAnnotation.value().isEmpty() &&
                                                                            mapperMethodName.equals(method.getName()));
    }

    private Method findMapperMethod(String mapperMethodName)
    {
        return Arrays.stream(getAllPublicMethods(m_mapperCls)).filter(method -> isMapperMethod(method, mapperMethodName))
                .findFirst().orElseThrow(() -> new MapperException("There is no such mapper method named " + mapperMethodName));
    }

    private Method findFieldMapperMethod(String fieldMapperMethodName)
    {
        return Arrays.stream(getAllPublicMethods(m_mapperCls)).filter(method -> isFieldMapperMethod(method, fieldMapperMethodName))
                .findFirst().orElseThrow(() -> new MapperException("There is no such field mapper method named " + fieldMapperMethodName));
    }

    private Optional<Mapping> findMappingAnnotationForDestField(String destFieldName, Method mapperMethod)
    {
        var mappingAnnotations = mapperMethod.getAnnotationsByType(Mapping.class);

        return Arrays.stream(mappingAnnotations).filter(ma -> ma.target().equals(destFieldName)).findFirst();
    }

    private Object getResultOfFieldMapperMethod(String methodName, Object argument) throws Throwable
    {
        var method = findFieldMapperMethod(methodName);

        return method.invoke(null, argument);
    }

    private Method getJustOneMapperMethod()
    {
        var mapperMethods = Arrays.stream(getAllPublicMethods(m_mapperCls))
                .filter(method -> method.isAnnotationPresent(MapperMethod.class)).toArray(Method[]::new);

        if (mapperMethods.length > 1)
            throw new MapperException("There are more methods than one");
        else if (mapperMethods.length == 0)
            throw new MapperException("There is not any mapper method");

        return mapperMethods[0];
    }

    private void setDestFieldIfNecessary(Class<?> destCls, Field destField, Object destObj, Class<?> srcCls, Object srcObj,
                                         Method mapperMethod) throws Throwable
    {
        var mappingAnnotationOpt = findMappingAnnotationForDestField(destField.getName(), mapperMethod);

        if (mappingAnnotationOpt.isEmpty()) {
            var srcField = srcCls.getField(destField.getName());

            setDestField(destCls, destField, destObj, srcCls, srcField, srcObj);
            return;
        }

        var mappingAnnotation = mappingAnnotationOpt.get();

        if (mappingAnnotation.ignore())
            return;

        var srcField = srcCls.getField(mappingAnnotation.source());

        if (mappingAnnotation.fieldMapper().isEmpty())
            setDestField(destCls, destField, destObj, srcCls, srcField, srcObj);
        else {
            var val = getResultOfFieldMapperMethod(mappingAnnotation.fieldMapper(), getValueOfField(srcCls, srcField, srcObj));

            setDestFieldWithoutGetter(destCls, destField, destObj, val);
        }
    }

    private void setDestField(Class<?> destCls, Field destField, Object destObj, Class<?> srcCls, Field srcField, Object srcObj) throws Throwable
    {
        var setter = getSetter(destCls, destField);
        var srcValue = getValueOfField(srcCls, srcField, srcObj);

        if (setter == null)
            destField.set(destObj, srcValue);
        else
            setter.invoke(destObj, srcValue);
    }

    private Object getValueOfField(Class<?> cls, Field field, Object obj) throws Throwable
    {
        var getter = getGetter(cls, field);

        return getter == null ? field.get(obj) : getter.invoke(obj);
    }

    private void setDestFieldWithoutGetter(Class<?> destCls, Field destField, Object destObj, Object val) throws Throwable
    {
        var setter = getSetter(destCls, destField);

        if (setter == null)
            destField.set(destObj, val);
        else
            setter.invoke(destObj, val);
    }

    private <T> T map(Class<T> destCls, Class<?> srcCls, Object srcObj, Method mapperMethod) throws Throwable
    {
        var destObj = createDestinationObject(destCls);

        for (var destField : getAllFields(destCls))
            setDestFieldIfNecessary(destCls, destField, destObj, srcCls, srcObj, mapperMethod);
        return destObj;
    }

    private void validateSourceObject(Class<?> srcCls, Object srcObj)
    {
        if(!srcCls.isInstance(srcObj))
            throw new MapperException("source object is not instance of %s".formatted(srcCls.getSimpleName()));
    }
}
