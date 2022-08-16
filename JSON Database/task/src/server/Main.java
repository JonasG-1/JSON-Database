package server;

import java.util.Scanner;

public class Main {

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String DELETE = "delete";
    private static final String EXIT = "exit";
    private static final String OK = "OK";
    private static final String ERR = "ERROR";
    private static final String[] database = new String[100];


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            String command = scanner.next();
            if (command.equals(EXIT)) {
                exit = true;
            }
            if (isCommand(command)) {
                String indexString = scanner.next();
                int index = 0;
                try {
                    index = Integer.parseInt(indexString) - 1;
                } catch (Exception ignored) {
                }
                if (index < 0 || index > 99) {
                    printStatus(false);
                    command = "";
                }
                switch (command) {
                    case SET -> {
                        String data = scanner.nextLine();
                        if (!data.isEmpty()) {
                            data = data.substring(1);
                        }
                        set(index, data);
                    }
                    case GET -> get(index);
                    case DELETE -> delete(index);
                }
            }
        }
    }

    private static boolean isCommand(String command) {
        return command.equals(SET) || command.equals(GET) || command.equals(DELETE);
    }

    private static void set(int index, String data) {
        database[index] = data;
        printStatus(true);
    }

    private static void get(int index) {
        String data = database[index];
        printStatus(data == null ? ERR : data);
    }

    private static void delete(int index) {
        database[index] = null;
        printStatus(true);
    }

    private static void printStatus(boolean success) {
        System.out.println(success ? OK : ERR);
    }

    private static void printStatus(String data) {
        System.out.println(data);
    }
}
