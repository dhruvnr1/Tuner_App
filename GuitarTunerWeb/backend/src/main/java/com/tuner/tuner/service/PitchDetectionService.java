package com.tuner.tuner.service;

import com.tuner.tuner.dto.PitchResponse;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.stereotype.Service;

@Service
public class PitchDetectionService {

    private static final int SAMPLE_RATE = 48000;
    private static final String[] NOTE_NAMES = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};

    public PitchResponse detectPitch(double[] audioData) {
        if (audioData == null || audioData.length == 0) {
            return new PitchResponse(0, "", 0);
        }

        // Normalize the byte data (0-255) to float data (-1.0 to 1.0)
        double[] normalizedData = new double[audioData.length];
        for (int i = 0; i < audioData.length; i++) {
            normalizedData[i] = (audioData[i] / 128.0) - 1.0;
        }

        double[] windowedData = applyHannWindow(normalizedData);

        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fft = transformer.transform(windowedData, TransformType.FORWARD);

        double fundamentalFrequency = findFundamentalFrequency(fft, audioData.length);

        return findClosestNote(fundamentalFrequency);
    }

    private double[] applyHannWindow(double[] data) {
        double[] windowed = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            windowed[i] = data[i] * 0.5 * (1 - Math.cos(2 * Math.PI * i / (data.length - 1)));
        }
        return windowed;
    }

    private double findFundamentalFrequency(Complex[] fft, int fftSize) {
        int magnitudeLength = fft.length / 2;
        double[] magnitude = new double[magnitudeLength];
        for (int i = 0; i < magnitudeLength; i++) {
            magnitude[i] = fft[i].abs();
        }

        // Find the peak in the magnitude spectrum
        int maxIndex = 0;
        double maxMag = 0.0;
        for (int i = 1; i < magnitudeLength; i++) { // Start from 1 to ignore DC component
            if (magnitude[i] > maxMag) {
                maxMag = magnitude[i];
                maxIndex = i;
            }
        }

        return (double) maxIndex * SAMPLE_RATE / fftSize;
    }

    private PitchResponse findClosestNote(double frequency) {
        if (frequency == 0) {
            return new PitchResponse(0, "", 0);
        }

        double a4 = 440.0;
        double c0 = a4 * Math.pow(2, -4.75);
        double h = Math.round(12 * Math.log(frequency / c0) / Math.log(2));
        double closestFreq = c0 * Math.pow(2, h / 12.0);
        int noteIndex = (int) (h % 12);
        if (noteIndex < 0) {
            noteIndex += 12;
        }
        String note = NOTE_NAMES[noteIndex];
        double deviation = 1200 * Math.log(frequency / closestFreq) / Math.log(2);

        return new PitchResponse(frequency, note, deviation);
    }
}
