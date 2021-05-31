import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public Main() throws IOException, SAXException, ParserConfigurationException {
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException {
//        Manager.objectToDatabase("Person");
//        List<Person> people = (List<Person>) Manager.databseToObject("people","Person");
//        for(Person p: people){
//            System.out.println(p);
//        }
//        Manager.deleteFromDatabase(4,"people");
//        Manager.searchById(2,"people","Person");
//
//        Manager.addElement("Person", "people");
//        Manager.update("people");
    }
}