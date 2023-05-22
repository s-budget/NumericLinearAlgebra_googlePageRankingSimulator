package org.example;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class Main {
    public static void main(String args[]) throws IOException {

        int maxDepth = 2;
        int currentDepth = 0;
        int totalExpanded=0;

        HashSet<String> visited = new HashSet<>();
        LinkedHashSet<String> toBeVisited = new LinkedHashSet<>();
        HashMap<String,HashSet<String>> children=new HashMap<>();

        toBeVisited.add("https://www.fer.unizg.hr");
        toBeVisited.add("ROWBREAK");
        //download("https://www.fer.unizg.hr/");
        while(!toBeVisited.isEmpty())
        {
            Optional<String> currentWebPageFound = toBeVisited.stream().findFirst();
            String currentWebPage= simplify(currentWebPageFound.get());
            if(currentWebPage.equals("ROWBREAK"))
            {
                currentDepth ++;
                toBeVisited.remove("ROWBREAK");
                toBeVisited.add("ROWBREAK");

                if(currentDepth == maxDepth)
                    break;
                continue;
            }
            if(visited.contains(currentWebPage))
            {
                toBeVisited.remove(currentWebPage);
                continue;
            }

            visited.add(currentWebPage);
            children.put(currentWebPage, findLinks(currentWebPage));
            toBeVisited.addAll(children.get(currentWebPage));
            totalExpanded++;
            toBeVisited.remove(currentWebPage);
            System.out.println("at detpth : "+currentDepth+" in total:  "+totalExpanded+"     "+currentWebPage);


        }
        System.out.println("gotov");
        HashSet<String>allWebPages=new HashSet<>();
        allWebPages.addAll(visited);
        allWebPages.addAll(toBeVisited);
    }


    private static HashSet<String> findLinks(String url) throws IOException {
        try{

        HashSet<String> links = new HashSet<>();
        HashSet<String> tempLinks = new HashSet<>();

        Document doc = Jsoup.connect(url)
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(3000)
                .get();

        Elements elements = doc.select("a[href]");
        for (Element element : elements) {
            links.add(element.attr("href"));
        }
        links.forEach(x->{
                        if (x.startsWith("/"))
                            {
                                tempLinks.add(simplify(getCore(url)+x));
                            }
                            else if(x.startsWith("http"))
                            {
                                tempLinks.add(simplify(x));
                            }
                         });

        return tempLinks;
        }
        catch (Throwable e)
        {
           return(new HashSet<>());
       }

    }

    private static String getCore(String url)
    {
        StringBuilder core= new StringBuilder();
        int slashCounter=0;
        for(int i=0;i<url.length();i++)
        {
            if(url.charAt(i)=='/')
            {
                slashCounter++;
                if(slashCounter==3)
                {
                    break;
                }
            }
            core.append(url.charAt(i));
        }
        return core.toString();
    }

    private static String simplify(String maybeLonger) {
        for(int i=maybeLonger.length()-1;i>=0;i--)
        {
            if(maybeLonger.charAt(i)=='/' ||maybeLonger.charAt(i)=='\\')
            {
                maybeLonger=maybeLonger.substring(0,i);
            }
            else
            {
                break;
            }
        }
        return maybeLonger;

    }
}