package game;

import objects.Finish;
import objects.Start;
import objects.X;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


public class Application {

    public static X minedX = new X();

    public static int startValue = ThreadLocalRandom.current().nextInt(1,3);

    public static int startValue2 = ThreadLocalRandom.current().nextInt(1,3);

    public static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Object[][] gameBoard = gameBoardCreator();
        boardInitializer(gameBoard);
        initialMenu(gameBoard);
    }

    public static Object[][] gameBoardCreator() throws IOException {
        String valueWidth;
        String valueHeight;
        int Rows;
        int Cols;

        String width = Files.readAllLines(Paths.get("resources/enemy_territory")).get(0);
        String[] widthSplitter = width.split("=");
        valueWidth = widthSplitter[1];
        Cols = Integer.parseInt(valueWidth);

        String height = Files.readAllLines(Paths.get("resources/enemy_territory")).get(1);
        String[] heightSplitter = height.split("=");
        valueHeight = heightSplitter[1];
        Rows = Integer.parseInt(valueHeight);
        Object[][] board = new Object[Cols][Rows];

        return board;
    }

    public static void fillBoard(Object[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                board[row][col] = X.getX();
            }
        }
    }

    public static void renderGameBoard(Object[][] board){
        for(int i=0;i<board.length;i++){
            for(int j=0; j<board.length;j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public static void setStartAndFinish(Object[][] board){
        boolean settingStartAndFinish = true;
        while(settingStartAndFinish) {
            int lowestValue = 0;
            int biggestValue = board.length;
            int value3 = ThreadLocalRandom.current().nextInt(1,3);
            int value4 = ThreadLocalRandom.current().nextInt(1,3);

            if(startValue == 1 && startValue2 == 1){
                board[lowestValue][lowestValue] = Start.getStart();
            } else if(startValue == 2 && startValue2 == 2){
                board[biggestValue-1][biggestValue-1] = Start.getStart();
            } else if(startValue == 2 && startValue2 == 1){
                board[biggestValue-1][lowestValue] = Start.getStart();
            } else if(startValue == 1 && startValue2 == 2){
                board[lowestValue][biggestValue-1] = Start.getStart();
            }

            if(value3 == startValue || value4 == startValue2){
                fillBoard(board);
                continue;
            } else if(value3 == 2 && value4 == 2){
                board[biggestValue-1][biggestValue-1] = Finish.getFinish();
            } else if(value3 == 2 && value4 == 1){
                board[biggestValue-1][lowestValue] = Finish.getFinish();
            } else if(value3 == 1 && value4 == 2){
                board[lowestValue][biggestValue-1] = Finish.getFinish();
            } else if(value3 == 1 && value4 == 1){
                board[lowestValue][lowestValue] = Finish.getFinish();
            }
            settingStartAndFinish=false;
        }
    }

    public static void mineSetUp(Object[][] board) throws IOException {
        String valueMines;
        int numberOfMines;
        String mines = Files.readAllLines(Paths.get("resources/enemy_territory")).get(2);
        String[] mineSplitter = mines.split("=");
        valueMines = mineSplitter[1];
        numberOfMines = Integer.parseInt(valueMines);
        boolean mineSettingUp = true;
        while (mineSettingUp) {
        for (int i = 0; i < numberOfMines; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, board.length);
            int random2 = ThreadLocalRandom.current().nextInt(0, board.length);
            if (board[random][random2].equals(Start.getStart())) {
                i--;
            } else if (board[random][random2].equals(Finish.getFinish())) {
                i--;
            } else {
                board[random][random2] = minedX;
            }
        }
            mineSettingUp = false;
        }
    }

    public static void boardInitializer(Object[][] board) throws IOException {
        fillBoard(board);
        setStartAndFinish(board);
        renderGameBoard(board);
        mineSetUp(board);
    }

    public static void initialMenu(Object[][] board) throws IOException {
        initialMenuRender();
        int selector = input.nextInt();
        switch (selector){
            case 3:
                movingSystem(board);
                renderGameBoard(board);
                initialMenu(board);
            case 2:
                String numberOfDefuses = Files.readAllLines(Paths.get("resources/configurations")).get(1);
                String[] numberOfDefusesSplitter = numberOfDefuses.split("=");
                String valueDefuses = numberOfDefusesSplitter[1];
                int numberForDefuses = Integer.parseInt(valueDefuses);
                defuser(board,numberForDefuses);
                numberForDefuses--;
                initialMenu(board);
                break;
            case 1:
                String numberOfProbes = Files.readAllLines(Paths.get("resources/configurations")).get(0);
                String[] numberOfProbesSplitter = numberOfProbes.split("=");
                String valueProbes = numberOfProbesSplitter[1];
                int numberForAnalyzes = Integer.parseInt(valueProbes);
                analyzer(board,numberForAnalyzes);
                numberForAnalyzes--;
                renderGameBoard(board);
                initialMenu(board);
                break;
            default:System.out.println("Invalid option try again");
            initialMenu(board);
        }
    }

    public static void initialMenuRender(){
        System.out.println();
        System.out.println("1.Analyze for mines");
        System.out.println("2.Defuse");
        System.out.println("3.Move to a new position on the battlefield");
    }

    public static void analyzer(Object[][] board,int number) throws IOException {
        if(number == 0){
            System.out.println("No more tries at analyzing available");
            initialMenu(board);
        }

        if(board[startValue -1][startValue2 -1].equals(minedX)){
            board[startValue -1][startValue2 -1] = " Y ";
        } else {
            board[startValue -1][startValue2 -1] = " N ";
        }

        if(board[startValue -1][startValue2].equals(minedX)){
            board[startValue -1][startValue2] = " Y ";
        } else {
            board[startValue -1][startValue2] = " N ";
        }

        if(board[startValue -1][startValue2 +1].equals(minedX)){
            board[startValue -1][startValue2 +1] = " Y ";
        } else {
            board[startValue -1][startValue2 +1] = " N ";
        }

        if(board[startValue][startValue2 -1].equals(minedX)){
            board[startValue][startValue2 -1] = " Y ";
        } else {
            board[startValue][startValue2 -1] = " N ";
        }

        if(board[startValue][startValue2 +1].equals(minedX)){
            board[startValue][startValue2 +1] = " Y ";
        } else {
            board[startValue][startValue2 +1] = " N ";
        }

        if(board[startValue +1][startValue2 -1].equals(minedX)){
            board[startValue +1][startValue2 -1] = " Y ";
        } else {
            board[startValue +1][startValue2 -1] = " N ";
        }

        if(board[startValue +1][startValue2].equals(minedX)){
            board[startValue +1][startValue2] = " Y ";
        } else {
            board[startValue +1][startValue2] = " N ";
        }

        if(board[startValue +1][startValue2 +1].equals(minedX)){
            board[startValue +1][startValue2 +1] = " Y ";
        } else {
            board[startValue +1][startValue2 +1] = " N ";
        }
        board[startValue][startValue2] = " S ";
    }

    public static void defuser(Object[][] board,int number) throws IOException {
        if(number == 0){
            System.out.println("No more tries at defusing available");
            initialMenu(board);
        }
        System.out.println("Select first coordinate");
        int selector = input.nextInt();
        System.out.println("Select second coordinate");
        int selector2 = input.nextInt();
        board[selector][selector2] = X.getX();
    }

    public static void movingSystem(Object[][] board) throws IOException {

        System.out.println("Select your coordinates");
        System.out.println("Select first coordinate");
        int firstValueOfStartingPosition = input.nextInt();
        System.out.println("Select second coordinate");
        int secondValueOfStartingPosition = input.nextInt();
        board[firstValueOfStartingPosition][secondValueOfStartingPosition] = " X ";

        System.out.println("Select the coordinates you wish to move to");
        System.out.println("Select first coordinate");
        int firstValueOfFinishingPosition = input.nextInt();
        System.out.println("Select second coordinate");
        int secondValueOfFinishingPosition = input.nextInt();
        if(board[firstValueOfFinishingPosition][secondValueOfFinishingPosition] == minedX){
            System.out.println("Game over");
            System.exit(0);
        }
        if(board[firstValueOfFinishingPosition][secondValueOfFinishingPosition] == Finish.getFinish()){
            System.out.println("You win");
            System.exit(0);
        }
        board[firstValueOfFinishingPosition][secondValueOfFinishingPosition] = " * ";
    }
}