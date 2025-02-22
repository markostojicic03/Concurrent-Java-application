package main;

import checkhomework.Student;

import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        int n = 10;
        ExecutorService executorServiceStudent = Executors.newCachedThreadPool();
        ExecutorService executorServiceProfessor = Executors.newFixedThreadPool(2);
        ExecutorService executorServiceAssistant = Executors.newSingleThreadExecutor();
        int totalStudentsForProfessor = 2;
        CyclicBarrier barrier = new CyclicBarrier(totalStudentsForProfessor);
        Semaphore semaphore = new Semaphore(totalStudentsForProfessor);
        final Object lock = "LOCK";

        try{
            for (int i = 0; i < n; i++) {
                long studentArrivalTimeMill = ThreadLocalRandom.current().nextInt(1, 1001);
                long startTimeForProf = System.currentTimeMillis();
                executorServiceStudent.execute(new Student(i, studentArrivalTimeMill+startTimeForProf, barrier, startTimeForProf, semaphore, lock, executorServiceProfessor, executorServiceAssistant));
            }
            try {
                Thread.sleep(5000); // Simulacija trajanja odbrana
            } catch (InterruptedException e) {
                executorServiceStudent.shutdownNow();
                executorServiceProfessor.shutdownNow();
                executorServiceAssistant.shutdownNow();
                //  e.printStackTrace();
            }
            System.out.println("Average grade: " + Student.sumOfAllGrades.get() * 1.0 / Student.actualNumberOfGradedStudents.get());
            executorServiceStudent.shutdown();
            executorServiceProfessor.shutdown();
            executorServiceAssistant.shutdown();
        }catch(Exception e){
            executorServiceStudent.shutdownNow();
            executorServiceAssistant.shutdownNow();
            executorServiceProfessor.shutdownNow();
            e.printStackTrace();
        }
    }
}
