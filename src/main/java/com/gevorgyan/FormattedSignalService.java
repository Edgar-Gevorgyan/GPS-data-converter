package com.gevorgyan;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/signals")
@RegisterRestClient(configKey="formatted-signal-api")
public interface FormattedSignalService {
    @POST
    @Path("/logformattedsignal")
    Response logFormattedSignal(SignalEntity signal);
}
