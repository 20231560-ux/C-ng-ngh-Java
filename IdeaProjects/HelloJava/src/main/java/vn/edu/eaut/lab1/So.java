package vn.edu.eaut.lab1;

public class So {

    public static int tongChanDenN(int n) {
        if (n <= 0) throw new IllegalArgumentException("n phai > 0");
        int tong = 0;
        for (int i = 2; i <= n; i += 2) {
            tong += i;
        }
        return tong;
    }

    public static double tongNghichDao(int n) {
        if (n <= 0) throw new IllegalArgumentException("n phai > 0");
        double tong = 0;
        for (int i = 1; i <= n; i++) {
            tong += 1.0 / i;
        }
        return tong;
    }

    public static boolean laSoNguyenTo(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static String loaiTamGiac(double a, double b, double c) {

        if (a <= 0 || b <= 0 || c <= 0)
            return "Khong phai tam giac";

        if (a + b <= c || a + c <= b || b + c <= a)
            return "Khong phai tam giac";

        if (a == b && b == c)
            return "Tam giac deu";

        if (a == b || a == c || b == c)
            return "Tam giac can";

        if (a * a + b * b == c * c ||
                a * a + c * c == b * b ||
                b * b + c * c == a * a)
            return "Tam giac vuong";

        return "Tam giac thuong";
    }

    public static String dayFibonacci(int n) {
        if (n <= 0) throw new IllegalArgumentException("n phai > 0");

        StringBuilder sb = new StringBuilder();

        long a = 0;
        long b = 1;

        for (int i = 0; i < n; i++) {
            sb.append(a);

            if (i < n - 1)
                sb.append(" ");

            long t = a + b;
            a = b;
            b = t;
        }

        return sb.toString();
    }
}