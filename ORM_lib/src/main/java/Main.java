import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {
    int a;
    public static void main(String[] args) throws IOException, NoSuchFieldException {

        System.out.println(Main.class.getDeclaredField("a").getType());
    }
}
