package com.skillmatch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SkillAiService {

    private final ChatClient chatClient;

    @Autowired
    public SkillAiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String analyzeSkills(String resume, String jobDescription) {
        String prompt = String.format("""
            You are a resume analysis assistant.
            Compare the following resume and job description and return JSON like:
            {
              "score": 0-100,
              "matchedSkills": [...],
              "missingSkills": [...]
            }

            Resume:
            %s

            Job Description:
            %s
            """, resume, jobDescription);
        log.info("Called analyzeSkills");

        return chatClient.call(prompt);
    }
}
