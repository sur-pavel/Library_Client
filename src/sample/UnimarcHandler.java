package sample;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.io.*;
import java.util.*;


class UnimarcHandler {

    private ArrayList<Record> records = new ArrayList<>();
    private Map<Leader, String> searchMap;
    private String mapFileName = "searchMap.ser";
    private ArrayList<File> marcFiles = new ArrayList<>();


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
        createSearchMap();
//        onChangeSerialize();
        return records;
    }

    private void onChangeSerialize() {
        TimerTask task = new DirWatcher("marcFiles", "ISO") {
            @Override
            protected void onChange(File file, String action) {
                // here we code the action on a change
                System.out.println("File " + file.getName() + " action: " + action);
                serializeMap();
            }

        };

        Timer timer = new Timer();
        timer.schedule(task, new Date(), 5000);
    }


    private void createSearchMap() {
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
//                System.out.println(searchString);
            }
            serializeMap();
        }
    }

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

