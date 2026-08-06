package lab2.lab2;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== LAB 2 JAVA MAVEN =====");

        System.out.print("Nhap ma sinh vien: ");
        String id = sc.nextLine();

        System.out.print("Nhap ho ten: ");
        String name = sc.nextLine();

        double attendance = inputScore(sc, "diem chuyen can");
        double midterm = inputScore(sc, "diem giua ky");
        double fin = inputScore(sc, "diem cuoi ky");

        Student student = new Student(id, name, attendance, midterm, fin);

        double total = GradeCalculator.calculateFinalScore(student);

        String grade = GradeCalculator.classify(total);

        System.out.println("\n===== KET QUA =====");
        System.out.println("Ma SV : " + student.getStudentId());
        System.out.println("Ho ten: " + student.getFullName());
        System.out.printf("Diem tong ket: %.2f\n", total);
        System.out.println("Xep loai: " + grade);

        sc.close();
    }

    private static double inputScore(Scanner sc, String label) {

        while (true) {

            try {

                System.out.print("Nhap " + label + ": ");

                double score = Double.parseDouble(sc.nextLine());

                GradeCalculator.validateScore(score, label);

                return score;

            } catch (Exception e) {

                System.out.println("Loi: " + e.getMessage());

            }

        }

    }

}