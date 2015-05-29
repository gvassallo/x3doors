

package x3doors.exporters;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.w3c.dom.Element;

import x3doors.DocInstance;

public class StyleText{ 

    public static Element getStyle(){
        String path = "res/style.css";
        String styleString = "";
        try{
        BufferedReader css = new BufferedReader(new FileReader(new File(path))); 
        String line = null; 
        while((line =css.readLine())!= null)
            styleString += line; 
        css.close();
        }catch (IOException e){
             System.out.println("style file not found");
             System.out.println(e.getMessage());
        }
        Element style = DocInstance.getInstance().createElement("style"); 
        style.setTextContent(styleString); 
        return style; 
    }


}
