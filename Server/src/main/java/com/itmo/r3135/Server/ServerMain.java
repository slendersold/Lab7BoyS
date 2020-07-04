package com.itmo.r3135.Server;

import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.BindException;

/**
 * @author vsevolod
 */
public class ServerMain {
    private static final Logger logger = LogManager.getLogger("ServerStarter");

    public static void main(String[] args) throws IOException {

        logger.info("The program started.");
        Scanner input = new Scanner(System.in);
        logger.info("The port reader started.");
        while (true) {
            logger.info("To start the server, enter the port or 'exit' to exit the program.");
            System.out.print("//: ");
            if (!input.hasNextLine()) {
                break;
            }
            String inputString = input.nextLine();
            if (inputString.equals("exit")) {
                logger.info("The program has completed.");
                System.exit(0);
            } else {
                try {
                    int port = Integer.parseInt(inputString);
                    if (port < 0 || port > 65535) {
                        logger.error("Wrong port!");
                        logger.error("Port is a number from 0 to 65535");
                    } else {
                        ServerWorker worker = new ServerWorker(port);
                        if (worker.SQLInit("localhost", 5432, "postgres", "postgres", "New_Romantic"))
                            try {
                                worker.startWork();
                            } catch (BindException e) {
                                logger.error("The port is busy.");
                            }
                        else {
                            logger.fatal("INITIALIZATION ERROR! CAN'T CONNECT TO POSTGRES DATABASE!");
                            System.exit(0);
                        }
                        break;
                    }
                } catch (NumberFormatException e) {
                    logger.error("Invalid number format in '" + inputString + "' !");
                } catch (BindException e) {
                    logger.error("The port " + inputString + " is busy.");
                }
            }
        }
    }
}

