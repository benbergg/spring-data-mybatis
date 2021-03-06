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
package org.springframework.data.mybatis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * MyBatis Repository Basic Interface.
 *
 * @param <T> domain type
 * @param <ID> primary key type
 * @author JARVIS SONG
 * @since 1.0.0
 */
@NoRepositoryBean
public interface MybatisRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {

	@Override
	List<T> findAll();

	@Override
	List<T> findAll(Sort sort);

	@Override
	List<T> findAllById(Iterable<ID> ids);

	@Override
	<S extends T> List<S> findAll(Example<S> example);

	@Override
	<S extends T> List<S> findAll(Example<S> example, Sort sort);

	T getById(ID id);

	@Override
	<S extends T> List<S> saveAll(Iterable<S> entities);

	<S extends T> List<S> saveSelectiveAll(Iterable<S> entities);

	<S extends T> List<S> saveAll(S... entities);

	<S extends T> List<S> saveSelectiveAll(S... entities);

	<S extends T> S saveSelective(S entity);

	<S extends T> S insert(S entity);

	<S extends T> S insertSelective(S entity);

	<S extends T> S insertCascade(S entity);

	<S extends T> S update(S entity);

	<S extends T> S update(ID id, S entity);

	<S extends T> S updateSelective(S entity);

	<S extends T> S updateSelective(ID id, S entity);

	int deleteAllInBatch();

	int deleteInBatchById(Iterable<ID> ids);

	int deleteInBatch(Iterable<T> entities);

	int removeById(ID id);

	int remove(T entity);

	int removeAll(Iterable<? extends T> entities);

	int removeAll();

	@Deprecated
	<X extends T> T getOne(X condition);

	@Deprecated
	<X extends T> Optional<T> findOne(X condition);

	@Deprecated
	<X extends T> List<T> findAll(X condition);

	@Deprecated
	<X extends T> List<T> findAll(Sort sort, X condition);

	@Deprecated
	<X extends T> Page<T> findAll(Pageable pageable, X condition);

	@Deprecated
	<X extends T> long countAll(X condition);

}
