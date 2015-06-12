package x3doors.exporters; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import org.w3c.dom.Element;

import x3doors.DocInstance;

public class Runtime { 
        
    public static Element getRuntimeScript(){
        String path = "res/scripts.js";
        Element script = DocInstance.getInstance().createElement("script"); 
        try {
            String scriptText = ""; 
            BufferedReader runtime = new BufferedReader(new FileReader(new File(path))); 
            String line = null ;     
            while ((line = runtime.readLine())!= null)
                scriptText += line + "\n"; 
            runtime.close(); 
            script.setTextContent(scriptText);  
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return script ;  
    }
    private static String getMenuString(){
        String path = "res/menu.html"; 
        String menuString = "" ; 
        try {
            BufferedReader menu = new BufferedReader(new FileReader(new File(path))); 
            String line = new String(); 
            while((line = menu.readLine())!=null)
                menuString+= line+"\n";
            menu.close();
            return menuString; 
        }catch(IOException e){
             System.out.println(e.getMessage());
             return null; 
        }
    }
    public static void appendMenu(Element body) {
        Element div = DocInstance.getInstance().createElement("div"); 
        div.setTextContent(getMenuString());

        body.appendChild(div); 
    }
}
