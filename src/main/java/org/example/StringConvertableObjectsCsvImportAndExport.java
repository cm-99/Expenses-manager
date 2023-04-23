package org.example;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface to be implemented by classes managing storage of StringConvertableObject objects in *.csv files.
 */
public interface StringConvertableObjectsCsvImportAndExport<T extends StringConvertableObject>{
    /**
     * Implementation should load specified type T of objects which extend StringConvertableObject.
     * @return - ArrayList of loaded objects implementing StringConvertableObject.
     */
    ArrayList<T> loadStringConvertableObjects() throws IOException;
    /**
     * Implementation should add specified string at rowIndex to managed transactions file.
     * @return - true if method was successful, otherwise false (possible cause - index not found).
     */
    boolean addStringConvertableObject(String convertableObjectAsString, int rowIndex) throws IOException;
    /**
     * Implementation should remove string at rowIndex in managed file.
     * @return - true if method was successful, otherwise false (possible cause - index not found).
     */
    boolean removeStringConvertableObject(int rowIndex) throws IOException;
    /**
     * Implementation should remove string at indexOfRowToRemove and add specified string as indexOfRowToAdd.
     * @return - true if method was successful, otherwise false (possible cause - index not found).
     */
    boolean replaceStringConvertableObject(int indexOfRowToRemove, String convertableObjectAsString, int indexOfRowToAdd);
}
