package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import bpmn2.TDefinitions;
import bpmn2.TProcess;

public class LoadXML {

	public LoadXML(){		
	}
	
	public TProcess XMLUnmarshal(String file){
		
		JAXBContext context;
		Unmarshaller u;
		JAXBElement parsed;
		TDefinitions def;
		TProcess d = null;
		try {
			context = JAXBContext.newInstance(TProcess.class);
			u = context.createUnmarshaller();		
			parsed = (JAXBElement) u.unmarshal(new FileInputStream(file));
			def = (TDefinitions) parsed.getValue();
			
			boolean done = false;
			int pos = 0;
			while(!done){
				try{
					d = (TProcess) def.getRootElement().get(pos).getValue();
					done = true;
				} catch (ClassCastException ce){
					//keep going
				}
				pos++;
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return d;
	}
	
}
