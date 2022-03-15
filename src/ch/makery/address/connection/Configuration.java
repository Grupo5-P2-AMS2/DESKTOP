package ch.makery.address.connection;

import ch.makery.address.connection.ReadXmlDomParser;

public class Configuration {
	private String DATABASE_URL;
	private static Configuration configuration = new Configuration();
	
	private Configuration() {
		 readConfigFile();
		
		//Show saved values
		System.out.println("DATABASE_URL: " + getDBURL());
	 }
	
	public static Configuration getConfiguration() {
		return configuration;
	}
	
	public void readConfigFile() {
		//New XmlReader instance for configuration file reading
		ReadXmlDomParser rxmldp = new ReadXmlDomParser();
	    rxmldp.readXML();
	    //Set values for further use
	    setDBURL(rxmldp.getDBURL());
		
	}

	public String getDBURL() {
		return DATABASE_URL;
	}

	public void setDBURL(String DATABASE_URL) {
		this.DATABASE_URL = DATABASE_URL;
	}

	 
	 
}