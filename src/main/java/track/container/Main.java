package track.container;

import track.container.beans.Car;
import track.container.beans.Engine;
import track.container.beans.Gear;
import track.container.config.Bean;
import track.container.config.ConfigReader;
import track.container.config.InvalidConfigurationException;

import java.io.File;
import java.util.List;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws InvalidConfigurationException {

        /*

        ПРИМЕР ИСПОЛЬЗОВАНИЯ

         */

//        // При чтении нужно обработать исключение
//        ConfigReader reader = new JsonReader();
//        List<Bean> beans = reader.parseBeans("config.json");
//        Container container = new Container(beans);
//
//        Car car = (Car) container.getByClass("track.container.beans.Car");
//        car = (Car) container.getById("carBean");

        ConfigReader reader = new XMLConfigReader();
        List<Bean> beans = reader.parseBeans(new File("C:/JavaLessons/track16-master/src/main/resources/config.xml"));
        Container container = new Container(beans);

        Car car = (Car) container.getByClass("track.container.beans.Car");
        System.out.println(car);

        Gear gear = (Gear) container.getById("gearBean");
        System.out.println(gear);

        Engine engine = (Engine) container.getByClass("track.container.beans.Engine");
        System.out.println(engine);
    }
}
