package com.example.calculator_final;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Counters implements Parcelable {

    private String counter1;
    private String counter2;
    private String counter3;
    private String counter4;

    public Counters() {

        counter1 = "";
        counter2 = "";
        counter3 = "";
        counter4 = "";
    }

    protected Counters(Parcel in) {
        counter1 = in.readString();
        counter2 = in.readString();
        counter3 = in.readString();
        counter4 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(counter1);
        dest.writeString(counter2);
        dest.writeString(counter3);
        dest.writeString(counter4);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Counters> CREATOR = new Creator<Counters>() {
        @Override
        public Counters createFromParcel(Parcel in) {
            return new Counters(in);
        }

        @Override
        public Counters[] newArray(int size) {
            return new Counters[size];
        }
    };

    public String getCounter1() {
        return counter1;
    }

    public void setCounter1(String s) {
        this.counter1 = s;
    }

    public void setCounter1() {
        String expText = brackets(this.counter2);

        while (expText.charAt(0) == '(' && expText.charAt(expText.length() - 1) == ')') {
            expText = expText.substring(1, expText.length() - 1);
        }

        if (this.counter1.equals("")) {
            this.counter1 += expText + this.counter3;
        } else {
            this.counter1 += "\n" + expText + this.counter3;
        }
        this.counter2 = getCounter3().substring(1);

    }

    public String getCounter2() {
        return counter2;
    }

    public void setCounter2(String counter2) {

        if (counter2.equals("del")) {
            this.counter2 = "";
            this.counter3 = "";
            return;
        }

        if (counter2.equals("bk")) {
            if (getCounter2().length() == 1) {
                this.counter2 = "";
                this.counter3 = "";
                return;
            }
            this.counter2 = this.counter2.substring(0, this.counter2.length() - 1);
            counter2 = "";

            if (getCounter2().charAt(getCounter2().length() - 1) > '9' ||
                    getCounter2().charAt(getCounter2().length() - 1) < '0') {

                for (int i = getCounter2().length() - 1; i >= 0; i--) {
                    if (getCounter2().charAt(i) <= '9' && getCounter2().charAt(i) >= '0') {
                        String resultCounter2 = getCounter2().substring(0, i + 1);
                        sum(resultCounter2);
                        return;
                    }
                }
                this.counter2 = "";
                this.counter3 = "";
                return;
            }
        }

        this.counter2 += counter2;
        switch (counter2) {
            case "+":
            case "-":
            case "×":
            case "÷":
            case "=":
            case "(":
            case ")":
                break;
            default:
                sum(this.counter2);
        }

    }

    private void sum(String _counter2) {
        List<Lexeme> lexemes = lexAnalyze(brackets(_counter2));
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        double result = expr(lexemeBuffer);
        if (getCounter4().equals("♾")) {
            setCounter3(getCounter4());
            setCounter4("");
        } else {
            BigDecimal decimal = new BigDecimal(result);
            result = Double.parseDouble(String.valueOf(decimal.setScale(4, RoundingMode.HALF_DOWN)));
            setCounter3(new DecimalFormat("#.####").format(result));
        }
    }

    public String getCounter3() {
        return counter3;
    }

    public void setCounter3(String counter3) {
        this.counter3 = "=" + counter3;
    }

    public String getCounter4() {
        return counter4;
    }

    public void setCounter4(String counter4) {
        this.counter4 = counter4;
    }

    public String brackets(String expText) {
        int l = 0;
        int r = 0;
        for (int i = 0; i < expText.length(); i++) {
            if (expText.charAt(i) == '(') {
                l++;
            }
            if (expText.charAt(i) == ')') {
                r++;
            }
        }
        if (l > r) {
            StringBuilder expTextBuilder = new StringBuilder(expText);
            for (int i = 0; i < l - r; i++) {
                expTextBuilder.append(')');
            }
            expText = expTextBuilder.toString();
        }
        return expText;
    }

    public List<Lexeme> lexAnalyze(String expText) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        while (pos < expText.length()) {
            char c = expText.charAt(pos);
            switch (c) {
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, c));
                    pos++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, c));
                    pos++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.OP_PLUS, c));
                    pos++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.OP_MINUS, c));
                    pos++;
                    continue;
                case '×':
                case '*':
                    lexemes.add(new Lexeme(LexemeType.OP_MUL, c));
                    pos++;
                    continue;
                case '÷':
                case '/':
                    lexemes.add(new Lexeme(LexemeType.OP_DIV, c));
                    pos++;
                    continue;
                default:
                    if (c <= '9' && c >= '0' || c == ',' || c == '.') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            if (c == ',') {
                                sb.append('.');
                            } else {
                                sb.append(c);
                            }
                            pos++;
                            if (pos >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(pos);
                        } while (c <= '9' && c >= '0' || c == ',');
                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected character: " + c);
                        }
                        pos++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }


    public Double expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return (double) 0;
        } else {
            lexemes.back();
            return plusMinus(lexemes);
        }
    }

    public Double plusMinus(LexemeBuffer lexemes) {
        double value = multiDiv(lexemes);
        if (getCounter4().equals("♾")) {
            return value;
        }
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OP_PLUS:
                    value += multiDiv(lexemes);
                    break;
                case OP_MINUS:
                    value -= multiDiv(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public Double multiDiv(LexemeBuffer lexemes) {
        double value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OP_MUL:
                    value *= factor(lexemes);
                    break;
                case OP_DIV:
                    double vel = factor(lexemes);
                    if (vel != 0) {
                        value /= vel;
                        break;
                    } else {
                        setCounter4("♾");
                    }
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public Double factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case NUMBER:
                return Double.valueOf(lexeme.value);
            case LEFT_BRACKET:
                Double value = expr(lexemes);
                lexemes.next();
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value
                        + " at position: " + lexemes.getPos());
        }
    }
}