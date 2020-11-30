import com.google.gson.*;

import java.io.*;

import java.util.Map;

public class Elastic {
    public static void main(String[] args) {
        Gson gson = new Gson();


        try {
            PrintWriter writerClean = new PrintWriter(new FileWriter("C:\\Users\\Michal Zajic\\workspaces\\elastic-population\\data\\result-tranformed.json", true));
            writerClean.print("");
            writerClean.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }




        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:\\Users\\Michal Zajic\\workspaces\\elastic-population\\data\\result.json"), "UTF-8"));
            String line;
            int counter = 0;
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Michal Zajic\\workspaces\\elastic-population\\data\\result-tranformed.json", true));
                writer.write( "[");
                writer.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            while(true) {
                line = reader.readLine();
                if(line == null) {
                    break;
                }
                counter++;
                System.out.println(counter);
                JsonObject article = new JsonParser().parse(line).getAsJsonObject();
                JsonObject languages = article.getAsJsonObject("languages");
                JsonArray newLanguages = new JsonArray();
                for (Map.Entry<String, JsonElement> entry : languages.entrySet()) {
                    String entryObj = entry.getValue().getAsString();
                    String[] stringArr = entryObj.split("/");
                    String languageTerm = stringArr[stringArr.length - 1 ];
                    JsonObject newLanguage = new JsonObject();
                    newLanguage.add("languageTerm", new JsonPrimitive(languageTerm.replaceAll("_", " ")));
                    newLanguage.add("languageUrl", new JsonPrimitive(entryObj));
                    newLanguage.add("languageAbr", new JsonPrimitive(entry.getKey()));
                    newLanguages.add(newLanguage);
                }
                article.add("languages", newLanguages);
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Michal Zajic\\workspaces\\elastic-population\\data\\result-tranformed.json", true));
                    writer.write(article + ",\n");
                    writer.close();
                } catch (Exception e) {
                    System.out.println(e);
                }


            }
            reader.close();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Michal Zajic\\workspaces\\elastic-population\\data\\result-tranformed.json", true));
                writer.write( "]");
                writer.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
