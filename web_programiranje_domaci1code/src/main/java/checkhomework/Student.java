package checkhomework;


import java.awt.desktop.SystemSleepEvent;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Student implements Runnable {
    private final int studentId;
    long studentArrivalTimeMill; // u milisekundama je izrazen
    long startTimeForProfAndAssis;
    long totalTimeForHomeworkChecking;
    int studentHomeworkGrade;
    CyclicBarrier barrier;
    private final Semaphore semaphore;
    public static AtomicInteger sumOfAllGrades = new AtomicInteger(0);
    public static AtomicInteger actualNumberOfGradedStudents = new AtomicInteger(0); // moram da inkrementujem kada ocenjujem studenta
    private final Object lock;
    private final ExecutorService executorServiceProfessor;
    private final ExecutorService executorServiceAssistant;

    public Student(int studentId, long studentArrivalTime, CyclicBarrier barrier, long startTimeForProfAndAssis, Semaphore semaphore, Object lock, ExecutorService executorServiceProfessor, ExecutorService executorServiceAssistant) {
        this.studentId = studentId;
        this.studentArrivalTimeMill = studentArrivalTime;
        this.barrier = barrier;
        this.startTimeForProfAndAssis = startTimeForProfAndAssis;
        this.semaphore = semaphore;
        this.lock = lock;
        this.executorServiceAssistant = executorServiceAssistant;
        this.executorServiceProfessor = executorServiceProfessor;
    }

    @Override
    public void run() {
        try {
            while (System.currentTimeMillis() < studentArrivalTimeMill) {
                //System.out.println("Cekamo studentaaaa/Waiting for student.....");
            }
            Random profOrAssis = new Random();
            int profOrAssisFlag = profOrAssis.nextInt(2);
            if(profOrAssisFlag == 0) {
                executorServiceProfessor.submit(this::professorHomeworkChecking);
            }
            else{
                executorServiceAssistant.submit(this::assistantHomeworkChecking);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    // proveriti vremena
    private void professorHomeworkChecking(){
        try {
           /* System.out.println("Student " + studentId + " is being checked by " +
                    (Thread.currentThread().getName()));*/
            long checkingStartTime = System.currentTimeMillis();
         //   System.out.println("Student" + studentId +" waiting for professor");
            barrier.await(5000, TimeUnit.MILLISECONDS);
         //   System.out.println("Professor is ready. We are starting our journey with student "+ studentId);
            semaphore.acquire();
            this.totalTimeForHomeworkChecking =  ThreadLocalRandom.current().nextLong(500, 1001);
            long timeLeftForChecking = 5000 - (checkingStartTime - startTimeForProfAndAssis);
            if(timeLeftForChecking < totalTimeForHomeworkChecking || timeLeftForChecking <= 0) {
                System.out.println("Thread: Student " + studentId + " Arrival: " + (studentArrivalTimeMill - startTimeForProfAndAssis) +
                        " ms Prof: professor TTC: " + totalTimeForHomeworkChecking +
                        " ms Score: x(time is up)");
                semaphore.release();
                return;
            }

            try {
                Thread.sleep(totalTimeForHomeworkChecking);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.studentHomeworkGrade =  ThreadLocalRandom.current().nextInt(5, 11);
            sumOfAllGrades.addAndGet(studentHomeworkGrade);
            actualNumberOfGradedStudents.incrementAndGet();

            System.out.println("Thread: Student " + studentId + " Arrival: " + (studentArrivalTimeMill - startTimeForProfAndAssis) +
                    " ms Prof: professor TTC: " + totalTimeForHomeworkChecking + " ms, Start: " + checkingStartTime +
                    " ms Score: " + studentHomeworkGrade);
            semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("Timeout: Professor waited too long. Student " + studentId + " is leaving.");
        }

    }

    private void assistantHomeworkChecking(){
       // System.out.println("Student" + studentId +" waiting for assistent");
        synchronized (this.lock){
            /*System.out.println("Student " + studentId + " is being checked by " +
                    (Thread.currentThread().getName()));*/
           // System.out.println("Assistant is ready. We are starting our journey with student "+ studentId);
            long checkingStartTime = System.currentTimeMillis();
            this.totalTimeForHomeworkChecking =  ThreadLocalRandom.current().nextLong(500, 1001);
            long timeLeftForChecking = 5000 - (System.currentTimeMillis() - startTimeForProfAndAssis);
            if(timeLeftForChecking < totalTimeForHomeworkChecking || timeLeftForChecking <= 0) {
                System.out.println("Thread: Student " + studentId + " Arrival: " + (studentArrivalTimeMill - startTimeForProfAndAssis) +
                        " ms Prof: assistant TTC: " + totalTimeForHomeworkChecking +
                        " ms Score: x(time is up)");
                return;
            }

            try {
                Thread.sleep(totalTimeForHomeworkChecking);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.studentHomeworkGrade =  ThreadLocalRandom.current().nextInt(5, 11);
            sumOfAllGrades.addAndGet(studentHomeworkGrade);
            actualNumberOfGradedStudents.incrementAndGet();

            System.out.println("Thread: Student " + studentId + " Arrival: " + (studentArrivalTimeMill - startTimeForProfAndAssis) +
                    " ms Prof: assistant TTC: " + totalTimeForHomeworkChecking + " ms, Start: " + checkingStartTime +
                    " ms Score: " + studentHomeworkGrade);
        }
    }
}
