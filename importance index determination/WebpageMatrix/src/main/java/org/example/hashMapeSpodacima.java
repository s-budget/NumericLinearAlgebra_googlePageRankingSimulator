package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class hashMapeSpodacima implements Serializable
{
    HashMap<String, HashSet<String>> children=new HashMap<>();
    HashMap<String,HashSet<String>> parents=new HashMap<>();


    public hashMapeSpodacima(HashMap<String, HashSet<String>> children, HashMap<String, HashSet<String>> parents) {
        this.children = children;
        this.parents = parents;
    }

    public HashMap<String, HashSet<String>> getChildren() {
        return children;
    }

    public void setChildren(HashMap<String, HashSet<String>> children) {
        this.children = children;
    }

    public HashMap<String, HashSet<String>> getParents() {
        return parents;
    }

    public void setParents(HashMap<String, HashSet<String>> parents) {
        this.parents = parents;
    }
    public void loadState() throws IOException, ClassNotFoundException {

        FileInputStream fileInputStream
                = new FileInputStream("Parents_Children.txt");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        hashMapeSpodacima h = (hashMapeSpodacima) objectInputStream.readObject();
        this.children=h.getChildren();
        this.parents=h.getParents();
        objectInputStream.close();
    }
    public void saveState() throws IOException {

        FileOutputStream fileOutputStream
                = new FileOutputStream("Parents_Children.txt");
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();
        objectOutputStream.close();
    }
}