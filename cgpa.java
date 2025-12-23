import java.util.Scanner;
import java.util.Locale;

class CgpACalculator {

    // Example mapping from letter grade to grade point (modify as needed)
    private static double letterToPoint(String letter) {
        letter = letter.trim().toUpperCase(Locale.ROOT);
        switch (letter) {
            case "A+": return 10.0;
            case "A":  return 9.0;
            case "B+": return 8.0;
            case "B":  return 7.0;
            case "C+": return 6.0;
            case "C":  return 5.0;
            case "D":  return 4.0;
            case "F":  return 0.0;
            default:   return Double.NaN; // invalid input
        }
    }

    // Example conversion from numeric marks (0-100) to grade point (modify cutoffs to match your rules)
    private static double marksToPoint(double marks) {
        if (marks >= 90) return 10.0;
        if (marks >= 80) return 9.0;
        if (marks >= 70) return 8.0;
        if (marks >= 60) return 7.0;
        if (marks >= 50) return 6.0;
        if (marks >= 40) return 5.0;
        return 0.0; // fail
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US);

        System.out.println("=== CGPA Calculator ===");
        System.out.print("How many subjects/entries do you want to enter? ");
        int n;
        try {
            n = Integer.parseInt(sc.nextLine().trim());
            if (n <= 0) {
                System.out.println("Number of subjects must be positive. Exiting.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid number. Exiting.");
            return;
        }

        double totalWeightedPoints = 0.0;
        double totalCredits = 0.0;

        System.out.println("For each subject you will enter:");
        System.out.println(" - credit (positive number)");
        System.out.println(" - then either: a numeric grade point (e.g. 9.0),");
        System.out.println("   or a letter grade (A+, A, B+, B, C+, C, D, F),");
        System.out.println("   or type 'marks' to enter numeric marks (0-100) which will be converted to grade point.");
        System.out.println();

        for (int i = 1; i <= n; i++) {
            System.out.println("Subject " + i + ":");
            double credits;
            try {
                System.out.print("  Credits: ");
                credits = Double.parseDouble(sc.nextLine().trim());
                if (credits <= 0) {
                    System.out.println("  Credits must be positive. Try again for this subject.");
                    i--;
                    continue;
                }
            } catch (Exception e) {
                System.out.println("  Invalid credit value. Try again for this subject.");
                i--;
                continue;
            }

            System.out.print("  Enter 'gp' for grade point, 'letter' for letter grade, or 'marks' for numeric marks: ");
            String mode = sc.nextLine().trim().toLowerCase(Locale.ROOT);

            double gradePoint = Double.NaN;

            if (mode.equals("gp")) {
                try {
                    System.out.print("  Grade point (e.g. 9.0): ");
                    gradePoint = Double.parseDouble(sc.nextLine().trim());
                    // Optional: validate gradePoint range
                    if (gradePoint < 0 || gradePoint > 10) {
                        System.out.println("  Grade point should be between 0 and 10. Try again for this subject.");
                        i--;
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("  Invalid grade point. Try again for this subject.");
                    i--;
                    continue;
                }
            } else if (mode.equals("letter")) {
                System.out.print("  Letter grade (A+, A, B+, B, C+, C, D, F): ");
                String letter = sc.nextLine().trim();
                gradePoint = letterToPoint(letter);
                if (Double.isNaN(gradePoint)) {
                    System.out.println("  Unknown letter grade. Try again for this subject.");
                    i--;
                    continue;
                }
            } else if (mode.equals("marks")) {
                try {
                    System.out.print("  Marks (0-100): ");
                    double marks = Double.parseDouble(sc.nextLine().trim());
                    if (marks < 0 || marks > 100) {
                        System.out.println("  Marks must be between 0 and 100. Try again for this subject.");
                        i--;
                        continue;
                    }
                    gradePoint = marksToPoint(marks);
                    System.out.printf("  Converted marks %.2f -> grade point %.2f%n", marks, gradePoint);
                } catch (Exception e) {
                    System.out.println("  Invalid marks. Try again for this subject.");
                    i--;
                    continue;
                }
            } else {
                System.out.println("  Unknown mode. Use 'gp', 'letter', or 'marks'. Try this subject again.");
                i--;
                continue;
            }

            // accumulate
            totalWeightedPoints += gradePoint * credits;
            totalCredits += credits;
            System.out.printf("  Recorded: credits=%.2f, gradePoint=%.2f%n", credits, gradePoint);
            System.out.println();
        }

        if (totalCredits == 0) {
            System.out.println("No credits entered. Cannot compute CGPA.");
            return;
        }

        double cgpa = totalWeightedPoints / totalCredits;

        // Print results
        System.out.println("=== Result ===");
        System.out.printf("Total weighted points: %.3f%n", totalWeightedPoints);
        System.out.printf("Total credits: %.3f%n", totalCredits);
        System.out.printf("CGPA: %.3f%n", cgpa);
        System.out.printf("CGPA (rounded to 2 decimals): %.2f%n", Math.round(cgpa * 100.0) / 100.0);
        sc.close();
    }
}
