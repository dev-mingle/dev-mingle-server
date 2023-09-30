package com.example.dm.repository;

import com.example.dm.entity.Posts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
class PostsRepositoryImpl implements PostsRepository{

    private final EntityManager em;

    @Override
    public Page<Posts> findAll(Long categoryId, String search, String[] conditions, double[] location, double distance, Pageable pageable) {
        boolean haveSearch = false;
        double latitude = location[0];
        double longitude = location[1];

        String jpql = """
                select p
                from Posts p
                left join fetch p.userProfile u
                where p.category.id = :categoryId
                and p.isDeleted = false
                and st_dwithin(st_point(:latitude, :longitude, 4326), st_point(p.latitude, p.longitude, 4326), :distance) = true
                """;

        String countJpql = """
                select count(p)
                from Posts p
                where p.category.id = :categoryId
                and p.isDeleted = false
                and st_dwithin(st_point(:latitude, :longitude, 4326), st_point(p.latitude, p.longitude, 4326), :distance) = true
                """;

        if (StringUtils.hasText(search) && conditions != null) {
            for (String condition : conditions) {
                String sql = switch (condition) {
                    case "title" ->"and p.title like concat('%', :search, '%')\n";
                    case "contents" -> "and p.contents like concat('%', :search, '%')\n";
                    case "writer" -> "and p.writer like concat('%', :search, '%')\n";
                    default -> null;
                };

                if (sql != null) {
                    jpql += sql;
                    countJpql += sql;
                    haveSearch = true;
                }
            }
        }

        jpql += "order by";
        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();
            String direction = order.getDirection().name();
            jpql += "\n\tp." + property + " " + direction;
        }

        TypedQuery<Posts> query = em.createQuery(jpql, Posts.class)
                .setParameter("categoryId", categoryId)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .setParameter("distance", distance);

        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class)
                .setParameter("categoryId", categoryId)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .setParameter("distance", distance);

        if (haveSearch) {
            query.setParameter("search", search);
            countQuery.setParameter("search", search);
        }

        int offset = (int) pageable.getOffset();
        int size = pageable.getPageSize();

        List<Posts> posts = query.setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::getSingleResult);
    }
}
