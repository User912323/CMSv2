package com.pixeltrice.springbootimagegalleryapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pixeltrice.springbootimagegalleryapp.entity.ImageGallery;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ImageGalleryRepository extends JpaRepository<ImageGallery, Long>{
//  ImageGallery findId(Long id);

  @Modifying
  @Transactional
  @Query("update ImageGallery u set u.status = :status where u.id = :id")
  void updatestatus(@Param("id") Long id,@Param("status") String status);
}


