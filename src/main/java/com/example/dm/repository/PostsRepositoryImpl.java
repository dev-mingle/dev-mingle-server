package com.example.dm.repository;

import com.example.dm.entity.Posts;
import com.example.dm.util.GeometryUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepository{

    private final EntityManager em;

    @Override
    public Page<Posts> findAll(Long categoryId, String search, String[] conditions, String[] location, Pageable pageable) {
        String jpql = """
                select p.title
                from Posts p
                where p.category.id = :categoryId
                and st_dwithin(:point, p.point, 3000, true) = true
                """;

        String countJpql = """
                select count(p)
                from Posts p
                where p.category.id = :categoryId
                and st_dwithin(:point, p.point, 3000, true) = true
                """;

        for (String condition : conditions) {
            String sql = switch (condition) {
                case "title" -> "and p.title like concat('%', :search, '%')\n";
                case "contents" -> "and p.contents like concat('%', :search, '%')\n";
                case "writer" -> "and p.writer like concat('%', :search, '%')\n";
                default -> throw new RuntimeException("에러발생! (에러 바꿔야함)");
            };

            jpql += sql;
            countJpql += sql;
        }

        if (location.length < 2) {
            throw new RuntimeException("에러발생! (에러 바꿔야함)");
        }

        Point point = GeometryUtils.createPoint(location[1], location[2]);

        TypedQuery<Posts> query = em.createQuery(jpql, Posts.class)
                .setParameter("categoryId", categoryId)
                .setParameter("point", point);

        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class)
                .setParameter("categoryId", categoryId)
                .setParameter("point", point);

        if (conditions.length != 0) {
            query.setParameter("search", search);
            countQuery.setParameter("search", search);
        }

        List<Posts> result = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return PageableExecutionUtils.getPage(result, pageable, countQuery::getSingleResult);
    }
}
