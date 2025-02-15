package checkhomework;


import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class Student implements Runnable {
    private final int studentId;
    long studentArrivalTimeMill; // u milisekundama je izrazen
    long totalTimeForHomeworkChecking;
    int studentHomeworkGrade;
    CyclicBarrier barrier;


    public Student(int studentId, long studentArrivalTime, CyclicBarrier barrier) {
        this.studentId = studentId;
        this.studentArrivalTimeMill = studentArrivalTime;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            System.out.println("Student " + studentId + " started at " + studentArrivalTimeMill);
            System.out.println("Student" + studentId +" waiting for professor");
            barrier.await(); // ceka se dva studenta
            System.out.println("We are starting our journey with student "+ studentId);
            professorHomeworkChecking();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void professorHomeworkChecking(){
        this.totalTimeForHomeworkChecking =  ThreadLocalRandom.current().nextLong(500, 1001);
        this.studentHomeworkGrade =  ThreadLocalRandom.current().nextInt(5, 11);
        System.out.println("Student " + studentId + " professor grade " + studentHomeworkGrade);

    }

    private void assistantHomeworkChecking(){

    }
}
