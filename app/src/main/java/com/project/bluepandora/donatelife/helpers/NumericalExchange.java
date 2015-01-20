package com.project.bluepandora.donatelife.helpers;

public class NumericalExchange {
    private static final char E_0 = '0';
    private static final char E_1 = '1';
    private static final char E_2 = '2';
    private static final char E_3 = '3';
    private static final char E_4 = '4';
    private static final char E_5 = '5';
    private static final char E_6 = '6';
    private static final char E_7 = '7';
    private static final char E_8 = '8';
    private static final char E_9 = '9';

    private static final char B_0 = '০';
    private static final char B_1 = '১';
    private static final char B_2 = '২';
    private static final char B_3 = '৩';
    private static final char B_4 = '৪';
    private static final char B_5 = '৫';
    private static final char B_6 = '৬';
    private static final char B_7 = '৭';
    private static final char B_8 = '৮';
    private static final char B_9 = '৯';

    public static String toBanglaNumerical(int time) {
        return toBanglaNumerical(Integer.toString(time));
    }

    public static String toBanglaNumerical(String digits) {
        String newDigits = "";
        for (char digit : digits.toCharArray()) {
            switch (digit) {
                case E_0:
                    newDigits += B_0;
                    break;
                case E_1:
                    newDigits += B_1;
                    break;
                case E_2:
                    newDigits += B_2;
                    break;
                case E_3:
                    newDigits += B_3;
                    break;
                case E_4:
                    newDigits += B_4;
                    break;
                case E_5:
                    newDigits += B_5;
                    break;
                case E_6:
                    newDigits += B_6;
                    break;
                case E_7:
                    newDigits += B_7;
                    break;
                case E_8:
                    newDigits += B_8;
                    break;
                case E_9:
                    newDigits += B_9;
                    break;
                default:
                    newDigits += digit;
            }
        }
        return newDigits;
    }

    public static String toEnglishNumerical(String digits) {
        String newDigits = "";
        for (char digit : digits.toCharArray()) {
            switch (digit) {
                case B_0:
                    newDigits += E_0;
                    break;
                case B_1:
                    newDigits += E_1;
                    break;
                case B_2:
                    newDigits += E_2;
                    break;
                case B_3:
                    newDigits += E_3;
                    break;
                case B_4:
                    newDigits += E_4;
                    break;
                case B_5:
                    newDigits += E_5;
                    break;
                case B_6:
                    newDigits += E_6;
                    break;
                case B_7:
                    newDigits += E_7;
                    break;
                case B_8:
                    newDigits += E_8;
                    break;
                case B_9:
                    newDigits += E_9;
                    break;
                default:
                    newDigits += digit;
            }
        }
        return newDigits;
    }
}
