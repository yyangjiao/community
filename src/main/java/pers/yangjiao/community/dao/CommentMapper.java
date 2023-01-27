package pers.yangjiao.community.dao;

import org.apache.ibatis.annotations.Mapper;
import pers.yangjiao.community.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);
    int selectCommentRowsByEntity(int entityType, int entityId);
    int insertComment(Comment comment);
}
