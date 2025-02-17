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
    public static AtomicInteger actualNumberOfGradedStudents = new AtomicInteger(0); // potrebno je da se inkrementuje kada se oceni student(svejedno da li kod profesora ili asistenta)
    private final Object lock;

    public Student(int studentId, long studentArrivalTime, CyclicBarrier barrier, long startTimeForProfAndAssis, Semaphore semaphore, Object lock) {
        this.studentId = studentId;
        this.studentArrivalTimeMill = studentArrivalTime;
        this.barrier = barrier;
        this.startTimeForProfAndAssis = startTimeForProfAndAssis;
        this.semaphore = semaphore;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            while (System.currentTimeMillis() < studentArrivalTimeMill) {
                //System.out.println("Cekamo studenta.");

            }
            Random profOrAssis = new Random();
            int profOrAssisFlag = profOrAssis.nextInt(2);
            if(profOrAssisFlag == 0) {
                professorHomeworkChecking();
            }
            else{
                assistantHomeworkChecking();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    // proveriti vremena
    private void professorHomeworkChecking(){
        try {
            long checkingStartTime = System.currentTimeMillis();
         //   System.out.println("Student" + studentId +" waiting for professor");
            barrier.await(5000, TimeUnit.MILLISECONDS);
         //   System.out.println("Professor is ready. We are starting our journey with student "+ studentId);
            semaphore.acquire();
            this.totalTimeForHomeworkChecking =  ThreadLocalRandom.current().nextLong(500, 1001);
            long timeLeftForChecking = 5000 - (checkingStartTime - startTimeForProfAndAssis);
            if(timeLeftForChecking < totalTimeForHomeworkChecking) {
                System.out.println("Student "+ this.studentId +" is late(5 seconds passed)...PROFA");
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

            long endTime = System.currentTimeMillis() - startTimeForProfAndAssis;// proveriti gde se koristi
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
           // System.out.println("Assistant is ready. We are starting our journey with student "+ studentId);
            long checkingStartTime = System.currentTimeMillis();
            this.totalTimeForHomeworkChecking =  ThreadLocalRandom.current().nextLong(500, 1001);
            long timeLeftForChecking = 5000 - (System.currentTimeMillis() - startTimeForProfAndAssis);
            if(timeLeftForChecking < totalTimeForHomeworkChecking) {
                System.out.println("Student "+ this.studentId +" is late...ASIST");
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

            long endTime = System.currentTimeMillis() - startTimeForProfAndAssis;// proveriti gde se koristi
            System.out.println("Thread: Student " + studentId + " Arrival: " + (studentArrivalTimeMill - startTimeForProfAndAssis) +
                    " ms Prof: assistant TTC: " + totalTimeForHomeworkChecking + " ms, Start: " + checkingStartTime +
                    " ms Score: " + studentHomeworkGrade);
        }
    }
}
