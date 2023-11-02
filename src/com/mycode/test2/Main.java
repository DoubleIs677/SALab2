package com.mycode.test2;

public class Main {
    public static void main(String[] args) {
        Input input = new Input();
        input.input("D:\\small project\\IdeaProjects\\input.txt");
        Shift shift = new Shift(input.getLineTxt());
        shift.shift();
        Alphabetizer alphabetizer = new Alphabetizer(shift.getKwicList());
        alphabetizer.sort();
        Output output = new Output(alphabetizer.getKwicList());
        output.output("D:\\small project\\IdeaProjects\\output.txt");

    }
}
