package com.github.dergil.hierarchynote.model.dto;

public class ResponseDto {
    private Long id;
    private String name;
    private String text;
    private String directory;
    private Boolean isDir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", directory='" + directory + '\'' +
                ", isDir=" + isDir +
                '}';
    }
}
