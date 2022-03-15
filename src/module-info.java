module aAddressApp {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.base;
	requires javafx.base;
	requires controlsfx;
	requires java.xml;
	requires mongo.java.driver;
	
	opens ch.makery.address to javafx.graphics, javafx.fxml;
	opens ch.makery.address.view to javafx.graphics, javafx.fxml;
}
