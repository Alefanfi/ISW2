package deliverable;

import java.io.IOException;
import org.json.JSONException;

public class Main {
	
	private static final String projName = "AVRO";
	private static final String projName2 = "BOOKKEEPER";
	
	public static void main(String[] args) throws JSONException, IOException {
	
		GetReleaseInfo.getRelease(projName);
		GetReleaseInfo.getRelease(projName2);
	
	}

}
