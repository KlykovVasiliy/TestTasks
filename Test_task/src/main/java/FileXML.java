import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;
import util.ConnectionManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileXML {
    private static final String FIRST_FILE = "src/main/resources/1.xml";
    private static final String SECOND_FILE = "src/main/resources/2.xml";
    private static final String XSLT_FILE = "src/main/resources/TemplateForTransform.xslt";

    static {
        writeDocumentToInXmlFile();
        transformXmlFile();
    }

    private FileXML() {
    }

    private static void writeDocumentToInXmlFile() {
        Document document = getDocumentXML();
        DOMImplementation impl = document.getImplementation();
        DOMImplementationLS implLS = (DOMImplementationLS) impl
                .getFeature("LS", "3.0");
        LSSerializer ser = implLS.createLSSerializer();
        ser.getDomConfig().setParameter("format-pretty-print", true);
        LSOutput out = implLS.createLSOutput();
        out.setEncoding("UTF-8");
        try {
            out.setByteStream(Files.newOutputStream(Paths.get(FIRST_FILE)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ser.write(document, out);
        printAboutFinishWriter();       //Лишний метод
    }

    private static Document getDocumentXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Element entries = document.createElement("entries");
        List<Integer> list = getValuesFromDataBase();
        for (Integer i : list) {
            Element entry = document.createElement("entry");
            Element field = document.createElement("field");
            entries.appendChild(entry);
            entry.appendChild(field);
            Text text = document.createTextNode(String.valueOf(i));
            field.appendChild(text);
        }
        document.appendChild(entries);
        return document;
    }

    private static List<Integer> getValuesFromDataBase() {
        String query = "SELECT FIELD FROM TEST";
        List<Integer> list = new ArrayList();
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                list.add(resultSet.getInt("field"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void transformXmlFile() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer =
                    transformerFactory.newTransformer(new StreamSource(XSLT_FILE));
            transformer.transform(new StreamSource(FIRST_FILE),
                    new StreamResult(new FileOutputStream(SECOND_FILE)));
        } catch (TransformerException | FileNotFoundException e) {
            e.printStackTrace();
        }
        printAboutFinishTransform();
    }

    public static int getSumAllNumbers() {
        NodeList nodeList = getValueAttributeATag();
        List<String> listNumbers = new ArrayList<>(1000000);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            listNumbers.add(element.getAttribute("field"));
        }
        return listNumbers.stream().mapToInt(Integer::parseInt).sum();
    }

    private static NodeList getValueAttributeATag() {
        File xmlFile = new File(SECOND_FILE);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        document.getDocumentElement().normalize();
        return document.getElementsByTagName("entry");
    }

    private static void printAboutFinishTransform() {
        System.out.println("Преобразование содержимого из 1.xml в 2.xml завершено");
    }

    private static void printAboutFinishWriter() {
        System.out.println("Содержимое строк БД записано в файл 1.xml");
    }
}