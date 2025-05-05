package web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionDto {
    private String title;
    private String weight;
    private String calories;
    private String proteins;
    private String carbs;
    private String fats;
    private String message;

    public static DescriptionDto parsePhotoAnalysis(String rawText) {
        String[] lines = rawText.split("\n");

        if (lines.length < 6) {
            return failureResult(rawText);
        }

        try {
            String title = extractField(lines[0], "Блюдо на фото: ");
            String weight = extractField(lines[1], "Примерный вес: ");
            String calories = extractField(lines[2], "Количество калорий: ");
            String proteins = extractField(lines[3], "Количество белков: ");
            String carbs = extractField(lines[4], "Количество углеводов: ");
            String fats = extractField(lines[5], "Количество жиров: ");

            if (title.isEmpty() || weight.isEmpty() ||
                    calories.isEmpty() || proteins.isEmpty() ||
                    carbs.isEmpty() || fats.isEmpty()) {
                return failureResult(rawText);
            }

            return DescriptionDto.builder()
                    .title(title)
                    .weight(weight)
                    .calories(calories)
                    .proteins(proteins)
                    .carbs(carbs)
                    .fats(fats)
                    .message(rawText)
                    .build();
        } catch (Exception e) {
            return failureResult(rawText);
        }
    }

    private static String extractField(String line, String prefix) {
        return line.replace(prefix, "").trim();
    }

    private static DescriptionDto failureResult(String message) {
        return DescriptionDto.builder()
                .message(message)
                .build();
    }

    @Override
    public String toString() {
        if (title == null || weight == null || calories == null ||
                proteins == null || carbs == null || fats == null) {
            return message;
        }

        return "Блюдо на фото: " + title + "\n" +
                "Примерный вес: " + weight + "\n" +
                "Количество калорий: " + calories + "\n" +
                "Количество белков: " + proteins + "\n" +
                "Количество углеводов: " + carbs + "\n" +
                "Количество жиров: " + fats;
    }
}
