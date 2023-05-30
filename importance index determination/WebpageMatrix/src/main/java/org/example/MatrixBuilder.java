package org.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.example.Main.findLinks;

public class MatrixBuilder
{
    public static void main(String... args) throws IOException, ClassNotFoundException {

        double m=0.15;

        hashMapeSpodacima podaci=new hashMapeSpodacima(null,null);

        podaci.loadState();
        SortedSet<String> sortedWebPages = new TreeSet<>(podaci.getChildren().keySet());

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

        for(String linkChild :sortedWebPages)
        {
            StringBuilder line= new StringBuilder();
            int i=0;
            for(String linkParent :sortedWebPages)
            {
                if(!podaci.getChildren().get(linkParent).contains(linkChild))
                {
                    line.append(String.format("%.5f",m/podaci.getChildren().size())+",");
                }
                else
                { i=1;
                    line.append(String.format("%.5f", (1f / podaci.getChildren().get(linkParent).size())*(1d-m)+(m/podaci.getChildren().size()))).append(",");
                }
            }
            //Dangling node aproximation
//            if(podaci.getChildren().get(linkChild).size()==0)
//            {System.setOut(console);
//                HashSet<String> bbb=findLinks(linkChild);
//                System.out.println(linkChild+" children");
//                System.setOut(o);
//            }
//            if(podaci.getParents().get(linkChild).size()==0)
//            {System.setOut(console);
//                System.out.println(linkChild+" parents");
//                System.setOut(o);
//            }
            line = new StringBuilder(line.substring(0, line.length() - 1));
            System.out.println(line);
        }
        System.setOut(console);
        System.out.println(
                "Matrix generated");
    }
}
