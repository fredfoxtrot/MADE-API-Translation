package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class Translate {

	public static void main(String[] args) {
		System.out.println("Starting application");
		HashMap<String, String> mapping;
		
		mapping = getMapping(args[0]);
		
		updateFunction(mapping, args[1]);
	}
	
	public static void updateFunction(HashMap<String, String> mapping, String functionFileName) {
		
		Scanner sc; 

		try {
			sc = new Scanner(new File(functionFileName),"UTF-8");
			
			String newFile = functionFileName.split("\\.")[0]+"_Updated.txt";
			
			Writer printWriter = null;
			BufferedWriter out = null;
			
			printWriter = new OutputStreamWriter(new FileOutputStream(newFile), StandardCharsets.UTF_8);
			
		        //for each line from function, check if it's has .get or .put
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				
				//for each line from function, check if  .get AND .put
				if(line.contains(".get") && line.contains(".put")) {
					
					Set keySet; 
					keySet = mapping.keySet();
					int changeHappened = 0;
						
					//loop through each key, some may have 2 or more keys
					for(Object object : keySet) {
					    String element = (String) object;
					    
					    //mapping found, replace
					    if(line.contains(element)) {
					    	changeHappened++;
					    	line = line.replace(element, mapping.get(element));	
					    	System.out.println("Changing : "+element+" | "+ mapping.get(element));
					    	System.out.println(line);
					    }
					}
					
					if(changeHappened>=2)
						printWriter.append(line+"\n");
					else if(changeHappened==1)
						printWriter.append(line+"\t //WARN: only one value updated\n");
					else
						printWriter.append(line+"\t //ERR: NO MAPPING FOUND\n");
					
				}
				//for each line from function, check if it's has .get OR .put OR .update
				else if(line.contains(".get") || line.contains(".put") || line.contains(".update") || line.contains(".search")) {
					
					//substitute with mapping
					Set keySet; 
					keySet = mapping.keySet();
					boolean found = false;
						
					//loop through each key, some may have 2 or more keys
					for(Object object : keySet) {
					    String element = (String) object;
					    
					    //mapping found, replace
					    if(line.contains(element)) {
					    	found = true;
					    	line = line.replace(element, mapping.get(element));	
					    	System.out.println("Changing : "+element+" | "+ mapping.get(element));
					    }
					}
					
					if(found)
						printWriter.append(line+"\n");
					else
						printWriter.append(line+"\t //ERR: NO MAPPING FOUND\n");
					
				//if no .get OR .put or .update	
				}else {
					//write original value
					printWriter.append(line+"\n");
				}
				
				
			}
			
			 printWriter.close();
			 sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Application Completed!");
	}
	
	
	public static HashMap<String, String> getMapping (String file)	{
		Scanner sc;
		HashMap<String, String> mapping;
		try {
			sc = new Scanner(new File(file));
			
			sc.nextLine(); // flush out the first line 
			
			mapping = new HashMap<String, String>();
			
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				
				String[] mapsArr = new String[2];
				
				mapsArr = line.split("\\|");
			
			mapping.put(mapsArr[0], mapsArr[1]);
				
			}
			
			sc.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
			
		}
	
		return mapping;
	}
		

}
