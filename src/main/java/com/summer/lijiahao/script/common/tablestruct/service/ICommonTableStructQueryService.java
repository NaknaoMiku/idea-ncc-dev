package com.summer.lijiahao.script.common.tablestruct.service;

import com.summer.lijiahao.base.BusinessException;
import com.summer.lijiahao.script.common.tablestruct.model.MainTableCfg;

import java.util.List;
import java.util.Properties;

public interface ICommonTableStructQueryService {
    List<MainTableCfg> getCommonMainTableCfgs() throws BusinessException;

    Properties getCommonMapping();
}
