import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        // Получаем массив с данными для расчета вида [0, 0, значение знака, значение системы]
        int[] exampleArraysAfterCheck = checkInput(inputFromConsole());
        // Проверена система и найден знак
        System.out.println("Массив для расчета - "+Arrays.toString(exampleArraysAfterCheck));
        // Запускаем массив в метод расчета
        String preEndSolution = solutionOfExample(exampleArraysAfterCheck);
        System.out.println(preEndSolution);
        String endSolution = null;
        if(exampleArraysAfterCheck[3] == 2){
            // Запускаем метод конвертации из арабских в римские числа
            endSolution = convertArabToRim(preEndSolution);
        }
        else {
            endSolution = preEndSolution;
        }
        System.out.println("Окончательный ответ: "+endSolution);
    }

    // метод ввода с консоли
    static String inputFromConsole() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите числовое выражение арабскими (1,2,3...) " +
                "или римскими (I,V,X...) цифрами.");
        String inputFromUser = sc.nextLine();
        System.out.println("Вы ввели: " + inputFromUser);
        return inputFromUser;
    }

    // метод проверки ввода с консоли, получаем массив после проверки вида [0, 0, значение знака, значение системы]
    static int[] checkInput(String inputFromUser) {
        int[] exampleArraysAfterCheck = null;
        String[] stringFirst = inputFromUser.split(""); // Создаем массив по всем знакам из строки
        System.out.println(stringFirst.getClass() + "awdawdawdawdad" + Arrays.toString(stringFirst));
        // делаем проверку на наличие и количество арифметических знаков
        try {
            int countSign = 0;  // создаем счетчик количества арифметических знаков
            for (String st : stringFirst) {
                System.out.println("Проверяемый знак из массива - " + st);
                Pattern pattern = Pattern.compile("[+\\-*/]");  // Ищем все арифметические знаки
                boolean findSign = st.matches(String.valueOf(pattern));
                System.out.println(findSign);
                if (findSign) {     // Если есть знак, увеличиваем счетчик
                    countSign++;
                    System.out.println("count - " + countSign);
                } else {            // Если знака нет, продолжаем проверку
                    System.out.println("Знак на этом этапе цикла арифметический знак отсутствует");
                }
                if (countSign < 1) {    // Проверяем количество арифметических знаков в массиве
                    System.out.println("Знак присутствует - " + countSign + " раз.");
                } else if (countSign == 1) {    // Если знак в единственном числе
                    System.out.println("Знак присутствует - 1 раз.");
                } else {
                    System.out.println("Знак присутствует - " + countSign + " раза или более.");
                    throw new Exception("Слишком много арифметических знаков");
                }
                System.out.println("Поиск наличия знака закончен.");
            }
            // Начинаем проверку на наличие римских или арабских цифр через отдельный метод
            exampleArraysAfterCheck = findRimOrArab(stringFirst);
            System.out.println("exampleArraysAfterCheck "+Arrays.toString(exampleArraysAfterCheck));
            System.out.println(exampleArraysAfterCheck.getClass());
            // Проверяем массив на систему: если арабская - передаем дальше, если римская - переводим в арабскую
            // Запускаем метод проверки
            int[] exampleAfterAOS = checkArrayOnSystem(exampleArraysAfterCheck, inputFromUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return exampleArraysAfterCheck;
    }

    static int[] findRimOrArab(String[] stringFirst) {
        // Создаем массив на 4 значения для передачи значений дальше для решения
        int[] exampleArrays = new int[] {0, 0, 0, 0};
        System.out.println("Входная строка в массиве - " + Arrays.toString(stringFirst));
        int c = 0;  // Значение арифметического знака: 1+, 2-, 3*, 4/.
        int d = 0;  // Значение системы: 1 - арабские цифры, 2 - римские цифры
        // Цикл определения арифметического знака
        for (String st : stringFirst) {
            System.out.println("Проверяемый знак из массива - " + st);
            Pattern pattern = Pattern.compile("[+\\-*/]");  // Ищем все арифметические знаки
            boolean findSign = st.matches(String.valueOf(pattern));
            System.out.println(findSign);
            // Ищем знак
            if (findSign) {
                switch (st) {
                    case "+" -> {
                        exampleArrays[2] = 1;
                    }
                    case "-" -> {
                        exampleArrays[2] = 2;
                    }
                    case "*" -> {
                        exampleArrays[2] = 3;
                    }
                    case "/" -> {
                        exampleArrays[2] = 4;
                    }
                }
            }
            System.out.println("Арифметический знак - " + c);
        }
        // Ищем римские цифры
        for (String st : stringFirst) {
            System.out.println("Проверяемый знак из массива - " + st);
            Pattern patternOtherSigns = Pattern.compile("[^+\\-*/IVX\\d]");  // Ищем все лишние знаки
            boolean findSign = st.matches(String.valueOf(patternOtherSigns));
            try {
                if (findSign) {
                    // Проверяем на недопустимые символы
                    throw new Exception("Присутствуют недопустимые символы");
                } else {
                    // Проверяем на все арабские знаки
                    System.out.println("Продолжаем проверку строки на арабские цифры и арифметические знаки");
                    Pattern patternArabSigns = Pattern.compile("[+\\-*/\\d]");
                    boolean findArabSign = st.matches(String.valueOf(patternArabSigns));
                    try {
                        if(findArabSign){
                            System.out.println("Присутствуют только арабские цифры и арифметический знак");
                            System.out.println("---Проверяем следующий символ");
                            exampleArrays[3] = 1;
                        }else {
                            System.out.println("В строке присутствуют римские цифры, проверяем массив заново");
                            for (String stRim : stringFirst) {
                                Pattern patternRimSigns = Pattern.compile("[+\\-*/IVX]");
                                boolean findRimSign = stRim.matches(String.valueOf(patternRimSigns));
                                if (findRimSign) {
                                    System.out.println("Присутствуют только римские цифры и арифметический знак");
                                    System.out.println("---Проверяем следующий символ");
                                    exampleArrays[3] = 2;
                                }else {
                                    throw new Exception("Присутствуют арабские и римские цифры");
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Arrays.toString(exampleArrays));
        return exampleArrays;
    }

    static int[] checkArrayOnSystem(int[] exampleArraysAfterCheck, String inputFromUser) throws Exception {
        int[] exampleAfterAOS = exampleArraysAfterCheck;
        int signInt = exampleArraysAfterCheck[2];
        System.out.println(signInt);
        String signString = null;
        if(signInt == 1){
            signString = "\\+";
        }
        else if(signInt == 2){
            signString = "-";
        }
        else if(signInt == 3){
            signString = "\\*";
        }
        else if(signInt == 4){
            signString = "/";
        }
        System.out.println("awdawdawdawdawdawdawddw"+Arrays.toString(exampleArraysAfterCheck));
        String[] arrayOfSummond = inputFromUser.split(signString);
        System.out.println("arrayOfSummond"+Arrays.toString(arrayOfSummond));
        System.out.println(arrayOfSummond[0]);
        System.out.println(arrayOfSummond[1]);
        int check = exampleArraysAfterCheck[3];
        if(check == 1){
            System.out.println("Используются только арабские цифры, передаем массив дальше");
            exampleArraysAfterCheck[0] = Integer.parseInt(arrayOfSummond[0]);
            exampleArraysAfterCheck[1] = Integer.parseInt(arrayOfSummond[1]);
            System.out.println("Массив в подсчет - "+Arrays.toString(exampleArraysAfterCheck));
        }else if(check == 2){
            System.out.println("Используются только римские цифры, передаем массив на конвертацию в арабские цифры");
            System.out.println("Массив римских цифр для конвертации - "+Arrays.toString(arrayOfSummond));
            // метод конвертации
            int[] converted = convertRimToArab(arrayOfSummond);
            exampleArraysAfterCheck[0] = converted[0];
            exampleArraysAfterCheck[1] = converted[1];
        }

        return exampleAfterAOS;
    }

    // Метод конвертации, получает String массив из двух римских знаков, возвращает int массив из двух чисел
    static int[] convertRimToArab(String[] arrayOfSummond) throws Exception {
        int[] converted = new int[2];
        System.out.println("Массив в конверторе - "+Arrays.toString(arrayOfSummond));

        String aR = arrayOfSummond[0];
        System.out.println(aR+aR.getClass());
        converted[0] = convertSymbol(aR);
        String bR = arrayOfSummond[1];
        System.out.println(bR+bR.getClass());
        converted[1] = convertSymbol(bR);

        System.out.println("Массив после конвертации - "+Arrays.toString(converted));
        return converted;
    }

    // метод конвертации римских чисел до 10
    static int convertSymbol(String stRim) throws Exception {
        System.out.println("Символ до конвертации - "+stRim);
        switch (stRim) {
            case "I" -> stRim = "1";
            case "II" -> stRim = "2";
            case "III" -> stRim = "3";
            case "IV" -> stRim = "4";
            case "V" -> stRim = "5";
            case "VI" -> stRim = "6";
            case "VII" -> stRim = "7";
            case "VIII" -> stRim = "8";
            case "IX" -> stRim = "9";
            case "X" -> stRim = "10";
            default -> throw new Exception("Неподходящая цифра");
        }
        System.out.println("Символ после конвертации - "+stRim);
        return Integer.parseInt(stRim);
    }

    // метод подсчета арабских цифр
    static String solutionOfExample(int[] arraysEnd){
        System.out.println(Arrays.toString(arraysEnd));
        int a =  arraysEnd[0];
        int b =  arraysEnd[1];
        int c =  arraysEnd[2];
        int end = 0;
        switch (c) {
            case 1 -> {
                end = a+b;
            }
            case 2 -> {
                end = a-b;
            }
            case 3 -> {
                end = a*b;
            }
            case 4 -> {
                end = a/b;
            }
        }
        return Integer.toString(end);
    }

    // Метод конвертации из арабских в римские числа
    static String convertArabToRim(String preEndSolution){
        System.out.println("preEndSolution - "+preEndSolution);
        int preConvert = Integer.parseInt(preEndSolution);
        System.out.println("preConvert в int - "+preConvert);
        String endString = null;
        int remainder = preConvert % 10;
        int predecades = preConvert / 10;
        String strAM = beforeTen(remainder);
        String decades = null;
        if(predecades == 10) decades = "C";
        else if (predecades == 9) decades = "XC";
        else if (predecades == 8) decades = "LXXX";
        else if (predecades == 7) decades = "LXX";
        else if (predecades == 6) decades = "LX";
        else if (predecades == 5) decades = "L";
        else if (predecades == 4) decades = "XL";
        else if (predecades == 3) decades = "XXX";
        else if (predecades == 2) decades = "XX";
        else if (predecades == 1) decades = "X";
        else if (predecades == 0) decades = "";
        if (strAM == null) endString = decades;
        else endString = decades + strAM;
        System.out.println(preConvert+" "+endString+" ");

        return endString;
    }

    // метод конвертации чисел до 10
    static String beforeTen(Integer remainder){
        String beforeTen = null;
        if(remainder == 1) beforeTen = "I";
        else if (remainder == 2) beforeTen = "II";
        else if (remainder == 3) beforeTen = "III";
        else if (remainder == 4) beforeTen = "IV";
        else if (remainder == 5) beforeTen = "V";
        else if (remainder == 6) beforeTen = "VI";
        else if (remainder == 7) beforeTen = "VII";
        else if (remainder == 8) beforeTen = "VIII";
        else if (remainder == 9) beforeTen = "IX";
        return beforeTen;
    }
}

// получаем массив чисел int из строки и знака с присвоением ему числа int
// [слагаемое, слагаемое, знак]
//    static int[] findSummands(String inputExample){
//        String[] arraySummands = inputExample.split("\\D");
//        int a = Integer.parseInt(arraySummands[0]);
//        int b = Integer.parseInt(arraySummands[1]);
//        String[] arraySign = inputExample.split("\\d*");
//        String str = arraySign[2];
//        int c = 0;
//        switch (str) {
//            case "+" -> {
//                c = 1;
//            }
//            case "-" -> {
//                c = 2;
//            }
//            case "*" -> {
//                c = 3;
//            }
//            case "/" -> {
//                c = 4;
//            }
//        }
//        int[] storage = {a, b, c};
//        return storage;
//    }

//        try{
//            int numOfChar = stringFirst.length;
//            System.out.println("Длина массива "+numOfChar);
//
//            if((numOfChar >= 3) && (numOfChar <= 5)){
//                System.out.println("Достаточно знаков");
//            }
//            else {
//                throw new Exception("Слишком много или слишком мало символов");
//            }
//            int countSign = 0;
//            for (String st : stringFirst) {
//                System.out.println(st);
//                Pattern pattern = Pattern.compile("[+\\-*/]");
//                boolean findSign = st.matches(String.valueOf(pattern));
//                System.out.println(findSign);
//                System.out.println("awdawdawd"+findSign);
//                if(findSign){
//                    countSign++;
//                    System.out.println("count - "+countSign);
//                }
//                else {
//                    System.out.println("Знак на этом этапе цикла отсутствует");
//                }
//                if(countSign < 1){
//                    System.out.println("Знак присутствует - "+countSign+" раз.");
//                }
//                else if(countSign == 1){
//                    System.out.println("Знак присутствует - 1 раз.");
//                }
//                else{
//                    System.out.println("Знак присутствует - "+countSign+" раз(а).");
//                    throw new Exception("Слишком много арифметических знаков");
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Проверка пройдена");
//        return inputFromUser;
//    }

