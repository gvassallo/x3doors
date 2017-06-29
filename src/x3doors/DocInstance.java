package x3doors; 

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class DocInstance {

    private static Document instance; 

    private DocInstance(){ 

    }

    public static Document getInstance() { 
        if (instance == null) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbf.newDocumentBuilder();
                instance = builder.newDocument(); 
            }
            catch (Exception e){
                e.printStackTrace(); 
            }
        }
        return instance ;
    }
}
