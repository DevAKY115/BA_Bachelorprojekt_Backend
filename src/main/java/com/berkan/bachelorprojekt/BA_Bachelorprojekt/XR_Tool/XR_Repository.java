package com.berkan.bachelorprojekt.BA_Bachelorprojekt.XR_Tool;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XR_Repository extends CrudRepository<XR_Tool, String> {

    @Query("SELECT titel FROM XR_Tool")
    public Iterable<String> findAllTitles();
}
