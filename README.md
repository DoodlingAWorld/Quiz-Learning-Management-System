# Quiz Learning Management System

Just a small project a few of us worked on towards the end of the semester. Simple multithreaded and concurrent program that uses GUIs to interact with the user. User's can create an account and add Courses and Quizzes as a teacher. Or they can take quizzes and view their scores as a student. Program creates a local server for the client to connect with.

To compile and run the program, download all of the following files. To run the main program:
      - First, run the server named LMSQuizServer.
      - Then, in a new terminal, run LMSQuizClient. 
      [NOTE: Some of the GUI screens during the program may not display all components due to varying System parameters. Please resize windows to ensure all panels and textfields are showing]

## Method List and Description

### User Class
The user class is used to create Student and Teacher object when somebody logs into our program. This class has three variables which are:

```java
private String username; 
private String password; 
private int typeOfUser;
```

These variables are used in a constructor which initializes these values to their specified values:

```java
public User(String username, String password, int typeOfUser) { 
      this.username = username; 
      this.password = password; 
      this.typeOfUser = typeOfUser; 
}
```

This constructor is used in the Student and Teacher methods to generate new Teacher and Student objects. This class also has getters and setters for the three variables used. Each getter method returns the current value of each variable and each setter object sets the value of each variable to whatever the input parameter is.

### Student Class
The Student class is used to create Student objects which assist in saving peoples login information. The Student class extends User and has two private variables:

```java
private String username; 
private String password;
```

These variables are used in a constructor that creates a new Student object with the variables from User along with the Username and Password from its own class.

```java
public Student(String username, String password) { 
      super(username, password, 2); 
}
```

The teacher constructor has a userType of 2, which lets us know that the user is a Student. This student class is very helpful for creating and identifying Students along with their login info. It also allows us to modify students info when necessary.

### Teacher Class
The Teacher class is used to create Teacher objects and this class also assists in saving Teachers login information. The Teacher class also extends User and has two of its own private variables:

```java
private String username; 
private String password;
```

These variables are used in a constructor along with the three variables from User which creates a new Teacher object with the specified parameters.

```java
public Teacher(String username, String password) { 
    super(username, password, 1); 
}
```

This Teacher class has a userType of 1 which lets us know that the user is a teacher. The Teacher class is very helpful for identifying Teachers along with their login info. It also allows us to easily modify account details of Teachers who choose to do so.

### Questions Class
The Questions class is used to create new Quizzes. The questions class has four private variables:

```java
private String question; 
private int correctAnswer; 
private String[] answerChoices; 
private int questionPoints;
```

These variables contain everything necessary for one Quiz question. This helps us later with displaying quizzes for the user to take, and It helps with saving the quiz questions into a list to generate a quiz. The constructor for this class initializes these four variables to their specified input value:

```java
public Questions(String question, int correctAnswer, String[] answerChoices, int questionPoints) { 
      this.correctAnswer = correctAnswer; 
      this.question = question; 
      this.answerChoices = answerChoices; 
      this.questionPoints = questionPoints; 
}
```

The Questions class is used to generate a list of question objects to be put into a quiz object. This helps to keep our quizzes and their corresponding questions organized and easy to locate for display.

### Quiz Class
The Quiz class is used to create Quiz objects whenever a Teacher creates a new quiz. This class has two private variables:

```java
private String quizName; 
private Questions[] quizQuestions;
```

These variables represent the name of the quiz being created and the list of questions in this specific quiz. The constructor for this class generates a new Quiz object and initializes these two variables to their corresponding input values:

```java
public Quiz(String quizName, Questions[] quizQuestions) { 
      this.quizName = quizName; 
      this.quizQuestions = quizQuestions; 
}
```
This class also has getters and setters for these two variables. Each getter returns the current value of the specified variable, and each setter sets the value of the specified value to the input parameter value.

### RequestObj Class
The LMSQuizClient class sends objects of the RequestObj class to the server, which triggers a response. It consists of a checkString, which informs the server of the type of request being made, and a String[] data array that contains the data required for the Server to carry out its process.

This class implements Serializable.

#### Class Fields
```java
public String checkString;
public String[] data;
```

An object of the RequestObj class is made with the following constructor:
```java
public RequestObj(String checkString, String[] data) {
        this.checkString = checkString;
        this.data = data;
}
```
This class also contains the default accessor and mutator methods for the fields, and a toString method:
```java
public String getCheckString()
public void setCheckString(String checkString)
public String[] getData()
public void setData(String[] data)

public String toString()
```

### ResponseObj Class
The LMSQuizServer class creates an object of the ResponseObj class whenever it needs to send a response back to the client. The class contains of several overloaded constructors, each of which represents a different form of response from the Server. I will begin by listing the class fields, and then going through the constructors.

#### Class Fields
```java
public boolean success; //  Declaring a variable that tells whether operation requested was a success or not
public String username; //  Declaring a variable to hold the username of the current user
public String[] quizScore; //  Declaring a variable to hold quiz score
public String[] quiz; //  Declaring a variable to hold quiz questions
public String quizScoreStr; //  Declaring a variable to hold quiz score
public String[] userNames; //  Declaring a variable to hold userNames
public ArrayList<String> courseList; //  Declaring an array list to hold course names
```
#### Constructors

##### success (basic constructor to inform client on success of their requested operations)
```java
public ResponseObj(boolean success) { //  Constructor to return modified value of success variable
        this.success = success;
        this.username = null;
}
```

##### quizQuestions (constructor to return the quiz questions of requested quiz)
```java
public ResponseObj(boolean success, String[] quiz) { //  Constructor to return quiz questions
        this.success = success;
        this.username = null;
        this.quiz = quiz;
}
```

##### quizScore (constructor to return the quiz score for a requested user)
```java
public ResponseObj(boolean success, String username, String[] quizScore) { //  Constructor to return quiz score
        this.success = success;
        this.username = username;
        this.quizScore = quizScore;
}
```

##### quizScoreStr (constructor to return the quiz scores as a String)
```java
public ResponseObj(boolean success, String quizScoreStr) { //  Constructor to send quiz score string
        this.success = success;
        this.quizScoreStr = quizScoreStr;
}
```

##### courseList (constructor to return the list of courses)
```java
public ResponseObj(boolean success, ArrayList<String> courseList) { //  Constructor to return a list of courses
        this.success = success;
        this.courseList = courseList;
}
```

For the fields, an assortment of default accessors and mutators are also provided:
```java
public ArrayList<String> getCourseList()
public void setCourseList(ArrayList<String> courseList)
public boolean isSuccess()
public void setSuccess(boolean success)
public String getUsername()
public void setUsername(String username)
public String[] getQuizScore()
public void setQuizScore(String[] quizScore)
public String[] getQuiz()
public void setQuiz(String[] quiz)
public String getQuizScoreStr()
public void setQuizScoreStr(String quizScoreStr)
public String[] getUserNames()
public void setUserNames(String[] userNames)
```

### LMSQuizClient Class
The LMSQuizClient class is the client class for this project. It contains all the GUIs, and basic logic code, that is used to communicate with the server. No reading or writing to files occurs in this class, but basic data structures (arrays, arrayLists etc) are employed for temporary storage in this class.

Each frame occupies its own method, and is linked to each other with buttons. The loginFrame GUI acts as the starting point in the run() method, creating links with other frames and providing the base frame for users.

I'm going to begin by listing all the class variables and their associated user.

#### Class Fields
```java
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
```

Our main method serves to only initialise and get a thread running:
```java
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
```

Our run method initiates a connectiong with the server, initialises the oos and ois objects created earlier, and jump starts the program by calling the loginFrame() method.
```java
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
```

Following the run method, several methods follow. Each method represents the functionality of a frame. All methods are also grouped together with which account they belong to in the project.

##### Frame methods
```java
public void loginFrame()

//  Main menu frames
public void teacherFrame()
public void studentFrame()

//  Settings Frames
public void settingsFrameT()
public void settingsFrameS()

//  Teacher Functions
public void teacherViewSubmissionList()
public void teacherViewSubmission()
public void teacherChangeScores()
public void teacherCreateQuiz()
public void teacherEditQuiz()
public void teacherCreateCourse()
public void teacherDeleteQuiz()
public void teacherDeleteCourse()

//  Student Functions
public void studentTakeQuiz()
public void studentViewScores()
```

Following these methods, a WindowListener method is created that is meant to close the frame it is called on, as well as inform the server that the user has logged out.

```java
void addWL (JFrame frame) { //  Closes frame when cross button closed    }
```

The main logic of our code on the client sides resides within the action listeners for each button. Once interacted with, the button sends an object of the RequestObj class to the server, which receives this object and replies with an object of the ResponseObj class. Based on the object sent back, the ActionListener decides what to display, such as whether the operation was a success or not.

Some operations, such as viewing submissions from the teachers point of view, or creating and editing quizzes, are split over multiple action listeners to prevent the EDT from freezing.

Below is a list of actionListeners that we use to accomplish user requests on the client side:
```java
// Login/Create Account Actions
if (e.getSource() == login)
if (e.getSource() == createAccount)

//  If Exit Button Clicked
if (e.getSource() == exit)
if (e.getSource() == logOut)

// Teacher Actions
if (e.getSource() == changeScores) //  Switches to the teacherChangeScores() frame
if (e.getSource() == enterTCS) //  If Teacher wishes to change the quiz score of a student
if (e.getSource() == enterTCS2) //  Second action listener that completes the teacher's request of changing the individual score of a student

if (e.getSource() == viewStudentSubmissionList) //  Switches to the teacherViewSubmissionList() frame
if (e.getSource() == enterTVSL) //  If Teacher wishes to view overall submission list for a quiz

if (e.getSource() == enterTVS) //  If teacher wishes to view the score of an individual student on a quiz
if (e.getSource() == enterTVS2) //  Second action listener that completes the teacher's request of viewing the score of an individual student on a quiz

if (e.getSource() == settingsT) //  Switches to the settingsFrameT() frame
if (e.getSource() == enterButtonT) //  Submits teacher's account changes to the server

if (e.getSource() == createQuiz) //  Switches to the teacherCreateQuiz() frame
if (e.getSource() == enterTCQ) //  Creates new quiz as specified by teacher

if (e.getSource() == editQuiz) //  Switches to the teacherEditQuiz() frame
if (e.getSource() == enterTEQ) //  Edits the selected quiz as specified by teacher
if (e.getSource() == enterTEQ2) //  Second action listener that completes the teacher's request of editing a quiz

if (e.getSource() == addCourse) //  Switches to the teacherCreateCourse() frame
if (e.getSource() == enterCreateCourse) //  If teacher wants to create a new course

if (e.getSource() == deleteCourse) //  Switches to the teacherDeleteCourse() frame
if (e.getSource() == enterDelCourse) //  Deletes the course selected by the teacher

if (e.getSource() == deleteQuiz) //  Switches to the teacherDeleteQuiz() frame
if (e.getSource() == enterDelQuiz) //  Deletes the quiz chosen by the teacher

// Student Actions
// Student Account Management
if (e.getSource() == settingsS)
if (e.getSource() == enterButtonS)

// Student Taking a Quiz
if (e.getSource() == takeQuiz)
if (e.getSource() == enterTakeQuiz)

// Student viewing their scores
if (e.getSource() == viewScores)
if (e.getSource() == enterViewQuiz)

// Student viewing the courses available to them
if (e.getSource() == viewScores)

```


### LMSQuizServer Class
The LMSQuizServer receives an object of the RequestObj class from the client, and uses the information provided to carry out the users request. This class is where any reading and writing from a file occurs. All access to files are synchronized to an object.

```java
public void convertToObjectsArray(String[] list, int typeOfUser) 
public static String[] readFile(String filename) throws FileNotFoundException 
public boolean changeUsername(String userName, String newUsername, int typeOfUser)
public boolean changePassword(String userName, String newPassword, int typeOfUser) 
public boolean deleteAccount(String userName, int typeOfUser) 
public static boolean createCourse(String courseName) 
public static boolean deleteCourse(String courseName) 
public static boolean delQuiz(String courseName, String quizName)
public static boolean courseQuizExist(String course, String quiz)
public static String[] takeQuizConversion(String course, String quiz)
public static boolean quizSubmission(String user, String course, String quiz, String totalPts, String userScore)
public static boolean quizSubmissionPersonal(String user, String course, String quiz, String[] questionScore)
public static void addUserQuiz(String user, String course, String quiz)
public static boolean checkUserTakenQuiz(String user, String course, String quiz)
public static String[] viewQuizConversion(String user, String course, String quiz)
public static boolean quizEditCheck(String courseName, String quizName)
public static boolean quizCheck(String courseName, String quizName)
public static boolean addQuestion(String courseName, String quizName, String question, String answerOne,
                                      String answerTwo, String answerThree, String answerFour, String answerFive,
                                      String correctAns, String questionPoints)
public static String getScore(String courseName, String quizName, String userName)
public static String[] getSubmissionList(String courseName, String quizName)
public static boolean changeScores(String courseName, String quizName, String userName, int questionNum,
                                       String questionChange)
```

#### convertToObjectsArray
This method takes in a list of either Teacher or Student login information and creates an array of objects of whatever user type the list is. This method is useful in editing peoples login information and for validating that a persons login exists.

#### readFile
This method takes in the name of a file and either creates a new file with that name, or reads the contents of the specified file and returns a list of strings which hold the data stored in that file. This method is useful for analyzing the data in files and for validating that certain words or lines are in the file being analyzed.

#### changeUsername
This method is pretty self-explanatory because this method changes the username of a user. This method takes in a current username, the username the user wants to change to, and the type of user. The method checks to see that the current username exists in our storage of usernames, and the new username overrides this old username as long as there are no exceptions.

#### changePassword
This method is also self explanatory because it changes the password of a user. This method takes in a current username, the password the user wants to change to, and the type of user. The method checks to see that the current username exists in our storage of usernames, and the new password overrides this old password for that user as long as there are no exceptions.

#### deleteAccount
This method is used to delete the account of a user. This method will remove a users login info from the database if it meets the requirements.

#### createCourse
This method creates a course for quizzes to be created in. This method validates that the input string is not already a created. If it is not already created it will create a new course with the name set to the input string.

#### deleteCourse
This method deletes a course. This method validates that the course exists by checking the input string. If the course does exist, the method will remove the course from the database.

#### delQuiz
This method is used to delete a quiz. If a teacher wishes to delete a quiz, the teacher must enter the name of the course the quiz is in and the name of the quiz. If the course and quiz exist, the quiz will be deleted and can no longer be accessed by students.

#### courseQuizExist
This method checks whether the course and quiz specified exists. If either one does not exist, a boolean false is returned.

#### takeQuizConversion
This method converts the quiz questions stored in a file into a formatted array that will be provided to the client.

#### quizSubmission
This method submits the user's attempt for a quiz to its respect quiz submission file.

#### quizSubmissionPersonal
This method submits the user's attempt for a quiz to his dedicated submission file for that specific quiz. This file only records the user's latest attempt for a quiz, and not their highest score

#### addUserQuiz
This method adds the user's username to the dedicated submissions file for that quiz, which contains a list of all the students who have attempted that quiz.

#### checkUserTakenQuiz
This method checks whether the user has made an attempt on the quiz specified.

#### viewQuizConversion
This method converts the user's personal submission file for a specified quiz into a formatter String array that will be displayed to the user.

#### quizEditCheck
This method checks for the whether the quiz in a specified course can be edited.

#### quizCheck
This method checks for whether a quiz of the same name already exists

#### addQuestion
This method is how the teacher inputs multiple questions into a single quiz.

#### getScore
This method returns a String that contains the quiz score for the specified username.

#### getSubmissionList
This method returns a String[] containing the list of all user submissions for a specified quiz

#### changeScores
This method allows a teacher to change the scores of a specified student who has taken a quiz.

## Program Description

The LMSQuizClient begins by first creating a connection with LMSQuizServer before displaying the loginGUI. Once the user successfully logs in, depending on their user type they are greeted with varying frames. Since this program is not sequential, multiple local users can connect to the server, which uses a while loop and ArrayList of LMSQuizServer objects to continuously accept new connections. Since all file reading and writing is concurrent, information is continuously being updated whenever a user switches to a new frame. This removes the need for a refresh button.
