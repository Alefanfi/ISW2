package deliverable;

import java.io.IOException;
import org.json.JSONException;

public class Main {
	
	private static final String project = "AVRO";
	private static final String project2 = "BOOKKEEPER";
	
	public static void main(String[] args) throws JSONException, IOException {
	
		GetReleaseInfo.getRelease(project);
		GetReleaseInfo.getRelease(project2);
	
	}

}
