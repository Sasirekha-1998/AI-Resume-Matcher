package com.skillmatch.controller;

import com.skillmatch.service.ResumeService;
import dto.JobDescriptionDto;
import dto.SkillMatchingResultDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("/hello")
    public ResponseEntity healthCheck()
    {
        return ResponseEntity.ok("Heloo");
    }

    @Operation(summary = "Upload resume file and extract text")
    @PostMapping(value = "/resume/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) {
        String extractedText = resumeService.extractTextFromResume(file);
        return ResponseEntity.ok(extractedText);
    }

    @PostMapping("/job/description")
    public ResponseEntity<String> uploadJobDescription(@RequestBody JobDescriptionDto dto) {
        resumeService.saveJobDescription(dto.getDescription());
        return ResponseEntity.ok("Job description saved successfully.");
    }

//    @GetMapping("/analysis")
//    public ResponseEntity<SkillMatchingResultDto> analyzeResume() {
//        SkillMatchingResultDto result = resumeService.compareResumeWithJob();
//        return ResponseEntity.ok(result);
//    }

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SkillMatchingResultDto> analyzeResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String jobDescription
    ) {
        SkillMatchingResultDto result = resumeService.compareResumeWithJob(file, jobDescription);
        return ResponseEntity.ok(result);
    }
}
