package in.praveen.kt.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestSchedule {
    enum Frequency {
        MONTHLY, WEEKLY, BIWEEKLY
    }

    static class Payment {
        int paymentNumber;
        LocalDate paymentDate;
        double paymentAmount;
        double interestAmount;
        double principalAmount;
        double remainingBalance;

        Payment(int num, LocalDate date, double payment, double interest, double principal, double balance) {
            this.paymentNumber = num;
            this.paymentDate = date;
            this.paymentAmount = payment;
            this.interestAmount = interest;
            this.principalAmount = principal;
            this.remainingBalance = balance;
        }

        @Override
        public String toString() {
            return String.format("%3d  %s  %.2f  %.2f  %.2f  %.2f",
                    paymentNumber, paymentDate, paymentAmount, interestAmount, principalAmount, remainingBalance);
        }
    }

    private static double calculatePaymentAmount(double principal, double annualRate, int totalPayments, Frequency freq) {
        double periodsPerYear = 12;
        switch (freq) {
            case MONTHLY: periodsPerYear = 12; break;
            case BIWEEKLY: periodsPerYear = 26; break;
            case WEEKLY: periodsPerYear = 52; break;
        }
        double periodRate = annualRate / 100.0 / periodsPerYear;
        return (principal * periodRate) / (1 - Math.pow(1 + periodRate, -totalPayments));
    }

    public static List<Payment> generateSchedule(double principal, double annualRate, int totalPayments, LocalDate startDate, Frequency freq) {
        List<Payment> schedule = new ArrayList<>();
        double paymentAmount = calculatePaymentAmount(principal, annualRate, totalPayments, freq);
        double balance = principal;
        double periodRate = annualRate / 100.0 /
                (freq == Frequency.MONTHLY ? 12 : freq == Frequency.BIWEEKLY ? 26 : 52);

        for (int i = 1; i <= totalPayments; ++i) {
            double interest = balance * periodRate;
            double principalPaid = paymentAmount - interest;
            balance -= principalPaid;
            if (i == totalPayments) balance = 0; // last payment adjustment

            LocalDate paymentDate;
            switch (freq) {
                case MONTHLY: paymentDate = startDate.plusMonths(i - 1); break;
                case BIWEEKLY: paymentDate = startDate.plusWeeks((i - 1) * 2); break;
                case WEEKLY: paymentDate = startDate.plusWeeks(i - 1); break;
                default: paymentDate = startDate.plusMonths(i - 1);
            }

            schedule.add(new Payment(i, paymentDate,
                    paymentAmount, interest, principalPaid, Math.max(0, balance)));
        }
        return schedule;
    }

    public static void main(String[] args) {
        double principal = 10000;
        double annualRate = 8.5;
        int months = 12;
        LocalDate startDate = LocalDate.of(2025, 9, 1);

        System.out.println("Num  Date        Payment  Interest  Principal  Balance");
        List<Payment> schedule = generateSchedule(principal, annualRate, months, startDate, Frequency.MONTHLY);
        schedule.forEach(System.out::println);
    }
}
