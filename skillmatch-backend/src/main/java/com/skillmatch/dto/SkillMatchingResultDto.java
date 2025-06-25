package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SkillMatchingResultDto {

    private int score;
    private List<String> matchedSkills;
    private List<String> missingSkills;
}
