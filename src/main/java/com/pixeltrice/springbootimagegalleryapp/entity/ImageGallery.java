package com.pixeltrice.springbootimagegalleryapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Arrays;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Data
@NoArgsConstructor
@Table(name = "attachmentv2")
public class ImageGallery {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private long id;

	private String fileName;
	private String fileType;
	private String file_id;
	private float similarity;
	private String path;
	private String username;
	private String password;
	private String no_hp;

	@Lob
	private byte[] data2;

	@Lob
	private byte[] data;

	public ImageGallery(String fileName, String fileType, byte[] data,byte[] data2, float similarity, String file_id, String path, String username, String password, String no_hp) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
		this.similarity = similarity;
		this.file_id = file_id;
		this.path = path;
		this.data2 = data2;
		this.username = username;
		this.password = password;
		this.no_hp = no_hp;
	}
   
}


