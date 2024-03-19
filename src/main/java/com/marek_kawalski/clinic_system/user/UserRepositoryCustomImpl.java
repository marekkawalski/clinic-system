package com.marek_kawalski.clinic_system.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private MongoTemplate mongoTemplate;

    @Override
    public Page<User> getPagedUsers(final UserRequestParams userRequestParams) {
        final int pageNumber = userRequestParams.getPageNumber();
        final int pageSize = userRequestParams.getPageSize();
        final List<UserRole> roles = userRequestParams.getRoles();
        final String search = userRequestParams.getSearch();

        final Pageable pageable = PageRequest.of(pageNumber, pageSize);

        final Query query = new Query();

        if (roles != null && !roles.isEmpty()) {
            query.addCriteria(Criteria.where("userRole").in(roles));
        }
        if (search != null && !search.isEmpty()) {
            Criteria nameCriteria = Criteria.where("name").regex(search, "i");
            Criteria surnameCriteria = Criteria.where("surname").regex(search, "i");
            Criteria emailCriteria = Criteria.where("email").regex(search, "i");
            Criteria idCriteria = Criteria.where("id").regex(search, "i");
            query.addCriteria(new Criteria().orOperator(nameCriteria, surnameCriteria, emailCriteria, idCriteria));
        }

        long totalCount = mongoTemplate.count(query, User.class);
        query.with(pageable);

        List<User> users = mongoTemplate.find(query, User.class);
        return PageableExecutionUtils.getPage(users, pageable, () -> totalCount);
    }
}
