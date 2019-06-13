package com.kuyuner.bg.recanddis.entity;

import com.kuyuner.common.persistence.BaseEntity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
/**
 * 发文公章实体
 *
 * @author administrator
 */
public class OfficialSeal extends BaseEntity {

	/**
     * 文件名称
     */     
	@NotNull(message = "文件名称不能为空")
	@Length(max = 200, message = "文件名称长度不能大于200")
	private String fileName;

	/**
     * 文件大小
     */     
	@NotNull(message = "文件大小不能为空")
	@Length(max = 11, message = "文件大小长度不能大于11")
	private Long fileSize;

	/**
     * 文件路径
     */     
	@NotNull(message = "文件路径不能为空")
	@Length(max = 500, message = "文件路径长度不能大于500")
	private String filePath;



    public OfficialSeal(String id) {
        this.id = id;
    }

    public OfficialSeal() {
        super();
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}



}