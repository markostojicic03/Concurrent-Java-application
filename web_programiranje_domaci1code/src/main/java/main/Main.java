package main;

import checkhomework.Student;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        int n = 4; // broj studenata; moze da se promeni da se ucitava sa konzole
        ExecutorService executorService = Executors.newCachedThreadPool();
        int totalStudentsForProfessor = 2;
        CyclicBarrier barrier = new CyclicBarrier(totalStudentsForProfessor);

        for (int i = 0; i < n; i++) {
            Random random = new Random();
            executorService.execute(new Student(i, random.nextInt(1000), barrier));
        }
        executorService.shutdown();
    }
}
