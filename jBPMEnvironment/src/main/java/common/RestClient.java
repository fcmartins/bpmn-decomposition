package common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;

public class RestClient {
	
	public static HttpResponse post(String url, String postdata, String[] headers)
			throws IOException, URISyntaxException,
			JSONException {
		HttpClient client = HttpClientBuilder.create().build();

		HttpPost request = new HttpPost(url);
		StringEntity entity = new StringEntity(postdata, StandardCharsets.UTF_8);
		request.setEntity(entity);
		if (headers != null && headers.length > 0) request.setHeaders(transform(headers));
		request.addHeader("Content-type", "application/json");
		
		System.out.println("POST request:");
		System.out.println("URL: " + url);
		System.out.print("postdata: ");
		InputStreamUtils.print(request.getEntity().getContent());

		HttpResponse response = client.execute(request);
		StatusLine statusLine = response.getStatusLine();
		
		System.out.println(statusLine);
		
		if (statusLine.getStatusCode() / 100 != 2)
			throw new IOException("REST request failed with code " + statusLine.getStatusCode());

		InputStreamUtils.print(response.getEntity().getContent());
		
		return response;
	}

	private static Header[] transform(String[] headers) {
		if (headers == null || headers.length == 0) return new Header[0];
		Header[] transformed = new Header[headers.length];
		int i = 0;
		for (String header : headers) {
			if (!header.contains(":")) continue;
			String name = header.substring(0, header.indexOf(":")).trim();
			String value = header.substring(header.indexOf(":") + 1).trim();
			transformed[i] = new BasicHeader(name, value);
			i++;
		}

		return transformed;
	}
}
