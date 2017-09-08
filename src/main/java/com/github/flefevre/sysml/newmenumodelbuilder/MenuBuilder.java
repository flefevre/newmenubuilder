package com.github.flefevre.sysml.newmenumodelbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author Francois Le Fevre
 */
public class MenuBuilder {
	public static void main(String[] args) {
		
		//Stretgy extract from the initial UML creationmenumodel the node that are shared with UML 4 SysML
		
		//Input files
		SAXBuilder builder = new SAXBuilder();
		File umlxmlFile = new File("src/main/resources/ori/UML.creationmenumodel");
		File umledgesxmlFile = new File("src/main/resources/ori/UMLEdges.creationmenumodel");
		File sysmlFile = new File("src/main/resources/inputsysml.txt");

		
		/*
		 * Extract the UML Element that are concerned by the new child menu
		 */
		ArrayList<String> lines = new ArrayList<String>();

		try {
			FileReader fileReader = new FileReader(sysmlFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}

			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//order the list with java 8 and lambda expression
		lines.stream().sorted((object1, object2) -> object1.toString().compareTo(object2.toString()));

		//List that will hold the relevant node
		List<Element> nodes= new ArrayList<Element>();

		//extract UML nodes
		try {

			Document document = (Document) builder.build(umlxmlFile);
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren("menu");

			for (Element node : list) {
				if(lines.contains(node.getAttributeValue("label"))) {
					nodes.add(node);
				}
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		//Extract UML edges
		try {

			Document document = (Document) builder.build(umledgesxmlFile);
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren("menu");

			for (Element node : list) {
				if(lines.contains(node.getAttributeValue("label"))) {
					nodes.add(node);
				}
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		//Order the node before exporting them to the console
		nodes.stream().sorted((object1, object2) -> object1.getAttributeValue("label").compareTo(object2.getAttributeValue("label")));
		try {
			for(Element n : nodes) {

				new XMLOutputter(Format.getPrettyFormat()).output(n, System.out);

				System.out.println("");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
