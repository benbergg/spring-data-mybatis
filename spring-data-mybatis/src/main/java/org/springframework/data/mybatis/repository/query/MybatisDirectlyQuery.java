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
package org.springframework.data.mybatis.repository.query;

import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * Will find statement from Mybatis Mapper xml file.
 *
 * @author JARVIS SONG
 * @since 2.0.1
 */
public class MybatisDirectlyQuery extends AbstractMybatisQuery {

	public MybatisDirectlyQuery(SqlSessionTemplate sqlSessionTemplate, MybatisQueryMethod method) {
		super(sqlSessionTemplate, method);
	}

	@Override
	public SqlCommandType getSqlCommandType() {
		return null;
	}

	@Override
	public String getStatementName() {
		return this.method.getAnnotationValue("statement", this.method.getName());
	}

}
