/**
 * Questions
 *
 * This is a class for Question objects that will be created in LMSQuizClient/Server.
 *
 * @author ArchitVarunSahu, JakobWachter
 * @version 1.0, (2022-04-26)
 */


public class Questions {
    private String question;
    private int correctAnswer;
    private String[] answerChoices;
    private int questionPoints;

    public Questions(String question, int correctAnswer, String[] answerChoices, int questionPoints) {
        this.correctAnswer = correctAnswer;
        this.question = question;
        this.answerChoices = answerChoices;
        this.questionPoints = questionPoints;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(String[] answerChoices) {
        this.answerChoices = answerChoices;
    }

    public int getQuestionPoints() {
        return questionPoints;
    }

    public void setQuestionPoints(int questionPoints) {
        this.questionPoints = questionPoints;
    }
}
