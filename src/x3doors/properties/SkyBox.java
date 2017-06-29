package x3doors.properties;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.MyNodeList;
import util.Utils;
import util.X3DomExportable;
import x3doors.DocInstance;
import x3doors.exporters.X3DomExporter;


public class Skybox implements  X3DomExportable {
    /* This array is used to check the correct skybox images extensions */
    private static final String[] SKYBOX_SUPPORTED_EXTENSIONS = new String[] {"gif", "jpg", "png"};

    /* The url of the front skybox image */
    private String frontUrl;
    /* The url of the back skybox image */
    private String backUrl;
    /* The url of the left skybox image */
    private String leftUrl;
    /* The url of the right skybox image */
    private String rightUrl;
    /* The url of the top skybox image */
    private String topUrl;
    /* The url of the bottom skybox image */
    private String bottomUrl;

    /* Creates a skybox with the given properties.
     * 
     * @param frontUrl The url of the front skybox image
     * @param backUrl The url of the back skybox image
     * @param leftUrl The url of the left skybox image
     * @param rightUrl The url of the right skybox image
     * @param topUrl The url of the top skybox image
     * @param bottomUrl The url of the bottom skybox image
     * @throws Exception When trying to specify a skybox image which does not exist or with an unsupported extension
     */
    Skybox(String frontUrl, String backUrl, String leftUrl, String rightUrl, String topUrl, String bottomUrl) throws Exception {
        Utils.checkContentsExistences(frontUrl, backUrl, leftUrl, rightUrl, topUrl, bottomUrl);
        Utils.checkContentsExtensions(SKYBOX_SUPPORTED_EXTENSIONS, frontUrl, backUrl, leftUrl, rightUrl, topUrl, bottomUrl);
        this.frontUrl = frontUrl;
        this.backUrl = backUrl;
        this.leftUrl = leftUrl;
        this.rightUrl = rightUrl;
        this.topUrl = topUrl;
        this.bottomUrl = bottomUrl;
    }
    private void createURLs(){
        try {
            String title = SceneProperties.getTitle();
            String path = X3DomExporter.getExportingFolderPath() + title + "/Textures/Skybox/";
            File dir = new File(path);
            dir.mkdirs();
            Files.copy(new File(frontUrl).toPath(), new File(path + "front.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(backUrl).toPath(), new File(path + "back.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(leftUrl).toPath(), new File(path + "left.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(rightUrl).toPath(), new File(path + "right.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(topUrl).toPath(), new File(path + "top.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(bottomUrl).toPath(), new File(path + "bottom.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception exception) {
            // This exception should never be thrown because the path are checked in the skybox constructor
            System.out.println("X3DExporter:\tfailure reported copying " + exception.getMessage());}

    }
    public MyNodeList toX3Dom(){ 
        createURLs(); 
        MyNodeList wrapper = new  MyNodeList(); 
        Document doc = DocInstance.getInstance(); 
        Element skybox = doc.createElement("Background"); 
        skybox.setAttribute("frontUrl", "Textures/Skybox/front.png"); 
        skybox.setAttribute("backUrl", "Textures/Skybox/back.png"); 
        skybox.setAttribute("leftUrl", "Textures/Skybox/left.png"); 
        skybox.setAttribute("rightUrl", "Textures/Skybox/right.png"); 
        skybox.setAttribute("topUrl", "Textures/Skybox/top.png"); 
        skybox.setAttribute("bottomUrl", "Textures/Skybox/bottom.png"); 
        wrapper.appendChild(skybox) ;
        return wrapper; 
    }



}
