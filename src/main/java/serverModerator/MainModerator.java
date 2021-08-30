package serverModerator;


public class MainModerator {
	public static void main(String[] argv) throws Exception {
		new ApplicationServerModerator(8081, new WelcomeServletModerator()).start();
}

}

