package com.restapi;

import com.google.gson.Gson;

public class ApplicationController {

	private static String tokenCredentialsPayload;

	private static String accessToken;

	static {
		AccessTokenCredentials accessTokenCredentials = new AccessTokenCredentials();
		accessTokenCredentials.setClient_id("0oatf5ukfkLIDZI6f357");
		accessTokenCredentials
				.setClient_secret("ArOVmSUy_LxFRzvRLIOmAW3qtUKPOfph4QCuBCDI");
		accessTokenCredentials.setAudience("https://navisphere.chrobinson.com");
		accessTokenCredentials.setGrant_type("client_credentials");
		tokenCredentialsPayload = new Gson().toJson(accessTokenCredentials);
		accessToken = ApplicationUtil.getAccessToken(tokenCredentialsPayload);
		System.out.println("AccessToken :: " + accessToken);
	}

	public static void main(String[] args) {

		ApplicationUtil.getPDFByInvoiceId(accessToken, "6000203291");
	}

}
