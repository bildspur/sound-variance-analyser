package ch.bildspur.json;

import processing.core.PApplet;
import processing.data.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by cansik on 24.09.16.
 */
public class JSON {

    public static JSONObject getJSON(Object obj)
    {
        JSONObject json = new JSONObject();
        Class<?> c = obj.getClass();

        // convert fields
        for(Field field : c.getDeclaredFields())
            if(field.isAnnotationPresent(JSONField.class))
                setJSONByField(json, obj, field);

        // convert getter & setter

        return json;
    }

    static void setJSONByField(JSONObject json, Object obj, Field field)
    {
        try {
            field.setAccessible(true);
            setJSONValue(json, field.getName(), field.get(obj), field.getType());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static void setJSONValue(JSONObject json, String name, Object value, Type type)
    {
        if(boolean.class.equals(type))
            json.setBoolean(name, (boolean)value);

        if(int.class.equals(type))
            json.setInt(name, (int)value);

        if(long.class.equals(type))
            json.setLong(name, (long)value);

        if(float.class.equals(type))
            json.setFloat(name, (float)value);

        if(double.class.equals(type))
            json.setDouble(name, (double)value);

        if(String.class.equals(type))
            json.setString(name, (String) value);
    }

    public static Object getObject(Object obj, JSONObject json)
    {
        Class<?> c = obj.getClass();

        // convert fields
        for(Field field : c.getDeclaredFields())
            if(field.isAnnotationPresent(JSONField.class))
                try {
                    setFieldByJSON(json, obj, field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

        return obj;
    }

    static void setFieldByJSON(JSONObject json, Object obj, Field field) throws IllegalAccessException {
        field.setAccessible(true);

        if(boolean.class.equals(field.getType()))
            field.setBoolean(obj, json.getBoolean(field.getName()));

        if(int.class.equals(field.getType()))
            field.setInt(obj, json.getInt(field.getName()));

        if(long.class.equals(field.getType()))
            field.setLong(obj, json.getLong(field.getName()));

        if(float.class.equals(field.getType()))
            field.setFloat(obj, json.getFloat(field.getName()));

        if(double.class.equals(field.getType()))
            field.setDouble(obj, json.getDouble(field.getName()));

        if(String.class.equals(field.getType()))
            field.set(obj, json.getString(field.getName()));
    }
}
