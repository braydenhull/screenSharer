import java.util.*;
import java.net.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.lang.Object.*;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.*;
import org.json.simple.parser.*;

class uploadImage {

	private static final String IMGUR_KEY = "4dc09c6d26de05b6102feea6178f74bd";
	private static final String IMGUR_POST_URI = "https://api.imgur.com/3/upload";
	private static final String IMGUR_CLIENT_ID = "91d979dc414be74";

	public static String uploadImage(String file, boolean link) {
		
		try {

			//System.out.println("Reading image...");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ImageIO.write(ImageIO.read(new File(file)), "png", baos);

			URL url = new URL(IMGUR_POST_URI);
			String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(Base64.encodeBase64String(baos.toByteArray()).toString(), "UTF-8");
	        data += "&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(IMGUR_KEY, "UTF-8");

	        URLConnection conn = url.openConnection();
	        conn.setDoOutput(true);
	        conn.addRequestProperty("Authorization", "Client-ID "+IMGUR_CLIENT_ID);

	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        //System.out.println("Sending data...");
	        wr.write(data);
	        wr.flush();

	        String reply = "";
	        StringBuffer sb = new StringBuffer();
	        InputStream inStream = conn.getInputStream();
	        Scanner input = new Scanner(inStream);
	        while (input.hasNextLine()) {
	        	String nextLine = input.nextLine();
	        	sb.append(nextLine);
	        	// System.out.println(nextLine);
	        }
	        reply = sb.toString();
	        JSONParser parser = new JSONParser();
			KeyFinder finder = new KeyFinder();

			finder.setMatchKey("id");

			String id = "";

			try {
				while(!finder.isEnd()) {
					parser.parse(reply, finder, true);
					if(finder.isFound()){
						finder.setFound(false);
						//System.out.println("found id:");
						//System.out.println(finder.getValue());
						id = (String) finder.getValue();
					}
				}

				if (link) {
					// print link
					return "http://imgur.com/"+id;
				}
				else {
					return id;
				}

			}
			catch(ParseException pe){
				//pe.printStackTrace();
			}
	    }
	    catch (Exception e) {
	    	return "-1";
	    	//e.printStackTrace();
	    }
	    return "-1";
	}

	public uploadImage() {

	}

}