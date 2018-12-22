package com.example.extra.ankallispankki;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.extra.ankallispankki.MainActivity.getAppContext;

public class Transactions {

    private String name;
    private String from;
    private String to;
    private Integer money;
    private String message;
    String time;

    public Transactions() {
    }
    // constructer for transactions
  public Transactions(String trans_name, String trans_from, String trans_to, Integer trans_money, String trans_message) {
        name = trans_name;
        from = trans_from;
        to = trans_to;
        money = trans_money;
        message = trans_message;
    }

    //method to write transaction history to json file. Gets parameters from activities
    public void writeJSONFile(String Filename, String Name, String From, String To, String Money, String Message) throws JSONException {
        //Sets values from parameters
        String write_name = Name;
        String write_from = From;
        String write_to = To;
        String write_money = Money;
        String write_message = Message;
        String filename = Filename;
        // Timestamp
        time = getTime();
        //Json object to make frame for inner objects
        JSONObject mainWriteObj = new JSONObject();
        //Creates new file and uses context from Mainactivity
        File checkFile = getAppContext().getFileStreamPath(filename);
        //checks if file exists and if not makes new one.
        if(!checkFile.exists()){
                checkFile = new File(getAppContext().getFilesDir(),filename);
        }
        // checks if the file is empty and writes to objects to it
        if (checkFile.length() == 0) {
            System.out.println("File is empty ..." + filename);
            JSONObject writeObj = new JSONObject();
            writeObj.put("name", write_name);
            writeObj.put("from", write_from);
            writeObj.put("to", write_to);
            writeObj.put("money", write_money);
            writeObj.put("message", write_message);
            writeObj.put("time",time);
            JSONArray writeArray = new JSONArray();
            writeArray.put(writeObj);
            mainWriteObj.put("users", writeArray);
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getAppContext().openFileOutput(filename, Context.MODE_PRIVATE));
                outputStreamWriter.write(mainWriteObj.toString());
                outputStreamWriter.flush();
                outputStreamWriter.close();
                System.out.println("tiedostoon meni!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If the file is not empty file it will be written and data is stored to objects.
        else {
            System.out.println("File is not empty ...");
            try {
                // get JSONObject from JSON file
                JSONObject writeObj = new JSONObject(loadJSONFromAsset(filename));
                // fetch JSONArray named users
                JSONArray userArray = writeObj.getJSONArray("users");
                // implement for loop for getting users list data
                JSONObject innerJson = new JSONObject();


                for (int i = 0; i < userArray.length(); i++) {
                    // create a JSONObject for fetching single user data
                    JSONObject userDetail = userArray.getJSONObject(i);
                    innerJson.put("name", userDetail.getString("name"));
                    innerJson.put("from", userDetail.getString("from"));
                    innerJson.put("to", userDetail.getString("to"));
                    innerJson.put("money", userDetail.getString("money"));
                    innerJson.put("message", userDetail.getString("message"));
                    innerJson.put("time", userDetail.getString("time"));
                }
                innerJson.put("name", write_name);
                innerJson.put("to", write_to);
                innerJson.put("money", write_money);
                innerJson.put("message", write_message);
                innerJson.put("time", time);
                userArray.put(innerJson);

                JSONObject currentJSON = new JSONObject();
                currentJSON.put("users", userArray);

                //After that new data is added and whole object is written a file
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getAppContext().openFileOutput(filename, Context.MODE_PRIVATE));
                    outputStreamWriter.write(writeObj.toString());
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    System.out.println("tiedostoon meni!");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Timestamp
    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String time = sdf.format(new Date());
        return time;
    }




    // Rreads and parsers data from  the file.
    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is =  getAppContext().openFileInput(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
