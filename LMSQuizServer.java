import java.net.ServerSocket;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * LMSQuizServer
 *
 * This is the server file for Project 5. It contains the methods to carry out various functionalities.
 * All reading and writing to files occurs here.
 *
 * @author ArchitVarunSahu, JakobWachter, SergioAlvarez
 * @version 1.0, (2022-04-22)
 */

public class LMSQuizServer implements Runnable, Serializable {
    //  Creating required readers and writers
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    Socket socket;

    public LMSQuizServer(Socket socket) { //  Incase problem occurs, delete this
        this.socket = socket;
    }

    public Teacher[] teachers; //  Declaring a class field of an array of Teacher objects
    public Student[] students; //  Declaring a class field of an array of Student objects

    //  Variables
    String [] loginList; //  Declaring a variable to store the list of users from a file
    boolean clientConnection = true; //  Declaring a variable to store whether client's connection status

    //  Synchronization
    private static final Object GATEKEEPER = new Object(); //  Declaring an object for Synchronization purposes

    public void run() {
        try {
//            ServerSocket serverSocket = new ServerSocket(4242); //  Incase problem occurs, uncomment these lines
//            socket = serverSocket.accept();
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            while (clientConnection) {
                RequestObj temp = (RequestObj) ois.readObject(); //  Capturing client's request

                if (temp.getCheckString().equalsIgnoreCase("exit")) {
                    clientConnection = false;
                    continue;
                }

                if (temp.getCheckString().equalsIgnoreCase("login")) { //  Case for user logging in
                    String userName = temp.getData()[0];
                    String password = temp.getData()[1];

                    boolean success = false;

                    if (Integer.parseInt(temp.getData()[2]) == 1) {
                        synchronized (GATEKEEPER) {
                            loginList = readFile("TeacherLogins.txt");
                            for (int i = 0; i < Objects.requireNonNull(loginList).length; i += 2) {
                                if (userName.equals(loginList[i]) && password.equals(loginList[i + 1])) {
                                    oos.writeObject(new ResponseObj(true));
                                    oos.flush();
                                    convertToObjectsArray(loginList, 1);
                                    success = true;
                                }
                            }
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            loginList = readFile("StudentLogins.txt");
                            for (int i = 0; i < Objects.requireNonNull(loginList).length; i += 2) {
                                if (userName.equals(loginList[i]) && password.equals(loginList[i + 1])) {
                                    oos.writeObject(new ResponseObj(true));
                                    oos.flush();
                                    convertToObjectsArray(loginList, 2);
                                    success = true;
                                }
                            }
                        }
                    }

                    if (!success) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("createacc")) { //  Case for user creating account
                    String userName = temp.getData()[0];
                    String password = temp.getData()[1];

                    if (Integer.parseInt(temp.getData()[2]) == 1) {
                        synchronized (GATEKEEPER) {
                            boolean userDuplicate = false;
                            loginList = readFile("TeacherLogins.txt");
                            for (int i = 0; i < Objects.requireNonNull(loginList).length; i += 2) {
                                if (userName.equals(loginList[i])) {
                                    userDuplicate = true;
                                }
                            }
                            if (userDuplicate) {
                                oos.writeObject(new ResponseObj(false));
                                oos.flush();
                            } else {
                                BufferedWriter bw = new BufferedWriter(new FileWriter("TeacherLogins.txt"));
                                for (int i = 0; i < Objects.requireNonNull(loginList).length; i++) {
                                    bw.write(loginList[i] + "\n");
                                }
                                bw.write(userName + "\n");
                                bw.write(password + "\n");
                                bw.close();

                                oos.writeObject(new ResponseObj(true));
                                oos.flush();
                            }
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            boolean userDuplicate = false;
                            loginList = readFile("StudentLogins.txt");
                            for (int i = 0; i < Objects.requireNonNull(loginList).length; i += 2) {
                                if (userName.equals(loginList[i]) && password.equals(loginList[i + 1])) {
                                    userDuplicate = true;
                                }
                            }
                            if (userDuplicate) {
                                oos.writeObject(new ResponseObj(false));
                                oos.flush();
                            } else {
                                BufferedWriter bw = new BufferedWriter(new FileWriter("StudentLogins.txt"));
                                for (int i = 0; i < Objects.requireNonNull(loginList).length; i++) {
                                    bw.write(loginList[i] + "\n");
                                }
                                bw.write(userName + "\n");
                                bw.write(password + "\n");
                                bw.close();

                                oos.writeObject(new ResponseObj(true));
                                oos.flush();
                            }
                        }
                    }
                }

                //  Student Functions

                if (temp.getCheckString().equalsIgnoreCase("changeaccstudent")) {
                    String userName = temp.getData()[0];
                    String change = temp.getData()[1];
                    int choice = Integer.parseInt(temp.getData()[2]);

                    boolean accChangeSuccess = false; //  Stores state of account detail change success

                    if (choice == 1) {
                        synchronized (GATEKEEPER) {
                            accChangeSuccess = deleteAccount(userName, 2);
                        }
                    } else if (choice == 2) {
                        synchronized (GATEKEEPER) {
                            accChangeSuccess = changeUsername(userName, change, 2);
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            accChangeSuccess = changePassword(userName, change, 2);
                        }
                    }

                    if (accChangeSuccess) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("takeQuiz")) {
                    String course = temp.getData()[0];
                    String quiz = temp.getData()[1];

                    boolean checkQuizExist = false;

                    synchronized (GATEKEEPER) {
                        checkQuizExist = courseQuizExist(course, quiz);
                    }

                    if (!checkQuizExist) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(true, takeQuizConversion(course, quiz)));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("submitQuiz")) {

                    boolean quizSubmit = false;
                    boolean quizSubmitPersonal = false;

                    String user = temp.getData()[0];
                    String course = temp.getData()[1];
                    String quiz = temp.getData()[2];
                    String totalQuizPoints = temp.getData()[3];
                    int totalUserPoints = 0;

                    ArrayList<String> questionsAndScore = new ArrayList<String>();

                    for (int i = 4; i < temp.getData().length; i += 2) {
                        totalUserPoints += Integer.parseInt(temp.getData()[i + 1]);
                        questionsAndScore.add(temp.getData()[i]);
                        questionsAndScore.add(temp.getData()[i + 1]);
                    }

                    String[] questionScore = new String[questionsAndScore.size()];
                    questionScore = questionsAndScore.toArray(questionScore);

                    synchronized (GATEKEEPER) {
                        quizSubmit = quizSubmission(user, course, quiz, totalQuizPoints,
                                Integer.toString(totalUserPoints));
                        quizSubmitPersonal = quizSubmissionPersonal(user, course, quiz, questionScore);
                        addUserQuiz(user, course, quiz);
                    }

                    if (quizSubmit && quizSubmitPersonal) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("viewQuiz")) {
                    String user = temp.getData()[0];
                    String course = temp.getData()[1];
                    String quiz = temp.getData()[2];

                    boolean checkQuizExist = false;

                    boolean userTakeQuiz = false;

                    synchronized (GATEKEEPER) {
                        checkQuizExist = courseQuizExist(course, quiz);
                    }

                    if (!checkQuizExist) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            userTakeQuiz = checkUserTakenQuiz(user, course, quiz);

                            if (!userTakeQuiz) {
                                oos.writeObject(new ResponseObj(false));
                                oos.flush();
                            } else {
                                oos.writeObject(new ResponseObj(true, user, viewQuizConversion(user, course, quiz)));
                                oos.flush();
                            }
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("viewCourses")) {
                    ArrayList<String> courseNames = new ArrayList<String>();

                    String[] tempList;

                    synchronized (GATEKEEPER) {
                        tempList = readFile("CourseList.txt");
                    }

                    if (tempList == null || tempList.length == 0) {
                        oos.writeObject(new ResponseObj(false));
                        oos.flush();
                    } else {
                        courseNames.addAll(Arrays.asList(tempList));

                        oos.writeObject(new ResponseObj(true, courseNames));
                        oos.flush();
                    }

                }

                //  Teacher Functions

                if (temp.getCheckString().equalsIgnoreCase("changeaccteacher")) {
                    String userName = temp.getData()[0];
                    String change = temp.getData()[1];
                    int choice = Integer.parseInt(temp.getData()[2]);

                    boolean accChangeSuccess = false; //  Stores state of account detail change success

                    if (choice == 1) {
                        synchronized (GATEKEEPER) {
                            accChangeSuccess = deleteAccount(userName, 1);
                        }
                    } else if (choice == 2) {
                        synchronized (GATEKEEPER) {
                            accChangeSuccess = changeUsername(userName, change, 1);
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            accChangeSuccess = changePassword(userName, change, 1);
                        }
                    }

                    if (accChangeSuccess) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("delQuiz")) {
                    String courseName = temp.getData()[0];
                    String quizName = temp.getData()[1];
                    boolean delQuizSuccess = false; //  Declaring a variable to hold success of deleting a quiz
                    synchronized (GATEKEEPER) {
                        delQuizSuccess = delQuiz(courseName, quizName);
                    }

                    if (delQuizSuccess) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("delCourse")) {
                    String courseName = temp.getData()[0];
                    boolean delCourseSuccess = false; //  Declaring a variable to hold success of deleting a course

                    synchronized (GATEKEEPER) {
                        delCourseSuccess = deleteCourse(courseName);
                    }

                    if (delCourseSuccess) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("createCourse")) {
                    String courseName = temp.getData()[0];
                    boolean createCourseSuccess = false;

                    synchronized (GATEKEEPER) {
                        createCourseSuccess = createCourse(courseName);
                    }

                    if (createCourseSuccess) {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        }
                    } else {
                        synchronized (GATEKEEPER) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("createQuiz")) {
                    String courseName = temp.getData()[0];
                    String quizName = temp.getData()[1];

                    boolean check = false;

                    synchronized (GATEKEEPER) {
                        check = quizCheck(courseName, quizName);
                    }

                    synchronized (GATEKEEPER) {
                        if (check) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        } else {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("createQuestion")) {
                    String question = temp.getData()[0];
                    String answerOne = temp.getData()[1];
                    String answerTwo = temp.getData()[2];
                    String answerThree = temp.getData()[3];
                    String answerFour = temp.getData()[4];
                    String answerFive = temp.getData()[5];
                    String correctAnswer = temp.getData()[6];
                    String questionPoints = temp.getData()[7];
                    String course = temp.getData()[8];
                    String quiz = temp.getData()[9];

                    boolean check = false;

                    synchronized (GATEKEEPER) {
                        check = addQuestion(course, quiz, question, answerOne, answerTwo, answerThree, answerFour,
                                answerFive, correctAnswer, questionPoints);
                    }

                    synchronized (GATEKEEPER) {
                        if (check) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        } else {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }

                }

                if (temp.getCheckString().equalsIgnoreCase("editQuiz")) {
                    String courseName = temp.getData()[0];
                    String quizName = temp.getData()[1];

                    boolean check = false;

                    synchronized (GATEKEEPER) {
                        check = quizEditCheck(courseName, quizName);
                    }

                    synchronized (GATEKEEPER) {
                        if (check) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        } else {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }

                }

                if (temp.getCheckString().equalsIgnoreCase("courseQuizCheck")) {
                    String courseName = temp.getData()[0];
                    String quizName = temp.getData()[1];

                    boolean check = false;

                    synchronized (GATEKEEPER) {
                        check = courseQuizExist(courseName, quizName);
                    }
                    synchronized (GATEKEEPER) {
                        if (check) {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        } else {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("teacherViewSubmission")) {
                    String userName = temp.getData()[0];
                    String courseName = temp.getData()[1];
                    String quizName = temp.getData()[2];

                    synchronized (GATEKEEPER) {
                        String score = getScore(courseName, quizName, userName);

                        if (score == null) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        } else {
                            oos.writeObject(new ResponseObj(true, score));
                            oos.flush();
                        }
                    }
                }

                if (temp.getCheckString().equalsIgnoreCase("teacherViewSubmissionList")) {
                    String courseName = temp.getData()[0];
                    String quizName = temp.getData()[1];

                    synchronized (GATEKEEPER) {
                        String[] submissions = getSubmissionList(courseName, quizName);

                        if (submissions == null) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        } else {
                            oos.writeObject(new ResponseObj(true, submissions));
                            oos.flush();
                        }
                    }

                }

                if (temp.getCheckString().equalsIgnoreCase("teacherChangeScores")) {
                    String userName = temp.getData()[2];
                    String courseName = temp.getData()[0];
                    String quizName = temp.getData()[1];
                    int questionNum = Integer.parseInt(temp.getData()[3]);
                    String questionChange = temp.getData()[4];

                    synchronized (GATEKEEPER) {
                        boolean success = changeScores(courseName, quizName, userName, questionNum, questionChange);

                        if (!success) {
                            oos.writeObject(new ResponseObj(false));
                            oos.flush();
                        } else {
                            oos.writeObject(new ResponseObj(true));
                            oos.flush();
                        }
                    }
                }
            }
        } catch (EOFException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Goodbye!",
                    "LMSQuizServer",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Port value does not exist",
                    "LMSQuizServer",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    //  Main method to run server infinitely
    public static void main(String[] args) {
        try {
            ArrayList<LMSQuizServer> lmsQuizServer = new ArrayList<LMSQuizServer>();
            ServerSocket serverSocket = new ServerSocket(4242);
            while (true) {
                Socket socket = serverSocket.accept();
                LMSQuizServer server = new LMSQuizServer(socket);
                lmsQuizServer.add(server);
                new Thread(server).start();
            }
//            LMSQuizServer server = new LMSQuizServer(); //  Back up run method for server
//            server.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
     * Function Methods
     */

    public void convertToObjectsArray(String[] list, int typeOfUser) {
        //  Converts inputted string array into array of specified user objects
        synchronized (GATEKEEPER) {
            if (typeOfUser == 1) {
                teachers = new Teacher[list.length / 2];
                int j = 0;
                for (int i = 0; i < list.length; i += 2) {
                    teachers[j] = new Teacher(list[i].trim(), list[i + 1].trim());
                    j++;
                }
            } else {
                students = new Student[list.length / 2];
                int j = 0;
                for (int i = 0; i < list.length; i += 2) {
                    students[j] = new Student(list[i].trim(), list[i + 1].trim());
                    j++;
                }
            }
        }
    }

    public static String[] readFile(String filename) throws FileNotFoundException {
        //  Reads inputted file and converts it into a String array
        synchronized (GATEKEEPER) {
            String[] loginList;
            try {
                File file = new File(filename);
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileReader fr1 = new FileReader(file);
                FileReader fr2 = new FileReader(file);
                BufferedReader br = new BufferedReader(fr2);
                LineNumberReader lr = new LineNumberReader(fr1);
                String line = "";
                int counter = 0;
                while (lr.readLine() != null) {
                    counter++;
                }
                loginList = new String[counter];
                for (int i = 0; i < loginList.length; i++) {
                    line = br.readLine();
                    loginList[i] = line;
                }
                return loginList;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public boolean changeUsername(String userName, String newUsername, int typeOfUser) {
        //  Changes username to new username provided by user
        boolean status = false;
        boolean canChange = true;
        if (typeOfUser == 1) {
            for (int i = 0; i < teachers.length; i++) {
                if (userName.equals(teachers[i].getUsername())) {
                    for (int j = 0; j < teachers.length; j++) {
                        if (newUsername.equals(teachers[j].getUsername())) {
                            canChange = false;
                        }
                    }
                    synchronized (GATEKEEPER) {
                        if (canChange) {
                            teachers[i].setUsername(newUsername);
                            File f = new File("TeacherLogins.txt");
                            try {
                                PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                                for (int k = 0; k < teachers.length; k++) {
                                    pw.write(teachers[k].getUsername() + "\n");
                                    pw.write(teachers[k].getPassword() + "\n");
                                }
                                pw.close();
                            } catch (IOException e) {
                                return false;
                            }
                            status = true;
                        }
                    }
                }
            }
        } else if (typeOfUser == 2) {
            for (int i = 0; i < students.length; i++) {
                if (userName.equals(students[i].getUsername())) {
                    for (int j = 0; j < students.length; j++) {
                        if (newUsername.equals(students[j].getUsername())) {
                            canChange = false;
                        }
                    }
                    synchronized (GATEKEEPER) {
                        if (canChange) {
                            students[i].setUsername(newUsername);
                            File f = new File("StudentLogins.txt");
                            try {
                                PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                                for (int k = 0; k < students.length; k++) {
                                    pw.write(students[k].getUsername() + "\n");
                                    pw.write(students[k].getPassword() + "\n");
                                }
                                pw.close();
                            } catch (IOException e) {
                                return false;
                            }
                            status = true;
                        }
                    }
                }
            }
        }
        return status;
    }

    public boolean changePassword(String userName, String newPassword, int typeOfUser) {
        //  Changes password to new password for specified user
        boolean status = false;
        synchronized (GATEKEEPER) {
            if (typeOfUser == 1) {
                for (int i = 0; i < teachers.length; i++) {
                    if (userName.equals(teachers[i].getUsername())) {
                        teachers[i].setPassword(newPassword);
                        File f = new File("TeacherLogins.txt");
                        try {
                            PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                            for (int k = 0; k < teachers.length; k++) {
                                pw.write(teachers[k].getUsername() + "\n");
                                pw.write(teachers[k].getPassword() + "\n");
                            }
                            pw.close();
                        } catch (IOException e) {
                            return false;
                        }
                        status = true;
                    }
                }
            } else {
                for (int i = 0; i < students.length; i++) {
                    if (userName.equals(students[i].getUsername())) {
                        students[i].setPassword(newPassword);
                        File f = new File("StudentLogins.txt");
                        try {
                            PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                            for (int k = 0; k < students.length; k++) {
                                pw.write(students[k].getUsername() + "\n");
                                pw.write(students[k].getPassword() + "\n");
                            }
                            pw.close();
                        } catch (IOException e) {
                            return false;
                        }
                        status = true;
                    }
                }
            }
            return status;
        }
    }

    public boolean deleteAccount(String userName, int typeOfUser) {
        //  Deletes account of user if called
        boolean status = false;
        synchronized (GATEKEEPER) {
            if (typeOfUser == 1) {
                for (int i = 0; i < teachers.length; i++) {
                    if (userName.equals(teachers[i].getUsername())) {
                        teachers[i].setUsername("loginDeleted");
                        teachers[i].setPassword("loginDeleted");
                        File f = new File("TeacherLogins.txt");
                        try {
                            PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                            for (int k = 0; k < teachers.length; k++) {
                                if (teachers[k].getUsername().equals("loginDeleted")) {

                                } else {
                                    pw.write(teachers[k].getUsername() + "\n");
                                    pw.write(teachers[k].getPassword() + "\n");
                                }
                            }
                            pw.close();
                        } catch (IOException e) {
                            return false;
                        }
                        status = true;
                    }
                }
            } else {
                for (int i = 0; i < students.length; i++) {
                    if (userName.equals(students[i].getUsername())) {
                        students[i].setUsername("loginDeleted");
                        students[i].setPassword("loginDeleted");
                        File f = new File("StudentLogins.txt");
                        try {
                            PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                            for (int k = 0; k < students.length; k++) {
                                if (students[k].getUsername().equals("loginDeleted")) {

                                } else {
                                    pw.write(students[k].getUsername() + "\n");
                                    pw.write(students[k].getPassword() + "\n");
                                }
                            }
                            pw.close();
                        } catch (IOException e) {
                            return false;
                        }
                        status = true;
                    }
                }
            }
            return status;
        }
    }

    public static boolean createCourse(String courseName) {
        //  Creates a new course. Only accessed by teachers
        boolean status = true;
        String[] courseList;

        try {
            synchronized (GATEKEEPER) {
                courseList = readFile("CourseList.txt");
                for (int i = 0; i < Objects.requireNonNull(courseList).length; i++) {
                    if (courseName.equals(courseList[i])) {
                        status = false;
                        return status;
                    }
                }
                readFile(courseName + "Quizzes.txt");
                BufferedWriter courseWriter = new BufferedWriter(new FileWriter("CourseList.txt"));
                for (int i = 0; i < Objects.requireNonNull(courseList).length; i++) {
                    courseWriter.write(courseList[i] + "\n");
                }
                courseWriter.write(courseName + "\n");
                courseWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static boolean deleteCourse(String courseName) {
        //  Deletes specified course. Only accessed by teachers
        boolean status = false;
        String[] courseList;
        try {
            synchronized (GATEKEEPER) {
                courseList = readFile("CourseList.txt");
                for (int i = 0; i < Objects.requireNonNull(courseList).length; i++) {
                    if ((courseName.equals(courseList[i]))) {
                        courseList[i] = "courseDeleted";
                        status = true;
                    }
                }

                if (!status) {
                    return status;
                }

                BufferedWriter courseWriter = new BufferedWriter(new FileWriter("CourseList.txt"));
                for (int i = 0; i < Objects.requireNonNull(courseList).length; i++) {
                    if (courseList[i].equals("courseDeleted")) {

                    } else {
                        courseWriter.write(courseList[i] + "\n");
                    }
                }
                courseWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return status;
        }
        return status;
    }

    public static boolean courseQuizExist(String course, String quiz) {
        boolean check = false;
        String[] courseList;
        String[] quizList;

        try {
            courseList = readFile("CourseList.txt");
            for (int i = 0; i < Objects.requireNonNull(courseList).length; i++) {
                if (course.equals(courseList[i])) {
                    check = true;
                }
            }

            if (!check) {
                return false;
            }

            quizList = readFile(course + "Quizzes.txt");
            for (int i = 0; i < Objects.requireNonNull(quizList).length; i++) {
                if (quiz.equals(quizList[i])) {
                    return true;
                }
            }

            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String[] takeQuizConversion(String course, String quiz) {
        ArrayList<String> quizQues = new ArrayList<String>();

        try {
            synchronized (GATEKEEPER) {
                String[] quizFile = readFile(course + quiz + ".txt");
                for (int i = 0; i < (Objects.requireNonNull(quizFile).length); i++) {
                    quizQues.add(quizFile[i]);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] questions = new String[quizQues.size()];
        questions = quizQues.toArray(questions);
        return questions;
    }

    public static boolean quizSubmission(String user, String course, String quiz, String totalPts, String userScore) {
        boolean success = false;

        String[] submissionList;

        try {
            synchronized (GATEKEEPER) {
                long datetime = System.currentTimeMillis();
                Timestamp timeStamp1 = new Timestamp(datetime);
                String timeStampStr = timeStamp1.toString();
                submissionList = readFile("QuizSubmissions.txt");
                BufferedWriter submissionWriter =
                        new BufferedWriter(new FileWriter("QuizSubmissions.txt"));
                for (int i = 0; i < Objects.requireNonNull(submissionList).length; i++) {
                    submissionWriter.write(submissionList[i] + "\n");
                }
                submissionWriter.write(course + "\n");
                submissionWriter.write(quiz + "\n");
                submissionWriter.write(user + "\n");
                submissionWriter.write(userScore + "/" + totalPts +
                        "\n");
                submissionWriter.write(timeStampStr + "\n");
                submissionWriter.close();

                success = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }

    public static boolean quizSubmissionPersonal(String user, String course, String quiz, String[] questionScore) {
        boolean success = false;

        String[] quesScore;

        try {
            synchronized (GATEKEEPER) {
                quesScore = readFile(user + course + quiz + ".txt");
                BufferedWriter personalWriter =
                        new BufferedWriter(new FileWriter(user + course + quiz + ".txt"));
                for (int i = 0; i < questionScore.length; i++) {
                    personalWriter.write(questionScore[i] + "\n");
                }
                personalWriter.close();

                success = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }

    public static void addUserQuiz(String user, String course, String quiz) {
        String[] prevUsers;
        boolean userExists = false;

        try {
            synchronized (GATEKEEPER) {
                prevUsers = readFile(course + quiz + "UserSubmissions.txt");
                BufferedWriter userWriter = new BufferedWriter(new FileWriter(course + quiz + "UserSubmissions.txt"));
                for (int i = 0; i < Objects.requireNonNull(prevUsers).length; i++) {
                    userWriter.write(prevUsers[i] + "\n");
                }
                for (int i = 0; i < Objects.requireNonNull(prevUsers).length; i++) {
                    if (prevUsers[i].equals(user)) {
                        userExists = true;
                    }
                }
                if (userExists) {

                } else {
                    userWriter.write(user);
                }

                userWriter.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean checkUserTakenQuiz(String user, String course, String quiz) {
        boolean success = false;

        String[] userList;

        try {
            synchronized (GATEKEEPER) {
                userList = readFile(course + quiz + "UserSubmissions.txt");
                for (int i = 0; i < Objects.requireNonNull(userList).length; i++) {
                    if (userList[i].equals(user)) {
                        success = true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return success;
    }

    public static String[] viewQuizConversion(String user, String course, String quiz) {
        ArrayList<String> viewQuizAL = new ArrayList<String>();

        String[] temp;

        try {
            synchronized (GATEKEEPER) {
                temp = readFile(user + course + quiz + ".txt");
                for (int i = 0; i < temp.length; i++) {
                    viewQuizAL.add(temp[i]);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] viewScores = new String[viewQuizAL.size()];
        viewScores = viewQuizAL.toArray(viewScores);

        return viewScores;
    }

    public static boolean quizEditCheck(String courseName, String quizName) {
        boolean check = false;
        String[] courseList;
        String[] quizList;

        try {
            synchronized (GATEKEEPER) {
                courseList = readFile("CourseList.txt");
                for (int i = 0; i < Objects.requireNonNull(courseList).length; i++) {
                    if (courseName.equals(courseList[i])) {
                        check = true;
                    }
                }
            }
            if (!check) {
                return false;
            }

            synchronized (GATEKEEPER) {
                quizList = readFile(courseName + "Quizzes.txt");
                for (int i = 0; i < Objects.requireNonNull(quizList).length; i++) {
                    if (quizName.equals(quizList[i])) {
                        check = true;
                    }
                }

            }

            if (!check) {
                return false;
            }

        } catch (Exception e) {
            return check;
        }
        return check;

    }

    public static boolean quizCheck(String courseName, String quizName) {
        boolean check = false;
        String[] courseList;
        String[] quizList;

        try {
            synchronized (GATEKEEPER) {
                courseList = readFile("CourseList.txt");
                for (int i = 0; i < Objects.requireNonNull(courseList).length; i++) {
                    if (courseName.equals(courseList[i])) {
                        check = true;
                    }
                }
            }

            if (!check) {
                return false;
            }

            synchronized (GATEKEEPER) {
                quizList = readFile(courseName + "Quizzes.txt");
                for (int i = 0; i < Objects.requireNonNull(quizList).length; i++) {
                    if (quizName.equals(quizList[i])) {
                        check = false;
                    }
                }
            }

            if (!check) {
                return false;
            }

            synchronized (GATEKEEPER) {
                BufferedWriter quizWriter = new BufferedWriter(new FileWriter(courseName + "Quizzes.txt"));
                for (int i = 0; i < Objects.requireNonNull(quizList).length; i++) {
                    quizWriter.write(quizList[i] + "\n");
                }
                quizWriter.write(quizName + "\n");
                quizWriter.close();
            }
        } catch (Exception e) {
            return check;
        }
        return check;
    }

    public static boolean addQuestion(String courseName, String quizName, String question, String answerOne,
                                      String answerTwo, String answerThree, String answerFour, String answerFive,
                                      String correctAns, String questionPoints) {
        //  Adds quiz to specified Course.
        boolean check = false;
        String[] questionList;

        try {
            synchronized (GATEKEEPER) {
                questionList = readFile(courseName + quizName + ".txt");
                BufferedWriter quizWriter = new BufferedWriter(new FileWriter(courseName + quizName + ".txt"));
                for (int i = 0; i < Objects.requireNonNull(questionList).length; i++) {
                    quizWriter.write(questionList[i] + "\n");
                }
                quizWriter.write(question + "\n");
                quizWriter.write(answerOne + "\n");
                quizWriter.write(answerTwo + "\n");
                quizWriter.write(answerThree + "\n");
                quizWriter.write(answerFour + "\n");
                quizWriter.write(answerFive + "\n");
                quizWriter.write(correctAns + "\n");
                quizWriter.write(questionPoints + "\n");
                quizWriter.close();

                check = true;
            }
        } catch (Exception e) {
            return check;
        }
        return check;
    }

    public static boolean delQuiz(String courseName, String quizName) {
        //  Deletes specified quiz from the course in question
        boolean check = false;
        String[] courseList;
        String[] quizList;

        try {
            synchronized (GATEKEEPER) {
                courseList = readFile("CourseList.txt");
                for (int i = 0; i < Objects.requireNonNull(courseList).length; i++) {
                    if ((courseName.equals(courseList[i]))) {
                        check = true;
                    }
                }
            }

            if (!check) {
                return check;
            }

            synchronized (GATEKEEPER) {
                quizList = readFile(courseName + "Quizzes.txt");
                for (int i = 0; i < Objects.requireNonNull(quizList).length; i++) {
                    if (quizName.equals(quizList[i])) {
                        check = true;
                    }
                }
            }

            if (!check) {
                return check;
            }

            for (int i = 0; i < Objects.requireNonNull(quizList).length; i++) {
                if ((quizName.equals(quizList[i]))) {
                    quizList[i] = "quizDeleted";
                }
            }

            synchronized (GATEKEEPER) {
                BufferedWriter quizWriter = new BufferedWriter(new FileWriter(courseName + "Quizzes.txt"));
                for (int i = 0; i < Objects.requireNonNull(quizList).length; i++) {
                    if (quizList[i].equals("quizDeleted")) {
                    } else {
                        quizWriter.write(quizList[i] + "\n");
                    }
                }
                quizWriter.flush();
                quizWriter.close();
            }
            check = true;
        } catch (Exception e) {
            return check;
        }
        return check;
    }

    public static String getScore(String courseName, String quizName, String userName) {
        String[] submissionsArray;
        String quizScore = null;

        try {
            submissionsArray = readFile("QuizSubmissions.txt");
            for (int i = 0; i < Objects.requireNonNull(submissionsArray).length; i += 5) {
                if (courseName.equals(submissionsArray[i])) {
                    if (quizName.equals(submissionsArray[i + 1])) {
                        if (userName.equals(submissionsArray[i + 2])) {
                            quizScore = submissionsArray[i + 3];
                        }
                    }
                }
            }
            return quizScore;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getSubmissionList(String courseName, String quizName) {
        String[] submissionList = null;
        try {
            submissionList = readFile(courseName + quizName + "UserSubmissions.txt");
            return submissionList;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean changeScores(String courseName, String quizName, String userName, int questionNum,
                                       String questionChange) {
        String[] scoresList;
        boolean check = false;
        try {
            scoresList = readFile(userName + courseName + quizName + ".txt");
            for (int i = 0; i < Objects.requireNonNull(scoresList).length; i += 2) {
                if ((i / 2) + 1 == questionNum) {
                    scoresList[i + 1] = questionChange;
                    check = true;
                }
            }
            BufferedWriter submissionWriter =
                    new BufferedWriter(new FileWriter(userName + courseName + quizName + ".txt"));
            for (int i = 0; i < Objects.requireNonNull(scoresList).length; i++) {
                submissionWriter.write(scoresList[i] + "\n");
            }
            submissionWriter.close();
            return check;
        } catch (Exception e) {
            return false;
        }

    }
}