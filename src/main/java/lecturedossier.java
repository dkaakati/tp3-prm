import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/* Created by dania on 17/11/16.
*/
public class lecturedossier {


    public static Map<String,String> vecteurs = new HashMap<>();
    public static Map<String,String> vecteurs2 = new HashMap<>();


    public static void main(String args[]){


        try{
            File dir = new File("/home/dania/Documents/Stages_old/");

            File[] fichiers = dir.listFiles();
            if (fichiers == null) {
                System.out.println("Directory does not exist or is not a Directory");
            } else {
                for (int i=0; i < dir.listFiles().length;i++) {

                    //Récuperation du contenu

                    String text ="";
                    try {
                        text = getText(fichiers[i]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    String contenu = text.toLowerCase();

                    //System.out.println(contenu);
                    System.out.println("---------------------- " + i + " --------------------------------------------");
                    vecteurs.put(fichiers[i].toString(), contenu);
                    vecteurs2.put(fichiers[i].toString(), contenu);


                }


                int k =0;
                int indic= 0;
                Iterator it1 = vecteurs.entrySet().iterator();
                while (it1.hasNext()){

                    Map.Entry<String,String> entry = (Map.Entry<String, String>)it1.next();
                    String cle1 = entry.getKey();
                    String valeur1 = entry.getValue();

                    Iterator it2 = vecteurs2.entrySet().iterator();

                   while(it2.hasNext()){
                       indic++;
                       System.out.println(indic);
                       Map.Entry<String,String> entry2 = (Map.Entry<String, String>)it2.next();
                        String cle2 = entry2.getKey();
                        String valeur2 = entry2.getValue();
                       //  Integer distance = levenshteinDistance(valeur1, valeur2);
                            if(valeur1.equals(valeur2)){
                                k++;
                                if(k>1){
                                it2.remove();
                                }
                            }
                    }
                    k=0;
                }
                System.out.println(vecteurs2.size());
                insertCsv(vecteurs2);
            }
        } catch (Exception ioe){
            ioe.printStackTrace();
        }



    }

    private static String getText(File file) throws IOException {
        PDDocument doc = PDDocument.load(file);
        String text = new PDFTextStripper().getText(doc);
        doc.close();
        return text;
    }

    public static void insertCsv (Map<String,String> list){
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter("/home/dania/Documents/pdfSansDoublons.csv");

            for(Map.Entry<String, String> maliste : list.entrySet()) {
                fileWriter.append(maliste.getKey());
                fileWriter.append("\n");
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }

    }

    public static Integer levenshteinDistance (CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }
}
