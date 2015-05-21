package util;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MyNodeList {
    public ArrayList<Node> nodeList ; 

    public MyNodeList(){ 
        nodeList = new ArrayList<Node>(); 
    }
    public int getLength()
    {
         return nodeList.size(); 
    }
    public MyNodeList appendChild(Node node){
         nodeList.add(node); 
         return this; 
    }
    public Element get (int i){
        if (i >= nodeList.size() )
            return null ; 
        return (Element) nodeList.get(i); 
    }
    public ArrayList<Node> getChildren(){
         return nodeList;
    }
}
