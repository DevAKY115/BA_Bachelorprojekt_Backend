package com.berkan.bachelorprojekt.BA_Bachelorprojekt.XR_Tool;

import com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions.XRToolError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
public class XR_ToolService {

    @Autowired
    XR_Repository repository;

    /**
     * Läd alle Dateien des XR-Tools in den Firebase Storage, und läd das XR-Tool selbst in die Datenbank.
     * @param tool Das XR-Tool was in die Datenbank übernommen wird.
     * @param titelbild Die Titelbild Datei.
     * @param bilder Die Bilder Dateien.
     * @param extra_material Die Extra Material Dateien.
     * @throws IOException
     */
    public void createXR_Tool(XR_Tool tool, MultipartFile titelbild,
                              MultipartFile[] bilder, MultipartFile[] extra_material) throws IOException {

        for(MultipartFile file: bilder){
            FireBaseInitializer.uploadFile(tool.getTitel(), file);
        }

        if(extra_material != null) {
            for (MultipartFile file : extra_material) {
                FireBaseInitializer.uploadFile(tool.getTitel(), file);
            }
        }

        FireBaseInitializer.uploadFile(tool.getTitel(), titelbild);

        repository.save(tool);
    }

    /**
     * Entnimmt der Datenbank das XR-Tool, und holt sich alle Dateien aus dem Firebase Storage, die mit dem Titel übereinstimmen.
     * @param titel Der Titel des XR-Tools nachdem gesucht wird.
     * @return XR-Tool
     */
    public XR_Tool getToolByTitel(String titel){
        if(findToolByTitel(titel)){

            XR_Tool tool = repository.findById(titel).get();

            String titelbild = tool.getTitelbild();

            String[] bilder = tool.getBilder().split(":::");
            String[] bilderBase64 = new String[bilder.length];
            String[] bilderURL = new String[bilder.length];

            tool.setTitelbildBase64(FireBaseInitializer.getFile(tool.getTitel(), titelbild));
            tool.setTitelbildURL(FireBaseInitializer.getFileURL(tool.getTitel(), titelbild));

            for(int i = 0; i < bilder.length; i++){
                bilderBase64[i] = FireBaseInitializer.getFile(tool.getTitel(), bilder[i]);
                bilderURL[i] = FireBaseInitializer.getFileURL(tool.getTitel(), bilder[i]);
            }

            tool.setBilderBase64(bilderBase64);
            tool.setBilderURL(bilderURL);

            if(!tool.getExtraFiles().isEmpty()){

                String[] extra_material = tool.getExtraFiles().split(":::");
                String[] extra_materialBase64 = new String[extra_material.length];
                String[] extraFilesURL = new String[extra_material.length];

                for(int i = 0; i < extra_material.length; i++){
                    extra_materialBase64[i] = FireBaseInitializer.getFile(titel, extra_material[i]);
                    extraFilesURL[i] = FireBaseInitializer.getFileURL(titel, extra_material[i]);
                }

                tool.setExtraFilesBase64(extra_materialBase64);
                tool.setExtraFilesURL(extraFilesURL);
            }


            return tool;

        }else {

            throw new XRToolError("No tool with this title");
        }
    }

    /**
     * Überprüft ob das XR-Tool in der Datenbank vorhanden ist.
     * @param titel Der Titel des XR-Tools nachdem gesucht wird.
     * @return Boolean
     */
    public boolean findToolByTitel(String titel){


     return repository.findById(titel).isPresent();
    }


    /**
     * Entfernt alle Dateien des XR-Tools aus dem Firebase Storage.
     * @param toolTitel Der Titel des XR-Tools.
     */
    public void deleteOldFiles(String toolTitel){


        XR_Tool oldTool = getToolByTitel(toolTitel);

        String titelbildName = oldTool.getTitelbild();
        String[] bilderNamen = oldTool.getBilder().split(":::");
        String[] extraFilesNamen = oldTool.getExtraFiles().split(":::");

        FireBaseInitializer.deleteFile(toolTitel, titelbildName);

        for(String filename : bilderNamen){
            FireBaseInitializer.deleteFile(toolTitel, filename);
        }

        for(String filename : extraFilesNamen){
            FireBaseInitializer.deleteFile(toolTitel, filename);
        }

    }

    /**
     * Sucht nach Tools die mit den gesuchten Werten übereinstimmen. Wendet Weighted Term Scoring an, um die 8 besten Tools zu finden, und sucht dann nochmals nach den Stichworten.
     * @param keywords Die Stichworte nachdem gesucht wird.
     * @param phaseValues Die Werte für die Projektphasen.
     * @param skillValues Die Werte für die Skillmatrix.
     * @return Eine Liste von XR-Tools die am meisten mit den gesuchten Kriterien übereinstimmen. Die Liste enthält 8 Tools.
     */
    public List<XR_Tool> searchTools(String[] keywords, int[] phaseValues, int[] skillValues){


        /* Weighted Term Scoring

        Höhere Werte kriegen größeres Weight
        aber eine Differenz von 2>= verliert punkte
        (nur bei Skillmatrix, weil extra projektphasen sind nicht schlecht)

        jeder Term kriegt als weight seinen Wert + 1
        so erhalten auch Übereinstimmungen mit einem Wert von 0 Punkte

         */


        // Alle XR-Tools
        ArrayList<XR_Tool> list = (ArrayList<XR_Tool>) repository.findAll();

        int[] queryValues = new int[14];

        for(int i = 0; i < skillValues.length; i++){
            queryValues[i] = skillValues[i];
        }

        for(int i = 6; i < queryValues.length; i++){
            queryValues[i] = phaseValues[i-6];
        }

        int[] toolValues;
        int score = 0;

        HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();

        for(XR_Tool tool : list){

            toolValues = combineValues(tool);

            for(int i = 0; i < 6; i++){

                if(queryValues[i] == -1){
                    continue;
                }


                if(toolValues[i] == queryValues[i]){
                    score +=  (queryValues[i] + 1);
                } else if(Math.abs(toolValues[i] - queryValues[i]) >= 3){
                    score -= 3;
                } else if(Math.abs(toolValues[i] - queryValues[i]) >= 2){
                    score -= 1;

                }
            }

            for(int i = 6; i < toolValues.length; i++){


                if(toolValues[i] == queryValues[i]){
                    score +=  (queryValues[i] + 1);
                }
            }

            scoreMap.put(tool.getTitel(), score);

            score = 0;
        }

        LinkedList<Integer> scoreList = new LinkedList<Integer>(scoreMap.values());

        Collections.sort(scoreList);


        LinkedList<XR_Tool> resultList = new LinkedList<>();

        String titel;

        for(int i = 0; i < 8; i ++){

            titel = getKey(scoreMap, scoreList.get(scoreList.size() -1 - i));
            if(titel != null){
                resultList.add(repository.findById(titel).get());
            }

        }

        String toolText = "";
        int keywordFrequency = 0;
        scoreMap.clear();

        for(XR_Tool tool : resultList){

            toolText += tool.getTitel();
            toolText += tool.getKurzbeschreibung();
            toolText += tool.getBeschreibung();
            toolText += tool.getXr_kontext();

            for(String keyword : keywords){

                while (toolText.contains(keyword)) {

                    toolText = removeSubstring(toolText, keyword);
                    keywordFrequency++;
                }

            }

            scoreMap.put(tool.getTitel(), keywordFrequency);
            keywordFrequency = 0;

        }

        scoreList = new LinkedList<>(scoreMap.values());
        Collections.sort(scoreList);

        resultList.clear();

        for(int i = 0; i < 8; i ++){

            titel = getKey(scoreMap, scoreList.get(scoreList.size() -1 - i));
            if(titel != null){
                resultList.add(getToolByTitel(titel));
            }

        }

        return resultList;
    }

    public List<XR_Tool> searchTools(String[] keywords){


        /* Weighted Term Scoring

        Höhere Werte kriegen größeres Weight
        aber eine Differenz von 2>= verliert punkte
        (nur bei Skillmatrix, weil extra projektphasen sind nicht schlecht)

        jeder Term kriegt als weight seinen Wert + 1
        so erhalten auch Übereinstimmungen mit einem Wert von 0 Punkte

         */


        // Alle XR-Tools
        ArrayList<XR_Tool> toolList = (ArrayList<XR_Tool>) repository.findAll();

        // XR-Tools mit scores
        HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();

        // Nur die scores um nach diesen zu sortieren
        LinkedList<Integer> scoreList = new LinkedList<Integer>(scoreMap.values());

        // Die Liste der am besten zutreffenden XR-Tools
        LinkedList<XR_Tool> resultList = new LinkedList<>();

        String toolText = "";
        int keywordFrequency = 0;

        for(XR_Tool tool : toolList){


            toolText += tool.getKurzbeschreibung();
            toolText += tool.getBeschreibung();
            toolText += tool.getXr_kontext();

            for(String keyword : keywords){

                if(tool.getTitel().contains(keyword)){
                    keywordFrequency += 100;
                }

                while (toolText.contains(keyword)) {

                    toolText = removeSubstring(toolText, keyword);
                    keywordFrequency++;
                }

            }

            scoreMap.put(tool.getTitel(), keywordFrequency);
            keywordFrequency = 0;
            toolText = "";

        }

        scoreList = new LinkedList<>(scoreMap.values());
        Collections.sort(scoreList);

        String titel;

        for(int i = 0; i < 8; i ++){

            titel = getKey(scoreMap, scoreList.get(scoreList.size() -1 - i));
            if(titel != null){
                resultList.add(getToolByTitel(titel));
            }

        }
        return resultList;
    }

    /**
     * Füllt die Datenbank mit XR-Tools für Tests.
     */
    public void testData() throws IOException {
        List<Integer> phaseList = Arrays.asList(2, 1, 1, 0, 0, 0, 0, 0);
        for(int i = 0; i < 100; i++){

            XR_Tool temp = new XR_Tool();

            temp.setTitel("00" + i);

            temp.setCoding(new Random().nextInt(4));
            temp.setKonzept(new Random().nextInt(4));
            temp.setIllustration(new Random().nextInt(4));
            temp.setDesign(new Random().nextInt(4));
            temp.setModelling_3D(new Random().nextInt(4));
            temp.setAnimation(new Random().nextInt(4));

            Collections.shuffle(phaseList);

            temp.setAnforderungs_analyse(phaseList.get(0));
            temp.setAnforderungs_definition(phaseList.get(1));
            temp.setIdeenskizze(phaseList.get(2));
            temp.setStrukturen(phaseList.get(3));
            temp.setLow_fidelity(phaseList.get(4));
            temp.setHigh_fidelity(phaseList.get(5));
            temp.setAssetproduktion(phaseList.get(6));
            temp.setSoftware_entwicklung(phaseList.get(7));

            temp.setKurzbeschreibung("Eine Kurzbeschreibung zu dem Tool");

            temp.setBeschreibung("Apple's Reality Composer ermöglicht die Konstruktion von 3D Kompositionen für AR Anwendungen. Im Kern des Werkzeugs steht eine iOS-Applikation, die es ermöglicht 3D Modelle mit verschiedenen Assets zu verknüpfen und ihnen ein gewünschtes Verhalten zuzuordnen. Durch die vereinfachte Verknüpfung von virtuellen Inhalten mit Triggern die ein definierbares Verhalten auslösen, können vereinfach prototypische AR-Anwendungen modelliert werden. \n" +
                    "\n" +
                    "Die in der iOS-Applikation modellierten Prototypen können exportiert und auf einem Mac bearbeitet werden. Das in XCode integrierte RealityKit Framework ermöglicht hierbei die Anpassung der Modellierung unter Verwendung identischer Trigger und Verhalten sowie die Manipulation modellierter Prototypen auf Code-Ebene. Eine stetige Synchronisation zwischen dem Mac-Framework und der Companion App realisiert die unmittelbare Überprüfung von Anpassungen an der intendierten AR-Applikation und erleichtert somit den Entwicklungsprozess.");

            temp.setXr_kontext("Apple's Reality Composer ermöglicht die Konstruktion von 3D Kompositionen für AR Anwendungen. Im Kern des Werkzeugs steht eine iOS-Applikation, die es ermöglicht 3D Modelle mit verschiedenen Assets zu verknüpfen und ihnen ein gewünschtes Verhalten zuzuordnen. Durch die vereinfachte Verknüpfung von virtuellen Inhalten mit Triggern die ein definierbares Verhalten auslösen, können vereinfach prototypische AR-Anwendungen modelliert werden. \n" +
                    "\n" +
                    "Die in der iOS-Applikation modellierten Prototypen können exportiert und auf einem Mac bearbeitet werden. Das in XCode integrierte RealityKit Framework ermöglicht hierbei die Anpassung der Modellierung unter Verwendung identischer Trigger und Verhalten sowie die Manipulation modellierter Prototypen auf Code-Ebene. Eine stetige Synchronisation zwischen dem Mac-Framework und der Companion App realisiert die unmittelbare Überprüfung von Anpassungen an der intendierten AR-Applikation und erleichtert somit den Entwicklungsprozess.");


            Path path = Paths.get("C:\\Users\\berka\\Desktop\\javafiles\\image4.jpg");
            String name = "image4.jpg";
            String originalFileName = "image4.jpeg";
            String contentType = "image/jpg";
            byte[] content = null;
            try {
                content = Files.readAllBytes(path);
            } catch (final IOException e) {
            }
            MultipartFile file = new MockMultipartFile(name,
                    originalFileName, contentType, content);
//            MultipartFile file = (MultipartFile) new File("C:\\Users\\berka\\Desktop\\javafiles\\image4.jpg");

            temp.setTitelbild(originalFileName);
            temp.setBilder(originalFileName);
            temp.setExtraFiles(originalFileName);

            createXR_Tool(temp, file, new MultipartFile[]{file}, new MultipartFile[]{file});


        }

    }

    /**
     * Fasst die Werte für Skillmatrix und Projektphasen in einem Array zusammen.
     * @param tool Das XR-Tool dessen Skillmatrix und Projektphasen Werte zu einem Array zusammengefasst werden soll.
     * @return int[14]
     */
    private int[] combineValues(XR_Tool tool){
        int[] result = new int[14];

        result[0] = tool.getCoding();
        result[1] = tool.getKonzept();
        result[2] = tool.getIllustration();
        result[3] = tool.getDesign();
        result[4] = tool.getModelling_3D();
        result[5] = tool.getAnimation();
        result[6] = tool.getAnforderungs_analyse();
        result[7] = tool.getAnforderungs_definition();
        result[8] = tool.getIdeenskizze();
        result[9] = tool.getStrukturen();
        result[10] = tool.getLow_fidelity();
        result[11] = tool.getHigh_fidelity();
        result[12] = tool.getAssetproduktion();
        result[13] = tool.getSoftware_entwicklung();

        return result;
    }

    /**
     * Durchsucht die gegebene Map nach dem Value und gibt den Key aus.
     * Entfernt den Eintrag in der Map, damit das selbe Ergebnis nicht mehrfach auftritt, sollte mehrmals nach dem selben Value gesucht werden.
     * @param map Die Map die durchsucht werden soll.
     * @param value Der Wert nachdem gesucht werden soll.
     * @return Den Key der dem gesuchtem Value entspricht, oder null wenn kein Key gefunden wurde.
     */
    private <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                map.remove(entry.getKey());
                return entry.getKey();
            }
        }
        return null;
    }


    /**
     * Entfernt den Substring aus dem Text.
     * @param text Der Text aus dem ein Substring entfernt werden soll.
     * @param substring Der Substring der entfernt werden soll.
     * @return Der Text ohne den Substring.
     */
    protected static String removeSubstring(String text, String substring) {

        int startIndex = text.indexOf(substring);

        String a = text.substring(0, startIndex);
        String b = text.substring(startIndex + substring.length());

        return a + b;
    }

    public List<String> getAllTitles(){
        return (List<String>) repository.findAllTitles();
    }
}
