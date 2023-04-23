package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class TransactionsCsvFileManager implements StringConvertableObjectsCsvImportAndExport{
    private final File transactionsDataFile;

    public TransactionsCsvFileManager(File file){
        if(file == null){
            throw new IllegalArgumentException("Parameter 'file' cannot be null");
        }

        String fileName = file.getName();
        String fileExtension = "";
        if(fileName.contains(".")){
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        if(!fileExtension.equals("csv")){
            throw new IllegalArgumentException("Parameter 'file' is not pointing to file with csv extension");
        }

        this.transactionsDataFile = file;
    }

    @Override
    public ArrayList<Transaction> loadStringConvertableObjects() throws IOException {
        if(transactionsDataFile == null){
            throw new IllegalArgumentException("Parameter 'file' cannot be null");
        }

        BufferedReader fileReader;
        try {
            fileReader = new BufferedReader(new FileReader(transactionsDataFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Transaction> transactionArrayListList = new ArrayList<>();
        String line;

        while ((line = fileReader.readLine()) != null){
            transactionArrayListList.add(new Transaction(line));
        }

        fileReader.close();
        return  transactionArrayListList;
    }

    // TODO: Add method parameter indicating that string should be appended at the end of the file.
    @Override
    public boolean addStringConvertableObject(String convertableObjectAsString, int rowIndex) throws IOException {
        File tempFile;
        boolean stringAdded = false;

        // TODO: Extract those try/catch blocks into setUp method
        try {
            tempFile = File.createTempFile("temp", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader fileReader;
        try {
            fileReader = new BufferedReader(new FileReader(transactionsDataFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter fileWriter;
        try {
            fileWriter = new BufferedWriter(new FileWriter(tempFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String currentLine;
        int currentRowIndex = 0;

        while((currentLine = fileReader.readLine()) != null){
            if(currentRowIndex == rowIndex) {
                fileWriter.write(convertableObjectAsString + System.getProperty("line.separator"));
                stringAdded = true;
            }

            fileWriter.write(currentLine + System.getProperty("line.separator"));
            currentRowIndex++;
        }

        // Add string at the end of the file if it has not been done yet
        if(!stringAdded){
            fileWriter.write(convertableObjectAsString + System.getProperty("line.separator"));
        }

        fileReader.close();
        fileWriter.close();

        // Replace old transactions file with temp file
        Path tempFilePath = tempFile.toPath();
        Path transactionsFilePath = transactionsDataFile.toPath();
        Files.move(tempFilePath, transactionsFilePath, StandardCopyOption.REPLACE_EXISTING);

        return true;
    }

    @Override
    public boolean removeStringConvertableObject(int rowIndex) throws IOException {
        File tempFile;
        boolean wasStringRemoved = false;

        try {
            tempFile = File.createTempFile("temp", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader fileReader;
        try {
            fileReader = new BufferedReader(new FileReader(transactionsDataFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter fileWriter;
        try {
            fileWriter = new BufferedWriter(new FileWriter(tempFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String currentLine;
        int currentRowIndex = 0;

        while((currentLine = fileReader.readLine()) != null){
            if(currentRowIndex == rowIndex) {
                currentRowIndex++;
                wasStringRemoved = true;
                continue;
            }

            fileWriter.write(currentLine + System.getProperty("line.separator"));
            currentRowIndex++;
        }

        fileReader.close();
        fileWriter.close();

        // Replace old transactions file with temp file
        Path tempFilePath = tempFile.toPath();
        Path transactionsFilePath = transactionsDataFile.toPath();
        Files.move(tempFilePath, transactionsFilePath, StandardCopyOption.REPLACE_EXISTING);

        return wasStringRemoved;
    }

    @Override
    public boolean replaceStringConvertableObject(int indexOfRowToRemove, String convertableObjectAsString, int indexOfRowToAdd) {
        boolean removalSuccessful;
        boolean additionSuccessful;

        try {
            removalSuccessful = removeStringConvertableObject(indexOfRowToRemove);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            additionSuccessful = addStringConvertableObject(convertableObjectAsString, indexOfRowToAdd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return removalSuccessful && additionSuccessful;
    }
}
