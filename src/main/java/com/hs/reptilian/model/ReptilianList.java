package com.hs.reptilian.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReptilianList {

    private Integer id;

    private String city;

    private String area;

    private String address;

    private String village;

    private Date createDate;

    private Integer status;

}
