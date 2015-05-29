package x3doors.exporters; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import x3doors.DocInstance;

public class Runtime { 
    private static  Document doc = DocInstance.getInstance();     
    public static void appendRuntimeObject(Element body){
        Element div = doc.createElement("div"); 
        div.setAttribute("class", "list"); 
        Element ul = doc.createElement("ul"); 
        Element li = doc.createElement("li"); 
        Element em = doc.createElement("em"); 
        Element ul1 = doc.createElement("ul"); 
        em.setTextContent("Viewpoint"); 
        li.appendChild(em); 
        Element li1 = doc.createElement("li"); 
        Element a1 = doc.createElement("a"); 
        a1.setAttribute("href", "#") ;
        a1.setAttribute("onclick" ,"$element.runtime.examine(); updateNavInfo(); return false; "); 
        a1.setTextContent("examine");
        li1.appendChild(a1);
        ul1.appendChild(li1); 
        Element li2 = doc.createElement("li"); 
        Element a2 = doc.createElement("a"); 
        a2.setAttribute("href", "#") ;
        /* a2.setAttribute("onclick" ,"$element.runtime.resetView();  return false; ");  */
        a2.setAttribute("onclick" ,"$element.runtime.walk(); updateNavInfo(); return false; "); 
        a2.setTextContent("walk");
        li2.appendChild(a2);
        ul1.appendChild(li2); 
        Element li3 = doc.createElement("li"); 
        Element a3 = doc.createElement("a"); 
        a3.setAttribute("href", "#") ;
        a3.setAttribute("onclick" ,"$element.runtime.fly(); updateNavInfo(); return false; "); 
        a3.setTextContent("fly");
        li3.appendChild(a3);
        ul1.appendChild(li3); 
        li.appendChild(ul1); 
        ul.appendChild(li);
        div.appendChild(ul);
        div.appendChild(getCameraAction());
        div.appendChild(getDebugAction());
        body.appendChild(getRuntimeScript()); 
        body.appendChild(div);
    }

    private static Element getCameraAction(){
        Element ul = doc.createElement("ul");
        Element li = doc.createElement("li"); 
        Element em = doc.createElement("em"); 
        em.setTextContent("Camera"); 
        li.appendChild(em);
        Element ul1 = doc.createElement("ul"); 
        Element li1 = doc.createElement("li");
        Element a1 = doc.createElement("a"); 
        a1.setAttribute("href", "#") ;
        a1.setAttribute("onclick" ,"$element.runtime.resetView(); return false; "); 
        a1.setTextContent("reset view");
        li1.appendChild(a1);
        ul1.appendChild(li1);
        li.appendChild(ul1);
        ul.appendChild(li);
        return ul ;
    }
    private static Element getDebugAction(){
        Element ul = doc.createElement("ul");
        Element li = doc.createElement("li"); 
        Element em = doc.createElement("em"); 
        em.setTextContent("Debug"); 
        li.appendChild(em);
        Element ul1 = doc.createElement("ul"); 
        Element li1 = doc.createElement("li");
        Element a1 = doc.createElement("a"); 
        a1.setAttribute("href", "#") ;
        a1.setAttribute("onclick" ,"toggleDebug(this); return false; "); 
        a1.setTextContent("Show debug");
        li1.appendChild(a1);
        ul1.appendChild(li1);
        Element li2 = doc.createElement("li");
        Element a2 = doc.createElement("a"); 
        a2.setAttribute("href", "#") ;
        a2.setAttribute("onclick" ,"toggleStats(this); return false; "); 
        a2.setTextContent("Show stats");
        li2.appendChild(a2);
        ul1.appendChild(li2);
        li.appendChild(ul1);
        ul.appendChild(li);
        return ul ;
    }
    private static Element getRuntimeScript(){
        String path = "res/runtime.js";
        Element script = doc.createElement("script"); 
        try {
            String scriptText = ""; 
            BufferedReader runtime = new BufferedReader(new FileReader(new File(path))); 
            String line = null ;     
            while ((line = runtime.readLine())!= null)
                scriptText += line; 
            runtime.close(); 
            script.setTextContent(scriptText);  
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return script ;  
    }
}
