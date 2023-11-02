package com.qnkj.core.webconfigs.configure;

/**
 * 常量
 *
 * @author Oldhand
 */
public class WebConstant {

    /**
     * 注册用户角色ID
     */
    public static final Long REGISTER_ROLE_ID = 2L;

    /**
     * 排序规则：降序
     */
    public static final String ORDER_DESC = "desc";

    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "asc";

    /**
     * 前端页面路径前缀
     */
    public static final String VIEW_PREFIX = "views/";

    /**
     * 验证码 Session Key
     */
    public static final String CODE_PREFIX = "web_captcha_";

    /**
     * 登录标记 Session Key
     */
    public static final String LOGIN_SIGN_PREFIX = "web_login_sign_";

    /**
     * 短信验证码 Session Key
     */
    public static final String SMSCODE_PREFIX = "web_smscode_";

    /**
     * 注册 Session Key
     */
    public static final String REGISTER_PREFIX = "web_register_";

    /**
     *
     */
    public static final String SUPPLIER_PREFIX = "supplier_";

    /**
     * 允许下载的文件类型，根据需求自己添加（小写）
     */
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    /**
     * 异步线程池名称
     */
    public static final String ASYNC_POOL = "webAsyncThreadPool";

    /**
     * 开发环境
     */
    public static final String DEVELOP = "dev";

    /**
     * Windows 操作系统
     */
    public static final String SYSTEM_WINDOWS = "windows";

    /**
     * Mac 系统
     */
    public static final String SYSTEM_MACOS = "Mac OS X";

    /**
     * 初始化状态
     */
    public static final String INIT_STATUS = "GET_LONGTIME_STATUS_WAIT_INIT";


    /**
     *
     */
    public static final String AUTOOPENBROWSER_PREFIX = "isautoopenbrowser";
}
