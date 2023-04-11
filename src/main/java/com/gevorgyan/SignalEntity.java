package com.gevorgyan;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SignalEntity {
    private String media;
    private String clientNo;
    private String description;
    private int moduleNo;
    private String notes;
    private String rawData;
    private int typeNo;
    private int userSpare;
    private int zoneUser;
    private double longitude;
    private double latitude;
    private String callerID;
}
