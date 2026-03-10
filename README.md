# Homework Defense Simulation

## Project Overview
This project is a concurrent Java application that simulates a homework defense process involving students, a professor, and a teaching assistant. The simulation uses multithreading to manage simultaneous arrivals and strict time constraints.

## Features
* Concurrent Execution: Utilizes a thread pool to simulate multiple students arriving and defending their assignments concurrently.
* Thread Synchronization: Implements a CyclicBarrier to ensure the professor only begins examining when exactly two students are ready. The assistant handles students individually.
* Time Constraints: The entire defense session is strictly limited to 5 seconds. Any ongoing defenses are automatically terminated when the time expires.
* Execution Logging: Outputs detailed logs tracking student arrival times, examination durations, assigned evaluators, and final scores.

## Technical Details
Developed using standard Java concurrency utilities. It demonstrates the use of thread pools, barriers, and thread interruption mechanisms without relying on single-thread executors.

---
Developed as a homework assignment for the Web Programming course.
