package com.tuner.tuner.service;

import com.tuner.tuner.dto.PitchResponse;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.stereotype.Service;

@Service
public class PitchDetectionService {

    private static final int SAMPLE_RATE = 44100;
    private static final String[] NOTE_NAMES = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};

    public PitchResponse detectPitch(double[] audioData) {
        if (audioData == null || audioData.length == 0) {
            return new PitchResponse(0, "", 0);
        }

        // 1. Apply Hann window
        double[] windowedData = applyHannWindow(audioData);

        // 2. Compute FFT
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fft = transformer.transform(windowedData, TransformType.FORWARD);

        // 3. Compute Harmonic Product Spectrum (HPS)
        double fundamentalFrequency = findFundamentalFrequency(fft);

        // 4. Find closest note and deviation
        return findClosestNote(fundamentalFrequency);
    }

    private double[] applyHannWindow(double[] data) {
        double[] windowed = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            windowed[i] = data[i] * 0.5 * (1 - Math.cos(2 * Math.PI * i / (data.length - 1)));
        }
        return windowed;
    }

    private double findFundamentalFrequency(Complex[] fft) {
        int maxIndex = 0;
        double maxMag = 0.0;

        double[] magnitude = new double[fft.length / 2];

        for (int i = 0; i < magnitude.length; i++) {
            magnitude[i] = fft[i].abs();
        }

        // HPS
        int harmonics = 5;
        double[] hps = new double[magnitude.length];
        System.arraycopy(magnitude, 0, hps, 0, magnitude.length);

        for (int h = 2; h <= harmonics; h++) {
            for (int i = 0; i < magnitude.length / h; i++) {
                hps[i] *= magnitude[i * h];
            }
        }

        for (int i = 0; i < hps.length; i++) {
            if (hps[i] > maxMag) {
                maxMag = hps[i];
                maxIndex = i;
            }
        }

        return (double) maxIndex * SAMPLE_RATE / (fft.length * 2);
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
        String note = NOTE_NAMES[noteIndex];

        double deviation = 1200 * Math.log(frequency / closestFreq) / Math.log(2);

        return new PitchResponse(frequency, note, deviation);
    }
}
