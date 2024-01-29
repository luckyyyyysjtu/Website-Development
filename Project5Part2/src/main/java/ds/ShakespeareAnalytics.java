/**
 * Author: Yi Guo
 * Andrew ID: yiguo
 * <p>
 * ShakespeareAnalytics is a Java Spark program that performs analytics on a All's Well That Ends Well text file.
 * It uses Apache Spark to analyze various aspects of the text, including the number of lines, words, distinct words,
 * symbols, distinct symbols, distinct letters, and allows the user to search for specific words within the text.
 * The program is executed from the command line with the file path of the text as an argument.
 * <p>
 * The program consists of methods for counting lines, words, symbols, distinct words, distinct symbols, and distinct letters,
 * as well as a method for searching for a specific word within the text.
 * <p>
 * SparkConf and JavaSparkContext are used to configure and initialize the Spark environment.
 * JavaRDD (Resilient Distributed Datasets) is used to represent the distributed collection of lines from the input text file.
 * <p>
 * The main method takes the file path as a command-line argument, initializes the Spark context, performs analytics, and displays the results.
 * <p>
 * The analytics include:
 * - Counting the number of lines in the text.
 * - Counting the number of words and distinct words.
 * - Counting the number of symbols and distinct symbols.
 * - Counting the number of distinct letters.
 * - Allowing the user to search for a specific word within the text. (case-sensitive)
 */

package ds;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ShakespeareAnalytics {
    // variables to store the analysis results
    private static long nLines;
    private static long nWords;
    private static long nDistinctWords;
    private static long nSymbols;
    private static long nDistinctSymbols;
    private static long nDistinctLetters;

    /**
     * Initialize Spark, read from files and perform Shakespeare analytics on the specified text file.
     * The analytics include:
     * - Counting the number of lines in the text.
     * - Counting the number of words and distinct words.
     * - Counting the number of symbols and distinct symbols.
     * - Counting the number of distinct letters.
     * - Allowing the user to search for a specific word within the text. (case-sensitive)
     *
     * @param fileName The file path of the text.
     */
    private static void shakespeareAnalytics(String fileName) {

        // Configure Spark and initialize Spark context
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Shakespeare Analytics");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // Read the text file into a JavaRDD
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);

        // Perform analytics on the text file
        // Task 0: Count lines
        countLines(inputFile);
        // Task 1,2: Count words and distinct words
        countWords(inputFile);
        // Task 3,4: Count symbols and distinct symbols
        countSymbols(inputFile);
        // Task 5: Count distinct letters
        countLetters(inputFile);

        // Print out the analysis statistics to the console
        System.out.println("Number of lines: " + nLines);
        System.out.println("Number of words: " + nWords);
        System.out.println("Number of distinct words: " + nDistinctWords);
        System.out.println("Number of symbols: " + nSymbols);
        System.out.println("Number of distinct symbols: " + nDistinctSymbols);
        System.out.println("Number of distinct letters: " + nDistinctLetters);

        // Task 6: Allow user to interactively search a word and show all lines that contain the word
        search(inputFile);
    }

    /**
     * Counts the number of elements in a JavaRDD.
     *
     * @param file The JavaRDD for which to count elements.
     * @return The count of elements in the JavaRDD.
     */
    private static long count(JavaRDD<String> file) {
        return file.count();
    }

    /**
     * Counts the number of distinct elements in a JavaRDD.
     *
     * @param file The JavaRDD for which to count distinct elements.
     * @return The count of distinct elements in the JavaRDD.
     */
    private static long countDistinct(JavaRDD<String> file) {
        return file.distinct().count();
    }

    /**
     * Counts the number of lines in the text file.
     *
     * @param inputFile The JavaRDD representing the lines of the text file.
     */
    private static void countLines(JavaRDD<String> inputFile) {
        // Assign the count to the object variable
        nLines = count(inputFile);
    }

    /**
     * Counts the number of words and distinct words in the text file.
     *
     * @param inputFile The JavaRDD representing the lines of the text file.
     */
    private static void countWords(JavaRDD<String> inputFile) {
        // Split the line using regex [^a-zA-Z]+ and apply flatMap to get the words
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split("[^a-zA-Z]+")));
        // Filter out empty string
        wordsFromFile = wordsFromFile.filter(k -> (!k.isEmpty()));

        // Assign the word count to the object variable
        nWords = count(wordsFromFile);
        // Assign the distinct word count to the object variable
        nDistinctWords = countDistinct(wordsFromFile);
    }

    /**
     * Counts the number of symbols and distinct symbols in the text file.
     *
     * @param inputFile The JavaRDD representing the lines of the text file.
     */
    private static void countSymbols(JavaRDD<String> inputFile) {
        // Split the line using regex "" and apply flatMap to get the symbols
        JavaRDD<String> symbolsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split("")));

        // Assign the symbol count to the object variable
        nSymbols = count(symbolsFromFile);
        // Assign the distinct symbol count to the object variable
        nDistinctSymbols = countDistinct(symbolsFromFile);
    }

    /**
     * Counts the number of distinct letters in the text file.
     *
     * @param inputFile The JavaRDD representing the lines of the text file.
     */
    private static void countLetters(JavaRDD<String> inputFile) {
        // Replace none English chars with empty string and split with regex "", then apply flatmap to get the letters
        JavaRDD<String> lettersFromFile = inputFile.flatMap(content -> Arrays.asList(content.replaceAll("[^a-zA-Z]", "").split("")));
        // Filter out empty strings
        lettersFromFile = lettersFromFile.filter(k -> (!k.isEmpty()));

        // Assign the distinct letter count to the object variable
        nDistinctLetters = countDistinct(lettersFromFile);
    }

    /**
     * Allows the user to search for a specific word within the text.
     * Print out the lines that contain the word.
     * This is a case-sensitive search.
     *
     * @param inputFile The JavaRDD representing the lines of the text file.
     */
    private static void search(JavaRDD<String> inputFile) {
        // Give instructions for user to enter a word
        System.out.println("Enter a word to search for from the file: ");
        // Read the input from user
        Scanner s = new Scanner(System.in);
        String searchWord = s.nextLine();

        // Filter lines containing the search word
        List<String> linesAfterSearch = inputFile.filter(k -> (k.contains(searchWord))).collect();

        // Loop through the result list and print out the lines
        System.out.println("Search Result:");
        for (String line : linesAfterSearch) {
            System.out.println(line);
        }
    }

    /**
     * Entry point for the program. Executes Shakespeare analytics on the specified text file.
     *
     * @param args Command-line arguments. Expects the file path of the All's Well That Ends Well text.
     */
    public static void main(String[] args) {

        // Check if a file path is provided
        if (args.length == 0) {
            // If no, exit and print information
            System.out.println("No files provided.");
            System.exit(0);
        }
        // Execute Shakespeare analytics on the specified text file
        shakespeareAnalytics(args[0]);
    }
}