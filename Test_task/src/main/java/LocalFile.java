import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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

public class LocalFile {
    private int fieldObject;
    private static String firstFile = "/1.xml";
    private static String secondFile = "/2.xml";
    private static String xsltFile = "src/main/resources/TemplateForTransform.xslt";

    public LocalFile(int fieldObject) {
        this.fieldObject = fieldObject;
    }

    public static List<Integer> getValuesFromDataBase(String url, String user, String password) {
        String query = "SELECT FIELD FROM TEST";
        List<Integer> list = new ArrayList();
        try (Connection connection = DriverManager.getConnection(url, user, password);
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

    public static void writeToInXmlFile(List<Integer> list) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = builder.newDocument();

        Element entries = document.createElement("entries");
        for (Integer i : list) {
            Element entry = document.createElement("entry");
            Element field = document.createElement("field");
            entries.appendChild(entry);
            entry.appendChild(field);
            Text text = document.createTextNode(String.valueOf(i));
            field.appendChild(text);
        }
        document.appendChild(entries);

        DOMImplementation impl = document.getImplementation();
        DOMImplementationLS implLS = (DOMImplementationLS) impl
                .getFeature("LS", "3.0");
        LSSerializer ser = implLS.createLSSerializer();
        ser.getDomConfig().setParameter("format-pretty-print", true);

        LSOutput out = implLS.createLSOutput();
        out.setEncoding("UTF-8");
        try {
            out.setByteStream(Files.newOutputStream(Paths.get(firstFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ser.write(document, out);
        printAboutFinishWriter();
        transformXmlFile(firstFile, secondFile, xsltFile);
    }

    public static void transformXmlFile(String srcFile, String targetFile, String templateXSLT) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer(new StreamSource(templateXSLT));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            transformer.transform(new StreamSource(srcFile),
                    new StreamResult(new FileOutputStream(targetFile)));
        } catch (TransformerException | FileNotFoundException e) {
            e.printStackTrace();
        }
        printAboutFinishTransform();
    }
    public static int getSumAllNumbers() {
        File xmlFile = new File(secondFile);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.parse(xmlFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("entry");

        List<String> listNumbers = new ArrayList<>(1000000);
        for(int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            listNumbers.add(element.getAttribute("field"));
        }
        return listNumbers.stream().mapToInt(Integer::parseInt).sum();
    }

    private static void printAboutFinishTransform() {
        System.out.println("Преобразование содержимого из 1.xml в 2.xml завершено");
    }

    private static void printAboutFinishWriter() {
        System.out.println("Содержимое строк БД записано в файл 1.xml");
    }

    @Override
    public String toString() {
        return "ObjectDateBase{fieldObject=" + fieldObject + '}';
    }
}