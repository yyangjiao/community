package pers.yangjiao.community.util;

public interface CommunityConstant {
    // 成功激活
    int ACTIVATION_SUCCESS = 0;
    // 重复激活
    int ACTIVATION_REPEAT = 1;
    // 激活失败
    int ACTIVATION_FAILURE = 2;
    // 默认状态下的登陆凭证超时时间（12小时）
    int DEFAULT_EXPIRED_SECONDS = 60 * 60 * 12;
    // 勾选记住我状态下的登陆凭证超时时间（30天）
    int REMEMBERME_EXPIRED_SECONDS = 60 * 60 * 24 * 30;
    // 实体类型
    int ENTITY_TYPE_POST = 1;
    int ENTITY_TYPE_COMMENT = 2;
}
