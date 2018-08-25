package track.container;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import track.container.config.*;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {
    public static final String CONF_FILE_PATH = "C:/JavaLessons/track16-master/src/main/resources/config.xml";
    private LinkedList<Bean> sortedBeans;
    private Map<String, Object> objByName; //маппинг ИмяБина->Объект
    private Map<String, Object> objByClassName; //маппинг ИмяКласса -> Объект

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) {
        BeanGraph beanGraph = new BeanGraph(beans);
        try {
            objByName = new HashMap<>();
            objByClassName = new HashMap<>();
            sortedBeans = beanGraph.topologicalSort();

            for (Bean bean : sortedBeans) {
                instantiateBean(bean);
            }
        } catch (CycleReferenceException e) {
            System.err.println("Cycle reference found in config file");
            e.printStackTrace();
        }
    }

    /**
     * Вернуть объект по имени бина из конфига
     * Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) {
        return objByName.get(id);
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) {
        return objByClassName.get(className);
    }

    private void instantiateBean(Bean bean) {
        String className = bean.getClassName();
        Class clazz = null;
        try {
            clazz = Class.forName(className);

            Constructor constructor = clazz.getConstructor();
            Object obj = constructor.newInstance();

            for (Property property : bean.getProperties().values()) {
                String fieldName = property.getName();

                Field field = clazz.getDeclaredField(fieldName);
                Class type = field.getType();

                //setvalue -> setValue
                String methodName = "set".concat((fieldName.substring(0, 1).toUpperCase()).
                        concat(fieldName.substring(1)));


                Method method = clazz.getMethod(methodName, type);

                //если поле - примитив
                if (property.getType() == ValueType.VAL) {
                    Object value = cast(property.getValue(), type);
                    method.invoke(obj, value);
                } else {
                    Object ref = objByName.get(property.getValue());
                    method.invoke(obj, ref);
                }
            }
            objByName.put(bean.getId(), obj);
            objByClassName.put(bean.getClassName(), obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NotPrimitiveException e) {
            e.printStackTrace();
        }
    }

    private Object cast(String value, Class type) throws NotPrimitiveException {
        if (type == Byte.TYPE)
            return Byte.parseByte(value);
        else if (type == Short.TYPE)
            return Short.parseShort(value);
        else if (type == Integer.TYPE)
            return Integer.parseInt(value);
        else if (type == Long.TYPE)
            return Long.parseLong(value);
        else if (type == Float.TYPE)
            return Float.parseFloat(value);
        else if (type == Double.TYPE)
            return Double.parseDouble(value);
        else if (type == Boolean.TYPE)
            return Boolean.parseBoolean(value);
        else if (type == Character.TYPE)
            return value.charAt(0);
        else
            throw new NotPrimitiveException("Cannot cast. Value from config file is not a primitive");
    }
}
