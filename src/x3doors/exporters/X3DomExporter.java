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

import util.*; 
import x3doors.DocInstance;
import x3doors.actions.Behavior;
import x3doors.actions.sensors.*;
import x3doors.nodes.*; 
import x3doors.properties.*; 

public class X3DomExporter {

    private static String exportingFolderPath = "ExportedScenes/";
    private static Document doc ; 
    private static MyNodeList scripts = new MyNodeList();

    public static String getExportingFolderPath() {
        return exportingFolderPath;
    }

    public static void setExportingFolderPath(String exportingFolderPath) {
        X3DomExporter.exportingFolderPath = exportingFolderPath;
    }

    public static void export() throws Exception {

        String title = SceneProperties.getTitle();  
        File dir = new File(exportingFolderPath + title);
        dir.mkdir();
        PrintWriter out = new PrintWriter(exportingFolderPath + title + "/" + title + ".xhtml", "UTF-8");

        doc = DocInstance.getInstance(); 
        /* create the Head of the html file  */
        Element html = SceneProperties.getInstance().toX3Dom().get(0); 
        Node head = html.getElementsByTagName("head").item(0);
        /* append the style.css file on the head  */
        head.appendChild(StyleText.getStyle());
        /* create the heading of the file, with a simple title  */
        Element body = doc.createElement("body"); 
        Element pageTitle = doc.createElement("h3");
        Element div = doc.createElement("div");
        div.setAttribute("class", "title");
        pageTitle.setAttribute("class", "title"); 
        pageTitle.setTextContent(title);
        div.appendChild(pageTitle);
        body.appendChild(div);

        /* create the X3D main tag  */
        Element X3D = doc.createElement("X3D") ;
        X3D.setAttribute("class", "someUniqueClass");
        X3D.setAttribute("xmlns", "http://www.web3d.org/specifications/x3d-namespace");
        X3D.setAttribute("id", "someUniqueId");
        X3D.setAttribute("showStat", "false");
        X3D.setAttribute("showLog", "false");

        /* start the creation of the x3d scene  */
        Element scene = createScene(doc);  
        X3D.appendChild(scene); 
        body.appendChild(X3D);

        /* two functions to active the slide menu  */
        String funcName = "classie(); nav();"; 

        /* if there are some distance sensors, with one of the two element that is a camera,  
           you have to attach to the file a script, one for each sensor,
           to get the current camera position, using the runtime api of x3dom  
           for each sensor create a function with his name and finally append functions after the x3d tag */

        if(scripts.getLength()!=0){
            Element script = doc.createElement("script");
            String scriptText = ""; 
            for(Node distance : scripts.getChildren()){
                scriptText += distance.getTextContent();
                funcName += " " +  ((Element)distance).getAttribute("name") + "();";
            }
            funcName += "";
            script.setTextContent(scriptText); 
            body.appendChild(script);
        }
        /* make these functions start when the html page is loaded */  
        body.setAttribute("onload", funcName); 
        /* then append also the runtime script */ 
        body.appendChild(Runtime.getRuntimeScript());
        /* finally append the html menu, to use the runtime script */  
        Runtime.appendMenu(body);
        html.appendChild(body); 
        doc.appendChild(html); 
        /* write the DOM document in the file */ 
        out.write(prettyPrint(doc));
        System.out.println(prettyPrint(doc)); 

        out.close(); 
    }

    /* convert the content of the DOM document in xml using a TransformerFactory */ 
    public static final String prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        String outString = out.toString();
        /* TransformerFactory escapes the <, > in a text node   */
        outString = outString.replaceAll("&lt;", "<");
        outString = outString.replaceAll("&gt;", ">");
        return (outString);
    }

    /* create the scene tag, append the background tag and start the export of each object */ 
    private static Element createScene(Document doc) throws Exception{
        Element scene = doc.createElement("Scene"); 
        if (SceneProperties.getInstance().getBackground() != null)
            scene.appendChild(SceneProperties.getInstance().getBackground().toX3Dom().get(0)); 
        else 
            scene.appendChild(SceneProperties.getInstance().getSkyBox().toX3Dom().get(0));
        MyNodeList behaviors = getBehaviors();
        scene.appendChild(getActiveCameraElement(doc)); 
        scene.appendChild(getSceneObject(Root.getInstance()).get(0)); 

        /* if there are some behavior, create the interactive group and export them  */
        if (behaviors.getLength()!=0){
            Element group = doc.createElement("Group"); 
            group.setAttribute("DEF", "Interactive"); 
            for (Node n : behaviors.getChildren())
                group.appendChild((Element) n);                  
            scene.appendChild(group); 
        }
        return scene;  
    }

    /* get the active camera of the scene and set the id used in the distance sensor scripts */ 
    private static Element getActiveCameraElement(Document doc ){
        Camera activeCamera = SceneProperties.getActiveCamera(); 
        Element camera = doc.createElement((activeCamera.perspective? "" : "Ortho") + "Viewpoint");  
        camera.setAttribute("DEF", activeCamera.getName() + "_View"); 
        camera.setAttribute("id", "viewpoint");
        camera.setAttribute("description", activeCamera.description); 
        camera.setAttribute("position", activeCamera.transform.worldTranslationCoordinates.toX3D()); 
        camera.setAttribute("orientation", activeCamera.transform.worldRotationCoordinates.toX3D()); 
        camera.setAttribute("fieldOfView", Utils.double2StringFormat(activeCamera.verticalAngle * 3.14 / 180) );
        return camera; 
    }

    /* recursive function that exports each object of the scene */ 
    private static MyNodeList getSceneObject(SceneObject sceneObject){
        boolean isCamera = sceneObject instanceof Camera; 
        boolean isLight = sceneObject instanceof Light; 
        boolean isMesh = sceneObject instanceof Mesh ; 
        MyNodeList wrapper = new MyNodeList(); 
        if( !isLight && (!isCamera || (isCamera && (sceneObject == SceneProperties.getActiveCamera())))){
            Element transform_1 = doc.createElement("Transform"); 
            transform_1.setAttribute("DEF", sceneObject.getName() + "_Translation"); 
            transform_1.setAttribute("translation", sceneObject.transform.localTranslationCoordinates.toX3D()); 
            Element transform_2 = doc.createElement("Transform"); 
            transform_2.setAttribute("DEF", sceneObject.getName() + "_Rotation"); 
            transform_2.setAttribute("rotation", sceneObject.transform.localRotationCoordinates.toX3D()); 
            Element scale = doc.createElement("Transform"); 
            scale.setAttribute("scale", Utils.double2StringFormat(sceneObject.transform.localScaleFactor.x) + " " + Utils.double2StringFormat(sceneObject.transform.localScaleFactor.y) + " " + Utils.double2StringFormat(sceneObject.transform.localScaleFactor.z)); 
            if (sceneObject instanceof Root){
                MyNodeList list = new MyNodeList(); 
                list = appendChildren(sceneObject, list); 
                for(int i = 0; i<list.getLength(); i++)
                    scale.appendChild(list.get(i));
                transform_2.appendChild(scale);
                transform_1.appendChild(transform_2);
                wrapper.appendChild(transform_1);
                return wrapper; 
            }
            else if (isMesh)
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

    /* if the object is a mesh, add the Switch group, to make the mesh visible or not  */
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

    /* for each child export it and append to the parent */ 
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
        Behavior behavior = null; 
        Sensor sensor = null; 
        for (int i = 0; i < Behavior.registerSize(); i++){
            behavior = Behavior.get(i); 
            sensor = behavior.getSensor(); 
            if (sensor instanceof Click ) 
                addClickSensor((Click)sensor);
            else if (sensor instanceof AND || sensor instanceof OR){
                if (((LogicalOperator)sensor).getSensor1() instanceof Click){
                    addClickSensor(((Click)((LogicalOperator) sensor).getSensor1())); 
                }
                if (((LogicalOperator)sensor).getSensor2() instanceof Click){
                    addClickSensor(((Click)((LogicalOperator) sensor).getSensor2())); 
                }
            }
            else if (sensor instanceof Distance){
                if(((Distance) sensor ).hasCameraElement()){
                    Element script = (((Distance) sensor).getScript()); 
                    scripts.appendChild(script);  
                }
            }
            for (Node n : behavior.toX3Dom().getChildren())
                behaviors.appendChild((Element) n); 
        }
        return behaviors; 
    }

    /* the touch sensor has to be attached to the object clickable  */
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
