package com.ankamma.user.application.exception;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

	private ErrorDecoder delegate = new ErrorDecoder.Default();

	@Override
	public Exception decode(String methodKey, Response response) {
		HttpHeaders responseHeaders = new HttpHeaders();
		response.headers().entrySet().stream()
				.forEach(entry -> responseHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue())));

		HttpStatus statusCode = HttpStatus.valueOf(response.status());

		byte[] responseBody;
		String message;
		JSONObject jsonObject;
		try {
			responseBody = IOUtils.toByteArray(response.body().asInputStream());
			message = new String(responseBody);
			jsonObject = new JSONObject(message);
		} catch (IOException e) {
			throw new RuntimeException("Failed to process response body.", e);
		}

		if (response.status() >= 400 && response.status() <= 499) {
			return new UserDetailsException(jsonObject.get("message").toString(), statusCode.toString(), null);
		}

		if (response.status() >= 500 && response.status() <= 599) {
			return new UserDetailsInternalException(jsonObject.get("message").toString(), statusCode.toString(), null);
		}
		return delegate.decode(methodKey, response);
	}
}
