package com.restapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApplicationUtil {

	public static String printException(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter, true);
		throwable.printStackTrace(printWriter);
		printWriter.flush();
		stringWriter.flush();
		return stringWriter.toString();
	}

	public static String getAccessToken(String tokenCredentialsPayload) {

		try {
			URL url = new URL(ApplicationConstants.ACCESS_TOKEN_API);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			PrintWriter out = new PrintWriter(connection.getOutputStream());
			out.print(tokenCredentialsPayload);
			out.close();

			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				StringBuilder response = new StringBuilder();

				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					response.append(new String(buffer, 0, bytesRead));
				}

				inputStream.close();
				connection.disconnect();

				JsonObject responseObject = new JsonParser().parse(response.toString()).getAsJsonObject();
				return responseObject.get("access_token").getAsString();
			} else {
				System.out.println("Failed to get access token. Unexpected response.");
			}
		} catch (Exception e) {
			System.err.println(printException(e));
		}

		return "";
	}

	public static void getPDFByInvoiceId(String accessToken, String invoiceId) {

		try {
			URL url = new URL(ApplicationConstants.INVOICE_GENERATE_API + invoiceId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				File file = new File("./RestAPI");

				FileOutputStream outputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				outputStream.close();

				inputStream.close();
				connection.disconnect();

				System.out.println("PDF file downloaded successfully.");
			} else {
				System.out.println("Failed to download the PDF file. Unexpected response.");
			}
		} catch (Exception e) {
			System.err.println(printException(e));
		}
	}
}
