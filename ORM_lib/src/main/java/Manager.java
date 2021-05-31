import Annotations.MyColumn;
import Annotations.MyEntity;
import Annotations.MyId;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Manager {

    static void objectToDatabase(String classPath) throws SQLException, ClassNotFoundException, IOException {
        String url = "jdbc:postgresql://localhost/LIBDB?user=postgres&password=pass";
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        Class c = Class.forName(classPath);
        Annotation a = c.getDeclaredAnnotation(MyEntity.class);
        String annotation = a.toString();
        String nameTemp = annotation.substring(29);
        String tableName = nameTemp.substring(0, nameTemp.length() - 2);
        String query = "CREATE TABLE " + tableName + " ( ";
        List<Field> fields = Arrays.asList(c.getDeclaredFields());
        for (Field x : fields) {
            if (x.getType().getName().equals("java.util.Date") && (x.getAnnotation(MyColumn.class)!=null || x.getAnnotation(MyId.class)!=null)) {
                if (x == fields.get(fields.size() - 1)) {
                    query += x.getAnnotation(Annotations.MyColumn.class).value() + " DATE ) ";
                } else
                    query += x.getAnnotation(Annotations.MyColumn.class).value() + " DATE, ";
            }
            if (x.getType().getName().equals("java.lang.String") && (x.getAnnotation(MyColumn.class)!=null || x.getAnnotation(MyId.class)!=null)) {
                if (x == fields.get(fields.size() - 1)) {
                    query += x.getAnnotation(Annotations.MyColumn.class).value() + " varchar(255) ) ";
                } else
                    query += x.getAnnotation(Annotations.MyColumn.class).value() + " varchar(255), ";
            }
            if (x.getType().getName().equals("double") && (x.getAnnotation(MyColumn.class)!=null || x.getAnnotation(MyId.class)!=null)) {
                if (x == fields.get(fields.size() - 1)) {
                    query += x.getAnnotation(Annotations.MyColumn.class).value() + " float ) ";
                } else
                    query += x.getAnnotation(Annotations.MyColumn.class).value() + " float, ";
            }
            if (x.getType().getName().equals("int") && (x.getAnnotation(MyColumn.class)!=null || x.getAnnotation(MyId.class)!=null)) {
                if (x == fields.get(fields.size() - 1)) {
                    query += x.getAnnotation(Annotations.MyColumn.class).value() + " int ) ";
                } else
                    query += x.getAnnotation(Annotations.MyColumn.class).value() + " int, ";
            }
            if (x.getAnnotation(Annotations.MyId.class) != null) {

                if (x == fields.get(fields.size() - 1)) {
                    query += "id int NOT NULL PRIMARY KEY )";
                } else
                    query += "id int NOT NULL PRIMARY KEY, ";
            }
        }
        System.out.println(query);
        stmt.executeUpdate(query);

        System.out.println("Table has successfully created \n");
    }

    static List<? extends Object> databseToObject(String tableName, String objectPath) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        Class c = Class.forName(objectPath);
        String url = databaseURL();
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from " + tableName + "");
        List<Field> fields = Arrays.asList(c.getDeclaredFields());
        for (Field field : fields) {
            field.setAccessible(true);
        }

        List<Object> list = new ArrayList<>();
        while (resultSet.next()) {
            Object dto = (Object) c.getConstructor().newInstance();

            for (Field field : fields) {
                String name = field.getName();

                try {
                    if (field.getType().getName().equals("long")) {
                        long value = resultSet.getInt(name);
                        field.set(dto, value);
                    }
                    if (field.getType().getName().equals("int")) {
                        int value = resultSet.getInt(name);
                        field.set(dto, value);
                    }
                    if (field.getType().getName().equals("java.lang.String")) {
                        String value = resultSet.getString(name);
                        field.set(dto, value);
                    }
                    if (field.getType().getName().equals("double")) {
                        double value = resultSet.getDouble(name);
                        field.set(dto, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            list.add(dto);
        }
        return list;
    }

    static String databaseURL() throws IOException {
        String xml = FileUtils.readFileToString(new File("config.xml"));
        return xml.split("<databaseurl>")[1].split("</databaseurl>")[0];
    }

    static void deleteFromDatabase(int rowId, String tableName) throws IOException, SQLException {
        String url = databaseURL();
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("delete from " + tableName + " where id=" + rowId + "");
        System.out.println("Row " + rowId + " is deleted successfully \n");
    }

    static void searchById(int rowId, String tableName, String objectPath) throws IOException, SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class c = Class.forName(objectPath);
        String url = databaseURL();
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from " + tableName + " where id=" + rowId + "");
        List<Field> fields = Arrays.asList(c.getDeclaredFields());
        for (Field field : fields) {
            field.setAccessible(true);
        }

        List<Object> list = new ArrayList<>();
        while (resultSet.next()) {

            Object dto = (Object) c.getConstructor().newInstance();


            for (Field field : fields) {
                String name = field.getName();

                try {
                    if (field.getType().getName().equals("long")) {
                        long value = resultSet.getInt(name);
                        field.set(dto, value);
                    }
                    if (field.getType().getName().equals("int")) {
                        int value = resultSet.getInt(name);
                        field.set(dto, value);
                    }
                    if (field.getType().getName().equals("java.lang.String")) {
                        String value = resultSet.getString(name);
                        field.set(dto, value);
                    }
                    if (field.getType().getName().equals("double")) {
                        double value = resultSet.getDouble(name);
                        field.set(dto, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            list.add(dto);
            for(Object x: list){
                System.out.println(x);
            }
        }
    }

    static void addElement(String objectPath, String tableName) throws ClassNotFoundException, IOException, SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Scanner scan = new Scanner(System.in);
        Class c = Class.forName(objectPath);
        String url = databaseURL();
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();

        String query = "INSERT INTO "+tableName+" VALUES ( ";
        List<Field> fields = Arrays.asList(c.getDeclaredFields());
        for (Field field : fields) {
            field.setAccessible(true);
        }
        List<Object> list = new ArrayList<>();
        Object dto = (Object) c.getConstructor().newInstance();

            for (Field field : fields) {
                String name = field.getName();

                try {
                    if (field.getType().getName().equals("long")) {
                        System.out.println("Enter value for field "+name);
                        long value = scan.nextLong();
                        if(field == fields.get(fields.size() - 1)){
                            query+= "'"+Long.toString(value)+"'"+")";
                        }
                        else
                            query+= "'"+Long.toString(value)+"'"+", ";
                        //field.set(dto, value);
                    }
                    if (field.getType().getName().equals("int")) {
                        System.out.println("Enter value for field "+name);
                        int value = scan.nextInt();
                        if(field == fields.get(fields.size() - 1)){
                            query+= "'"+Integer.toString(value)+"'"+")";
                        }
                        else
                            query+= "'"+Integer.toString(value)+"'"+", ";
                        //field.set(dto, value);
                    }
                    if (field.getType().getName().equals("java.lang.String")) {
                        System.out.println("Enter value for field "+name);
                        String value = scan.nextLine();
                        value = scan.nextLine();
                        if(field == fields.get(fields.size() - 1)){
                            query+= "'"+value+"'"+")";
                        }
                        else
                            query+="'"+ value+"'"+", ";
                        //field.set(dto, value);
                    }
                    if (field.getType().getName().equals("double")) {
                        System.out.println("Enter value for field "+name);
                        double value = scan.nextDouble();
                        if(field == fields.get(fields.size() - 1)){
                            query+= "'"+Double.toString(value)+"'"+")";
                        }
                        else
                            query+= "'"+Double.toString(value)+"'"+", ";
                        //field.set(dto, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        System.out.println(query);
//            list.add(dto);
////            for(Object x: list){
////                System.out.println(x);
////        }
        stmt.executeUpdate(query);
    }

    static void update(String tableName) throws IOException, SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter id of row: \n");
        int rowId = scan.nextInt();
        System.out.println("Enter field name to update: \n");
        String fieldName = scan.nextLine();
        fieldName=scan.nextLine();
        System.out.println("Enter new value: \n");
        String newValue = scan.nextLine();

        String url = databaseURL();
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();

        String query = "UPDATE "+tableName+" SET "+fieldName+"='"+newValue+"' WHERE id="+rowId+"";
        stmt.executeUpdate(query);
        System.out.println("Row "+rowId+" is updated successfully \n");
        }
}