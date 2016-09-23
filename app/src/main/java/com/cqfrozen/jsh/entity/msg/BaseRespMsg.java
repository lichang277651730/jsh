package com.cqfrozen.jsh.entity.msg;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/23.
 */
public class BaseRespMsg implements Serializable {
    public String code;
    public String msg;
    public Object data;
}
