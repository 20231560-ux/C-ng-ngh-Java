package lab2.lab2;

public class GradeCalculator {

    public static double calculateFinalScore(Student student) {
        return student.getAttendanceScore() * 0.1
                + student.getMidtermScore() * 0.3
                + student.getFinalScore() * 0.6;
    }

    public static String classify(double score) {
        if (score >= 8.5)
            return "A";
        else if (score >= 7)
            return "B";
        else if (score >= 5.5)
            return "C";
        else if (score >= 4)
            return "D";
        else
            return "F";
    }

    public static void validateScore(double score, String field) {
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException(field + " phải nằm trong khoảng 0 đến 10");
        }
    }
}	