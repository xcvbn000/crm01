package com.yjxxt.crm.dto;

public class TreeDto {
    private Integer id;
    private String name;
    private Integer pId;
    private boolean checked;
    public TreeDto() {
    }

    public TreeDto(Integer id, String name, Integer pId, boolean checked) {
        this.id = id;
        this.name = name;
        this.pId = pId;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "TreeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pId=" + pId +
                ", checked=" + checked +
                '}';
    }

}
