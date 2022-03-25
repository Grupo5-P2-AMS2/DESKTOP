module aAddressApp {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.base;
	requires javafx.base;
	requires controlsfx;
	requires java.xml;
	requires mongo.java.driver;
	requires json.simple;
	
	opens ch.makery.address to javafx.graphics, javafx.fxml;
	opens ch.makery.address.view to javafx.graphics, javafx.fxml;
}
