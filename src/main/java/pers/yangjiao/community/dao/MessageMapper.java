package pers.yangjiao.community.dao;

import org.apache.ibatis.annotations.Mapper;
import pers.yangjiao.community.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> selectConversations(int userId, int offset, int limit);

    int selectConversationCount(int userId);

    List<Message> selectLetters(String conversationId, int offset, int limit);

    int selectLetterCount(String conversationId);

    int selectLetterUnreadCount(int userId, String conversationId);

    int insertMessage(Message message);

    int updateStatus(List<Integer> ids, int status);
}
