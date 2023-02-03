package com.berkan.bachelorprojekt.BA_Bachelorprojekt.XR_Tool;

import com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions.SearchError;
import com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions.XRToolError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class XR_ToolController {

    @Autowired
    XR_ToolService service;

    @PostMapping(path = "/creation")
    public ResponseEntity<String> createTool(@RequestParam String titel, @RequestParam String kurzbeschreibung, @RequestParam String beschreibung,
                     @RequestParam String xr_kontext, @RequestParam(required = false)  MultipartFile[] bilder, @RequestParam int[] projektphasen,
                     @RequestParam int[] skillmatrix, @RequestParam(required = false) String[] steckbrief, @RequestParam(required = false) String[] musterprojekt,
                     @RequestParam(required = false) MultipartFile[] extra_material, @RequestParam(required = false) MultipartFile titelbild) throws IOException, SQLException {

        if(service.findToolByTitel(titel)){
            throw new XRToolError("Es existiert bereits ein XR-Tool mit diesem Titel. Bitte wählen Sie einen anderen Titel.");
//            return new ResponseEntity<>("Es existiert bereits ein XR-Tool mit diesem Titel. \n" +
//                    "Bitte wählen Sie einen anderen Titel.", HttpStatus.BAD_REQUEST);
        }

        if(     titel.isEmpty() || titel.length() < 3 ||
                kurzbeschreibung.isEmpty() || kurzbeschreibung.length() < 3 ||
                beschreibung.isEmpty() || beschreibung.length() < 12 ||
                xr_kontext.isEmpty() || xr_kontext.length() < 12 ||
                bilder.length == 0 ||
                projektphasen.length == 0 ||
                skillmatrix.length == 0 ||
                titelbild.isEmpty()
        ){
            throw new XRToolError("Formular ist nicht komplett oder inkorrekt ausgefüllt");
//            return new ResponseEntity<>("Formular ist nicht komplett oder inkorrekt ausgefüllt", HttpStatus.BAD_REQUEST);
        }

        if(!titelbild.getContentType().matches("image/.+")){
            throw new XRToolError("Titelbild darf nur eine Bilddatei sein");
//            return new ResponseEntity<>("Titelbild darf nur eine Bilddatei sein", HttpStatus.BAD_REQUEST);
        }

        for (MultipartFile multipartFile : bilder) {
            if (!multipartFile.getContentType().matches("image/.+")) {
                throw new XRToolError("Bilder dürfen nur aus Bilddateien bestehen");
//                return new ResponseEntity<>("Bilder dürfen nur aus Bilddateien bestehen", HttpStatus.BAD_REQUEST);
            }
        }

        int sum = 0;
        for(int i : projektphasen){
            sum += i;

            if(i<0 || i > 2){
                throw new XRToolError("Fehler: Projektphasen außerhalb zulässigen Wertebereichs");
//                return new ResponseEntity<>("Fehler: Projektphasen außerhalb zulässigen Wertebereichs", HttpStatus.BAD_REQUEST);
            }
        }

        if(sum == 0){
            throw new XRToolError("Bitte wählen Sie mindestens eine Projektphase");
//            return new ResponseEntity<>("Bitte wählen Sie mindestens eine Projektphase", HttpStatus.BAD_REQUEST);
        }

        sum = 0;
        for(int i : skillmatrix){
            sum += i;
            if(i < 0 || i > 3){
                throw new XRToolError("Fehler: Skillmatrix außerhalb zulässigen Wertebereichs");
//                return new ResponseEntity<>("Fehler: Skillmatrix außerhalb zulässigen Wertebereichs", HttpStatus.BAD_REQUEST);
            }
        }

        if(sum == 0){
            throw new XRToolError("Bitte füllen Sie die Skillmatrix aus");
//            return new ResponseEntity<>("Bitte füllen Sie die Skillmatrix aus", HttpStatus.BAD_REQUEST);
        }

        XR_Tool tool = new XR_Tool();

        tool.setTitel(titel);
        tool.setKurzbeschreibung(kurzbeschreibung);
        tool.setBeschreibung(beschreibung);
        tool.setXr_kontext(xr_kontext);

        tool.setCoding(skillmatrix[0]);
        tool.setKonzept(skillmatrix[1]);
        tool.setIllustration(skillmatrix[2]);
        tool.setDesign(skillmatrix[3]);
        tool.setModelling_3D(skillmatrix[4]);
        tool.setAnimation(skillmatrix[5]);
        tool.setAnforderungs_analyse(projektphasen[0]);
        tool.setAnforderungs_definition(projektphasen[1]);
        tool.setIdeenskizze(projektphasen[2]);
        tool.setStrukturen(projektphasen[3]);
        tool.setLow_fidelity(projektphasen[4]);
        tool.setHigh_fidelity(projektphasen[5]);
        tool.setAssetproduktion(projektphasen[6]);
        tool.setSoftware_entwicklung(projektphasen[7]);

        tool.setTitelbild(titelbild.getOriginalFilename());
        tool.setBilder("");
        tool.setExtraFiles("");
        tool.setFileTypes("");

        for(MultipartFile file : bilder){
            tool.setBilder(tool.getBilder().concat(":::" + file.getOriginalFilename()));
        }
        tool.setBilder(tool.getBilder().substring(3));

        if(extra_material != null) {
            for (MultipartFile file : extra_material) {
                tool.setExtraFiles(tool.getExtraFiles().concat(":::" + file.getOriginalFilename()));
                tool.setFileTypes(tool.getFileTypes().concat(":::" + file.getContentType()));
            }
            tool.setExtraFiles(tool.getExtraFiles().substring(3));
            tool.setFileTypes(tool.getFileTypes().substring(3));
        }

        if(steckbrief != null) {
            tool.setSteckbrief("");
            for(String eintrag : steckbrief){
                tool.setSteckbrief(tool.getSteckbrief().concat("::::" + eintrag));
            }
            tool.setSteckbrief(tool.getSteckbrief().substring(4));
        }

        if(musterprojekt != null) {
            tool.setMusterprojekt("");
            for(String eintrag : musterprojekt){
                tool.setMusterprojekt(tool.getMusterprojekt().concat("::::" + eintrag));
            }
            tool.setMusterprojekt(tool.getMusterprojekt().substring(4));
        }



        service.createXR_Tool(tool, titelbild, bilder, extra_material);

        return new ResponseEntity<>("Created XR-Tool", HttpStatus.CREATED);
    }

    @GetMapping(path = "/xrtool/{titel}")
    public XR_Tool getToolByTitle(@PathVariable String titel) throws SQLException {

//        throw new UserNotFoundException();
        return service.getToolByTitel(titel);
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<String> editToolByTitle(@RequestParam String titel, @RequestParam String kurzbeschreibung, @RequestParam String beschreibung,
                                   @RequestParam String xr_kontext, @RequestParam(required = false)  MultipartFile[] bilder, @RequestParam int[] projektphasen,
                                   @RequestParam int[] skillmatrix, @RequestParam(required = false) String[] steckbrief, @RequestParam(required = false) String[] musterprojekt,
                                   @RequestParam(required = false) MultipartFile[] extra_material, @RequestParam(required = false) MultipartFile titelbild) throws IOException, SQLException {

        if(     titel.isEmpty() || titel.length() < 3 ||
                kurzbeschreibung.isEmpty() || kurzbeschreibung.length() < 3 ||
                beschreibung.isEmpty() || beschreibung.length() < 12 ||
                xr_kontext.isEmpty() || xr_kontext.length() < 12 ||
                bilder.length == 0 ||
                projektphasen.length == 0 ||
                skillmatrix.length == 0 ||
                titelbild.isEmpty()
        ){
            return new ResponseEntity<>("Formular ist nicht komplett oder inkorrekt ausgefüllt", HttpStatus.BAD_REQUEST);
        }

        if(!titelbild.getContentType().matches("image/.+")){
            return new ResponseEntity<>("Titelbild darf nur eine Bilddatei sein", HttpStatus.BAD_REQUEST);
        }

        for (MultipartFile multipartFile : bilder) {
            if (!multipartFile.getContentType().matches("image/.+")) {
                return new ResponseEntity<>("Bilder dürfen nur aus Bilddateien bestehen", HttpStatus.BAD_REQUEST);
            }
        }

        int sum = 0;
        for(int i : projektphasen){
            sum += i;

            if(i<0 || i > 2){
                return new ResponseEntity<>("Fehler: Projektphasen außerhalb zulässigen Wertebereichs", HttpStatus.BAD_REQUEST);
            }
        }

        if(sum == 0){
            return new ResponseEntity<>("Bitte wählen Sie mindestens eine Projektphase", HttpStatus.BAD_REQUEST);
        }

        sum = 0;
        for(int i : skillmatrix){
            sum += i;
            if(i < 0 || i > 3){
                return new ResponseEntity<>("Fehler: Skillmatrix außerhalb zulässigen Wertebereichs", HttpStatus.BAD_REQUEST);
            }
        }

        if(sum == 0){
            return new ResponseEntity<>("Bitte füllen Sie die Skillmatrix aus", HttpStatus.BAD_REQUEST);
        }

        XR_Tool tool = new XR_Tool();

        tool.setTitel(titel);
        tool.setKurzbeschreibung(kurzbeschreibung);
        tool.setBeschreibung(beschreibung);
        tool.setXr_kontext(xr_kontext);

        tool.setCoding(skillmatrix[0]);
        tool.setKonzept(skillmatrix[1]);
        tool.setIllustration(skillmatrix[2]);
        tool.setDesign(skillmatrix[3]);
        tool.setModelling_3D(skillmatrix[4]);
        tool.setAnimation(skillmatrix[5]);
        tool.setAnforderungs_analyse(projektphasen[0]);
        tool.setAnforderungs_definition(projektphasen[1]);
        tool.setIdeenskizze(projektphasen[2]);
        tool.setStrukturen(projektphasen[3]);
        tool.setLow_fidelity(projektphasen[4]);
        tool.setHigh_fidelity(projektphasen[5]);
        tool.setAssetproduktion(projektphasen[6]);
        tool.setSoftware_entwicklung(projektphasen[7]);

        tool.setTitelbild(titelbild.getOriginalFilename());
        tool.setBilder("");
        tool.setExtraFiles("");
        tool.setFileTypes("");

        for(MultipartFile file : bilder){
            tool.setBilder(tool.getBilder().concat(":::" + file.getOriginalFilename()));
        }
        tool.setBilder(tool.getBilder().substring(3));

        if(extra_material != null) {
            for (MultipartFile file : extra_material) {
                tool.setExtraFiles(tool.getExtraFiles().concat(":::" + file.getOriginalFilename()));
                tool.setFileTypes(tool.getFileTypes().concat(":::" + file.getContentType()));
            }
            tool.setExtraFiles(tool.getExtraFiles().substring(3));
            tool.setFileTypes(tool.getFileTypes().substring(3));
        }

        if(steckbrief != null) {
            tool.setSteckbrief("");
            for(String eintrag : steckbrief){
                tool.setSteckbrief(tool.getSteckbrief().concat("::::" + eintrag));
            }
            tool.setSteckbrief(tool.getSteckbrief().substring(4));
        }

        if(musterprojekt != null) {
            tool.setMusterprojekt("");
            for(String eintrag : musterprojekt){
                tool.setMusterprojekt(tool.getMusterprojekt().concat("::::" + eintrag));
            }
            tool.setMusterprojekt(tool.getMusterprojekt().substring(4));
        }

        service.deleteOldFiles(tool.getTitel());

        service.createXR_Tool(tool, titelbild, bilder, extra_material);

        return new ResponseEntity<>("Edited XR-Tool", HttpStatus.CREATED);
    }


    @PostMapping(path = "/advanced_search")
    public List<XR_Tool> searchTool(@RequestParam String keywords, @RequestParam int[] phaseValues, @RequestParam int[] skillValues){

//        service.testData();

        if(phaseValues.length != 8 || skillValues.length != 6 || keywords.isEmpty()){
            throw new SearchError("Search parameters not correct");
        }

        return service.searchTools(keywords.split(" "), phaseValues, skillValues);
    }

    @GetMapping(path = "allTitles")
    public List<String> getAllTitles(){

        return service.getAllTitles();
    }

    @PostMapping(path = "/searchByTitle")
    public List<XR_Tool> searchByTitle(@RequestBody String title) throws SQLException {

        if(title.isEmpty()){
            throw new SearchError("Search parameters not correct");
        }

        return service.searchTools(title.split(" "));
    }

}
