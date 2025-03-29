package com.luizjacomn.billreminder.repository.spec;

import com.luizjacomn.billreminder.model.entity.Bill;
import com.luizjacomn.billreminder.model.filter.BillFilter;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BillSpecs {

    public static Specification<Bill> byFilter(BillFilter filter) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            root.fetch("categories", JoinType.LEFT);

            if (StringUtils.isNotBlank(filter.title())) {
                predicates.add(
                    builder.like(builder.lower(root.get("title")), "%" + filter.title().toLowerCase().trim() + "%")
                );
            }

            if (!CollectionUtils.isEmpty(filter.categories())) {
                var categoriesJoin = root.join("categories", JoinType.LEFT);
                predicates.add(categoriesJoin.in(filter.categories()));
            }

            if (Objects.nonNull(filter.dueDayOfMonth())) {
                predicates.add(
                    builder.equal(root.get("dueDayOfMonth"), filter.dueDayOfMonth())
                );
            }

            if (Objects.nonNull(filter.businessDayOfMonth())) {
                predicates.add(
                    builder.equal(root.get("businessDayOfMonth"), filter.businessDayOfMonth())
                );
            }

            query.orderBy(builder.asc(root.get("dueDayOfMonth")), builder.asc(root.get("businessDayOfMonth")), builder.asc(root.get("title")));

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
