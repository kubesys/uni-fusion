package com.qnkj.common.services;

import com.qnkj.common.entitys.PopupDialog;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface IPopupDialogServices {

    void clear();

    void clear(String modulename);

    PopupDialog get(String modulename);

    void update(PopupDialog popupDialog) throws Exception;
}
