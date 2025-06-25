package com.skillmatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.SkillMatchingResultDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
public class ResumeService {

   // @Autowired
   // OpenAiService openAiService;
    @Autowired
    private SkillAiService skillAiService;

    private String resumeText = "";
    private String jobDescription = "";


    // Extract plain text from resume using Apache Tika
    public String extractTextFromResume(MultipartFile file) {
        try {
            Tika tika = new Tika();
            this.resumeText = tika.parseToString(file.getInputStream());
            return resumeText;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse resume file", e);
        }
    }

    public void saveJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

//    public SkillMatchingResultDto compareResumeWithJob() {
//        String prompt = String.format("""
//                You are a skill-matching assistant.
//                Given the following resume and job description:
//
//                Resume:
//                %s
//
//                Job Description:
//                %s
//
//                Compare the resume and job description. Return a JSON like:
//                {
//                  "score": 0-100,
//                  "matchedSkills": ["Java", "Spring"],
//                  "missingSkills": ["AWS", "Docker"]
//                }
//                """, resumeText, jobDescription);
//
//        String aiResponse = openAiService.askOpenAi(prompt);
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(aiResponse, SkillMatchingResultDto.class);
//        } catch (Exception e) {
//            // fallback to keyword-based in case of failure
//            return fallbackKeywordMatching();
//        }
//    }
//
    public SkillMatchingResultDto fallbackKeywordMatching() {

        log.info("Called fallbackKeywordMatching");
        List<String> skills = List.of("Java", "Spring", "REST", "React", "SQL", "Docker", "AWS");

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String skill : skills) {
            if (resumeText.toLowerCase().contains(skill.toLowerCase())) {
                matched.add(skill);
            } else if (jobDescription.toLowerCase().contains(skill.toLowerCase())) {
                missing.add(skill);
            }
        }

        int score = (int) (((double) matched.size() / (matched.size() + missing.size())) * 100);
        return new SkillMatchingResultDto(score, matched, missing);
    }


    public SkillMatchingResultDto compareResumeWithJob(MultipartFile resumeFile, String jobDescription) {
        try {
            log.info("Called compareResumeWithJob");
            String resumeText = extractTextFromResume(resumeFile);
            String aiResponse = skillAiService.analyzeSkills(resumeText, jobDescription);
            return new ObjectMapper().readValue(aiResponse, SkillMatchingResultDto.class);
        } catch (Exception e) {
            return fallbackKeywordMatching();
        }
    }




}
