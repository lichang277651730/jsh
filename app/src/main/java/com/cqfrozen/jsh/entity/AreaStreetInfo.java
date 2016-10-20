package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */
public class AreaStreetInfo extends BaseEntity {
    public String area_id;
    public String area_name;
    public List<StreetInfo> street;

    public class StreetInfo{
        public String st_id;
        public String street_name;
    }

}
