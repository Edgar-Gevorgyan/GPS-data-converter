package com.gevorgyan;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/signals")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class SignalController {

    private static final String KEEP_ALIVE_EVENT_CODE = "ALM-T";

    @Inject
    @RestClient
    FormattedSignalService formattedSignalService;

    @POST
    @Path("/logsignal")
    public Response logSignal(String rawData) {
        validateRawData(rawData);
        SignalEntity signal = processRawData(rawData);
        if(signal == null) {
           return Response.ok().build();
        }

        return formattedSignalService.logFormattedSignal(signal);
    }


    private static void validateRawData(String rawData) {
        if (!rawData.startsWith("*") || !rawData.endsWith("#") ) {
            throw new BadRequestException("Invalid data prefix and/or postfix");
        }

        final String[] fields = rawData.split(",");
        if(fields.length != 3 && fields.length != 6) {
            throw new BadRequestException("Invalid data prefix and/or postfix");
        }
    }

    private static SignalEntity processRawData(String rawData) {
        final String[] fields = rawData.substring(1, rawData.length() - 1).split(",");

        SignalEntity signal = new SignalEntity();
        // rawData
        signal.setRawData(rawData);
        // clientNo
        signal.setClientNo(fields[1] + "BASET1");
        // typeNo
        String eventCode = fields[2];
        if (KEEP_ALIVE_EVENT_CODE.equals(eventCode)) {
            return null;
        }
        int requiredCode = fromEventCodeToRequiredCode(eventCode);
        signal.setTypeNo(requiredCode);

        // check rawData format
        if (fields.length == 6) {
            // latitude
            double latitude = extractCoordinateValue(fields[4]);
            if (fields[4].endsWith("S")) {
                latitude *= -1;
            }
            signal.setLatitude(latitude);

            // longitude
            double longitude = extractCoordinateValue(fields[5]);
            if (fields[5].endsWith("W")) {
                longitude *= -1;
            }
            signal.setLongitude(longitude);
        }

        return signal;
    }

    private static int fromEventCodeToRequiredCode(String eventCode) {
        char lastChar = eventCode.charAt(eventCode.length() - 1);
        int offset = lastChar - '@';
        if (lastChar > 'T') {
            offset -= 1;
        }

        return 700 + offset;
    }

    private static double extractCoordinateValue(String geographicCoordinate){
        int lastCharIndex = geographicCoordinate.length() - 1;
        return Double.parseDouble(geographicCoordinate.substring(4, lastCharIndex));
    }
}