package com.awesome.solutions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoboTaxiPosition {
    Integer coordX;
    Integer coordY;
    String direction;
    Long timestamp;
}
