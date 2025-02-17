package main;

import checkhomework.Student;

import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        int n = 20; // broj studenata; moze da se promeni da se ucitava sa konzole
        ExecutorService executorService = Executors.newCachedThreadPool();
        int totalStudentsForProfessor = 2;
        CyclicBarrier barrier = new CyclicBarrier(totalStudentsForProfessor);
        Semaphore semaphore = new Semaphore(totalStudentsForProfessor);
        final Object lock = "LOCK";
        for (int i = 0; i < n; i++) {
            long studentArrivalTimeMill = ThreadLocalRandom.current().nextInt(1, 1001);
            long startTimeForProf = System.currentTimeMillis();
            executorService.execute(new Student(i, studentArrivalTimeMill+startTimeForProf, barrier, startTimeForProf, semaphore, lock));
        }
        try {
            Thread.sleep(5000); // Simulacija trajanja odbrana PROVERITI DA LI JE OVO POTREBNO DA OSTANE U OVOM OBLIKU
        } catch (InterruptedException e) {
            executorService.shutdownNow();
          //  e.printStackTrace();
        }
        System.out.println("Average grade: " + Student.sumOfAllGrades.get() * 1.0 / Student.actualNumberOfGradedStudents.get());
        executorService.shutdown();
    }
}
