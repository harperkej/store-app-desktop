package thesis.buyproducts.constant;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Constants {

	private Constants() {
	}

	public static final String TITLE_MAIN_WINDOW = "Buy producs";

	public static double getWindowsHeight() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return screenSize.getHeight() - 120;
	}

	public static double getWindowsWidth() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return screenSize.getWidth() - 70;
	}

}
