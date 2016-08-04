package de.serviceflow.lbt4j.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import de.serviceflow.codegenj.CodegenJ;
import de.serviceflow.codegenj.Node;

public class TestCodegenJ {

	@Test
	public final void testMain() throws FileNotFoundException, JAXBException,
			SAXException, XMLStreamException {
		System.out.println("*** Testing CodegenJ");

		String[] args = {
				"-d",
				new File(".").getAbsolutePath() + "/target/dummy",
				"-l",
				"dummydbus",
				"-b",
				"my.dum",
				"-i",
				"my.dum.",
				new File(".").getAbsolutePath()
						+ "/src/test/resources/dum_my.xml" };
		System.err.println("Usage: CodegenJ [-d <destination>] -l <library> -b <busname> -i <interfaceprefix> <xmlfile>");

		CodegenJ testSubject = new CodegenJ();

		testSubject.initHelper.initializeProcessing();
		System.out.println("- initializeProcessing() OK");

		String xmlfile = testSubject.parseOptions(args);
		assertNotNull(xmlfile);
		System.out.println("- parseOptions() OK");

		Node node = testSubject.parseXML(xmlfile);
		assertNotNull(node);
		System.out.println("- parseXML() OK");

		testSubject.generateCode(node, null);
		System.out.println("*** test successful ***");
	}

}
