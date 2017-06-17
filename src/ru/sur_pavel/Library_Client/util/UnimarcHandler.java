package ru.sur_pavel.Library_Client.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import ru.sur_pavel.Library_Client.model.SearchValue;

import java.io.*;
import java.util.*;

/**
 * Class for obtaining data from unimarc files
 */
public class UnimarcHandler implements Runnable {



    private ArrayList<Record> records = new ArrayList<>();
    private Map<Leader, String> searchMap;
    private String mapFileName = "searchMap.ser";
    private String sRecordsFileName = "shortRecords.ser";
    private ArrayList<File> marcFiles = new ArrayList<>();
    private ObservableList<SearchValue> shortRecords = FXCollections.observableArrayList();


    /**
     * @return HashMap with short records for search
     */
    public Map<Leader, String> getSearchMap() {
        return searchMap;
    }

    /**
     * @return list of all unimarc records
     */
    public ArrayList<Record> getRecords() {
        return records;
    }

    /**
     * Loads all files in directory "marcFiles"
     * forms list of unimarc records
     */
    private void getUnimarc() {

        File folder = new File("marcFiles");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().contains("")) {
                    marcFiles.add(listOfFile);
                    System.out.println("File " + listOfFile.getName() + " added.");
                }
            }
        }


        for (File marcFile : marcFiles) {
            InputStream in = null;
            try {
                in = new FileInputStream(marcFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(marcFile + "not found.");
            }


            assert in != null;

            MarcReader reader = new MarcStreamReader(in, "UTF8");


            // fill records

            while (reader.hasNext()) {
                Record record = reader.next();
                records.add(record);
            }
        }
        createSMapSRecords();
//        onChangeSerialize();

    }

    /**
     * call serialize method when files in marcFiles directory changes
     */
    private void onChangeSerialize() {
        TimerTask task = new DirWatcher("marcFiles", "ISO") {
            @Override
            protected void onChange(File file, String action) {
                // here we code the action on a change
                System.out.println("File " + file.getName() + " action: " + action);
                serialize(mapFileName, searchMap);
            }

        };

        Timer timer = new Timer();
        timer.schedule(task, new Date(), 5000);
    }


    /**
     * Forms HashMap for search
     * from deSerialized file or
     * from unimarc records
     */
    private void createSMapSRecords() {
        if (deSerializeMap() != null) {
            searchMap = deSerializeMap();
        } else {
            searchMap = new HashMap<>();
            for (Record record : records) {
                Leader leader = record.getLeader();
                String searchString = subFieldData(record, "700", 'a') +
                        subFieldData(record, "200", 'a') +
                        subFieldData(record, "600", 'a');
                searchMap.put(leader, searchString);


                String title = new StringJoiner(" ")
                        .add(subFieldData(record, "200", 'a'))
                        .add(subFieldData(record, "200", 'h'))
                        .add(subFieldData(record, "205", 'a'))
                        .toString();
                String year = subFieldData(record, "210", 'd');
                shortRecords.add(new SearchValue(leader.toString(), year, title));
            }
            serialize(mapFileName, searchMap);
            serialize(sRecordsFileName, shortRecords);
        }
    }

    /**
     * Gets data from unimarc record subField
     * @param record whose subFields
     * @param tag of field
     * @param code of subField
     * @return string with data of subField
     */
    private String subFieldData(Record record, String tag, char code) {
        StringBuilder builder = new StringBuilder();
        if (record.getVariableField(tag) != null) {
            DataField field = (DataField) record.getVariableField(tag);
            List subfields = field.getSubfields();
            for (Object subField : subfields) {
                Subfield subF = (Subfield) subField;
                if (subF.getCode() == code)
                    if(!subF.getData().equals(""))
                    builder.append(subF.getData()).append(". ");
            }
        }
        return builder.toString();
    }

    /**
     * deserialize HashMap for search from file
     * @return HashMap
     */
    private HashMap<Leader, String> deSerializeMap() {

        HashMap<Leader, String> deSerializedMap = null;
        File f = new File(mapFileName);
        if (f.exists() && !f.isDirectory()) {
            System.out.println("File exists.");
            try {
                FileInputStream fis = new FileInputStream(mapFileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                deSerializedMap = (HashMap<Leader, String>) ois.readObject();
                ois.close();
                fis.close();
                System.out.println("HashMap deserialized");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println(ioe.toString());

            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();

            }
        }
        return deSerializedMap;

    }


    /**
     * serialize object
     * @param fileName to save in
     * @param object for serializing
     */
    private void serialize(String fileName, Object object){
        try {
            FileOutputStream fos =
                    new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
            System.out.printf("Serialized" + object.toString() +
                    " data is saved in " +
                    fileName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void run() {
        getUnimarc();
    }
}

