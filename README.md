# coding-puzzle
Brackets coding puzzle
======================

Given a string comprising just the characters (,),{,},[,] determine if
it is well-formed or not by applying the following rules:
* **Each type of bracket needs to be first opened then closed**
  * Good: () or [] or {}
  * Bad: (() or {}}
* **You can only close the last bracket that was opened**
  * Good: ({})
  * Bad: ({)}
* **Inside parenthesis () only braces {} are allowed**
  * Good: ({})
  * Bad: ([]) or (())
* **Inside braces {} only square brackets [] are allowed**
  * Good: {[]}
  * Bad: {()} or {{}}
* **Any bracket can appear (directly) inside square brackets []**
  * Good: [()] or [{}] or [[]] or [[[]]]
  * Bad: [([])]
* **You can use a list of braces where a single one is allowed of that type**
  * Good: \[()()] or {\[][()()]} or ()()
* **An empty string is not valid a expression**
* **Any other characters than (){}[] will invalidate the string**

For a given string print out ``True`` if the string is well-formed or
``False`` if otherwise.

Part I.)
--------

Write a program that can read from stdin. Process all lines and print
out the result to stdout. 

Part II.)
--------- 

Enhance the program from "Part I" so it uses a multi-threaded
approach. Dispatch the actual strings to test to as many threads as
the machine has cores. Because the order is not necessarily the same
as the input provide the output with the referring index of the nth
input like this (no spaces):

```
0:True 
1:False
```

Usage for Part1
===============

```
$> java -jar coding-puzzle-part1.jar \[-n <numberOfLinesToBeRead>] [-f <file>]
```

* If both the optional parameters are not given, the the program will run infinitely until it is terminated by Ctrl-C 
* If both the arguments are given and are valid, only the given number of lines (value of -n parameter) are processed from file 
* If only -n parameter is specified, then the given number of lines are processed from cmd-line (stdin) 
* If only -f parameter is specified, all the lines from the file are processed 

Usage for Part2
===============

```
$> java -jar coding-puzzle-part2.jar \[-n <numberOfLinesToBeRead>] [-f <file>]
```

* If both the optional parameters are not given, the the program will run infinitely until it is terminated by Ctrl-C 
* If both the arguments are given and are valid, only the given number of lines (value of -n parameter) are processed from file 
* If only -n parameter is specified, then the given number of lines are processed from cmd-line (stdin) 
* If only -f parameter is specified, all the lines from the file are processed 

Compilation and build
---------------------

Execute with JDK >= 1.7

```
mvn clean install
```