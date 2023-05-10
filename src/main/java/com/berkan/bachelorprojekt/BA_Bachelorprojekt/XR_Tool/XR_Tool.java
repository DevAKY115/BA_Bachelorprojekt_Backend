package com.berkan.bachelorprojekt.BA_Bachelorprojekt.XR_Tool;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Arrays;

@Entity
public class XR_Tool {


    @Id
    private String titel;
    private String kurzbeschreibung;
    private String beschreibung;
    private String xr_kontext;

    private int coding;
    private int konzept;
    private int illustration;
    private int design;
    private int modelling_3D;
    private int animation;

    private int anforderungs_analyse;
    private int anforderungs_definition;
    private int ideenskizze;
    private int strukturen;
    private int low_fidelity;
    private int high_fidelity;
    private int assetproduktion;
    private int software_entwicklung;

    private String steckbrief;
    private String musterprojekt;

    //Namen
    private String titelbild;
    private String bilder;
    private String extraFiles;

    private String fileTypes;


/*    @Transient
    private String titelbildBase64;
    @Transient
    private String[] bilderBase64;
    @Transient
    private String[] extraFilesBase64;*/

    @Transient
    private String titelbildURL;
    @Transient
    private String[] bilderURL;
    @Transient
    private String[] extraFilesURL;


    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getKurzbeschreibung() {
        return kurzbeschreibung;
    }

    public void setKurzbeschreibung(String kurzbeschreibung) {
        this.kurzbeschreibung = kurzbeschreibung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getXr_kontext() {
        return xr_kontext;
    }

    public void setXr_kontext(String xr_kontext) {
        this.xr_kontext = xr_kontext;
    }

    public int getCoding() {
        return coding;
    }

    public void setCoding(int coding) {
        this.coding = coding;
    }

    public int getKonzept() {
        return konzept;
    }

    public void setKonzept(int konzept) {
        this.konzept = konzept;
    }

    public int getIllustration() {
        return illustration;
    }

    public void setIllustration(int illustration) {
        this.illustration = illustration;
    }

    public int getDesign() {
        return design;
    }

    public void setDesign(int design) {
        this.design = design;
    }

    public int getModelling_3D() {
        return modelling_3D;
    }

    public void setModelling_3D(int modelling_3D) {
        this.modelling_3D = modelling_3D;
    }

    public int getAnimation() {
        return animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
    }

    public int getAnforderungs_analyse() {
        return anforderungs_analyse;
    }

    public void setAnforderungs_analyse(int anforderungs_analyse) {
        this.anforderungs_analyse = anforderungs_analyse;
    }

    public int getAnforderungs_definition() {
        return anforderungs_definition;
    }

    public void setAnforderungs_definition(int anforderungs_definition) {
        this.anforderungs_definition = anforderungs_definition;
    }

    public int getIdeenskizze() {
        return ideenskizze;
    }

    public void setIdeenskizze(int ideenskizze) {
        this.ideenskizze = ideenskizze;
    }

    public int getStrukturen() {
        return strukturen;
    }

    public void setStrukturen(int strukturen) {
        this.strukturen = strukturen;
    }

    public int getLow_fidelity() {
        return low_fidelity;
    }

    public void setLow_fidelity(int low_fidelity) {
        this.low_fidelity = low_fidelity;
    }

    public int getHigh_fidelity() {
        return high_fidelity;
    }

    public void setHigh_fidelity(int high_fidelity) {
        this.high_fidelity = high_fidelity;
    }

    public int getSoftware_entwicklung() {
        return software_entwicklung;
    }

    public void setSoftware_entwicklung(int software_entwicklung) {
        this.software_entwicklung = software_entwicklung;
    }

    public String getTitelbild() {
        return titelbild;
    }

    public void setTitelbild(String titelbild) {
        this.titelbild = titelbild;
    }

    public String getBilder() {
        return bilder;
    }

    public void setBilder(String bilder) {
        this.bilder = bilder;
    }

    public String getExtraFiles() {
        return extraFiles;
    }

    public void setExtraFiles(String extraFiles) {
        this.extraFiles = extraFiles;
    }

    public String getSteckbrief() {
        return steckbrief;
    }

    public void setSteckbrief(String steckbrief) {
        this.steckbrief = steckbrief;
    }

/*    public String getTitelbildBase64() {
        return titelbildBase64;
    }

    public void setTitelbildBase64(String titelbildBase64) {
        this.titelbildBase64 = titelbildBase64;
    }

    public String[] getBilderBase64() {
        return bilderBase64;
    }

    public void setBilderBase64(String[] bilderBase64) {
        this.bilderBase64 = bilderBase64;
    }

    public String[] getExtraFilesBase64() {
        return extraFilesBase64;
    }

    public void setExtraFilesBase64(String[] extraFilesBase64) {
        this.extraFilesBase64 = extraFilesBase64;
    }*/

    public String getMusterprojekt() {
        return musterprojekt;
    }

    public void setMusterprojekt(String musterprojekt) {
        this.musterprojekt = musterprojekt;
    }

    public String getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(String fileTypes) {
        this.fileTypes = fileTypes;
    }

    public int getAssetproduktion() {
        return assetproduktion;
    }

    public void setAssetproduktion(int assetproduktion) {
        this.assetproduktion = assetproduktion;
    }

    public String getTitelbildURL() {
        return titelbildURL;
    }

    public void setTitelbildURL(String titelbildURL) {
        this.titelbildURL = titelbildURL;
    }

    public String[] getBilderURL() {
        return bilderURL;
    }

    public void setBilderURL(String[] bilderURL) {
        this.bilderURL = bilderURL;
    }

    public String[] getExtraFilesURL() {
        return extraFilesURL;
    }

    public void setExtraFilesURL(String[] extraFilesURL) {
        this.extraFilesURL = extraFilesURL;
    }

}
