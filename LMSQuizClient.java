import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * LMSQuizClient
 *
 * This is the client file for Project 5. It contains the GUI's, and logic to decipher the response from Server
 *
 * @author ArchitVarunSahu, JakobWachter, SergioAlvarez
 * @version 1.0, (2022-04-22)
 */

public class LMSQuizClient extends JComponent implements Runnable, Serializable {
    LMSQuizClient lmsClient; //  Creating an object of the LMSQuizClient class

    //  Creating required readers, writers and Synchronisation objects
    ObjectOutputStream oos = null; //  Declaring ObjectOutputStream variable
    ObjectInputStream ois = null; //  Declaring ObjectInputStream variable
    private static final Object GATEKEEPER = new Object(); //  Declaring an object for Synchronization purposes

    //  Network socket declaration
    Socket socket = null;

    //  LoginFrame
    String userName;
    JFrame loginFrame; //  Declaring a frame for login
    JButton login; //  Declaring a button for login
    JButton createAccount; //  Declaring a button to create an account
    JButton exit; //  Declaring a button to exit from login page
    JButton goBack = new JButton("Go Back");   //   Declaring a button to go back to the main screen

    //  General Buttons
    JButton logOut;   //   Button to logout of the program

    //  Teacher menu buttons
    JFrame teacherFrame;
    JFrame settingsFrameT;
    JButton createQuiz;
    JButton editQuiz;
    JButton deleteQuiz;
    JButton addCourse;
    JButton deleteCourse;
    JButton viewStudentSubmission;
    JButton viewStudentSubmissionList;
    JButton changeScores;
    JButton enterButtonT;
    JButton settingsT;


    //  Student menu buttons
    JFrame studentFrame;
    JFrame settingsFrameS;
    JButton viewCourses;
    JButton takeQuiz;
    JButton viewScores;
    JButton enterButtonS;
    JButton settingsS;

    //  Teacher Account Management
    JTextField userInputTeacher = new JTextField(10); //  Declaring a text field to store the username input
    JTextField changeInputTeacher = new JTextField(10); //  Declaring a text field to store the password input
    int settingsChoiceTeacher = 0;

    //  Student Account Management
    JTextField userInputStudent = new JTextField(10); //  Declaring a text field to store the username input
    JTextField changeInputStudent = new JTextField(10); //  Declaring a text field to store the password input
    int settingsChoiceStudent = 0;

    //  Both teacher/student options
    JTextField userNameInput; //  Declaring a text field to store the username input
    JPasswordField pwdInput; //  Declaring a text field to store the password input
    int userTypeInt = 0; //  Declaring a variable to store the type of user currently logging in.

    //  Teacher Functionality Frames

    //  Create Course
    JFrame teacherCreateCourse;
    JButton enterCreateCourse = new JButton("Enter");
    String createCourseName;
    JTextField courseInputCC = new JTextField(10);

    //  Create Quiz
    JFrame teacherCreateQuiz;
    JButton enterTCQ;
    JButton enterCourse;
    JPanel inputs;
    JTextField courseInput = new JTextField(10);
    JTextField quizInput = new JTextField(10);
    JTextField questionInput = new JTextField(30);
    JTextField answerOneInput = new JTextField(30);
    JTextField answerTwoInput = new JTextField(30);
    JTextField answerThreeInput = new JTextField(30);
    JTextField answerFourInput = new JTextField(30);
    JTextField answerFiveInput = new JTextField(30);
    JTextField correctAnsInput = new JTextField(5);
    JTextField questionPointsInput = new JTextField(5);

    //  Edit Quiz
    JFrame teacherEditQuiz;
    JButton enterTEQ;
    JButton enterTEQ2;

    //  Delete Quiz
    JFrame teacherDelQuiz;
    JButton enterDelQuiz = new JButton("Enter");
    JTextField courseInputDQ = new JTextField(10);
    JTextField quizInputDQ = new JTextField(10);
    String courseNameDelQuiz;
    String quizNameDelQuiz;

    //  Delete Course
    JFrame teacherDelCourse;
    JButton enterDelCourse = new JButton("Enter");
    String courseNameDelCourse;
    JTextField courseInputDC = new JTextField(10);

    // View Submissions
    JFrame teacherViewSubmissionList;
    JFrame teacherViewSubmission;
    JFrame teacherChangeScores;
    JButton enterTVS;
    JButton enterTVS2;
    JButton enterTCS;
    JButton enterTCS2;
    JButton enterTVSL;
    JPanel userInput;
    JPanel changeScoreInput;
    JTextField questionNumInput;
    JTextField questionChangeInput;

    //  Student Functionality Frames

    //  Take Quiz
    JFrame studentTakeQuiz;
    JTextField courseInputSTQ = new JTextField(10);
    JTextField quizInputSTQ = new JTextField(10);
    JButton enterTakeQuiz = new JButton("Enter");

    //  View Quiz Scores
    JFrame studentViewQuiz;
    JTextField courseInputSVQ = new JTextField(10);
    JTextField quizInputSVQ = new JTextField(10);
    JButton enterViewQuiz = new JButton("Enter");

    public static void main(String[] args) {
        LMSQuizClient client = new LMSQuizClient();
        Thread clientThread = new Thread(client);
        SwingUtilities.invokeLater(new Thread(new LMSQuizClient()));
        try {
            clientThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socket = new Socket("localhost", 4242);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginFrame();
    }

    public void loginFrame() {
        JLabel greeting = new JLabel("Welcome to our Learning Management System Quiz Tool!");
        greeting.setFont(new Font("Verdana", Font.PLAIN, 16));
        greeting.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel userNameLogin = new JLabel("Username:");
        userNameLogin.setFont(new Font("Verdana", Font.PLAIN, 13));
        JLabel password = new JLabel("Password:");
        password.setFont(new Font("Verdana", Font.PLAIN, 13));
        JLabel userType = new JLabel("Are you a Student or a Teacher?");
        userType.setFont(new Font("Verdana", Font.PLAIN, 13));

        loginFrame = new JFrame("LMSQuiz Login Page");

        login = new JButton("Login");
        createAccount = new JButton("Create Account");
        exit = new JButton("Exit");

        userNameInput = new JTextField(10);
        pwdInput = new JPasswordField(10);

        JRadioButton teacher = new JRadioButton("Teacher");
        JRadioButton student = new JRadioButton("Student");
        ButtonGroup userGroup = new ButtonGroup();
        userGroup.add(teacher);
        userGroup.add(student);

        Container container = loginFrame.getContentPane();

        container.setLayout(new BorderLayout());
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        loginFrame.setSize(550, 150);
        loginFrame.setResizable(true);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setVisible(true);

        loginFrame.add(greeting, BorderLayout.PAGE_START);

        JPanel userPanel = new JPanel();
        userPanel.add(userNameLogin);
        userPanel.add(userNameInput);
        userPanel.add(password);
        userPanel.add(pwdInput);
        userPanel.add(userType);
        userPanel.add(teacher);
        userPanel.add(student);
        container.add(userPanel, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
        panelSouth.setBackground(Color.lightGray);
        panelSouth.add(login);
        panelSouth.add(createAccount);
        panelSouth.add(exit);
        container.add(panelSouth, BorderLayout.PAGE_END);

        teacher.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    userTypeInt = 1;
            }
        });

        student.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    userTypeInt = 2;
            }
        });

        login.addActionListener(actionListener);
        exit.addActionListener(actionListener);
        createAccount.addActionListener(actionListener);
        addWL(loginFrame);

    }

    //  Main menu frames
    public void teacherFrame() {
        teacherFrame = new JFrame("Welcome Teacher!");
        logOut = new JButton("Log Out");

        JLabel greeting = new JLabel("Please select an option from below");
        createQuiz = new JButton("Create Quiz");
        editQuiz = new JButton("Edit Quiz");
        deleteQuiz = new JButton("Delete Quiz");
        addCourse = new JButton("Create Course");
        deleteCourse = new JButton("Delete Course");
        viewStudentSubmission = new JButton("View a Students Quiz Submission");
        settingsT = new JButton("Settings");
        viewStudentSubmissionList = new JButton("View Submission List");
        changeScores = new JButton("Change Student Scores");

        Container container = teacherFrame.getContentPane();

        teacherFrame.setSize(600, 300);
        teacherFrame.setLocationRelativeTo(null);
        teacherFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherFrame.setResizable(true);
        teacherFrame.setVisible(true);

        JPanel panelNorth = new JPanel();
        panelNorth.add(greeting);
        panelNorth.add(settingsT);
        container.add(panelNorth, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel();
        panelCentral.add(createQuiz);
        panelCentral.add(editQuiz);
        panelCentral.add(deleteQuiz);
        panelCentral.add(addCourse);
        panelCentral.add(deleteCourse);
        panelCentral.add(viewStudentSubmission);
        panelCentral.add(viewStudentSubmissionList);
        panelCentral.add(changeScores);
        panelCentral.add(logOut);
        container.add(panelCentral, BorderLayout.CENTER);

        createQuiz.addActionListener(actionListener);
        editQuiz.addActionListener(actionListener);
        deleteQuiz.addActionListener(actionListener);
        addCourse.addActionListener(actionListener);
        deleteCourse.addActionListener(actionListener);
        viewStudentSubmission.addActionListener(actionListener);
        settingsT.addActionListener(actionListener);
        viewStudentSubmissionList.addActionListener(actionListener);
        changeScores.addActionListener(actionListener);
        logOut.addActionListener(actionListener);
        addWL(teacherFrame);
    }

    public void studentFrame() {
        studentFrame = new JFrame("Welcome Student");
        logOut = new JButton("Log Out");
        takeQuiz = new JButton("Take Quiz");
        viewScores = new JButton("View Scores");
        viewCourses = new JButton("View Courses");
        settingsS = new JButton("Settings");

        Container container = studentFrame.getContentPane();
        studentFrame.setSize(600, 300);
        studentFrame.setLocationRelativeTo(null);
        studentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studentFrame.setResizable(true);
        studentFrame.setVisible(true);

        JPanel panelCentral = new JPanel();
        panelCentral.add(takeQuiz);
        panelCentral.add(viewScores);
        panelCentral.add(viewCourses);
        panelCentral.add(settingsS);
        container.add(panelCentral, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
        panelSouth.add(logOut);
        container.add(panelSouth, BorderLayout.SOUTH);

        takeQuiz.addActionListener(actionListener);
        viewScores.addActionListener(actionListener);
        viewCourses.addActionListener(actionListener);
        logOut.addActionListener(actionListener);
        settingsS.addActionListener(actionListener);
        addWL(studentFrame);
    }

    //  Settings Frames

    public void settingsFrameT() {
        settingsFrameT = new JFrame("Settings");
        JLabel instructions = new JLabel("Please input your current username and select from the menu to " +
                "edit your account!");
        JRadioButton changeUsername = new JRadioButton("Change Username");
        JRadioButton changePassword = new JRadioButton("Change Password");
        JRadioButton deleteAccount = new JRadioButton("Delete Account");
        ButtonGroup userGroup = new ButtonGroup();
        enterButtonT = new JButton("Enter");

        userGroup.add(deleteAccount);
        userGroup.add(changeUsername);
        userGroup.add(changePassword);
        JLabel userNameS = new JLabel("Current Username:");
        JLabel changeS = new JLabel("New Username or Password:");

        Container container = settingsFrameT.getContentPane();

        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        settingsFrameT.setSize(600, 300);
        settingsFrameT.setLocationRelativeTo(null);
        settingsFrameT.setResizable(true);
        settingsFrameT.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrameT.setVisible(true);

        JPanel userPanelS = new JPanel();
        userPanelS.add(instructions);
        container.add(userPanelS, BorderLayout.PAGE_START);

        JPanel passwordPanel = new JPanel();
        passwordPanel.add(userNameS);
        passwordPanel.add(userInputTeacher);
        passwordPanel.add(changeS);
        passwordPanel.add(changeInputTeacher);

        JPanel selectionPanel = new JPanel();
        selectionPanel.add(changeUsername);
        selectionPanel.add(changePassword);
        selectionPanel.add(deleteAccount);
        selectionPanel.add(enterButtonT);

        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, passwordPanel, selectionPanel);
        pane.setDividerSize(1);
        pane.setContinuousLayout(true);
        container.add(pane, BorderLayout.CENTER);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.PAGE_END);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsFrameT.dispose();
                teacherFrame.setVisible(true);
            }
        });

        changePassword.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    settingsChoiceTeacher = 3;
            }
        });

        changeUsername.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    settingsChoiceTeacher = 2;
            }
        });

        deleteAccount.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    settingsChoiceTeacher = 1;
            }
        });

        enterButtonT.addActionListener(actionListener);
        addWL(settingsFrameT);
    }

    public void settingsFrameS() {
        settingsFrameS = new JFrame("Settings");
        JLabel instructions = new JLabel("Please input your current username and select from the menu to " +
                "edit your account!");
        JRadioButton changeUsername = new JRadioButton("Change Username");
        JRadioButton changePassword = new JRadioButton("Change Password");
        JRadioButton deleteAccount = new JRadioButton("Delete Account");
        ButtonGroup userGroup = new ButtonGroup();
        enterButtonS = new JButton("Enter");

        userGroup.add(deleteAccount);
        userGroup.add(changeUsername);
        userGroup.add(changePassword);
        JLabel userNameS = new JLabel("Current Username:");
        JLabel changeS = new JLabel("New Username or Password:");

        Container container = settingsFrameS.getContentPane();

        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        settingsFrameS.setSize(600, 300);
        settingsFrameS.setLocationRelativeTo(null);
        settingsFrameS.setResizable(true);
        settingsFrameS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrameS.setVisible(true);


        JPanel userPanelS = new JPanel();
        userPanelS.add(instructions);

        container.add(userPanelS, BorderLayout.PAGE_START);

        JPanel passwordPanel = new JPanel();
        passwordPanel.add(userNameS);
        passwordPanel.add(userInputStudent);
        passwordPanel.add(changeS);
        passwordPanel.add(changeInputStudent);

        JPanel selectionPanel = new JPanel();
        selectionPanel.add(changeUsername);
        selectionPanel.add(changePassword);
        selectionPanel.add(deleteAccount);
        selectionPanel.add(enterButtonS);

        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, passwordPanel, selectionPanel);
        pane.setDividerSize(1);
        pane.setContinuousLayout(true);
        container.add(pane, BorderLayout.CENTER);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.PAGE_END);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsFrameS.dispose();
                studentFrame.setVisible(true);
            }
        });

        changePassword.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    settingsChoiceStudent = 3;
            }
        });

        changeUsername.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    settingsChoiceStudent = 2;
            }
        });

        deleteAccount.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    settingsChoiceStudent = 1;
            }
        });

        enterButtonS.addActionListener(actionListener);
        addWL(settingsFrameS);
    }

    //  Teacher Functions

    public void teacherViewSubmissionList() {
        teacherViewSubmissionList = new JFrame("View Submission List");
        JLabel course = new JLabel("Enter the course and quiz names in the respective boxes:");
        courseInput = new JTextField(10);
        quizInput = new JTextField(10);
        enterTVSL = new JButton("Enter");

        Container container = teacherViewSubmissionList.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        teacherViewSubmissionList.setSize(800, 400);
        teacherViewSubmissionList.setLocationRelativeTo(null);
        teacherViewSubmissionList.setResizable(true);
        teacherViewSubmissionList.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherViewSubmissionList.setVisible(true);

        JPanel courseAskPanel = new JPanel();
        courseAskPanel.add(course);
        courseAskPanel.add(courseInput);
        courseAskPanel.add(quizInput);
        courseAskPanel.add(enterTVSL);
        container.add(courseAskPanel, BorderLayout.NORTH);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.PAGE_END);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewSubmissionList.dispose();
                teacherFrame.setVisible(true);
            }
        });

        enterTVSL.addActionListener(actionListener);
        addWL(teacherViewSubmissionList);
    }

    public void teacherViewSubmission() {
        teacherViewSubmission = new JFrame("View Submission List");
        JLabel course = new JLabel("Enter the course and quiz names in the respective boxes:");
        courseInput = new JTextField(10);
        JLabel userLabel = new JLabel("Enter the name of the User you would like to check the submission for:");
        userNameInput = new JTextField(15);
        quizInput = new JTextField(10);
        enterTVS = new JButton("Enter");
        enterTVS2 = new JButton("Enter");

        Container container = teacherViewSubmission.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        teacherViewSubmission.setSize(800, 400);
        teacherViewSubmission.setLocationRelativeTo(null);
        teacherViewSubmission.setResizable(true);
        teacherViewSubmission.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherViewSubmission.setVisible(true);

        JPanel courseAskPanel = new JPanel();
        courseAskPanel.add(course);
        courseAskPanel.add(courseInput);
        courseAskPanel.add(quizInput);
        courseAskPanel.add(enterTVS2);
        container.add(courseAskPanel, BorderLayout.NORTH);

        userInput = new JPanel();
        userInput.add(userLabel);
        userInput.add(userNameInput);
        userInput.add(enterTVS);
        container.add(userInput, BorderLayout.CENTER);
        userInput.setVisible(false);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.PAGE_END);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewSubmission.dispose();
                teacherFrame.setVisible(true);
            }
        });

        enterTVS.addActionListener(actionListener);
        enterTVS2.addActionListener(actionListener);
        addWL(teacherViewSubmission);
    }

    public void teacherChangeScores() {
        teacherChangeScores = new JFrame("View Submission List");
        JLabel course = new JLabel("Enter the course and quiz names in the respective boxes:");
        courseInput = new JTextField(10);
        quizInput = new JTextField(10);
        enterTCS = new JButton("Enter");
        enterTCS2 = new JButton("Enter");
        JLabel userLabel = new JLabel("Enter the name of the User you would like to change the score of:");
        userNameInput = new JTextField(15);
        JLabel questionNumLabel = new JLabel("Enter the question number you would like to change the score of:");
        questionNumInput = new JTextField(15);
        JLabel questionChangeLabel = new JLabel("Enter the score you would like to change it to:");
        questionChangeInput = new JTextField(15);

        Container container = teacherChangeScores.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        teacherChangeScores.setSize(800, 400);
        teacherChangeScores.setLocationRelativeTo(null);
        teacherChangeScores.setResizable(true);
        teacherChangeScores.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherChangeScores.setVisible(true);

        JPanel courseAskPanel = new JPanel();
        courseAskPanel.add(course);
        courseAskPanel.add(courseInput);
        courseAskPanel.add(quizInput);
        courseAskPanel.add(enterTCS2);
        container.add(courseAskPanel, BorderLayout.NORTH);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.PAGE_END);

        changeScoreInput = new JPanel();
        changeScoreInput.add(userLabel);
        changeScoreInput.add(userNameInput);
        changeScoreInput.add(questionNumLabel);
        changeScoreInput.add(questionNumInput);
        changeScoreInput.add(questionChangeLabel);
        changeScoreInput.add(questionChangeInput);
        changeScoreInput.add(enterTCS);
        container.add(changeScoreInput, BorderLayout.CENTER);
        changeScoreInput.setVisible(false);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherChangeScores.dispose();
                teacherFrame.setVisible(true);
            }
        });

        enterTCS.addActionListener(actionListener);
        enterTCS2.addActionListener(actionListener);
        addWL(teacherChangeScores);
    }

    public void teacherCreateQuiz() { //  Creates a quiz

        teacherCreateQuiz = new JFrame("Create new Quiz");
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JLabel questionLabel = new JLabel("What is the Question?");
        questionInput = new JTextField(30);
        JLabel answerOneLabel = new JLabel("What is answer number 1?");
        answerOneInput = new JTextField(30);
        JLabel answerTwoLabel = new JLabel("What is answer number 2?");
        answerTwoInput = new JTextField(30);
        JLabel answerThreeLabel = new JLabel("What is answer number 3?");
        answerThreeInput = new JTextField(30);
        JLabel answerFourLabel = new JLabel("What is answer number 4?");
        answerFourInput = new JTextField(30);
        JLabel answerFiveLabel = new JLabel("What is answer number 5?");
        answerFiveInput = new JTextField(30);
        JLabel correctAnsLabel = new JLabel("Which option is correct?");
        correctAnsInput = new JTextField(8);
        JLabel questionPointsLabel = new JLabel("How many points is this question worth?");
        questionPointsInput = new JTextField(8);
        JLabel course = new JLabel("Enter the course and quiz names in the respective boxes:");
        courseInput = new JTextField(10);
        quizInput = new JTextField(10);
        JLabel info = new JLabel("Please Enter your question and its corresponding answers below! Do not change " +
                "above text!");
        info.setFont(new Font("Verdana", Font.PLAIN, 12));
        enterTCQ = new JButton("Enter");
        enterCourse = new JButton("Enter");

        Container container = teacherCreateQuiz.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        teacherCreateQuiz.setSize(800, 400);
        teacherCreateQuiz.setLocationRelativeTo(null);
        teacherCreateQuiz.setResizable(true);
        teacherCreateQuiz.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherCreateQuiz.setVisible(true);

        JPanel courseAskPanel = new JPanel();
        courseAskPanel.add(course);
        courseAskPanel.add(courseInput);
        courseAskPanel.add(quizInput);
        courseAskPanel.add(enterCourse);
        container.add(courseAskPanel, BorderLayout.NORTH);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.PAGE_END);

        inputs = new JPanel();
        inputs.add(info);
        inputs.add(questionLabel);
        inputs.add(questionInput);
        inputs.add(answerOneLabel);
        inputs.add(answerOneInput);
        inputs.add(answerTwoLabel);
        inputs.add(answerTwoInput);
        inputs.add(answerThreeLabel);
        inputs.add(answerThreeInput);
        inputs.add(answerFourLabel);
        inputs.add(answerFourInput);
        inputs.add(answerFiveLabel);
        inputs.add(answerFiveInput);
        inputs.add(correctAnsLabel);
        inputs.add(correctAnsInput);
        inputs.add(questionPointsLabel);
        inputs.add(questionPointsInput);
        inputs.add(enterTCQ);
        container.add(inputs, BorderLayout.CENTER);
        inputs.setVisible(false);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherCreateQuiz.dispose();
                teacherFrame.setVisible(true);
            }
        });

        enterTCQ.addActionListener(actionListener);
        enterCourse.addActionListener(actionListener);

        addWL(teacherCreateQuiz);
    }

    public void teacherEditQuiz() {
        teacherEditQuiz = new JFrame("Edit Quiz");
        JLabel questionLabel = new JLabel("What is the Question?");
        questionInput = new JTextField(30);
        JLabel answerOneLabel = new JLabel("What is answer number 1?");
        answerOneInput = new JTextField(30);
        JLabel answerTwoLabel = new JLabel("What is answer number 2?");
        answerTwoInput = new JTextField(30);
        JLabel answerThreeLabel = new JLabel("What is answer number 3?");
        answerThreeInput = new JTextField(30);
        JLabel answerFourLabel = new JLabel("What is answer number 4?");
        answerFourInput = new JTextField(30);
        JLabel answerFiveLabel = new JLabel("What is answer number 5?");
        answerFiveInput = new JTextField(30);
        JLabel correctAnsLabel = new JLabel("Which option is correct?");
        correctAnsInput = new JTextField(8);
        JLabel questionPointsLabel = new JLabel("How many points is this question worth?");
        questionPointsInput = new JTextField(8);
        JLabel course = new JLabel("Enter the course and quiz names in the respective boxes:");
        courseInput = new JTextField(10);
        quizInput = new JTextField(10);
        JLabel info = new JLabel("Please Enter your question and its corresponding answers below! Do not change " +
                "above text!");
        info.setFont(new Font("Verdana", Font.PLAIN, 12));
        enterTEQ = new JButton("Enter");
        enterTEQ2 = new JButton("Enter");

        Container container = teacherEditQuiz.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        teacherEditQuiz.setSize(800, 400);
        teacherEditQuiz.setLocationRelativeTo(null);
        teacherEditQuiz.setResizable(true);
        teacherEditQuiz.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherEditQuiz.setVisible(true);

        JPanel courseAskPanel = new JPanel();
        courseAskPanel.add(course);
        courseAskPanel.add(courseInput);
        courseAskPanel.add(quizInput);
        courseAskPanel.add(enterTEQ2);
        container.add(courseAskPanel, BorderLayout.NORTH);

        inputs = new JPanel();
        inputs.add(info);
        inputs.add(questionLabel);
        inputs.add(questionInput);
        inputs.add(answerOneLabel);
        inputs.add(answerOneInput);
        inputs.add(answerTwoLabel);
        inputs.add(answerTwoInput);
        inputs.add(answerThreeLabel);
        inputs.add(answerThreeInput);
        inputs.add(answerFourLabel);
        inputs.add(answerFourInput);
        inputs.add(answerFiveLabel);
        inputs.add(answerFiveInput);
        inputs.add(correctAnsLabel);
        inputs.add(correctAnsInput);
        inputs.add(questionPointsLabel);
        inputs.add(questionPointsInput);
        inputs.add(enterTEQ);
        container.add(inputs, BorderLayout.CENTER);
        inputs.setVisible(false);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.PAGE_END);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherEditQuiz.dispose();
                teacherFrame.setVisible(true);
            }
        });


        enterTEQ.addActionListener(actionListener);
        enterTEQ2.addActionListener(actionListener);

        addWL(teacherEditQuiz);

    }

    public void teacherCreateCourse() {
        teacherCreateCourse = new JFrame("Create new Course");
        JLabel course = new JLabel("Please enter the name of the course: ");

        Container container = teacherCreateCourse.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        teacherCreateCourse.setSize(600, 300);
        teacherCreateCourse.setLocationRelativeTo(null);
        teacherCreateCourse.setResizable(true);
        teacherCreateCourse.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherCreateCourse.setVisible(true);

        JPanel courseAskPanel = new JPanel();
        courseAskPanel.add(course);
        courseAskPanel.add(courseInputCC);
        courseAskPanel.add(enterCreateCourse);
        container.add(courseAskPanel, BorderLayout.NORTH);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.SOUTH);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherCreateCourse.dispose();
                teacherFrame.setVisible(true);
            }
        });

        enterCreateCourse.addActionListener(actionListener);

        addWL(teacherCreateCourse);
    }

    public void teacherDeleteQuiz() {
        //  Opening of delete quiz screen
        teacherDelQuiz = new JFrame("Delete a quiz");
        JLabel course = new JLabel("Please enter the name of the course: ");

        //  Asking quiz number to be deleted
        JLabel quizAsk = new JLabel("Please enter the name of the quiz: ");

        Container container = teacherDelQuiz.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        teacherDelQuiz.setSize(600, 300);
        teacherDelQuiz.setLocationRelativeTo(null);
        teacherDelQuiz.setResizable(true);
        teacherDelQuiz.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherDelQuiz.setVisible(true);

        JPanel courseAskPanel = new JPanel();
        courseAskPanel.add(course);
        courseAskPanel.add(courseInputDQ);
        container.add(courseAskPanel, BorderLayout.NORTH);

        JPanel quizPanel = new JPanel();
        quizPanel.add(quizAsk);
        quizPanel.add(quizInputDQ);
        quizPanel.add(enterDelQuiz);
        container.add(quizPanel, BorderLayout.CENTER);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.SOUTH);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherDelQuiz.dispose();
                teacherFrame.setVisible(true);
            }
        });

        enterDelQuiz.addActionListener(actionListener);
        addWL(teacherDelQuiz);
    }

    public void teacherDeleteCourse() {
        //  Opening of teacher delete course screen
        teacherDelCourse = new JFrame("Delete a course");
        JLabel course = new JLabel("Please enter the name of the course you want to delete: ");

        Container container = teacherDelCourse.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        teacherDelCourse.setSize(600, 300);
        teacherDelCourse.setLocationRelativeTo(null);
        teacherDelCourse.setResizable(true);
        teacherDelCourse.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherDelCourse.setVisible(true);

        JPanel courseAskPanel = new JPanel();
        courseAskPanel.add(course);
        courseAskPanel.add(courseInputDC);
        courseAskPanel.add(enterDelCourse);
        container.add(courseAskPanel, BorderLayout.CENTER);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.SOUTH);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherDelCourse.dispose();
                teacherFrame.setVisible(true);
            }
        });

        enterDelCourse.addActionListener(actionListener);
        addWL(teacherDelCourse);
    }

    //  Student Functions

    public void studentTakeQuiz() {
        studentTakeQuiz = new JFrame("Take a Quiz");
        JLabel course = new JLabel("Please enter the name of the course: ");
        JLabel quiz = new JLabel("Please enter the name of the quiz: ");

        Container container = studentTakeQuiz.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        studentTakeQuiz.setSize(600, 300);
        studentTakeQuiz.setLocationRelativeTo(null);
        studentTakeQuiz.setResizable(true);
        studentTakeQuiz.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studentTakeQuiz.setVisible(true);

        JPanel coursePanel = new JPanel();
        coursePanel.add(course);
        coursePanel.add(courseInputSTQ);

        JPanel quizPanel = new JPanel();
        quizPanel.add(quiz);
        quizPanel.add(quizInputSTQ);
        quizPanel.add(enterTakeQuiz);

        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, coursePanel, quizPanel);
        pane.setDividerSize(1);
        pane.setContinuousLayout(true);
        container.add(pane, BorderLayout.CENTER);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.SOUTH);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentTakeQuiz.dispose();
                studentFrame.setVisible(true);
            }
        });

        enterTakeQuiz.addActionListener(actionListener);

        addWL(studentTakeQuiz);
    }

    public void studentViewScores() {
        studentViewQuiz = new JFrame("View a Score");
        JLabel course = new JLabel("Please enter the name of the course: ");
        JLabel quiz = new JLabel("Please enter the name of the quiz: ");

        Container container = studentViewQuiz.getContentPane();
        lmsClient = new LMSQuizClient();
        container.add(lmsClient, BorderLayout.CENTER);

        studentViewQuiz.setSize(600, 300);
        studentViewQuiz.setLocationRelativeTo(null);
        studentViewQuiz.setResizable(true);
        studentViewQuiz.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studentViewQuiz.setVisible(true);

        JPanel coursePanel = new JPanel();
        coursePanel.add(course);
        coursePanel.add(courseInputSVQ);

        JPanel quizPanel = new JPanel();
        quizPanel.add(quiz);
        quizPanel.add(quizInputSVQ);
        quizPanel.add(enterViewQuiz);

        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, coursePanel, quizPanel);
        pane.setDividerSize(1);
        pane.setContinuousLayout(true);
        container.add(pane, BorderLayout.CENTER);

        JPanel goBackPanel = new JPanel();
        goBackPanel.add(goBack);
        container.add(goBackPanel, BorderLayout.SOUTH);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentViewQuiz.dispose();
                studentFrame.setVisible(true);
            }
        });

        enterViewQuiz.addActionListener(actionListener);

        addWL(studentViewQuiz);
    }

    void addWL(JFrame frame) { //  Closes frame when cross button closed
        frame.addWindowListener(new WindowAdapter() {
            public void windowClose(WindowEvent e) {
                synchronized (GATEKEEPER) {
                    frame.dispose();
                    try {
                        oos.writeObject(new RequestObj("exit", null));
                    } catch (IOException a) {
                        a.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(
                            null,
                            "Thank You for using our Quiz Application!",
                            "LMSQuizTool",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Login/Create Account Actions

            if (e.getSource() == login) { //  User clicks Login.
                String user = userNameInput.getText();
                String pwd = "";
                char[] tempPwd = pwdInput.getPassword();
                for (int i = 0; i < tempPwd.length; i++) {
                    if (tempPwd[i] == '[' || tempPwd[i] == ']') { //  Write in docu that '[]' chars aren't allowed

                    } else {
                        pwd += tempPwd[i];
                    }
                }

                userName = user;

                if (userTypeInt == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Please select an option!",
                            "Choose an Option!", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        String[] userLoginArray = {user, pwd, Integer.toString(userTypeInt)};

                        oos.flush();
                        oos.writeObject(new RequestObj("login", userLoginArray));
                        oos.flush();

                        ResponseObj loginResponse = (ResponseObj) ois.readObject();

                        boolean loginSuccess = loginResponse.isSuccess(); //  Variable stores success of user's login

                        if (loginSuccess) {
                            userNameInput.setText("");
                            pwdInput.setText("");
                            loginFrame.dispose();
                            if (userTypeInt == 1) {
                                teacherFrame();
                            } else {
                                studentFrame();
                            }
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Connection Successful!\n" + "Welcome " + user + "!",
                                    "LMSQuizTool",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            userNameInput.setText("");
                            pwdInput.setText("");

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Login Unsuccessful. Please try again later.",
                                    "LMSQuizTool",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (Exception a) {
                        JOptionPane.showMessageDialog(
                                null,
                                "An error occurred. Please try again later!",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                        closeGUI(loginFrame);
                    }
                }
            }

            if (e.getSource() == createAccount) { //  User creates an account
                String user = userNameInput.getText();
                String pwd = "";
                char[] tempPwd = pwdInput.getPassword();
                for (int i = 0; i < tempPwd.length; i++) {
                    if (tempPwd[i] == '[' || tempPwd[i] == ']') { //  Write in docu that '[]' chars aren't allowed

                    } else {
                        pwd += tempPwd[i];
                    }
                }

                try {
                    String[] userCreateAcc = {user, pwd, Integer.toString(userTypeInt)};

                    oos.writeObject(new RequestObj("createacc", userCreateAcc));
                    oos.flush();

                    ResponseObj createAccResp = (ResponseObj) ois.readObject();

                    boolean createAccSuccess = createAccResp.isSuccess();

                    if (createAccSuccess) {
                        userNameInput.setText("");
                        pwdInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Account Created Successfully!\n" + "Please log in again.",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        userNameInput.setText("");
                        pwdInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Account creation failed. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                    closeGUI(loginFrame);
                }
            }

            //  If Exit Button Clicked

            if (e.getSource() == exit) { //  User clicks exit button during Login frame
                try {
                    oos.writeObject(new RequestObj("exit", null));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(
                        null,
                        "Thank You for using out Quiz Application!",
                        "LMSQuizTool",
                        JOptionPane.INFORMATION_MESSAGE);
                closeGUI(loginFrame);
            }

            if (e.getSource() == logOut) { //  User clicks log out button during respective frame
                try {
                    oos.writeObject(new RequestObj("exit", null));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(
                        null,
                        "Thank You for using out Quiz Application!",
                        "LMSQuizTool",
                        JOptionPane.INFORMATION_MESSAGE);

                if (userTypeInt == 1) {
                    closeGUI(teacherFrame);
                } else {
                    closeGUI(studentFrame);
                }
            }

            // Teacher Actions

            if (e.getSource() == enterTCS) { //  If teacher wants to change course of individual student
                String course = courseInput.getText();
                String quiz = quizInput.getText();
                String user = userNameInput.getText();
                String questionNum = questionNumInput.getText();
                String questionChange = questionChangeInput.getText();

                try {
                    String[] createQuizArray = {course, quiz, user, questionNum, questionChange};

                    oos.flush();
                    oos.writeObject(new RequestObj("teacherChangeScores", createQuizArray));
                    oos.flush();

                    ResponseObj courseQuizCheckResponse = (ResponseObj) ois.readObject();

                    boolean courseQuizSuccess = courseQuizCheckResponse.isSuccess();

                    if (courseQuizSuccess) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Score change Successful!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {

                        JOptionPane.showMessageDialog(
                                null,
                                "Score cannot be edited. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

            if (e.getSource() == enterTVSL) { //  If teacher wants to view an overall submission list for a Quiz
                String course = courseInput.getText();
                String quiz = quizInput.getText();

                try {
                    String[] createQuizArray = {course, quiz};

                    oos.flush();
                    oos.writeObject(new RequestObj("courseQuizCheck", createQuizArray));
                    oos.flush();

                    ResponseObj courseQuizCheckResponse = (ResponseObj) ois.readObject();

                    boolean courseQuizSuccess = courseQuizCheckResponse.isSuccess();

                    if (courseQuizSuccess) {
                        oos.flush();
                        oos.writeObject(new RequestObj("teacherViewSubmissionList", createQuizArray));
                        oos.flush();

                        ResponseObj response = (ResponseObj) ois.readObject();

                        boolean success = response.isSuccess(); //  Variable stores success of user's login
                        String[] submissions = response.getQuiz();

                        if (success) {
                            String user = "";
                            for (int i = 0; i < submissions.length; i++) {
                                user += submissions[i] + "\n";
                            }
                            JOptionPane.showMessageDialog(
                                    null, "List of Users who have submitted this quiz:\n" + user
                                    ,
                                    "LMSQuizTool",
                                    JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "There are no submissions for this quiz!",
                                    "LMSQuizTool",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        quizInput.setText("");
                        courseInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Course or quiz name is Invalid. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            if (e.getSource() == enterTCS2) { //  Second part of the Teacher changing individual student's score
                String course = courseInput.getText();
                String quiz = quizInput.getText();

                try {
                    String[] createQuizArray = {course, quiz};

                    oos.flush();
                    oos.writeObject(new RequestObj("courseQuizCheck", createQuizArray));
                    oos.flush();

                    ResponseObj courseQuizCheckResponse = (ResponseObj) ois.readObject();

                    boolean courseQuizSuccess = courseQuizCheckResponse.isSuccess();

                    if (courseQuizSuccess) {
                        changeScoreInput.setVisible(true);

                        JOptionPane.showMessageDialog(
                                null,
                                "Enter a Students Username and the Question Number you would like to update " +
                                        "their score for!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        quizInput.setText("");
                        courseInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Course or quiz name is Invalid. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            if (e.getSource() == enterTVS) { //  If teacher wants to view submission of a quiz
                String userNameLocal = userNameInput.getText();
                String course = courseInput.getText();
                String quiz = quizInput.getText();

                try {
                    String[] createQuizArray = {course, quiz};

                    oos.flush();
                    oos.writeObject(new RequestObj("courseQuizCheck", createQuizArray));
                    oos.flush();

                    ResponseObj courseQuizCheckResponse = (ResponseObj) ois.readObject();

                    boolean courseQuizSuccess = courseQuizCheckResponse.isSuccess();
                    //  Variable stores success of user's login

                    if (courseQuizSuccess) {
                        String[] userNameArray = {userNameLocal, course, quiz};

                        oos.flush();
                        oos.writeObject(new RequestObj("teacherViewSubmission", userNameArray));
                        oos.flush();

                        ResponseObj response = (ResponseObj) ois.readObject();

                        boolean success = response.isSuccess(); //  Variable stores success of user's login
                        String score = response.quizScoreStr;

                        if (success) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    userNameLocal + "'s score is: " + score,
                                    "LMSQuizTool",
                                    JOptionPane.INFORMATION_MESSAGE);

                        } else {

                            JOptionPane.showMessageDialog(
                                    null,
                                    "This student has no submission for this quiz!",
                                    "LMSQuizTool",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        quizInput.setText("");
                        courseInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Course or quiz name is Invalid. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            if (e.getSource() == enterTVS2) { //  Second part of the Teacher viewing submissions of a quiz
                String course = courseInput.getText();
                String quiz = quizInput.getText();

                try {
                    String[] createQuizArray = {course, quiz};

                    oos.flush();
                    oos.writeObject(new RequestObj("courseQuizCheck", createQuizArray));
                    oos.flush();

                    ResponseObj courseQuizCheckResponse = (ResponseObj) ois.readObject();

                    boolean courseQuizSuccess = courseQuizCheckResponse.isSuccess();

                    if (courseQuizSuccess) {
                        userInput.setVisible(true);
//                        courseEnter.setVisible(false);

                        JOptionPane.showMessageDialog(
                                null,
                                "Please enter the Username of the Student Submission you would like to view!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        quizInput.setText("");
                        courseInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Course or quiz name is Invalid. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }


            }

            if (e.getSource() == viewStudentSubmissionList) { //  Switches to view submission frame
                teacherViewSubmissionList();
                teacherFrame.setVisible(false);
            }

            if (e.getSource() == changeScores) { //  Switches to change scores frame
                teacherChangeScores();
                teacherFrame.setVisible(false);
            }

            if (e.getSource() == settingsT) { //  switches to teacher account management frame
                settingsFrameT();
                teacherFrame.setVisible(false);
            }

            if (e.getSource() == createQuiz) { //  switches to create quiz frame
                teacherFrame.setVisible(false);
                teacherCreateQuiz();
            }

            if (e.getSource() == enterButtonT) { //  Enter button to change teacher's account details as required
                if (settingsChoiceTeacher == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Please select an option!",
                            "Choose an Option!", JOptionPane.ERROR_MESSAGE);
                } else {
                    String username = userInputTeacher.getText();
                    String change = changeInputTeacher.getText();
                    String choice = Integer.toString(settingsChoiceTeacher);

                    String[] changeAccDetails = {username, change, choice};

                    try {
                        oos.writeObject(new RequestObj("changeaccteacher", changeAccDetails));
                        oos.flush();

                        ResponseObj changeAccResp = (ResponseObj) ois.readObject();

                        if (changeAccResp.isSuccess() && settingsChoiceTeacher == 1) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "We are sorry to see you go!\n" + "Good Bye!",
                                    "LMSQuizTool",
                                    JOptionPane.INFORMATION_MESSAGE);
                            try {
                                oos.writeObject(new RequestObj("exit", null));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            closeGUI(settingsFrameT);

                        } else if (changeAccResp.isSuccess()) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Account details have been successfully changed!",
                                    "LMSQuizTool",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "An error occurred. Please try again later.",
                                    "LMSQuizTool",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }

            if (e.getSource() == addCourse) { //  switches to add course frame
                teacherCreateCourse();
                teacherFrame.setVisible(false);
            }

            if (e.getSource() == enterCreateCourse) { //  Enter button to create new course as required
                createCourseName = courseInputCC.getText();
                String[] createCourseArray = {createCourseName};

                try {
                    oos.writeObject(new RequestObj("createCourse", createCourseArray));
                    oos.flush();

                    ResponseObj createCourseResp = (ResponseObj) ois.readObject();

                    if (createCourseResp.isSuccess()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Course has been successfully created!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "An error occurred. Please try again later.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == editQuiz) { //  switches to edit quiz frame
                teacherFrame.setVisible(false);
                teacherEditQuiz();
            }

            if (e.getSource() == viewStudentSubmission) { //  switches to view student submission frame
                teacherViewSubmission();
                teacherFrame.setVisible(false);

            }

            if (e.getSource() == deleteQuiz) { //  switches to delete a quiz frame
                teacherDeleteQuiz();
                teacherFrame.setVisible(false);
            }

            if (e.getSource() == enterDelQuiz) { //  enter button to delete a quiz as required
                courseNameDelQuiz = courseInputDQ.getText();
                quizNameDelQuiz = quizInputDQ.getText();
                String[] requestQuizDelArray = {courseNameDelQuiz, quizNameDelQuiz};

                try {
                    oos.writeObject(new RequestObj("delQuiz", requestQuizDelArray));
                    oos.flush();

                    ResponseObj quizDeleteResp = (ResponseObj) ois.readObject();

                    if (quizDeleteResp.isSuccess()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Quiz has been successfully deleted!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "An error occurred. Please try again later.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == deleteCourse) { //  switches to delete course frame
                teacherDeleteCourse();
                teacherFrame.setVisible(false);
            }

            if (e.getSource() == enterDelCourse) { //  enter button to delete a course as required
                courseNameDelCourse = courseInputDC.getText();
                String[] requestCourseDelArray = {courseNameDelCourse};
                try {
                    oos.writeObject(new RequestObj("delCourse", requestCourseDelArray));
                    oos.flush();

                    ResponseObj courseDeleteResp = (ResponseObj) ois.readObject();

                    if (courseDeleteResp.isSuccess()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Course has been successfully deleted!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "An error occurred. Please try again later.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == enterCourse) { //  enter button in create quiz frame to capture parent course
                String course = courseInput.getText();
                String quiz = quizInput.getText();

                try {
                    String[] createQuizArray = {course, quiz};

                    oos.flush();
                    oos.writeObject(new RequestObj("createQuiz", createQuizArray));
                    oos.flush();

                    ResponseObj createQuizResponse = (ResponseObj) ois.readObject();

                    boolean createQuizSuccess = createQuizResponse.isSuccess();

                    if (createQuizSuccess) {
                        inputs.setVisible(true);

                        JOptionPane.showMessageDialog(
                                null,
                                "Please add your Quiz Questions!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        quizInput.setText("");
                        courseInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Course or quiz name is Invalid. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

            if (e.getSource() == enterTCQ) { // enter button to create question in a quiz
                //Send question to server
                String ques = questionInput.getText();
                String aOne = answerOneInput.getText();
                String aTwo = answerTwoInput.getText();
                String aThree = answerThreeInput.getText();
                String aFour = answerFourInput.getText();
                String aFive = answerFiveInput.getText();
                String corrAns = correctAnsInput.getText();
                String quesPts = questionPointsInput.getText();
                String course = courseInput.getText();
                String quiz = quizInput.getText();
                try {
                    String[] createQuesArray = {ques, aOne, aTwo, aThree, aFour, aFive, corrAns, quesPts, course, quiz};
                    oos.flush();
                    oos.writeObject(new RequestObj("createQuestion", createQuesArray));
                    oos.flush();

                    ResponseObj createQuestionResponse = (ResponseObj) ois.readObject();

                    boolean createQuestionSuccess = createQuestionResponse.isSuccess();
                    //  Variable stores success of user's creating a question

                    if (createQuestionSuccess) {
                        questionInput.setText("");
                        answerOneInput.setText("");
                        answerTwoInput.setText("");
                        answerThreeInput.setText("");
                        answerFourInput.setText("");
                        answerFiveInput.setText("");
                        correctAnsInput.setText("");
                        questionPointsInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Your Question has been added!\nPlease Enter another " +
                                        "question if you would like!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);


                    } else {
                        questionInput.setText("");
                        answerOneInput.setText("");
                        answerTwoInput.setText("");
                        answerThreeInput.setText("");
                        answerFourInput.setText("");
                        answerFiveInput.setText("");
                        correctAnsInput.setText("");
                        questionPointsInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Cannot add Question. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);

                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

            if (e.getSource() == enterTEQ) { //  enter button to edit question in a quiz
                //Send question to server
                String ques = questionInput.getText();
                String aOne = answerOneInput.getText();
                String aTwo = answerTwoInput.getText();
                String aThree = answerThreeInput.getText();
                String aFour = answerFourInput.getText();
                String aFive = answerFiveInput.getText();
                String corrAns = correctAnsInput.getText();
                String quesPts = questionPointsInput.getText();
                String course = courseInput.getText();
                String quiz = quizInput.getText();
                try {
                    String[] createQuesArr = {ques, aOne, aTwo, aThree, aFour, aFive, corrAns, quesPts, course, quiz};

                    oos.flush();
                    oos.writeObject(new RequestObj("createQuestion", createQuesArr));
                    oos.flush();

                    ResponseObj createQuestionResponse = (ResponseObj) ois.readObject();

                    boolean createQuestionSuccess = createQuestionResponse.isSuccess();

                    if (createQuestionSuccess) {
                        questionInput.setText("");
                        answerOneInput.setText("");
                        answerTwoInput.setText("");
                        answerThreeInput.setText("");
                        answerFourInput.setText("");
                        answerFiveInput.setText("");
                        correctAnsInput.setText("");
                        questionPointsInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Your Question has been added!\nPlease Enter another " +
                                        "question if you would like!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);


                    } else {
                        questionInput.setText("");
                        answerOneInput.setText("");
                        answerTwoInput.setText("");
                        answerThreeInput.setText("");
                        answerFourInput.setText("");
                        answerFiveInput.setText("");
                        correctAnsInput.setText("");
                        questionPointsInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Cannot add Question. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);

                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

            if (e.getSource() == enterTEQ2) { //  enter button to edit question in a quiz

                String course = courseInput.getText();
                String quiz = quizInput.getText();

                try {
                    String[] createQuizArray = {course, quiz};

                    oos.flush();
                    oos.writeObject(new RequestObj("editQuiz", createQuizArray));
                    oos.flush();

                    ResponseObj createQuizResponse = (ResponseObj) ois.readObject();

                    boolean createQuizSuccess = createQuizResponse.isSuccess();

                    if (createQuizSuccess) {
                        inputs.setVisible(true);

                        JOptionPane.showMessageDialog(
                                null,
                                "Please add your Quiz Questions!",
                                "LMSQuizTool",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        quizInput.setText("");
                        courseInput.setText("");

                        JOptionPane.showMessageDialog(
                                null,
                                "Course or quiz name is Invalid. Please try again.",
                                "LMSQuizTool",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception a) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An error occurred. Please try again later!",
                            "LMSQuizTool",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

            // Student Actions

            if (e.getSource() == settingsS) { //  switches to settings frame for student
                settingsFrameS();
                studentFrame.setVisible(false);
            }

            if (e.getSource() == enterButtonS) { //  sends student's updated account parameters to server
                if (settingsChoiceStudent == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Please select an option!",
                            "Choose an Option!", JOptionPane.ERROR_MESSAGE);
                } else {
                    String username = userInputStudent.getText();
                    String change = changeInputStudent.getText();
                    String choice = Integer.toString(settingsChoiceStudent);

                    String[] changeAccDetails = {username, change, choice};

                    try {
                        oos.writeObject(new RequestObj("changeaccstudent", changeAccDetails));
                        oos.flush();

                        ResponseObj changeAccResp = (ResponseObj) ois.readObject();

                        if (changeAccResp.isSuccess() && settingsChoiceStudent == 1) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "We are sorry to see you go!\n" + "Good Bye!",
                                    "LMSQuizTool",
                                    JOptionPane.INFORMATION_MESSAGE);
                            try {
                                oos.writeObject(new RequestObj("exit", null));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            closeGUI(settingsFrameS);

                        } else if (changeAccResp.isSuccess()) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Account details have been successfully changed!",
                                    "LMSQuizTool",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "An error occurred. Please try again later.",
                                    "LMSQuizTool",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }

            if (e.getSource() == takeQuiz) { //  switches to take quiz frame
                studentTakeQuiz();
                studentFrame.setVisible(false);
            }

            if (e.getSource() == enterTakeQuiz) { //  enter button to send user answer to server
                String course = courseInputSTQ.getText();
                String quiz = quizInputSTQ.getText();

                String[] studentTakeQuizLocal = {course, quiz};

                try {
                    oos.writeObject(new RequestObj("takeQuiz", studentTakeQuizLocal));
                    oos.flush();

                    ResponseObj quizQuesResp = (ResponseObj) ois.readObject();

                    if (!quizQuesResp.isSuccess()) {
                        JOptionPane.showMessageDialog(null,
                                "Course or Quiz does not exist!",
                                "Take a Quiz",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        String[] quizListQues = quizQuesResp.getQuiz();

                        ArrayList<String> quizSub = new ArrayList<>();
                        quizSub.add(userName);
                        quizSub.add(course);
                        quizSub.add(quiz);

                        int totalPointsOfQuiz = 0;
                        int userCorrectAnswers = 0;

                        for (int i = 0; i < (Objects.requireNonNull(quizListQues).length);
                             i += 8) {
                            String answer = JOptionPane.showInputDialog(
                                    null,
                                    quizListQues[i] +
                                            "\n Please enter the option number of your choice.\n" +
                                            "1) " + quizListQues[i + 1] + "\n" +
                                            "2) " + quizListQues[i + 2] + "\n" +
                                            "3) " + quizListQues[i + 3] + "\n" +
                                            "4) " + quizListQues[i + 4] + "\n" +
                                            "5) " + quizListQues[i + 5] + "\n",
                                    quiz,
                                    JOptionPane.QUESTION_MESSAGE);

                            quizSub.add(quizListQues[i]);
                            if (answer.equals(quizListQues[i + 6])) {
                                totalPointsOfQuiz += Integer.parseInt(quizListQues[i + 7]);
                                userCorrectAnswers += Integer.parseInt(quizListQues[i + 7]);
                                quizSub.add(quizListQues[i + 7]);
                            } else {
                                totalPointsOfQuiz += Integer.parseInt(quizListQues[i + 7]);
                                quizSub.add("0");
                            }
                        }

                        quizSub.add(3, Integer.toString(totalPointsOfQuiz));

                        String[] submission = new String[quizSub.size()];
                        submission = quizSub.toArray(submission);
                        oos.writeObject(new RequestObj("submitQuiz", submission));

                        ResponseObj submitQuizResp = (ResponseObj) ois.readObject();

                        if (submitQuizResp.isSuccess()) {
                            JOptionPane.showMessageDialog(null,
                                    "Quiz Has Been Submitted!",
                                    "Success!",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "An error occurred.",
                                    "Quiz Submission",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == viewScores) { //  switches to view scores frame
                studentViewScores();
                studentFrame.setVisible(false);
            }

            if (e.getSource() == viewCourses) {

                String[] courseList;

                try {
                    oos.writeObject(new RequestObj("viewCourses", null));
                    oos.flush();

                    ResponseObj courseViewResp = (ResponseObj) ois.readObject();

                    if (!courseViewResp.isSuccess()) {
                        JOptionPane.showMessageDialog(null,
                                "There was an error. Please try again later.",
                                "View Courses",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        courseList = new String[courseViewResp.getCourseList().size()];
                        courseList = courseViewResp.getCourseList().toArray(courseList);

                        StringBuilder temp = new StringBuilder();

                        for (int i = 0; i < courseList.length; i++) {
                            temp.append(courseList[i]).append("\n");
                        }

                        JOptionPane.showMessageDialog(null,
                                "The following courses are available to you:\n" + temp.toString(),
                                "View Courses",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == enterViewQuiz) { //  Enter button to display student's individual score in quiz
                String course = courseInputSVQ.getText();
                String quiz = quizInputSVQ.getText();

                String[] studentViewQuizLocal = {userName, course, quiz};

                String[] viewQuiz;

                try {
                    oos.writeObject(new RequestObj("viewQuiz", studentViewQuizLocal));
                    oos.flush();

                    ResponseObj quizViewResp = (ResponseObj) ois.readObject();

                    if (!quizViewResp.isSuccess()) {
                        JOptionPane.showMessageDialog(null,
                                "You may not have taken this quiz yet!",
                                "View a Quiz",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        viewQuiz = quizViewResp.getQuizScore();
                        StringBuilder display = new StringBuilder();
                        for (int i = 0; i < viewQuiz.length; i++) {
                            if (i % 2 == 0) {
                                display.append(viewQuiz[i]).append("\n");
                            } else {
                                display.append("Score: ").append(viewQuiz[i]).append("\n");
                            }
                        }
                        JOptionPane.showMessageDialog(null,
                                display.toString(),
                                "Viewing Scores for " + quiz,
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    };

    public static void closeGUI(JFrame frame) { //  Static method to close frame it is called upon
        frame.dispose();
    }
}