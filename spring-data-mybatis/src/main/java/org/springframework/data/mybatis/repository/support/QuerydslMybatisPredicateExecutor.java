/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mybatis.repository.support;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.sql.AbstractSQLQuery;
import org.mybatis.spring.SqlSessionTemplate;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mybatis.dialect.Dialect;
import org.springframework.data.mybatis.mapping.MybatisMappingContext;
import org.springframework.data.mybatis.querydsl.MybatisRelationalPathBase;
import org.springframework.data.mybatis.querydsl.MybatisSQLQuery;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

/**
 * .
 *
 * @param <Q> query type
 * @param <T> domain type
 * @author JARVIS SONG
 * @since 2.0.2
 */
public class QuerydslMybatisPredicateExecutor<Q, T> implements QuerydslPredicateExecutor<T> {

	private final MybatisRelationalPathBase<Q, T> path;

	private final Querydsl<Q, T> querydsl;

	private FactoryExpression<T> bean;

	public QuerydslMybatisPredicateExecutor(MybatisEntityInformation<T, ?> entityInformation,
			EntityPathResolver resolver, MybatisMappingContext mappingContext, SqlSessionTemplate sqlSessionTemplate,
			Dialect dialect) {
		this.path = (MybatisRelationalPathBase<Q, T>) resolver.createPath(entityInformation.getJavaType());
		this.querydsl = new Querydsl<>(sqlSessionTemplate, mappingContext, dialect, entityInformation,
				new PathBuilder<>(this.path.getType(), this.path.getMetadata()));
		this.bean = this.path.projections();
	}

	protected MybatisSQLQuery<T> createQuery(Predicate... predicate) {
		Assert.notNull(predicate, "Predicate must not be null!");
		AbstractSQLQuery<T, ?> query = this.doCreateQuery(predicate);
		return (MybatisSQLQuery<T>) query;
	}

	private AbstractSQLQuery<T, ?> doCreateQuery(Predicate... predicate) {
		AbstractSQLQuery<T, MybatisSQLQuery<T>> query = this.querydsl.createQuery(this.path);
		if (predicate != null) {
			query = query.where(predicate);
		}
		return query;
	}

	private List<T> executeSorted(MybatisSQLQuery<T> query, Sort sort) {
		return this.querydsl.applySorting(sort, query).fetch();
	}

	private List<T> executeSorted(MybatisSQLQuery<T> query, OrderSpecifier<?>... orders) {
		return this.executeSorted(query, new QSort(orders));
	}

	@Override
	public Optional<T> findOne(Predicate predicate) {
		Assert.notNull(predicate, "Predicate must not be null!");
		try {
			return Optional.ofNullable(this.path.handleQuery(this.createQuery(predicate).select(this.bean)).fetchOne());
		}
		catch (NonUniqueResultException ex) {
			throw new IncorrectResultSizeDataAccessException(ex.getMessage(), 1, ex);
		}
	}

	@Override
	public Iterable<T> findAll(Predicate predicate) {
		Assert.notNull(predicate, "Predicate must not be null!");

		return this.path.handleQuery(this.createQuery(predicate).select(this.bean)).fetch();
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, Sort sort) {
		Assert.notNull(predicate, "Predicate must not be null!");
		Assert.notNull(sort, "Sort must not be null!");

		return this.executeSorted(this.path.handleQuery(this.createQuery(predicate).select(this.bean)), sort);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
		Assert.notNull(predicate, "Predicate must not be null!");
		Assert.notNull(orders, "Order specifiers must not be null!");

		return this.executeSorted(this.path.handleQuery(this.createQuery(predicate).select(this.bean)), orders);
	}

	@Override
	public Iterable<T> findAll(OrderSpecifier<?>... orders) {
		Assert.notNull(orders, "Order specifiers must not be null!");

		return this.executeSorted(this.path.handleQuery(this.createQuery(new Predicate[0]).select(this.bean)), orders);
	}

	@Override
	public Page<T> findAll(Predicate predicate, Pageable pageable) {
		Assert.notNull(predicate, "Predicate must not be null!");
		Assert.notNull(pageable, "Pageable must not be null!");

		final MybatisSQLQuery<?> countQuery = this.createQuery(predicate);
		MybatisSQLQuery<T> query = this.querydsl.applyPagination(pageable,
				this.path.handleQuery(this.createQuery(new Predicate[0]).select(this.bean)));

		return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchCount);
	}

	@Override
	public long count(Predicate predicate) {
		return this.createQuery(predicate).fetchCount();
	}

	@Override
	public boolean exists(Predicate predicate) {
		return this.createQuery(predicate).fetchCount() > 0;
	}

}
