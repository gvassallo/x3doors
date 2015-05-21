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
import org.w3c.dom.Node;

import util.Actionable;
import util.MyNodeList;
import util.Utils;

import x3doors.DocInstance;
import x3doors.actions.Behaviour;
import x3doors.actions.sensors.AND;
import x3doors.actions.sensors.Click;
import x3doors.actions.sensors.LogicalOperator;
import x3doors.actions.sensors.OR;
import x3doors.actions.sensors.Sensor;
import x3doors.nodes.Camera;
import x3doors.nodes.Light;
import x3doors.nodes.Mesh;
import x3doors.nodes.Root;
import x3doors.nodes.SceneObject;
import x3doors.properties.SceneProperties;
public class X3DomExporter {

    private static String exportingFolderPath = "ExportedScenes/";

    private static Document doc ; 

    public static String getExportingFolderPath() {
        return exportingFolderPath;
    }

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
        X3D.setAttribute("width", "700px");
        X3D.setAttribute("height", "700px");
        Element scene = createScene(doc);  
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
    private static Element createScene(Document doc) throws Exception{
        Element scene = doc.createElement("Scene"); 
        if (SceneProperties.getInstance().getBackground() != null)
            scene.appendChild(SceneProperties.getInstance().getBackground().toX3Dom().get(0)); 
        else 
            scene.appendChild(SceneProperties.getInstance().getSkyBox().toX3Dom().get(0));
        
        MyNodeList behaviors = getBehaviors();
        scene.appendChild(getActiveCameraElement(doc)); 
        scene.appendChild(getSceneObject(Root.getInstance()).get(0)); 
         
        if (behaviors.getLength()!=0){
             Element group = doc.createElement("Group"); 
             group.setAttribute("DEF", "Interactive"); 
             for (Node n : behaviors.getChildren())
                group.appendChild((Element) n);                  
            scene.appendChild(group); 
        }
        return scene;  
    
    }
    private static Element getActiveCameraElement(Document doc ){
         Camera activeCamera = SceneProperties.getActiveCamera(); 
         Element camera = doc.createElement((activeCamera.perspective? "" : "Ortho") + "Viewpoint");  
         camera.setAttribute("DEF", activeCamera.getName() + "_View"); 
         camera.setAttribute("description", activeCamera.description); 
         camera.setAttribute("position", activeCamera.transform.worldTranslationCoordinates.toX3D()); 
         camera.setAttribute("orientation", activeCamera.transform.worldRotationCoordinates.toX3D()); 
         camera.setAttribute("fieldOfView", Utils.double2StringFormat(activeCamera.verticalAngle * 3.14 / 180) );
         return camera; 
    }
private static MyNodeList getSceneObject(SceneObject sceneObject){
    boolean isCamera = sceneObject instanceof Camera; 
    boolean isLight = sceneObject instanceof Light; 
    boolean isMesh = sceneObject instanceof Mesh ; 
    MyNodeList wrapper = new MyNodeList(); 
    if( !isLight && (!isCamera || (isCamera && (sceneObject == SceneProperties.getActiveCamera())))){

        Element transform_1 = doc.createElement("Transform"); 
        transform_1.setAttribute("DEF", sceneObject.getName() + /* (isLight ? "_Placeholder" : "") + */  "_Translation"); 
        transform_1.setAttribute("translation", sceneObject.transform.localTranslationCoordinates.toX3D()); 
        Element transform_2 = doc.createElement("Transform"); 
        transform_2.setAttribute("DEF", sceneObject.getName() + /* (isLight ? "_Placeholder" : "") + */ "_Rotation"); 
        transform_2.setAttribute("rotation", sceneObject.transform.localRotationCoordinates.toX3D()); 
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
        if (isMesh){
        	Element e = ((Mesh)sceneObject).getElementToAppend(); 
        	if(e != null){
        	e.appendChild(transform_1);
        	wrapper.appendChild(e);
        	}else 
        		wrapper.appendChild(transform_1);
        	
        }else 
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

private static MyNodeList getBehaviors(){ 
    MyNodeList behaviors = new MyNodeList(); 
    Behaviour behavior = null; 
    Sensor sensor = null; 
    Actionable action = null; 
    for (int i = 0; i < Behaviour.registerSize(); i++){
        behavior = Behaviour.get(i); 
        sensor = behavior.getSensor(); 
        if (sensor instanceof Click ) 
            addClickSensor((Click)sensor);
        if (sensor instanceof AND || sensor instanceof OR){
             if (((LogicalOperator)sensor).getSensor1() instanceof Click){
                 addClickSensor(((Click)((LogicalOperator) sensor).getSensor1())); 
             }
             if (((LogicalOperator)sensor).getSensor2() instanceof Click){
                 addClickSensor(((Click)((LogicalOperator) sensor).getSensor2())); 
             }
             
        }
        action = behavior.getAction(); 
        for (Node n : behavior.toX3Dom().getChildren())
            behaviors.appendChild((Element) n); 

    }
    return behaviors; 
}
private static void addClickSensor(Click click){ 
    SceneObject sensorObject = SceneObject.get((click).getObjectToClick()); 
    Element group = doc.createElement("Group"); 
    group.setAttribute("DEF", sensorObject.getName() + "_ClickSensor"); 
    Element touchSensor = doc.createElement("TouchSensor"); 
    touchSensor.setAttribute("DEF", click.getName()); 
    touchSensor.setAttribute("enabled", "true"); 
    group.appendChild(touchSensor);
    ((Mesh)sensorObject).appendElement(group);  
     
}
}
