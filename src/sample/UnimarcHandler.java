package sample;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.Record;

import java.io.*;
import java.util.*;


class UnimarcHandler {

    private ArrayList<Record> records = new ArrayList<>();
    private Map<Leader, String> searchMap;
    private String mapFileName = "searchMap.ser";
    private ArrayList<File> marcFiles = new ArrayList<>();
    private ArrayList<Integer> searchFields = new ArrayList<>
            (Arrays.asList(700, 200, 606, 210, 205, 200));


    public Map<Leader, String> getSearchMap() {
        return searchMap;
    }

    ArrayList<Record> getUnimarc() {

        File folder = new File("marcFiles");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().contains("")) {
                    marcFiles.add(listOfFile);
                    System.out.println("File " + listOfFile.getName() + "added.");
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
/*
        createSearchMap();
        serializeMap();
*/
        return records;
    }


    private void createSearchMap() {

        searchMap = deSerializeMap() != null ? deSerializeMap() : new HashMap<>();

        for (Record record : records) {
            Leader leader = record.getLeader();
            StringBuilder searchString = new StringBuilder();
            for (Integer searchField : searchFields) {
                if (record.getVariableField(String.valueOf(searchField)) != null) {
                    DataField field = (DataField) record.getVariableField(String.valueOf(searchField));
                    searchString.append(field.toString().toLowerCase());
                }
            }
            searchMap.put(leader, searchString.toString());
        }
    }

    private HashMap<Leader, String> deSerializeMap() {

        HashMap<Leader, String> deSerializedMap = null;

        try {
            FileInputStream fis = new FileInputStream(mapFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            deSerializedMap = (HashMap<Leader, String>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println(ioe.toString());

        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();

        }
        System.out.println("HashMap deserialized");

        return deSerializedMap;
    }


    private void serializeMap() {
        try {
            FileOutputStream fos =
                    new FileOutputStream(mapFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(searchMap);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in searchMap.ser");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


}

