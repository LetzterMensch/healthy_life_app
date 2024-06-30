
package com.example.gr.model.data.sample;

public interface TemperatureSample extends TimeSample {
    /**
     * Returns the temperature value.
     */
    float getTemperature();
    /**
     * Returns the temperature type (the position on the body where the measurement was taken).
     */
    int getTemperatureType();
}
