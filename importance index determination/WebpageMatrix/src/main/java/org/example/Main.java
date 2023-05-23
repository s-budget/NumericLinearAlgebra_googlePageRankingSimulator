package org.example;
import java.io.*;
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
    static String beginPage="https://solo-leveling.fandom.com";
    public static void main(String args[]) throws IOException {

        int maxDepth = 3;
        int currentDepth = 0;
        int totalExpanded=0;

        HashSet<String> visited = new HashSet<>();
        LinkedHashSet<String> toBeVisited = new LinkedHashSet<>();
        HashMap<String,HashSet<String>> children=new HashMap<>();
        HashMap<String,HashSet<String>> parents=new HashMap<>();

        toBeVisited.add(simplify(beginPage));
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
            for(String child : children.get(currentWebPage))
            {
                parents.putIfAbsent(child,new HashSet<>());
                parents.get(child).add(currentWebPage);
            }
            toBeVisited.addAll(children.get(currentWebPage));
            totalExpanded++;
            toBeVisited.remove(currentWebPage);
            //System.out.println("at detpth : "+currentDepth+" in total:  "+totalExpanded+"     "+currentWebPage);


        }
        System.out.println("pronasao Stranice");
        while(!toBeVisited.isEmpty())
        {
            Optional<String> currentWebPageFound = toBeVisited.stream().findFirst();
            String currentWebPage= simplify(currentWebPageFound.get());
            if(currentWebPage.equals("ROWBREAK"))
            {
                break;
            }
            if(visited.contains(currentWebPage))
            {
                toBeVisited.remove(currentWebPage);
                continue;
            }

            visited.add(currentWebPage);
            HashSet<String>alreadyFoundChildren= findLinks(currentWebPage);
            alreadyFoundChildren.removeIf(x->!visited.contains(x)&&!toBeVisited.contains(x));
            children.put(currentWebPage, alreadyFoundChildren);
            for(String child : children.get(currentWebPage))
            {
                parents.putIfAbsent(child,new HashSet<>());
                parents.get(child).add(currentWebPage);
            }
            totalExpanded++;
            toBeVisited.remove(currentWebPage);
            //System.out.println("at detpth : "+currentDepth+" in total:  "+totalExpanded+"     "+currentWebPage);


        }
        System.out.println("zagaldio dangling nodes");
        SortedSet<String>allWebPages=new TreeSet<>();
        allWebPages.addAll(visited);
        allWebPages.addAll(toBeVisited);
        hashMapeSpodacima podaci=new hashMapeSpodacima(children,parents);
        podaci.saveState();
        System.out.println("Spremio odnose");
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
                                String s=simplify(getCore(url)+x);
                                if(s!=null)
                                    tempLinks.add(s);
                            }
                            else if(x.startsWith("http"))
                            {
                                String s=simplify(x);
                                if(s!=null)
                                  tempLinks.add(s);
                            }
                         });
        tempLinks.removeIf(x->x.equals(url));
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
        maybeLonger=maybeLonger.split(" ")[0];
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
        if (maybeLonger.contains("Special:UserRights/"))
        {
            maybeLonger=maybeLonger.split("Special:UserRights")[0]+"Special:UserRights";
        }
        if(maybeLonger.contains("/r/"))
        {
            maybeLonger=maybeLonger.split("/r/")[0];
        }
       if(maybeLonger.contains("/Message_Wall:"))
       {
           maybeLonger=maybeLonger.replace("/Message_Wall:","/User:");
       }
        if(maybeLonger.contains("Special:Contributions/"))
        {
            maybeLonger=maybeLonger.replace("Special:Contributions/","User:");
        }

        if((maybeLonger.contains("solo-leveling.fandom")|| maybeLonger.equals("ROWBREAK")) && !maybeLonger.contains("Special:Log/")&& !maybeLonger.contains("?")&& !maybeLonger.contains("User_blog:")&& !maybeLonger.contains("@comment")&& !maybeLonger.contains("File:")&& !maybeLonger.contains("Special:Search") && !maybeLonger.contains("Special:UserProfileActivity") )
            return maybeLonger;
        return null;
    }
}

