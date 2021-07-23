/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package rest;

import core.ZOSConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class TextRequestTest {

    private HttpClient httpClient;
    private JsonRequest getRequest;
    private JsonRequest putRequest;

    @Before
    public void init() {
        HttpGet httpGet = Mockito.mock(HttpGet.class);
        httpClient = Mockito.mock(HttpClient.class);
        ZOSConnection connection = new ZOSConnection("", "", "", "");

        getRequest = new JsonRequest(connection, httpGet);
        Whitebox.setInternalState(getRequest, "client", httpClient);

        HttpPut httpPut = Mockito.mock(HttpPut.class);
        putRequest = new JsonRequest(connection, httpPut, Optional.empty());
        Whitebox.setInternalState(putRequest, "client", httpClient);
    }

    @Test
    public void tstHttpGetThrowsException() throws IOException {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class)))
               .thenThrow(new IOException());

        assertThrows(IOException.class, getRequest::httpGet);
        Mockito.verify(httpClient, Mockito.times(1))
               .execute(any(HttpGet.class), any(ResponseHandler.class));
    }

}
