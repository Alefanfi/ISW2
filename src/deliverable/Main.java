package deliverable;

import java.io.IOException;
import org.json.JSONException;

public class Main {
	
	private static final String PROJECT = "AVRO";
	private static final String project = "BOOKKEEPER";
	
	public static void main(String[] args) throws JSONException, IOException {
	
		GetReleaseInfo.getRelease(PROJECT);
		GetReleaseInfo.getRelease(project);
	
	}

}
