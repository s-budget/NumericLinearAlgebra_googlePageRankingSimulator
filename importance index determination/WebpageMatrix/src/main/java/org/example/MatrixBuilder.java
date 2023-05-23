package org.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.SortedSet;
import java.util.TreeSet;

public class MatrixBuilder
{
    public static void main(String... args) throws IOException, ClassNotFoundException {
        hashMapeSpodacima podaci=new hashMapeSpodacima(null,null);
        podaci.loadState();
        System.out.println("bob");
        SortedSet<String> sortedWebPages=new TreeSet<>();
        sortedWebPages.addAll(podaci.getChildren().keySet());

        PrintStream o = new PrintStream(new File("Matrix.txt"));
        PrintStream console = System.out;
        System.setOut(o);

        for(String link :sortedWebPages){
            if(link.equals(sortedWebPages.last())) {
                System.out.println(link );
            }
            else {
                System.out.print(link+ ",");
            }

        }
        System.setOut(console);
        System.out.println(
                "Matrix generated");
    }
}
