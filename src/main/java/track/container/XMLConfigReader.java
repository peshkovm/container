package track.container;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import track.container.config.*;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLConfigReader implements ConfigReader {
    private String id; // Уникальный ID бина
    private String className; // Класс бина
    private String name; // Имя поля
    private String value; // Значение поля
    private ValueType type; // Метка ссылочное значение или примитив
    private ArrayList<Bean> outBeanList; //Выходной лист бинов

    @Override
    public List<Bean> parseBeans(File configFile) throws InvalidConfigurationException {
        outBeanList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("C:/JavaLessons/track16-master/src/main/resources/config.xml"));
            document.getDocumentElement().normalize();
/*            Element root = document.getDocumentElement();
            System.out.println(root.getTagName());*/

            NodeList beanList = document.getElementsByTagName("bean");

/*            Element root = document.getDocumentElement();
            NodeList beanList = root.getChildNodes();*/

            for (int i = 0; i < beanList.getLength(); i++) {
                if (!(beanList.item(i) instanceof Element))
                    continue;
                Map<String, Property> properties = new HashMap<>();

                Node bean = beanList.item(i);
                //bean.normalize();
                NamedNodeMap beanAttrs = bean.getAttributes();
                id = beanAttrs.getNamedItem("id").getNodeValue();
                className = beanAttrs.getNamedItem("class").getNodeValue();

/*                System.out.println("id = " + id);
                System.out.println("class = " + className);*/

                //NodeList propertyList = bean.getChildNodes();
                NodeList propertyList = ((Element) bean).getElementsByTagName("property");

                for (int j = 0; j < propertyList.getLength(); j++) {
/*                    if (!(propertyList.item(i) instanceof Element))
                        continue;*/

                    Node property = propertyList.item(j);
                    NamedNodeMap propertyAttrs = property.getAttributes();

                    name = propertyAttrs.item(0).getNodeValue();
                    value = propertyAttrs.item(1).getNodeValue();
                    String strType = propertyAttrs.item(1).getNodeName();

                    if (strType.equals("val"))
                        type = ValueType.VAL;
                    else if (strType.equals("ref"))
                        type = ValueType.REF;
                    else
                        throw new InvalidConfigurationException("Type of property must be 'val' or 'ref'. Received: " + strType);

/*                    System.out.print("  name = " + name + " ");
                    System.out.print("  value = " + value + " ");
                    System.out.print("  type = " + type + '\n');*/

                    Property property1 = new Property(name, value, type);
                    properties.put(name, property1);
                }
                Bean outBean = new Bean(id, className, properties);
                outBeanList.add(outBean);
                //System.out.println();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outBeanList;
    }
}
