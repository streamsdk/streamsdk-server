package com.ugc.utils;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.*;


public class XmlUtils
{
    public static void addContent(String value, Element root) {
        if (value != null)
            root.addContent(value);
    }

    public static void addAttribute(String key, String value, Element root) {
        if (value != null && !value.isEmpty())
            root.setAttribute(key, value);
    }

    public static void addAttribute(String key, String value, Element root, Namespace namespace) {
        if (value != null && !value.isEmpty())
            root.setAttribute(key, value, namespace);
    }

    public static Element addElement(String key, String value, Element root) {
        Element element = null;
        if (key != null) {
            element  = new Element(key, root.getNamespace());
            element.setText(value);
            root.addContent(element);
        }
        return element;
    }

    private static Attribute getAttribute(Element element, String string) {
           return element.getAttribute(string);
    }

    public static String outputString(Element element){
        XMLOutputter outputter = new XMLOutputter();
        return element != null ? outputter.outputString(element).trim() : null;
    }

    public static String outputString(Document document){
        XMLOutputter outputter = new XMLOutputter();
        return document != null ? outputter.outputString(document).trim() : null;
    }

    public static Document createDocument(Element root) {
        Document doc = null;
        if(root !=null){
            doc = new Document();
            doc.setRootElement(root);
        }
        return doc;
    }

    public static Element getChild(Element element, String string) {
        return element.getChild(string);
    }

     public static Element createElement(String xmlString) {
        return createDocument(xmlString).getRootElement();
     }

    public static Document createDocument(String xmlString) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        return createDocument(inputStream);
    }

    public static Document createDocument(InputStream inputStream) {
        try {
            return new SAXBuilder(false).build(inputStream);
        } catch (JDOMException e) {

        } catch (IOException e) {

        }
        return null;
    }

}

