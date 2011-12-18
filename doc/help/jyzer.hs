<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<? TestTarget - this is data for the test target ?>

<helpset version="1.0">

  <!-- title -->
  
  <title>JYzer - A Minimal Java disassembler</title>

  <!-- maps -->
  
  <maps>
     <homeID>about.intro</homeID>
     <mapref location="jyzer.jhm"/>
  </maps>

  <!-- views -->
  
  <view>
    <name>TOC</name>
    <label>Help contents</label>
    <type>javax.help.TOCView</type>
    <data>toc.xml</data>
  </view>

  <view>
    <name>Index</name>
    <label>Index</label>
    <type>javax.help.IndexView</type>
    <data>index.xml</data>
  </view>

  <!--
  <view>
    <name>Search</name>
    <label>Search</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      SearchIndex
    </data>
  </view>
  -->
  
</helpset>
