package com.tuner.tuner.controller;

import com.tuner.tuner.dto.AudioData;
import com.tuner.tuner.dto.PitchResponse;
import com.tuner.tuner.service.PitchDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PitchDetectionController {

    @Autowired
    private PitchDetectionService pitchDetectionService;

    @PostMapping("/analyze-audio")
    public PitchResponse analyzeAudio(@RequestBody AudioData audioData) {
        return pitchDetectionService.detectPitch(audioData.getAudioData());
    }
}
