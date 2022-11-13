package com.example.nongdam.repository;

import com.example.nongdam.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CropRepository extends JpaRepository<Crop, Integer> {

    @Query("Select this_ from Crop this_ order by this_.category, this_.type")
    List<Crop> findAllOrderByCategoryAndType();

    @Query("Select this_ from Crop this_ where this_.id in :ids")
    List<Crop> findAllIds(@Param("ids") List<Integer> ids);

    @Query("Select count(this_) from Crop this_")
    int countCrops();
}