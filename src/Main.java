import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        // Получаем массив с данными для расчета вида [0, 0, значение знака, значение системы]
        int[] exampleArraysAfterCheck = checkInput(inputFromConsole());
        // Запускаем массив в метод расчета
        String preEndSolution = solutionOfExample(exampleArraysAfterCheck);
        if((exampleArraysAfterCheck[3] == 2) && (Integer.parseInt(preEndSolution) < 1)){
            throw new Exception("Выражение с использованием римских цифр не может быть отрицательным");
        }
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
                "или римскими (I,V,X) цифрами.");
        String inputFromUser = sc.nextLine();
        System.out.println("Вы ввели: " + inputFromUser);
        return inputFromUser;
    }

    // метод проверки ввода с консоли, получаем массив после проверки вида [0, 0, значение знака, значение системы]
    static int[] checkInput(String inputFromUser) {
        int[] exampleArraysAfterCheck = null;
        String[] stringFirst = inputFromUser.split(""); // Создаем массив по всем знакам из строки
        // делаем проверку на наличие и количество арифметических знаков
        try {
            int countSign = 0;  // создаем счетчик количества арифметических знаков
            for (String st : stringFirst) {
                Pattern pattern = Pattern.compile("[+\\-*/]");  // Ищем все арифметические знаки
                boolean findSign = st.matches(String.valueOf(pattern));
                if (findSign) {     // Если есть знак, увеличиваем счетчик
                    countSign++;
                } else {
                    // Если знака нет, продолжаем проверку
                    continue;
                }
                if (countSign < 1) {
                    // Проверяем количество арифметических знаков в массиве
                    continue;
                } else if (countSign == 1) {
                    // Если знак в единственном числе
                    continue;
                } else {
                    throw new Exception("Слишком много арифметических знаков");
                }
            }
            // Начинаем проверку на наличие римских или арабских цифр через отдельный метод
            exampleArraysAfterCheck = findRimOrArab(stringFirst);
            // Проверяем массив на систему: если арабская - передаем дальше, если римская - переводим в арабскую
            // Запускаем метод проверки
            int[] exampleAfterAOS = checkArrayOnSystem(exampleArraysAfterCheck, inputFromUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return exampleArraysAfterCheck;
    }

    // Метод определения системы (арабская или римская)
    static int[] findRimOrArab(String[] stringFirst) {
        // Создаем массив на 4 значения для передачи значений дальше для решения
        int[] exampleArrays = new int[] {0, 0, 0, 0};
        int c = 0;  // Значение арифметического знака: 1+, 2-, 3*, 4/.
        int d = 0;  // Значение системы: 1 - арабские цифры, 2 - римские цифры
        // Цикл определения арифметического знака
        for (String st : stringFirst) {
            Pattern pattern = Pattern.compile("[+\\-*/]");  // Ищем все арифметические знаки
            boolean findSign = st.matches(String.valueOf(pattern));
            // Ищем знак
            if (findSign) {
                switch (st) {
                    case "+" -> exampleArrays[2] = 1;
                    case "-" -> exampleArrays[2] = 2;
                    case "*" -> exampleArrays[2] = 3;
                    case "/" -> exampleArrays[2] = 4;
                }
            }
        }
        // Ищем римские цифры
        for (String st : stringFirst) {
            Pattern patternOtherSigns = Pattern.compile("[^+\\-*/IVX\\d]");  // Ищем все лишние знаки
            boolean findSign = st.matches(String.valueOf(patternOtherSigns));
            try {
                if (findSign) {
                    // Проверяем на недопустимые символы
                    throw new Exception("Присутствуют недопустимые символы");
                } else {
                    // Проверяем на все арабские знаки
                    Pattern patternArabSigns = Pattern.compile("[+\\-*/\\d]");
                    boolean findArabSign = st.matches(String.valueOf(patternArabSigns));
                    try {
                        if(findArabSign){
                            exampleArrays[3] = 1;
                        }else {
                            for (String stRim : stringFirst) {
                                Pattern patternRimSigns = Pattern.compile("[+\\-*/IVX]");
                                boolean findRimSign = stRim.matches(String.valueOf(patternRimSigns));
                                if (findRimSign) {
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
        return exampleArrays;
    }

    // Метод проверки на римские/арабские цифры
    static int[] checkArrayOnSystem(int[] exampleArraysAfterCheck, String inputFromUser) throws Exception {
        int[] exampleAfterAOS = exampleArraysAfterCheck;
        int signInt = exampleArraysAfterCheck[2];
        String signString = null;
        if(signInt == 1) signString = "\\+";
        else if(signInt == 2) signString = "-";
        else if(signInt == 3) signString = "\\*";
        else if(signInt == 4) signString = "/";
        try{
            String[] arrayOfSummond = inputFromUser.split(signString);
            int check = exampleArraysAfterCheck[3];
            if(check == 1){
                exampleArraysAfterCheck[0] = Integer.parseInt(arrayOfSummond[0]);
                exampleArraysAfterCheck[1] = Integer.parseInt(arrayOfSummond[1]);
            }else if(check == 2){
                // вызов метода конвертации
                int[] converted = convertRimToArab(arrayOfSummond);
                exampleArraysAfterCheck[0] = converted[0];
                exampleArraysAfterCheck[1] = converted[1];
            }
        } catch (Exception e) {
            throw new RuntimeException("Неправильный формат ввода");
        }
        return exampleAfterAOS;
    }

    // Метод конвертации, получает String массив из двух римских знаков, возвращает int массив из двух чисел
    static int[] convertRimToArab(String[] arrayOfSummond) throws Exception {
        int[] converted = new int[2];
        String aR = arrayOfSummond[0];
        converted[0] = convertSymbol(aR);
        String bR = arrayOfSummond[1];
        converted[1] = convertSymbol(bR);
        return converted;
    }

    // метод конвертации римских чисел до 10
    static int convertSymbol(String stRim) throws Exception {
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
        return Integer.parseInt(stRim);
    }

    // метод подсчета арабских цифр
    static String solutionOfExample(int[] arraysEnd) throws Exception {
        int a =  arraysEnd[0];
        int b =  arraysEnd[1];
        int c =  arraysEnd[2];
        int end = 0;
        if ((a<=10) && (a>=0)){
            if ((b<=10) && (b>=0)){
                switch (c) {
                    case 1 -> end = a+b;
                    case 2 -> end = a-b;
                    case 3 -> end = a*b;
                    case 4 -> end = a/b;
                }
            }else throw new Exception("Неподходящее значение знаменателя");
        }else throw new Exception("Неподходящее значение знаменателя");
        return Integer.toString(end);
    }

    // Метод конвертации из арабских в римские числа
    static String convertArabToRim(String preEndSolution){
        int preConvert = Integer.parseInt(preEndSolution);
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