package com.javarush.task.task33.task3309;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/*
Комментарий внутри xml
*/
public class Solution {
    public static String toXmlWithComment(Object obj, String tagName, String comment) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setCoalescing(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            marshaller.marshal(obj, doc);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

            NodeList nodeList = doc.getElementsByTagName("*");
            if (nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeName().equals(tagName)) {
                        node.getParentNode().insertBefore(doc.createComment(comment), node);
                    }
                    if (node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                        Node currentNode = node.getFirstChild();
                        if (currentNode.getTextContent().matches(".*[<>&\'\"].*")) {
                            String content = currentNode.getTextContent();
                            CDATASection cdataSection = doc.createCDATASection(content);
                            node.replaceChild(cdataSection, currentNode);
                        }
                    }
                }
            }

            StringWriter write = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(write));
            return write.toString();

        } catch (JAXBException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
    }
}
