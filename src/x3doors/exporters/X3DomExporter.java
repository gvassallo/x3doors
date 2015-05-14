package x3doors.exporters; 

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.MyNodeList;
import util.Utils;

import x3doors.DocInstance;
import x3doors.nodes.Camera;
import x3doors.nodes.Light;
import x3doors.nodes.Mesh;
import x3doors.nodes.Root;
import x3doors.nodes.SceneObject;
import x3doors.properties.SceneProperties;
public class X3DomExporter {

    private static String exportingFolderPath = "ExportedScenes/";
    private static Document doc ; 
    /**
     * @return the exportingFolderPath
     */
    public static String getExportingFolderPath() {
        return exportingFolderPath;
    }

    /**
     * @param exportingFolderPath the exportingFolderPath to set
     */
    public static void setExportingFolderPath(String exportingFolderPath) {
        X3DomExporter.exportingFolderPath = exportingFolderPath;
    }

    public static void export() throws Exception {
        doc = DocInstance.getInstance(); 
        Element html = SceneProperties.getInstance().toX3Dom().get(0); 
        String title = SceneProperties.getTitle();  

        File dir = new File(exportingFolderPath + title);
        // Create a new folder for the exported scene
        dir.mkdir();
        // Create the exported scene file
        PrintWriter out = new PrintWriter(exportingFolderPath + title + "/" + title + ".xhtml", "UTF-8");
        
        Element body = doc.createElement("body"); 
        Element X3D = doc.createElement("X3D") ;
        X3D.setAttribute("xmlns", "http://www.web3d.org/specifications/x3d-namespace");
        X3D.setAttribute("id", "someUniqueId");
        X3D.setAttribute("showStat", "false");
        X3D.setAttribute("showLog", "false");
        X3D.setAttribute("x", "0px");
        X3D.setAttribute("y", "0px");
        X3D.setAttribute("width", "400px");
        X3D.setAttribute("height", "400px");
        Element scene= doc.createElement("Scene");
        scene.appendChild(getSceneObject(Root.getInstance()).get(0)); 
        X3D.appendChild(scene); 
        body.appendChild(X3D);
        html.appendChild(body); 
        doc.appendChild(html); 
        out.write(prettyPrint(doc));
        System.out.println(prettyPrint(doc)); 
        out.close(); 
    }
    public static final String prettyPrint(Document xml) throws Exception {

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        return (out.toString());
    }

private static MyNodeList getSceneObject(SceneObject sceneObject){
    boolean isCamera = sceneObject instanceof Camera; 
    boolean isLight = sceneObject instanceof Light; 
    boolean isMesh = sceneObject instanceof Mesh ; 
    MyNodeList wrapper = new MyNodeList(); 
    if( !isLight && (!isCamera || (isCamera && (sceneObject == SceneProperties.getActiveCamera())))){

        Element transform_1 = doc.createElement("Transform"); 
        transform_1.setAttribute("DEF", sceneObject.getName() + /* (isLight ? "_Placeholder" : "") + */  "_Translation"); 
        Element transform_2 = doc.createElement("Transform"); 
        transform_2.setAttribute("DEF", sceneObject.getName() + /* (isLight ? "_Placeholder" : "") + */ "_Rotation"); 
        Element scale = doc.createElement("Transform"); 
        scale.setAttribute("scale", Utils.double2StringFormat(sceneObject.transform.localScaleFactor.x) + " " + Utils.double2StringFormat(sceneObject.transform.localScaleFactor.y) + " " + Utils.double2StringFormat(sceneObject.transform.localScaleFactor.z)); 
        if (isMesh)
            scale.appendChild(getMesh(sceneObject)); 
        else if(!isCamera || (isCamera && (sceneObject == SceneProperties.getActiveCamera()))){
            MyNodeList list = sceneObject.toX3Dom();  
            list = appendChildren(sceneObject, list);
            for (int i = 0; i < list.getLength(); i++)
                scale.appendChild(list.get(i)); 
        }
        // is light or else     
        transform_2.appendChild(scale); 
        transform_1.appendChild(transform_2); 
        wrapper.appendChild(transform_1); 
    }
    else {
         wrapper = sceneObject.toX3Dom(); 
         wrapper = appendChildren(sceneObject, wrapper); 
    }
    return wrapper; 
}

private static Element getMesh(SceneObject sceneObject){
    Element Switch = doc.createElement("Switch"); 
    Switch.setAttribute("DEF", sceneObject.getName() + "_Switch"); 
    Switch.setAttribute("whichChoice", (sceneObject.visible ? "0" : "-1")) ;
    Element group = doc.createElement("Group"); 
    group.setAttribute("DEF", sceneObject.getName() + "_SwitchGroup"); 
    MyNodeList object = sceneObject.toX3Dom(); 
    object  = appendChildren(sceneObject, object); 
    for (int i = 0; i< object.getLength(); i++){
        group.appendChild(object.get(i)); 
    }
    Switch.appendChild(group); 
    return Switch; 
}

private static MyNodeList  appendChildren(SceneObject sceneObject, MyNodeList parent){
    for (SceneObject child : sceneObject.getChildren()){
        MyNodeList n = getSceneObject(child);   
        for (int i = 0 ; i < n.getLength(); i++) 
            parent.appendChild(((Element) n.get(i))); 
    }       
    return parent; 
}
}
