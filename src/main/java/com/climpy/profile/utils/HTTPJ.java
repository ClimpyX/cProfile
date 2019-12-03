package com.climpy.profile.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;

public class HTTPJ
{
  public static String getUUID(String playerName) {
    try {
      URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
      BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder entirePage = new StringBuilder();
      String inputLine;
      while ((inputLine = stream.readLine()) != null)
        entirePage.append(inputLine); 
      stream.close();
      if (!entirePage.toString().contains("\"id\":\""))
        return null; 
      return entirePage.toString().split("\"id\":\"")[1].split("\",")[0];
    } catch (IOException e) {
      e.printStackTrace();
      
      return null;
    } 
  }
  public static String getCountry(InetSocketAddress ip) {
    try {
      URL url = new URL("http://ip-api.com/json/" + ip.getHostName());
      BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder entirePage = new StringBuilder();
      String inputLine;
      while ((inputLine = stream.readLine()) != null)
        entirePage.append(inputLine); 
      stream.close();
      if (!entirePage.toString().contains("\"country\":\""))
        return null; 
      return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
    } catch (IOException e) {
      e.printStackTrace();
      
      return null;
    } 
  }
  public static String getCity(InetSocketAddress ip) {
    try {
      URL url = new URL("http://ip-api.com/json/" + ip.getHostName());
      BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder entirePage = new StringBuilder();
      String inputLine;
      while ((inputLine = stream.readLine()) != null)
        entirePage.append(inputLine); 
      stream.close();
      if (!entirePage.toString().contains("\"city\":\""))
        return null; 
      return entirePage.toString().split("\"city\":\"")[1].split("\",")[0];
    } catch (IOException e) {
      e.printStackTrace();
      
      return null;
    } 
  }
  public static String getISP(InetSocketAddress ip) {
    try {
      URL url = new URL("http://ip-api.com/json/" + ip.getHostName());
      BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder entirePage = new StringBuilder();
      String inputLine;
      while ((inputLine = stream.readLine()) != null)
        entirePage.append(inputLine); 
      stream.close();
      if (!entirePage.toString().contains("\"isp\":\""))
        return null; 
      return entirePage.toString().split("\"isp\":\"")[1].split("\",")[0];
    } catch (IOException e) {
      e.printStackTrace();
      
      return null;
    } 
  }
  public static String getTimezone(InetSocketAddress ip) {
    try {
      URL url = new URL("http://ip-api.com/json/" + ip.getHostName());
      BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder entirePage = new StringBuilder();
      String inputLine;
      while ((inputLine = stream.readLine()) != null)
        entirePage.append(inputLine); 
      stream.close();
      if (!entirePage.toString().contains("\"timezone\":\""))
        return null; 
      return entirePage.toString().split("\"timezone\":\"")[1].split("\",")[0];
    } catch (IOException e) {
      e.printStackTrace();
      
      return null;
    } 
  }
}
