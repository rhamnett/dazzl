package org.hamnett.adm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


public class YamlParser {

	public static void main(String[] args) {

		String fullfilename =
				System.getProperty("user.home") //$NON-NLS-1$
				+ File.separator
				+ "lightwaverf-config.yml";

		File newConfiguration = new File(fullfilename);
		InputStream is = null;
		try {
			is = new FileInputStream(newConfiguration);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		Yaml yaml = new Yaml();

	    @SuppressWarnings("unchecked")
	    Map<String, ArrayList> yamlParsers = (Map<String, ArrayList>) yaml.load(is);
	    
		ArrayList rooms = (ArrayList) yamlParsers.get("room");
		
		for (int i = 0; i < rooms.size(); i++) {
			Map room = (Map<String, ArrayList>)rooms.get(i);
			ArrayList<String> devices = (ArrayList) room.get("device");
			
			System.out.println("Room: "+(i+1)+" name: "+room.get("name"));
			foo(room.get("name").toString());
			for(int d = 0; d < devices.size(); d++) {
				String device = devices.get(d);
				System.out.println((d+1) + " "+device);
			}			
		}
		
	}
	
	private static void foo(String s){
		System.out.println(s);
	}

}
