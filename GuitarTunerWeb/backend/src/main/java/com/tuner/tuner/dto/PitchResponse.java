package com.tuner.tuner.dto;

public class PitchResponse {
    private double frequency;
    private String note;
    private double deviation;

    public PitchResponse(double frequency, String note, double deviation) {
        this.frequency = frequency;
        this.note = note;
        this.deviation = deviation;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getDeviation() {
        return deviation;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }
}
