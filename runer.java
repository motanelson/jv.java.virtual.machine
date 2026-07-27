import java.io.*;
import java.util.*;

public class runer {

    static int[] stack = new int[256];
    static int[] locals = new int[256];

    static String[] code = new String[1024];

    static int sp = 0;
    static int codeSize = 0;


    //=================================================

    static void push(int x) {
        stack[sp++] = x;
    }

    static int pop() {
        sp--;
        return stack[sp];
    }


    //=================================================

    static void iadd() {
        int b = pop();
        int a = pop();

        push(a + b);
    }


    static void isub() {
        int b = pop();
        int a = pop();

        push(a - b);
    }


    static void imul() {
        int b = pop();
        int a = pop();

        push(a * b);
    }


    static void idiv() {
        int b = pop();
        int a = pop();

        push(a / b);
    }


    //=================================================

    static void invokeVirtual(String line) {

        if (line.contains("PrintStream.println")) {
            System.out.println(pop());
        }

    }


    //=================================================

    static void loadMain(String fileName)
            throws Exception {

        BufferedReader br =
                new BufferedReader(
                        new FileReader(fileName));

        String line;

        boolean insideMain = false;

        while ((line = br.readLine()) != null) {

            line = line.trim();

            if (line.contains("main")) {
                insideMain = true;
                continue;
            }

            if (insideMain) {

                if (line.equals("}"))
                    break;

                if (line.contains("{"))
                    continue;

                if (line.contains("stack"))
                    continue;

                if (line.length() < 2)
                    continue;

                code[codeSize++] = line;

            }

        }

        br.close();

    }


    //=================================================

    static void execute() {

        for (int i = 0; i < codeSize; i++) {

            String line = code[i].trim();

            line = line.replace(";", "");


            //--------------------------------------


            if (line.contains("iconst_0"))
                push(0);

            else if (line.contains("iconst_1"))
                push(1);

            else if (line.contains("iconst_2"))
                push(2);

            else if (line.contains("iconst_3"))
                push(3);

            else if (line.contains("iconst_4"))
                push(4);

            else if (line.contains("iconst_5"))
                push(5);


            //--------------------------------------


            else if (line.contains("bipush")) {

                String value =
                        line.substring(
                                line.indexOf("bipush") + 7
                        ).trim();

                push(Integer.parseInt(value));

            }


            //--------------------------------------


            else if (line.contains("iload_")) {

                String value =
                        line.substring(
                                line.indexOf("iload_") + 6
                        ).trim();

                int n = Integer.parseInt(value);

                push(locals[n]);

            }


            //--------------------------------------


            else if (line.contains("istore_")) {

                String value =
                        line.substring(
                                line.indexOf("istore_") + 7
                        ).trim();

                int n = Integer.parseInt(value);

                locals[n] = pop();

            }


            //--------------------------------------


            else if (line.equals("iadd")) {
                iadd();
            }

            else if (line.equals("isub")) {
                isub();
            }

            else if (line.equals("imul")) {
                imul();
            }

            else if (line.equals("idiv")) {
                idiv();
            }


            //--------------------------------------


            else if (line.contains("invokevirtual")) {

                invokeVirtual(line);

            }


            //--------------------------------------


            else if (line.equals("return")) {

                System.out.println();
                System.out.println("program finished.");

                return;
            }


        }

    }


    //=================================================

    public static void main(String[] args)
            throws Exception {


        Scanner sc = new Scanner(System.in);


        System.out.println("\033c\033[47;30m\ngive me file .jasm ?");
        System.out.println();

        String fileName = sc.nextLine();


        File f = new File(fileName);

        if (!f.exists()) {
            System.out.println("file not found.");
            return;
        }


        loadMain(fileName);


        System.out.println();
        System.out.println("----- EXECUTION -----");
        System.out.println();


        execute();

    }

}
