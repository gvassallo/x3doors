package util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
 
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
 
public class XMLFormatter {
	/* The document to pretty-print */
	private Document document;
	
	/** Creates a XML formatter with the given properties.
	 * 
	 * @param xml The xml string to pretty print
	 * @throws Exception When something goes wrong during the pretty-printing process (i.e. the string is not well formed)
	 */
	public XMLFormatter(String xml) throws Exception {
	    document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
	}
	
	/** @return A string containing the pretty-printed version of the current document */
	public String getXML() {
		DOMImplementationLS impl = (DOMImplementationLS) document.getImplementation().getFeature("LS", "3.0");
		
		LSSerializer lsSerializer = impl.createLSSerializer();
		lsSerializer.getDomConfig().setParameter("format-pretty-print", true);
		
		LSOutput lsOutput = impl.createLSOutput();
		lsOutput.setEncoding("UTF-8");
		StringWriter stringWriter = new StringWriter();
		lsOutput.setCharacterStream(stringWriter);
		lsSerializer.write(document, lsOutput);
		 
		return stringWriter.toString();
	}
}
