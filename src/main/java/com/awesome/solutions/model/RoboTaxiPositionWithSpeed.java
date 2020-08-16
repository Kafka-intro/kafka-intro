package com.awesome.solutions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.checkerframework.checker.units.qual.A;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RoboTaxiPositionWithSpeed extends RoboTaxiPosition {
    Double speed;
}
