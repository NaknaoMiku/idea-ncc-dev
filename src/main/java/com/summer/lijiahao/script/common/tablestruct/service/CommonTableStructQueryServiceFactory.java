package com.summer.lijiahao.script.common.tablestruct.service;

import com.summer.lijiahao.base.BusinessException;
import com.summer.lijiahao.script.common.tablestruct.service.impl.URLZipFileQueryService;

public class CommonTableStructQueryServiceFactory {
    public static ICommonTableStructQueryService getService() throws BusinessException {
        return URLZipFileQueryService.getSingleton();
    }
}
