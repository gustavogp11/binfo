/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bioinfo.tp.ej3;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/**
 *
 * @author Gustavo
 */
public class BlastOutput {

    private static void parseBlastOutput(String inFile, String pattern, String outFile) throws Exception{
        File fXmlFile = new File(inFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Hit");

        Document out = dBuilder.newDocument();
        Element root = out.createElement("ParsedBlast");
        out.appendChild(root);

        Element hit = out.createElement("Iteration_hits");
        root.appendChild(hit);


        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String def = eElement.getElementsByTagName("Hit_def").item(0).getTextContent();
                if (def.matches(pattern)){
                    Node newNode = nNode.cloneNode(true);
                    out.adoptNode(newNode);
                    hit.appendChild(newNode);
                }

            }
        }

        doc.getDocumentElement().normalize();
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File(outFile));
        transformer.transform(source, result);
        System.out.println("File saved!");

    }

    private static void checkArgs(String[] args) {
        if(args == null || (args.length < 3))
            throw new RuntimeException("Debe ingresar 1- ruta y nombre completo del archivo input en formato Fasta. "
                    + "2- pattern "
                    + "3- ruta y nombre completo del archivo output (blast)");
    }

    public static void main(String[] args) throws Exception {
        try {
         //   args = new String[] { "C:\\Users\\KinSniK\\Documents\\Java\\Bio\\binfo\\tp\\src\\main\\resources\\output.xml" ,
         //            ".*Insulin B chain.*",
         //            "C:\\Users\\KinSniK\\Documents\\Java\\Bio\\binfo\\tp\\src\\main\\resources\\lista.xml"};
            checkArgs(args);
            String input = args[0].toString();
            String pattern = args[1].toString();
            String outputFile = args[2].toString();



            parseBlastOutput(input, pattern, outputFile);
        } catch(Exception e) {
            System.out.println("Error (" + e + "): " + e.getMessage());
        }
    }
}
